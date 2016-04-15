package br.com.softctrl.net.rest;

import java.io.InputStream;

import br.com.softctrl.net.rest.listener.RequestFinishedListener;
import br.com.softctrl.net.rest.listener.ResponseErrorListener;
import br.com.softctrl.net.rest.listener.ResponseListener;

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
public class DownloadFileHTTPRestfulClient extends AbstractHTTPRestfulClient<String, InputStream> {
	
	/**
	 * 
	 */
	public DownloadFileHTTPRestfulClient() {
		super();
	}	

	/**
	 * @param responseListener
	 * @param responseErrorListener
	 * @param requestFinishedListener
	 */
	public DownloadFileHTTPRestfulClient(ResponseListener<InputStream> responseListener,
			ResponseErrorListener responseErrorListener, RequestFinishedListener<InputStream> requestFinishedListener) {
		super(responseListener, responseErrorListener, requestFinishedListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.net.rest.AbstractHTTPRestfulClient#createRequest(br.com.
	 * softctrl.net.rest.HttpMethod, java.lang.String, java.lang.Object,
	 * br.com.softctrl.net.rest.Parameter[],
	 * br.com.softctrl.net.rest.Property[])
	 */
	@Override
	protected Request<String, InputStream> createRequest(HttpMethod httpMethod, String url, String body,
			Parameter[] parameters, Property[] properties) {

		final Request<String, InputStream> request = new Request<String, InputStream>(httpMethod, url, body) {
			@Override
			public Response<InputStream> parseResponse(int statusCode, InputStream in) {
				return new Response<InputStream>(statusCode, in);
			}

			@Override
			public byte[] bodyToByteArray() {
				return (getBody() + "").getBytes();
			}
		};
		this.loadData(request, parameters, properties);
		return request;

	}

}
