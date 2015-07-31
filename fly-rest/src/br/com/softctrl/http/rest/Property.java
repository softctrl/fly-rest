package br.com.softctrl.http.rest;

/*
The MIT License (MIT)

Copyright (c) 2015 Carlos Timoshenko Rodrigues Lopes
http://www.0x09.com.br

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import static java.net.URLEncoder.encode;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public final class Property {
	private String mKey;
	private String mValue;

	public Property(String key, String value) {
		this.mKey = key;
		this.mValue = value;
	}

	@Override
	public boolean equals(Object o) {
		return Objects.equals(this.mKey, ((Property) o).mKey);		
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return mKey;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return mValue;
	}
	
	public synchronized static final Property getBasicHttpAuthenticationProperty(String username, String password) {
		StringBuilder encoded = new StringBuilder("Basic ").append(Base64.getEncoder()
				.encodeToString((new StringBuilder(username).append(':').append(password)).toString().getBytes()));
		return new Property("Authorization", encoded.toString());
	}


	@Override
	public String toString() {
		try {
			return (new StringBuilder(encode(this.mKey, AbstractHTTPRestfulClient.Constants.UTF_8))).append('=')
					.append(encode(this.mValue, AbstractHTTPRestfulClient.Constants.UTF_8)).toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return String.format("%s=%s", this.mKey, this.mValue);
	}

}
