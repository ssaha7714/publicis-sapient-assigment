package com.publicis.sapient.weather.controller;

import com.publicis.sapient.weather.dto.WeatherResponse;
import com.publicis.sapient.weather.validator.ValidateInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {
    @InjectMocks
    WeatherController weatherController;

    @Mock
    ValidateInput inputData;

    @Mock
    ResponseEntity<WeatherResponse> mockResponseEntity;

    @Test
    void testGetWeatherInfoByCityNameWhenDaysGreaterThan5() {
        //given
        ResponseEntity<WeatherResponse> responseEntity;
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setMessage("Forecast can only be done on next 5 days");
        responseEntity = new ResponseEntity<>(new WeatherResponse(HttpStatus.PRECONDITION_FAILED, weatherResponse), HttpStatus.PRECONDITION_FAILED);

        when(inputData.validateInputData(6)).thenReturn(responseEntity);

        //when
        mockResponseEntity = weatherController.getWeatherInfoByCityName("2.5", anyString(), 6);
        //then

        assertThat(mockResponseEntity.getStatusCode().value()).isEqualTo(responseEntity.getStatusCode().value());
        assertThat(((WeatherResponse) mockResponseEntity.getBody().getResponse()).getMessage()).isEqualTo(((WeatherResponse) responseEntity.getBody().getResponse()).getMessage());
    }

}

