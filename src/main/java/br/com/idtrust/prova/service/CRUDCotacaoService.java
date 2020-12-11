package br.com.idtrust.prova.service;

import br.com.idtrust.prova.entity.Cotacao;
import br.com.idtrust.prova.repository.CotacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CRUDCotacaoService {

    private static final Logger log = LoggerFactory.getLogger(CRUDCotacaoService.class);
    private CotacaoRepository repository;


    @Autowired
    public CRUDCotacaoService(CotacaoRepository repository) {
        this.repository = repository;
    }

    public Cotacao novo(Cotacao cotacao) {
        return repository.saveAndFlush(cotacao);
    }

    public Cotacao buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Recurso não localizado para o ID informado"));
    }
    public Cotacao buscarPorDataCotacaoECultura(LocalDate data, String cultura) {
        return repository.findByCotationDateAndCulture(data, cultura);
    }

    public List<Cotacao> buscarTodos() {
        return repository.findAll(Sort.by(Sort.Order.asc("cotation_date")));
    }

    public Cotacao atualizar(Cotacao cotacao) {
        Cotacao cotacaoLocalizada = buscarPorId(cotacao.getId());
        BeanUtils.copyProperties(cotacao, cotacaoLocalizada, "id");
        return repository.saveAndFlush(cotacao);
    }

    public void deletar(Cotacao cotacao) {
        repository.delete(cotacao);
    }
}
