package com.example.currency;

import com.example.currency.api.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class CurrencyConverterServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	CurrencyService currencyService;

	@Test
	void testLoadLatestRatesAPI() throws Exception {
		mockMvc.perform(get("/api/latest")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testInvalidCurrencyCode() throws Exception {
		mockMvc.perform(get("/api/base")
				.param("base", "XYZ")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testDateParamQuerySearch() throws Exception {
		mockMvc.perform(get("/api/search")
				.param("base", "USD")
				.param("date", "2019-11-10")
				.param("symbols", "INR","EUR","CAD")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testTimePeriodQuerySearch() throws Exception {
		mockMvc.perform(get("/api/search")
				.param("base", "USD")
				.param("period.startAt", "2019-11-10")
				.param("period.endAt", "2019-11-15")
				.param("symbols", "INR,EUR,CAD")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testGetConversionConfig() throws Exception {
		mockMvc.perform(get("/api/config")
				.param("symbols", "USD,EUR")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testCsrf() throws Exception {
		mockMvc.perform(post("/api/test")
				.content("sample content")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}


}
