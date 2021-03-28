package com.publicis.sapient.weather.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publicis.sapient.weather.dto.WeatherData;
import com.publicis.sapient.weather.dto.WeatherResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Component
public class WeatherExchange {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherExchange.class);
	private final WeatherRestTemplate restTemplate = WeatherRestTemplate.getInstance();
	private final WeatherRestTemplate restTemplateSSL = WeatherRestTemplate.getInstanceSSL();

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private CommonConstants commonConstants;

	@Value("${application.base.url}")
	private String baseUrl;

	public WeatherResponse restExchangeGeneric(final Map<String, Object> headerMap, final Object payloadDto,
											   final String targetUrl,
											   final Map<String, Object> queryMap, final HttpMethod method,
											   final String errString, boolean isSSL) {
		WeatherResponse weatherResponse = new WeatherResponse();
		WeatherData weatherData = null;


		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		for (final Map.Entry<String, Object> entry : headerMap.entrySet()) {
			if (entry.getValue() != null) {
				final String strValue = String.valueOf(entry.getValue());
				if (StringUtils.isNotBlank(strValue)) {
					headers.add(entry.getKey(), strValue);
				}
			}
		}

		HttpEntity<String> requestEntity = null;
		if (payloadDto == null) {
			requestEntity = new HttpEntity<>(headers);
		} else {
			requestEntity = new HttpEntity<>(Utility.toJson(payloadDto), headers);
		}

		String urlResult = baseUrl+ targetUrl;

		final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlResult);

		for (final Map.Entry<String, Object> entry : queryMap.entrySet()) {
			if (entry.getValue() != null) {
				if (entry.getValue() instanceof String) {
					final String strValue = String.valueOf(entry.getValue());
					if (StringUtils.isNotBlank(strValue)) {
						builder.queryParam(entry.getKey(), strValue);
					}
				} else {
					builder.queryParam(entry.getKey(), entry.getValue());
				}
			}
		}

		final URI uri = builder.build().encode().toUri();
		final ResponseEntity<String> response;
		if(isSSL)
			response = restTemplateSSL.exchangeSSL(uri, method, null, String.class);
		else
			response = restTemplate.exchange(uri, method, null, String.class);

		final String body = response.getBody();
		try {
			weatherData = (WeatherData) mapper.readValue(body, new TypeReference<WeatherData>() {});
		} catch (final IOException e) {
			LOGGER.error(errString + e.getMessage(), e);
			weatherResponse.setMessage(body);
		}
		weatherResponse.setStatus(response.getStatusCode().value());
		weatherResponse.setResponseHeaders(response.getHeaders());
		weatherResponse.setResponse(weatherData);

		return weatherResponse;
	}
}