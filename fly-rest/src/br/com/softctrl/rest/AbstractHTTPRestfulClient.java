package br.com.softctrl.rest;

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

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.softctrl.http.util.HTTPStatusCode;
import br.com.softctrl.rest.listener.RequestFinishedListener;
import br.com.softctrl.rest.listener.ResponseErrorListener;
import br.com.softctrl.rest.listener.ResponseListener;

/**
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public abstract class AbstractHTTPRestfulClient<T> {

	private int mConnectTimeout = Constants.CONNECT_TIMEOUT;
	private int mReadTimeout = Constants.READ_TIMEOUT;
	private ResponseListener<T> mResponseListener;
	private ResponseErrorListener mResponseErrorListener;
	private RequestFinishedListener<T> mRequestFinishedListener;

	/**
	 * @param responseListener
	 * @param responseErrorListener
	 * @param requestFinishedListener
	 */
	public AbstractHTTPRestfulClient(final ResponseListener<T> responseListener,
			final ResponseErrorListener responseErrorListener,
			final RequestFinishedListener<T> requestFinishedListener) {
		this.mResponseListener = responseListener;
		this.mResponseErrorListener = responseErrorListener;
		this.mRequestFinishedListener = requestFinishedListener;
	}

	/**
	 * @param inputStream
	 * @return
	 */
	private static final String inputStreamToString(InputStream inputStream) {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		if (inputStream != null) {
			final byte[] buffer = new byte[1024];
			int length = 0;
			try {
				while ((length = inputStream.read(buffer)) != -1) {
					byteArrayOutputStream.write(buffer, 0, length);
				}
				return byteArrayOutputStream.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @param connectTimeout
	 * @return
	 */
	public AbstractHTTPRestfulClient<T> setConnectTimeout(int connectTimeout) {
		this.mConnectTimeout = connectTimeout;
		return this;
	}

	/**
	 * @param readTimeout
	 * @return
	 */
	public AbstractHTTPRestfulClient<T> setReadTimeout(int readTimeout) {
		this.mReadTimeout = readTimeout;
		return this;
	}

	/**
	 * @param url
	 * @param httpMethod
	 * @return
	 */
	private final HttpURLConnection newHttpConnection(HttpMethod httpMethod, String url) {

		URL uri;
		HttpURLConnection connection = null;
		try {
			uri = new URL(url);
			connection = (HttpURLConnection) uri.openConnection();
			connection.setRequestMethod(httpMethod.getName());
			connection.setConnectTimeout(this.mConnectTimeout);
			connection.setReadTimeout(this.mReadTimeout);
			connection.setRequestProperty(Constants.ACCEPT_ENCODING, Constants.UTF_8);
			connection.setRequestProperty(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;

	}

	/**
	 * @param request
	 * @return
	 */
	private Response<T> perform(final Request<T> request) {

		if (request == null)
			throw new RuntimeException("You need to send a request data.");
		Response<T> result = null;
		final HttpURLConnection connection = newHttpConnection(request.getHttpMethod(), request.getUrl());
		try {
			final String parameters = request.getParameters();
			connection.setDoInput(true);
			final boolean isPOST = HttpMethod.POST.equals(request.getHttpMethod());
			connection.setDoOutput(isPOST);
			// If is a POST:
			if (isPOST) {
				// If exists parameters:
				if (parameters != null) {
					OutputStream outputStream = connection.getOutputStream();
					BufferedWriter parametersWriter = new BufferedWriter(
							new OutputStreamWriter(outputStream, Constants.UTF_8));
					parametersWriter.write(parameters);
					parametersWriter.flush();
					parametersWriter.close();
					outputStream.close();
				}
				// If exists body:
				if (request.getBody() != null) {
					DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
					dataOutputStream.writeBytes(request.getBody().toString());
					dataOutputStream.flush();
					dataOutputStream.close();
				}
			}
			connection.connect();
			final String response = inputStreamToString(connection.getInputStream());
			result = request.parseResponse(connection.getResponseCode(), response);
			this.mResponseListener.onResponse(result.getResult());
		} catch (IOException e) {
			try {
				final String response = inputStreamToString(connection.getErrorStream());
				final HTTPStatusCode.StatusCode statusCode = HTTPStatusCode
						.resolveStatusCode(connection.getResponseCode());
				this.mResponseErrorListener.onResponseError(statusCode, response, e);
			} catch (IOException e1) {
				this.mResponseErrorListener.onResponseError(null, null, e1);
			}
		}
		return result;

	}

	/**
	 * @param url
	 * @param body
	 * @param parameters
	 */
	public synchronized final void send(final HttpMethod httpMethod, final String url, final T body,
			final Parameter... parameters) {
		final Request<T> request = createRequest(httpMethod, url, body, parameters);
		send((Request<T>) request);

	}

	/**
	 * @return
	 */
	protected abstract Request<T> createRequest(final HttpMethod httpMethod, final String url, final T body,
			final Parameter... parameters);

	public synchronized final void send(final Request<T> request) {
		final Response<T> response = perform(request);
		final HTTPStatusCode.StatusCode statusCode = (response == null ? null : response.getStatusCode());
		final T result = (response == null ? null : response.getResult());
		this.mRequestFinishedListener.onRequestFinished(statusCode, result);
	}

	/**
	 * @param url
	 * @param body
	 * @param parameters
	 */
	public synchronized final void get(final String url, final T body, final Parameter... parameters) {
		this.send(HttpMethod.GET, url, body, parameters);
	}

	/**
	 * @param url
	 * @param body
	 * @param parameters
	 */
	public synchronized final void post(final String url, final T body, final Parameter... parameters) {
		this.send(HttpMethod.POST, url, body, parameters);
	}

	/**
	 * @param url
	 * @param body
	 * @param parameters
	 */
	public synchronized final void delete(final String url, final T body, final Parameter... parameters) {
		this.send(HttpMethod.DELETE, url, body, parameters);
	}

	public static final class Constants {

		public static final int CONNECT_TIMEOUT = 5000; // 5 Segundos
		public static final int READ_TIMEOUT = CONNECT_TIMEOUT;

		public static final String ACCEPT_ENCODING = "Accept-Encoding";
		public static final String UTF_8 = "UTF-8";
		public static final String CONTENT_TYPE = "Content-Type";
		public static final String APPLICATION_JSON = "application/json";

	}

}
