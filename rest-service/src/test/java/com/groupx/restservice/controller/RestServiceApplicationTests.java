package com.groupx.restservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupx.restservice.model.CurrentPercentage;
import com.groupx.restservice.model.HourlyAggregate;
import com.groupx.restservice.service.EnergyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnergyController.class)
@Import(EnergyControllerTests.TestConfig.class)
class EnergyControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EnergyService service;

	@Autowired
	private ObjectMapper mapper;

	@TestConfiguration
	static class TestConfig {
		@Bean
		public EnergyService energyService() {
			// register a Mockito mock as the EnergyService bean
			return Mockito.mock(EnergyService.class);
		}
	}

	@Test
	@DisplayName("GET /energy/current returns 200 and JSON body")
	void testGetCurrent() throws Exception {
		CurrentPercentage cp = new CurrentPercentage();
		cp.setHour(LocalDateTime.of(2025,6,22,14,0));
		cp.setCommunityDepleted(75.5);
		cp.setGridPortion(24.5);

		Mockito.when(service.getCurrentPercentage()).thenReturn(cp);

		mockMvc.perform(get("/energy/current"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.communityDepleted").value(75.5))
				.andExpect(jsonPath("$.gridPortion").value(24.5))
				.andExpect(jsonPath("$.hour").value("2025-06-22T14:00:00"));
	}

	@Test
	@DisplayName("GET /energy/historical returns list between dates")
	void testGetHistorical() throws Exception {
		HourlyAggregate h1 = new HourlyAggregate();
		h1.setHour(LocalDateTime.of(2025,6,22,12,0));
		h1.setCommunityProduced(100);
		h1.setCommunityUsed(90);
		h1.setGridUsed(10);

		HourlyAggregate h2 = new HourlyAggregate();
		h2.setHour(LocalDateTime.of(2025,6,22,13,0));
		h2.setCommunityProduced(120);
		h2.setCommunityUsed(110);
		h2.setGridUsed(10);

		Mockito.when(service.getHistorical(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(List.of(h1, h2));

		mockMvc.perform(get("/energy/historical")
						.param("start", "2025-06-22T12:00:00")
						.param("end",   "2025-06-22T14:00:00"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].hour").value("2025-06-22T12:00:00"))
				.andExpect(jsonPath("$[0].communityProduced").value(100.0))
				.andExpect(jsonPath("$[0].communityUsed").value(90.0))
				.andExpect(jsonPath("$[0].gridUsed").value(10.0))
				.andExpect(jsonPath("$[1].hour").value("2025-06-22T13:00:00"))
				.andExpect(jsonPath("$[1].gridUsed").value(10.0));
	}
}
