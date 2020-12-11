package br.com.idtrust.prova.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class CotacaoDTO {

    @JsonProperty("dataset_code")
    private String culture;

    @JsonProperty("end_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate cotationDate;

    @JsonIgnore
    private BigDecimal priceUSD;
    @JsonIgnore
    private BigDecimal dolarBRL;

    @JsonIgnore
    private BigDecimal priceBRL;

    @JsonProperty("data")
    private List<DataDTO> data;

    public BigDecimal getPriceBRL() {
        if (priceBRL != null) {
            return priceBRL;
        }
        if (data != null && !data.isEmpty()) {
            BigDecimal priceUSD = new BigDecimal(data.get(0).getPriceUSD());
            if (dolarBRL != null) {
                this.priceBRL = priceUSD.multiply(dolarBRL).setScale(2, RoundingMode.DOWN);
                return this.priceBRL;
            }
        }
        return new BigDecimal(0);
    }

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @Data
    public static class DataDTO {
        @JsonProperty() String date;
        @JsonProperty() Double priceUSD;
        @JsonProperty() Double dailyVariation;
        @JsonProperty() Double monthlyVariation;
    }

}
