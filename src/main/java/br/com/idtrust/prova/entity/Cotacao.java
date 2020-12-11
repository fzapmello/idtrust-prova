package br.com.idtrust.prova.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "seq_tb_cotacao", sequenceName = "seq_tb_cotacao", allocationSize = 1)
@Table(name = "tb_cotacao")
public class Cotacao implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tb_cotacao")
    @Column(name = "id")
    private Long id;

    @Column(name = "culture")
    private String culture;
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "cotation_date")
    private LocalDate cotationDate;

    @Column(name = "price_USD")
    private BigDecimal priceUSD;

    @Column(name = "dolar_BRL")
    private BigDecimal dolarBRL;

    @Column(name = "price_BRL")
    private BigDecimal priceBRL;

}