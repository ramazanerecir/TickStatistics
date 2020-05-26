package com.solactive.tickstatistics.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solactive.tickstatistics.entity.dto.StatisticsDto;
import com.solactive.tickstatistics.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StatisticsApiImpl.class)
class StatisticsApiImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatisticsService statisticsService;

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    @Test
    void statistics() throws Exception {
        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setCount(1);

        when(statisticsService.getStatistics()).thenReturn(statisticsDto);

        MvcResult mvcResult = mockMvc.perform(get("/statistics")).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        verify(statisticsService, times(1)).getStatistics();
        assertThat(objectMapper.writeValueAsString(statisticsDto))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    void statisticsNoData() throws Exception {
        StatisticsDto statisticsDto = new StatisticsDto();

        when(statisticsService.getStatistics()).thenReturn(statisticsDto);

        MvcResult mvcResult = mockMvc.perform(get("/statistics")).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        verify(statisticsService, times(1)).getStatistics();
        assertThat(objectMapper.writeValueAsString(statisticsDto))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    void statisticsInstrument() throws Exception {
        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setCount(1);

        String instrument = "IBM.N";

        when(statisticsService.getStatistics(instrument)).thenReturn(statisticsDto);

        MvcResult mvcResult = mockMvc.perform(get("/statistics/{instrument}", instrument)).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        verify(statisticsService, times(1)).getStatistics(captor.capture());
        assertEquals(captor.getValue(), instrument);
        assertThat(objectMapper.writeValueAsString(statisticsDto))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    void statisticsInstrumentNoData() throws Exception {
        StatisticsDto statisticsDto = new StatisticsDto();

        String instrument = "IBM.N";

        when(statisticsService.getStatistics(instrument)).thenReturn(statisticsDto);

        MvcResult mvcResult = mockMvc.perform(get("/statistics/{instrument}", instrument)).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        verify(statisticsService, times(1)).getStatistics(captor.capture());
        assertEquals(captor.getValue(), instrument);
        assertThat(objectMapper.writeValueAsString(statisticsDto))
                .isEqualToIgnoringWhitespace(responseBody);
    }
}