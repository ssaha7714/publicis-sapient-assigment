package com.publicis.sapient.weather.utility;

import org.springframework.stereotype.Component;

@Component
public class CommonConstants {
    public static final String GET_WEATHER_INFO_API = "/data/{version}/forecast";
    public static final String SERVICE_UNKNOWN_EXCEPTION = "Unknown error occurred while accessing service. Please try after sometime";
    public static final String VERSION = "version";
    public static final String APP_ID = "appid";
    public static final String FILTER = "q";
    public static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

}
