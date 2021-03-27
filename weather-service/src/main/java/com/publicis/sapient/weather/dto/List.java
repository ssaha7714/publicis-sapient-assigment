
package com.publicis.sapient.weather.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.publicis.sapient.weather.utility.DateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class List {

    private Integer dt;
    private Main main;
    private java.util.List<Weather> weather = null;
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private Snow snow;
    private Sys sys;
    @JsonDeserialize(using = DateSerializer.class)
    private LocalDate dt_txt;

    private Map<String, Object> additionalFields = new HashMap<>();


}
