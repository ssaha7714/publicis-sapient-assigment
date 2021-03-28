package com.publicis.sapient.weather.service;

import com.publicis.sapient.weather.dto.List;
import com.publicis.sapient.weather.dto.*;
import com.publicis.sapient.weather.utility.CommonConstants;
import com.publicis.sapient.weather.utility.WeatherExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);

    @Value("${application.id}")
    private String appId;

    @Autowired
    WeatherExchange weatherExchange;

    /**
     * Service method to get weather info for <days> days.
     *
     * @param version API version
     * @param filter city value
     * @param days days value
     * @return ResponseEntity<WeatherResponse>
     */
    public ResponseEntity<WeatherResponse> getWeatherInfo(String version, String filter, int days) {
        final String errString = "Error occurred while fetching weather data";
        Map<String, Object> vMap = new HashMap<>();
        vMap.put(CommonConstants.VERSION, version);

        final Map<String, Object> queryMap = new HashMap<>();
        queryMap.put(CommonConstants.FILTER, filter);
        queryMap.put(CommonConstants.APP_ID, appId);

        final UriTemplate url = new UriTemplate(CommonConstants.GET_WEATHER_INFO_API);

        final WeatherResponse thirdPartyWeatherData;
        try {
            thirdPartyWeatherData = weatherExchange.restExchangeGeneric(Collections.emptyMap(), null, url.expand(vMap).toString(),
                    queryMap, HttpMethod.GET, errString, true);
        } catch (final Exception e) {
            LOGGER.error(errString + e.getMessage(), e);
            return new ResponseEntity<>(new WeatherResponse(HttpStatus.INTERNAL_SERVER_ERROR, CommonConstants.SERVICE_UNKNOWN_EXCEPTION), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Filtering out required data from original set
        java.util.List<List> desiredData = filterDesiredDaysData(thirdPartyWeatherData, days);

        // Applying required business logic
        FinalResponse finalResponse = doProcessing(desiredData);
        return new ResponseEntity<>(new WeatherResponse(HttpStatus.OK, finalResponse), HttpStatus.OK);
    }

    /**
     * This method applies the logic: find out city's next 3 days (assuming current day as first day) high and
     * low temperatures. If rain is predicted in next 3 days or temperature goes above 40 degree celsius then mention
     * 'Carry umbrella' or 'Use sunscreen lotion' respectively, in the output, for that day.
     *
     * @param desiredData
     * @return
     */
    private FinalResponse doProcessing(java.util.List<List> desiredData){
        // Grouping datasets in a Map based on date
        Map<LocalDate, Set<List>> collectedData = desiredData.stream()
                .collect(Collectors.groupingBy(List::getDt_txt,
                        Collectors.mapping(item -> item, Collectors.toSet())
                ));

        FinalResponse finalResponse = new FinalResponse();
        java.util.List<DayWiseData> resultSet = new ArrayList<>();
        for (LocalDate element : collectedData.keySet()) {
            Set<Double> minTempSet = collectedData.get(element).stream().map(item -> item.getMain().getTemp_min()).collect(Collectors.toSet());
            double minTempInKelvin = minTempSet.stream().mapToDouble(value->value).min().orElseThrow(NoSuchElementException::new);

            Set<Double> maxTempSet = collectedData.get(element).stream().map(item -> item.getMain().getTemp_min()).collect(Collectors.toSet());
            double maxTempInKelvin = maxTempSet.stream().mapToDouble(value->value).max().orElseThrow(NoSuchElementException::new);

            // As default unit is Kelvin, converting to celsius
            double maxTemp = maxTempInKelvin - 273.15;

            String message = null;
            if(maxTemp>40){
                message = "Use sunscreen lotion";
            }
            Set<Double> rainCollector = collectedData.get(element).stream()
                    .filter(item->item.getRain()!=null)
                    .map(item -> item.getRain().get_3h())
                    .collect(Collectors.toSet());

            if(!rainCollector.isEmpty()){
                message = "Carry umbrella";
            }

            DayWiseData dayWiseData = new DayWiseData(element, maxTempInKelvin, minTempInKelvin, message);
            resultSet.add(dayWiseData);
        }
        // Sorting result set based on date
        Collections.sort(resultSet, (x, y) -> x.getDate().compareTo(y.getDate()));
        finalResponse.setData(resultSet);
        return  finalResponse;
    }


    /**
     * This method filters out all the data which are higher than <days> day(s).
     * For example, if <days> value is 3, we are filtering those records which fall under first 3 days.
     *
     * @param weatherResponse
     * @param days
     * @return
     */
    private java.util.List<List> filterDesiredDaysData(WeatherResponse weatherResponse, int days) {
        WeatherData response = (WeatherData)weatherResponse.getResponse();
        java.util.List<List> weatherInfo = response.getList();
        LocalDate first =  weatherInfo.get(0).getDt_txt();

        java.util.List<List> collectedData = weatherInfo.stream()
                .filter(item -> ChronoUnit.DAYS.between(first, item.getDt_txt()) <= (days - 1))
                .collect(Collectors.toList());
        return collectedData;
    }


}
