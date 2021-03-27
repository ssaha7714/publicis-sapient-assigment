
package com.publicis.sapient.weather.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WeatherData {

    private String cod;
    private Double message;
    private Integer cnt;
    private java.util.List<com.publicis.sapient.weather.dto.List> list = null;
    private City city;

    private Map<String, Object> additionalFields = new HashMap<>();

}
