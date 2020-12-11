package br.com.idtrust.prova.util;

import br.com.idtrust.prova.dto.CotacaoDTO;
import br.com.idtrust.prova.entity.Cotacao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CotacaoUtil {

    public static CotacaoDTO converterCotacaoToDTO(Cotacao cotacao) {
        CotacaoDTO cotacaoDTO = new CotacaoDTO();
        cotacaoDTO.setCotationDate(cotacao.getCotationDate());
        cotacaoDTO.setCulture(cotacao.getCulture());
        cotacaoDTO.setDolarBRL(cotacao.getDolarBRL());
        cotacaoDTO.setPriceUSD(cotacao.getPriceUSD());
        cotacaoDTO.setPriceBRL(cotacao.getPriceBRL());
        return cotacaoDTO;
    }

    public static Cotacao converterDTOToCotacao(CotacaoDTO cotacaoDTO) {
        Cotacao cotacao = new Cotacao();
        cotacao.setCotationDate(cotacaoDTO.getCotationDate());
        cotacao.setCulture(cotacaoDTO.getCulture());
        cotacao.setDolarBRL(cotacaoDTO.getDolarBRL());
        cotacao.setPriceUSD(getPriceUSD(cotacaoDTO));
        cotacao.setPriceBRL(cotacaoDTO.getPriceBRL());
        return cotacao;
    }

    private static BigDecimal getPriceUSD(CotacaoDTO cotacaoDTO) {
        if (cotacaoDTO.getData() != null && !cotacaoDTO.getData().isEmpty()){
            return new BigDecimal(cotacaoDTO.getData().get(0).getPriceUSD()).setScale(2, RoundingMode.DOWN);
        }
        return null;
    }

    public static CotacaoDTO converterJsonToDTO(JsonNode dataset) throws JsonProcessingException {
        if (dataset != null) {
            //JSON input
            String json = dataset.toPrettyString();
            //Object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            //Convert JSON to DTO
            CotacaoDTO cotacaoDTO = mapper.readValue(json, CotacaoDTO.class);
            return cotacaoDTO;
        }
        return null;
    }
}
