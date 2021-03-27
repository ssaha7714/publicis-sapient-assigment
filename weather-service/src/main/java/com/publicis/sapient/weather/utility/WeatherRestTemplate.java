/*******************************************************************************
 * Copyright (c)  2018, RS Software India Pvt. Ltd. (RS). All rights reserved.
 *  
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions
 *   are met:
 *  
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *  
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *  
 *     - Neither the name of RS or the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *  
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 *   IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 *   THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *   PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *   CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.publicis.sapient.weather.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class WeatherRestTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherRestTemplate.class);

	private static int MAX_RETRY_COUNT = 1;

	private RestTemplate restTemplate;
	private static WeatherRestTemplate weatherRestTemplate;

	private WeatherRestTemplate() {
		restTemplate = new RestTemplate(getClientHttpRequestFactory());
		restTemplate.setErrorHandler(new WeatherRestResponseErrorHandler());
	}
	
	private WeatherRestTemplate(ClientHttpRequestFactory client) {
		restTemplate = new RestTemplate(getClientHttpRequestFactory());
		restTemplate.setErrorHandler(new WeatherRestResponseErrorHandler());
	}

	public synchronized static WeatherRestTemplate getInstance() {
		if (weatherRestTemplate == null)
			weatherRestTemplate = new WeatherRestTemplate();
		return weatherRestTemplate;
	}
	
	public synchronized static WeatherRestTemplate getWithProxyInstance(ClientHttpRequestFactory client) {
		if (weatherRestTemplate == null)
			weatherRestTemplate = new WeatherRestTemplate(client);
		return weatherRestTemplate;
	}
	
	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 5000;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(timeout);
		return clientHttpRequestFactory;
	}
	
	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType) {
		int i = 0;
		while (true) {
			try {
				return restTemplate.getForEntity(url, responseType);
			} catch (RestClientException e) {
				if (i >= MAX_RETRY_COUNT) {
					throw e;
				}
			}
			i++;
		}
	}


	public <T> ResponseEntity<T> exchange(URI uri, HttpMethod method, HttpEntity<String> requestEntity,
			Class<T> responseType) {
		int i = 0;
		while (true) {
			try {
				LOGGER.info("Inside WeatherRestTemplate. Before calling exchange..");
				return restTemplate.exchange(uri, method, requestEntity, responseType);
			} catch (RestClientException e) {
				LOGGER.error("Inside WeatherRestTemplate. Error while accessing resource API {} method {} ", uri, method);
				if (i >= MAX_RETRY_COUNT) {
					throw e;
				}
			}
			i++;
		}

	}
}
