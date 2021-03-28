package com.publicis.sapient.weather.validator;

import com.publicis.sapient.weather.dto.WeatherResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Configuration
public class ValidateInput {
    public ResponseEntity<WeatherResponse> validateInputData(Integer days) {

        WeatherResponse weatherResponse = new WeatherResponse();
        if(days <1 || days>5 ){
            weatherResponse.setMessage("Forecast can only be done on next 5 days");
            return new ResponseEntity<WeatherResponse>(new WeatherResponse(HttpStatus.PRECONDITION_FAILED, weatherResponse), HttpStatus.PRECONDITION_FAILED);
        }
        return new ResponseEntity<>(new WeatherResponse(HttpStatus.OK, weatherResponse), HttpStatus.OK);
    }
}
