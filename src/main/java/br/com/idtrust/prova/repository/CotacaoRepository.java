package br.com.idtrust.prova.repository;

import br.com.idtrust.prova.entity.Cotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CotacaoRepository extends JpaRepository<Cotacao, Long> {

    Cotacao findByCotationDateAndCulture(LocalDate data, String culture);
}
