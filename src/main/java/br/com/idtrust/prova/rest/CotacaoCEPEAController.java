package br.com.idtrust.prova.rest;

import br.com.idtrust.prova.service.BuscarCotacaoAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cotacao-cepea")
public class CotacaoCEPEAController {

    @Autowired
    private BuscarCotacaoAPIService buscarCotacaoAPIService;

    @GetMapping("/{cultura}/{dataCotacao}")
    @ResponseBody
    public ResponseEntity<?> obterCotacaoCEPEA(@PathVariable("cultura") String cultura,
                                               @PathVariable("dataCotacao")
                                               @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dataCotacao) throws Exception {
        return ResponseEntity.ok(buscarCotacaoAPIService.obterCotacaoCEPEA(cultura, dataCotacao));
    }
}
