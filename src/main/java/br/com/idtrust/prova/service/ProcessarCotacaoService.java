package br.com.idtrust.prova.service;

import br.com.idtrust.prova.dto.CotacaoDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

import static br.com.idtrust.prova.config.WebClientConfiguration.*;

@Service
public class ProcessarCotacaoService {

    private static final Logger log = LoggerFactory.getLogger(ProcessarCotacaoService.class);


    private Environment env;
    private final WebClient defaultWebClient;

    @Autowired
    public ProcessarCotacaoService(WebClient defaultWebClient, Environment env) {
        this.defaultWebClient = defaultWebClient;
        this.env = env;
    }

    public CotacaoDTO processarCotacao(CotacaoDTO cotacaoDTO) throws Exception {
        if (cotacaoDTO == null) {
            throw new IllegalArgumentException("cotacaoDTO não pode ser nulo");
        }
        if (cotacaoDTO.getDolarBRL() == null) {
            buscarCotacaoDolar(cotacaoDTO);
        }
        mostrarCotacao(cotacaoDTO);
        return cotacaoDTO;
    }

    private void buscarCotacaoDolar(CotacaoDTO cotacaoDTO) throws Exception {
        JsonNode block = null;
        try {
             block = this.defaultWebClient.get()
                    .uri(env.getProperty(MOEDAS_API_URL_PROPERTY), uriBuilder -> uriBuilder
                            .path("/" + URI_MOEDA_USD_BRL)
                            .build())
                    .retrieve().bodyToMono(JsonNode.class).block();
        } catch (WebClientResponseException e) {
             e.printStackTrace();
        }

        if (block != null) {
            JsonNode jsonNode = block.get(0);
            if (jsonNode.hasNonNull("bid")) {
                cotacaoDTO.setDolarBRL(new BigDecimal(jsonNode.get("bid").asDouble()).setScale(2, RoundingMode.DOWN));
            } else {
                throw new Exception("Falha ao obter cotação do dólar. Atributo desconhecido");
            }
        } else {
            throw new Exception("Não foi possível obter a cotação do dólar");
        }
    }

    private void mostrarCotacao(CotacaoDTO cotacaoDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println();
        System.out.println("##########################################");
        System.out.println("--");
        System.out.println("--  COTAÇÃO DO DIA "+ formatter.format(cotacaoDTO.getCotationDate()));
        System.out.println("--");
        System.out.println("--  CULTURA: " +  cotacaoDTO.getCulture());
        System.out.println("--  VALOR EM R$: " + cotacaoDTO.getPriceBRL() );
        System.out.println("--");
        System.out.println("##########################################");
        System.out.println();

    }
}
