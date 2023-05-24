package com.hsenid.surveyapp.controler;

import com.hsenid.surveyapp.controller.SurveyController;
import com.hsenid.surveyapp.dto.SurveyResponseDto;
import com.hsenid.surveyapp.model.Question;
import com.hsenid.surveyapp.model.Survey;
import com.hsenid.surveyapp.model.User;
import com.hsenid.surveyapp.service.SurveyService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SurveyControllerTest {

    @InjectMocks
    private SurveyController surveyController;

    @Mock
    private SurveyService surveyService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(surveyController).build();
    }

    @Test
    public void testGetAllSurveys() throws Exception {
        List<SurveyResponseDto> surveys = Arrays.asList(
                new SurveyResponseDto("646a45c8827bd00967181ae3", "Survey 01", "description edited suvey 01", null, null),
                new SurveyResponseDto("646a45c8827bd00967181ae4", "Survey 02", "description edited suvey 02", null, null)
        );

        Mockito.when(surveyService.getAllSurveysWithQuestions()).thenReturn(surveys);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/survey/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("646a45c8827bd00967181ae3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("Survey 01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is("646a45c8827bd00967181ae4")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", Matchers.is("Survey 02")));

        Mockito.verify(surveyService).getAllSurveysWithQuestions();
    }

    @Test
    public void testGetSurveyById() throws Exception {
        SurveyResponseDto survey = new SurveyResponseDto("646a45c8827bd00967181ae3", "Survey 01", "description edited suvey 01", null, null);

        Mockito.when(surveyService.getSurveyById("646a45c8827bd00967181ae3")).thenReturn(survey);

        mockMvc.perform(MockMvcRequestBuilders.get("api/survey/{id}", "646a45c8827bd00967181ae3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("646a45c8827bd00967181ae3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Survey 01")));

        Mockito.verify(surveyService).getSurveyById("646a45c8827bd00967181ae3");
    }


}
