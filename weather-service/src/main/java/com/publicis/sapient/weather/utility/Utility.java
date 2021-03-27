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

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides lots of utility methods used throughout the system
 * 
 * @author RS Software
 */
public class Utility {

	private static final JsonParser jsonParser = new JsonParser();
	private Utility() {
	}



	/**
	 * Sets the Content-Type & Accept HTTP headers for REST using JSON.
	 * 
	 * @param HttpHeaders responseHeaders
	 * @return
	 * @Return HttpHeaders
	 */
	public static HttpHeaders setJsonRestHeaders(final HttpHeaders responseHeaders) {
		final List<MediaType> acceptableMediaTypes = new ArrayList<>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		responseHeaders.setAccept(acceptableMediaTypes);
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return responseHeaders;
	}

	/**
	 * Convert object to json string
	 *
	 * @param obj
	 *            source object
	 * @return Json string
	 */
	public static String toJson(final Object obj) {
		return jsonParser.toJson(obj);
	}


}