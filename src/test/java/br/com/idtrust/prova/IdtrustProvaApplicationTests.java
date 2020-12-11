package br.com.idtrust.prova;

import br.com.idtrust.prova.entity.Cotacao;
import br.com.idtrust.prova.rest.CotacaoCEPEAController;
import br.com.idtrust.prova.service.BuscarCotacaoAPIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class IdtrustProvaApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BuscarCotacaoAPIService buscarCotacaoAPIService;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(CotacaoCEPEAController.class).build();
	}

	@Test
	void case1() throws Exception {
		ResultActions response = mockMvc.perform(
				get("/cotacao-cepea/COTTON_D/12-03-2020")
						.contentType(MediaType.TEXT_HTML));

		response.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{'priceBRL':304.51}"));
	}


	@Test
	void case2() throws Exception {
		ResultActions response = mockMvc.perform(
				get("/cotacao-cepea/cdswcsdv/12-03-2020")
						.contentType(MediaType.TEXT_HTML));

		response.andExpect(status().isBadRequest());
	}


	@Test
	void case3() throws Exception {
		ResultActions response = mockMvc.perform(
				get("/cotacao-cepea/cdswcsdv/20-90-2020")
						.contentType(MediaType.TEXT_HTML));

		response.andExpect(status().isBadRequest());
	}
}


