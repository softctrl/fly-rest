package br.com.softctrl.http.rest;

import java.io.InputStream;

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

import java.util.HashSet;
import java.util.Set;

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

/**
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public abstract class Request<R, S> {

	private HttpMethod mHttpMethod = HttpMethod.GET; // Default method
	private String mUrl;
	private Set<Parameter> mParameters = new HashSet<Parameter>();
	private Set<Property> mProperties = new HashSet<Property>();
	private R mBody;

	public Request(HttpMethod httpMethod, String url, R body) {
		this.mHttpMethod = httpMethod;
		this.mUrl = url;
		this.mBody = body;
	}

	public Request<R, S> addParameter(String name, String value) {
		return this.addParameter(new Parameter(name, value));
	}

	public Request<R, S> addParameter(Parameter parameter) {
		this.mParameters.add(parameter);
		return this;
	}

	public Request<R, S> addProperty(String name, String value) {
		return this.addProperty(new Property(name, value));
	}

	public Request<R, S> addProperty(Property property) {
		this.mProperties.add(property);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public HttpMethod getHttpMethod() {
		return mHttpMethod;
	}

	/**
	 * 
	 * @return
	 */
	public String getUrl() {
		final StringBuilder url = new StringBuilder(this.mUrl);
		if (HttpMethod.GET.equals(this.getHttpMethod())) {
			url.append('?').append(this.getParameters());
		}
		return url.toString();
	}

	/**
	 * 
	 * @return
	 */
	public R getBody() {
		return mBody;
	}

	/**
	 * We need implement this method to get bytes from body.
	 * 
	 * @return
	 */
	public byte[] bodyToByteArray() {
		throw new NoSuchMethodError("You need to implement this method.");
	}

	/**
	 * 
	 * @return
	 */
	public String getParameters() {

		if (this.mParameters.size() > 0) {
			StringBuilder parameters = new StringBuilder();
			Parameter[] params = this.mParameters.toArray(new Parameter[] {});
			parameters.append(params[0].toString());
			for (int idx = 1; idx < params.length; idx++) {
				parameters.append('&').append(params[idx].toString());
			}
			return parameters.toString();
		}
		return null;

	}

	/**
	 * @return the mProperties
	 */
	public Set<Property> getProperties() {
		return mProperties;
	}

	/**
	 * We need implement this method to parse the response.
	 * 
	 * @param statusCode
	 * @param result
	 * @return
	 */
	public abstract Response<S> parseResponse(int statusCode, InputStream result);

}