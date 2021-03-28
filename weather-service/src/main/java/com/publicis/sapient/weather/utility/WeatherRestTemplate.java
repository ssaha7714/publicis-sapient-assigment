package com.publicis.sapient.weather.utility;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class WeatherRestTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherRestTemplate.class);

	private static int MAX_RETRY_COUNT = 1;

	private RestTemplate restTemplate;
	private RestTemplate restTemplateSSL;
	private static WeatherRestTemplate weatherRestTemplate;
	private static WeatherRestTemplate weatherRestTemplateSSL;

	private WeatherRestTemplate() {
		restTemplate = new RestTemplate(getClientHttpRequestFactory());
		restTemplate.setErrorHandler(new WeatherRestResponseErrorHandler());
	}

	private WeatherRestTemplate(boolean ssl) {
		restTemplateSSL = new RestTemplate(getClientHttpRequestFactorySSL());
		restTemplateSSL.setErrorHandler(new WeatherRestResponseErrorHandler());
	}


	public synchronized static WeatherRestTemplate getInstance() {
		if (weatherRestTemplate == null)
			weatherRestTemplate = new WeatherRestTemplate();
		return weatherRestTemplate;
	}

	public synchronized static WeatherRestTemplate getInstanceSSL() {
		if (weatherRestTemplateSSL == null)
			weatherRestTemplateSSL = new WeatherRestTemplate(true);
		return weatherRestTemplateSSL;
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(5000);
		return clientHttpRequestFactory;
	}

	// Below code snippet bypasses the certificate validation error
	// Ref: https://stackoverflow.com/questions/4072585/disabling-ssl-certificate-validation-in-spring-resttemplate
	private ClientHttpRequestFactory getClientHttpRequestFactorySSL() {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = null;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(5000);
		clientHttpRequestFactory.setHttpClient(httpClient);

		return clientHttpRequestFactory;
	}

	/**
	 * This method will be used for non SSL API calls
	 *
	 * @param uri
	 * @param method
	 * @param requestEntity
	 * @param responseType
	 * @param <T>
	 * @return
	 */
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

	/**
	 * This method would be used for such API calls where we need to bypass certificate validation error
	 *
	 * @param uri
	 * @param method
	 * @param requestEntity
	 * @param responseType
	 * @param <T>
	 * @return
	 */
	public <T> ResponseEntity<T> exchangeSSL(URI uri, HttpMethod method, HttpEntity<String> requestEntity,
										  Class<T> responseType) {
		int i = 0;
		while (true) {
			try {
				LOGGER.info("Inside WeatherRestTemplate. Before calling exchange..");
				return restTemplateSSL.exchange(uri, method, requestEntity, responseType);
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
