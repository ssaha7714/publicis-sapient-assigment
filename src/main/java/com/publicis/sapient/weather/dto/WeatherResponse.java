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
package com.publicis.sapient.weather.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class WeatherResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private Integer status;
	private String message;
	private Object response;
	private HttpHeaders responseHeaders;


	public WeatherResponse(HttpStatus status, String message) {
		this(status, message, null, null);
	}

	public WeatherResponse(HttpStatus status, Object response) {
		this(status, null, response, null);
	}

	public WeatherResponse(HttpStatus status, String message, Object response) {
		this(status, message, response, null);
	}

	public WeatherResponse(HttpStatus status, String message, HttpHeaders responseHeaders) {
		this(status, message, null, responseHeaders);
	}

	public WeatherResponse(HttpStatus status, Object response, HttpHeaders responseHeaders) {
		this(status, null, response, responseHeaders);
	}

	public WeatherResponse(HttpStatus status, String message, Object response, HttpHeaders responseHeaders) {
		super();
		this.status = status.value();
		this.message = message;
		this.response = response;
		this.responseHeaders = responseHeaders;
	}

	@Override
	public String toString() {
		return "WeatherResponse [status=" + status + ", message=" + message + ", response=" + response
				+ ", responseHeaders=" + responseHeaders + "]";
	}
}