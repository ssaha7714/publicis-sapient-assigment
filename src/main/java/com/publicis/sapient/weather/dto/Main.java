
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
public class Main {

    private Double temp;
    private Double temp_min;
    private Double temp_max;
    private Double pressure;
    private Double sea_level;
    private Double grnd_level;
    private Integer humidity;
    private Integer temp_kf;
    private Map<String, Object> additionalProperties = new HashMap<>();
}
