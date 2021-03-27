package com.publicis.sapient.weather.controller;

import com.publicis.sapient.weather.dto.WeatherResponse;
import com.publicis.sapient.weather.service.WeatherService;
import com.publicis.sapient.weather.validator.ValidateInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);
    @Autowired
    WeatherService weatherService;

    @Autowired
    ValidateInput inputData;

    @GetMapping("/weather/{version}/forecast")
    public ResponseEntity<WeatherResponse> getWeatherInfoByCityName(@PathVariable("version") final String version,
       @RequestParam(value = "city", required = true) String filter, @RequestParam(value = "days", defaultValue = "3") Integer days) {
        LOGGER.info("Executing getWeatherInfoByCityName with user input version: {} city {} days {} ", version, filter, days );
        if(days !=3) {
            ResponseEntity<WeatherResponse> responseEntity = inputData.validateInputData(days);
            if(!responseEntity.getStatusCode().equals(HttpStatus.OK))
                return (ResponseEntity<WeatherResponse>) responseEntity;
        }
        return weatherService.getWeatherInfo(version, filter, days);
    }

}
