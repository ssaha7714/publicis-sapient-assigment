
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
public class Weather {

    private Integer id;
    private String main;
    private String description;
    private String icon;
    private Map<String, Object> additionalFields = new HashMap<>();
}
