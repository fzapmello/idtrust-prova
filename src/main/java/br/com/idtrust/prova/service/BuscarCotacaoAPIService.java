package br.com.idtrust.prova.service;

import br.com.idtrust.prova.dto.CotacaoDTO;
import br.com.idtrust.prova.entity.Cotacao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static br.com.idtrust.prova.config.WebClientConfiguration.API_KEY_PROPERTY;
import static br.com.idtrust.prova.config.WebClientConfiguration.CEPEA_API_URL_PROPERTY;
import static br.com.idtrust.prova.util.CotacaoUtil.*;

@Service
public class BuscarCotacaoAPIService {

    private static final Logger log = LoggerFactory.getLogger(BuscarCotacaoAPIService.class);

    private Environment env;
    private ProcessarCotacaoService processarCotacaoService;
    private CRUDCotacaoService cotacaoService;
    private final WebClient defaultWebClient;

    @Autowired
    public BuscarCotacaoAPIService(WebClient defaultWebClient, Environment env,
                                   @Lazy ProcessarCotacaoService processarCotacaoService,
                                   @Lazy CRUDCotacaoService cotacaoService) {
        this.defaultWebClient = defaultWebClient;
        this.env = env;
        this.processarCotacaoService = processarCotacaoService;
        this.cotacaoService = cotacaoService;
    }

    public Cotacao obterCotacaoCEPEA(String cultura, LocalDate dataCotacao) throws Exception {
        validarParametros(cultura, dataCotacao);
        Cotacao cotacao = buscarCotacaoBD(dataCotacao,cultura);
        if (cotacao != null) {
            processarCotacaoService.processarCotacao(converterCotacaoToDTO(cotacao));
            return cotacao;
        } else {
            CotacaoDTO cotacaoDTO = buscarCotacaoAPI(cultura, dataCotacao);
            cotacao = converterDTOToCotacao(cotacaoDTO);
            salvarCotacao(cotacao);
            return cotacao;
        }
    }

    private CotacaoDTO buscarCotacaoAPI(String cultura, LocalDate dataCotacao) throws Exception {
        try {
            JsonNode block = this.defaultWebClient.get()
                    .uri(env.getProperty(CEPEA_API_URL_PROPERTY), uriBuilder -> uriBuilder
                            .path("/" + cultura + "/")
                            .queryParam("start_date", dataCotacao)
                            .queryParam("end_date", dataCotacao)
                            .query(env.getProperty(API_KEY_PROPERTY))
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse ->  Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL inválida")))
                    .bodyToMono(JsonNode.class).block();

            if (block != null) {
                if (block.hasNonNull("dataset")) {
                    if (block.get("dataset").hasNonNull("data")) {
                        JsonNode jsonNode = block.get("dataset");
                        try {
                            CotacaoDTO cotacaoDTO = converterJsonToDTO(jsonNode);
                            return processarCotacaoService.processarCotacao(cotacaoDTO);
                        } catch (JsonProcessingException e) {
                            log.error("Falha ao realizar parser JSON: " + jsonNode.toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 429) {
                throw new Exception("Atingido número máximo de requisições para: " + env.getProperty(CEPEA_API_URL_PROPERTY));
            }
            throw e;
        }

        return null;
    }

    private Cotacao buscarCotacaoBD(LocalDate dataCotacao, String cultura) {
        return cotacaoService.buscarPorDataCotacaoECultura(dataCotacao, cultura);
    }

    private void salvarCotacao(Cotacao cotacao) {
        cotacaoService.novo(cotacao);
    }

    private void validarParametros(String cultura, LocalDate dataCotacao) {
        if (StringUtils.isBlank(cultura)){
            throw new IllegalArgumentException("O parâmetor 'cultura' não pode ser nulo ou vazio");
        }
        if (dataCotacao == null) {
            throw new IllegalArgumentException("O parâmetor 'dataCotacao' não pode ser nulo");
        }
    }
}
