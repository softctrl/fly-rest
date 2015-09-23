package br.com.softctrl.net.rest;

import static br.com.softctrl.net.util.Constants.ACCEPT_ENCODING;
import static br.com.softctrl.net.util.Constants.APPLICATION_JSON;
import static br.com.softctrl.net.util.Constants.CONNECT_TIMEOUT;
import static br.com.softctrl.net.util.Constants.CONTENT_TYPE;
import static br.com.softctrl.net.util.Constants.READ_TIMEOUT;
import static br.com.softctrl.net.util.Constants.UTF_8;
import static br.com.softctrl.net.util.StreamUtils.streamToString;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;

import br.com.softctrl.net.rest.listener.RequestFinishedListener;
import br.com.softctrl.net.rest.listener.ResponseErrorListener;
import br.com.softctrl.net.rest.listener.ResponseListener;
import br.com.softctrl.net.util.HTTPStatusCode;
import br.com.softctrl.net.util.HTTPStatusCode.StatusCode;

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
 * [R]equest
 * Re[S]ponse
 * 
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public abstract class AbstractHTTPRestfulClient<R, S> implements IRestfulClient<R, S> {
	
	private static final String TAG = AbstractHTTPRestfulClient.class.getSimpleName();

	protected int mConnectTimeout = CONNECT_TIMEOUT;
	protected Property mBasicHttpAuthentication = null;
	protected int mReadTimeout = READ_TIMEOUT;
	protected Charset mCharset = UTF_8;
	protected String mContentType = APPLICATION_JSON;

	protected ResponseListener<S> mResponseListener;
	protected ResponseErrorListener mResponseErrorListener;
	protected RequestFinishedListener<S> mRequestFinishedListener;

	protected Proxy mProxy = null;

	/**
	 * Default constructor with basic listeners.
	 */
	public AbstractHTTPRestfulClient() {
		this(new ResponseListener<S>() {
			@Override
			public void onResponse(S response) {
				System.out.format("%s - %s", TAG, response + "");
			}
		}, new ResponseErrorListener() {
			@Override
			public void onResponseError(StatusCode statusCode, String serverMessage, Throwable throwable) {
				System.out.format("%s\nMessage: %s\nStatus code: %s", TAG, serverMessage, statusCode);
				throwable.printStackTrace();
			}
		}, new RequestFinishedListener<S>() {
			@Override
			public void onRequestFinished(StatusCode statusCode, S response) {
				System.out.format("%s\nStatus code: %s\nResponse: ", TAG, statusCode, response);
			}
		});
	}

	/**
	 * @param responseListener
	 * @param responseErrorListener
	 * @param requestFinishedListener
	 */
	public AbstractHTTPRestfulClient(final ResponseListener<S> responseListener,
			final ResponseErrorListener responseErrorListener,
			final RequestFinishedListener<S> requestFinishedListener) {
		this.mResponseListener = responseListener;
		this.mResponseErrorListener = responseErrorListener;
		this.mRequestFinishedListener = requestFinishedListener;
		this.validateListeners();
	}

	/**
	 * 
	 */
	protected final void validateListeners() {
		if (this.mResponseListener == null)
			throw new IllegalArgumentException("You need to provide a valid ResponseListener.");
		if (this.mResponseErrorListener == null)
			throw new IllegalArgumentException("You need to provide a valid ResponseErrorListener.");
		if (this.mRequestFinishedListener == null)
			throw new IllegalArgumentException("You need to provide a valid RequestFinishedListener.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.net.rest.IRestfulClient#setConnectTimeout(int)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setConnectTimeout(int connectTimeout) {
		this.mConnectTimeout = connectTimeout * 1000;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.net.rest.IRestfulClient#setReadTimeout(int)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setReadTimeout(int readTimeout) {
		this.mReadTimeout = readTimeout * 1000;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.net.rest.IRestfulClient#setCharset(java.nio.charset.
	 * Charset)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setCharset(Charset charset) {
		this.mCharset = charset;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setContentType(java.lang.String)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setContentType(String contentType) {
		this.mContentType = contentType;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.net.rest.IRestfulClient#setBasicAuthentication(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setBasicAuthentication(String username, String password) {
		this.mBasicHttpAuthentication = Property.getBasicHttpAuthenticationProperty(username, password);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.net.rest.IRestfulClient#setProxy(java.net.Proxy)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setProxy(Proxy proxy) {
		this.mProxy = proxy;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.net.rest.IRestfulClient#setProxy(java.lang.String,
	 * int)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setProxy(String hostname, int port) {
		return setProxy(Proxy.Type.HTTP, hostname, port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.net.rest.IRestfulClient#setProxy(java.net.Proxy.Type,
	 * java.lang.String, int)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setProxy(Proxy.Type type, String hostname, int port) {
		this.mProxy = new Proxy(type, new InetSocketAddress(hostname, port));
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.net.rest.IRestfulClient#setProxy(java.lang.String,
	 * java.lang.String, java.lang.String, int)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setProxy(final String username, final String password, final String hostname,
			final int port) {
		Authenticator authenticator = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication(username, (password + "").toCharArray()));
			}
		};
		Authenticator.setDefault(authenticator);
		return setProxy(hostname, port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.net.rest.IRestfulClient#setProxy(java.net.Proxy.Type,
	 * java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public AbstractHTTPRestfulClient<R, S> setProxy(Proxy.Type type, final String username, final String password,
			String hostname, int port) {
		Authenticator authenticator = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication(username, (password + "").toCharArray()));
			}
		};
		Authenticator.setDefault(authenticator);
		return setProxy(type, hostname, port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.net.rest.IRestfulClient#newHttpConnection(br.com.softctrl
	 * .net.rest.HttpMethod, java.lang.String)
	 */
	@Override
	public HttpURLConnection newURLConnection(HttpMethod httpMethod, String url) {

		URL uri;
		HttpURLConnection connection = null;
		try {
			uri = new URL(url);
			if (this.mProxy == null)
				connection = (HttpURLConnection) uri.openConnection();
			else
				connection = (HttpURLConnection) uri.openConnection(mProxy);
			connection.setRequestMethod(httpMethod.getName());
			connection.setConnectTimeout(this.mConnectTimeout);
			connection.setReadTimeout(this.mReadTimeout);
			connection.setRequestProperty(ACCEPT_ENCODING, this.mCharset.name());
			connection.setRequestProperty(CONTENT_TYPE, this.mContentType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return connection;

	}
	
	/**
	 * @param request
	 * @return
	 */
	private Response<S> perform(final Request<R, S> request) {

		if (request == null)
			throw new IllegalArgumentException("You need to send a request data.");
		Response<S> result = null;
		final HttpURLConnection connection = newURLConnection(request.getHttpMethod(), request.getUrl());
		try {
			final String parameters = request.getParameters();
			connection.setDoInput(true);
			final boolean isPOST = HttpMethod.POST.equals(request.getHttpMethod());
			connection.setDoOutput(isPOST);
			
			// Basic HTTP Authentication
			if (this.mBasicHttpAuthentication != null) {
				connection.setRequestProperty(this.mBasicHttpAuthentication.getKey(),
						this.mBasicHttpAuthentication.getValue());
			}
			
			// Request properties
			final Set<Property> properties = request.getProperties();
			if (properties != null && properties.size() > 0) {
				for (Property property : properties) {
					connection.setRequestProperty(property.getKey(), property.getValue());
				}
			}
			
			// If is a POST:
			if (isPOST) {
				// If exists parameters:
				if (parameters != null) {
					OutputStream outputStream = connection.getOutputStream();
					BufferedWriter parametersWriter = new BufferedWriter(
							new OutputStreamWriter(outputStream, this.mCharset));
					parametersWriter.write(parameters);
					parametersWriter.flush();
					parametersWriter.close();
					outputStream.close();
				}
				// If exists body:
				if (request.getBody() != null) {
					DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
					dataOutputStream.write(request.bodyToByteArray());
					dataOutputStream.flush();
					dataOutputStream.close();
				}
			}
			connection.connect();
			result = request.parseResponse(connection.getResponseCode(), connection.getInputStream());
			this.mResponseListener.onResponse(result == null ? null : result.getResult());
		} catch (java.net.SocketTimeoutException e) {
			final String response = streamToString(connection.getErrorStream());
			final HTTPStatusCode.StatusCode statusCode = HTTPStatusCode.resolveStatusCode(408);
			this.mResponseErrorListener.onResponseError(statusCode, response, e);
		} catch (IOException e) {
			try {
				final String response = streamToString(connection.getErrorStream());
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
	 * 
	 * @param httpMethod
	 * @param url
	 * @param body
	 * @param parameters
	 */
	public synchronized final void send(final HttpMethod httpMethod, final String url, final R body,
			final Parameter... parameters) {
		final Request<R, S> request = createRequest(httpMethod, url, body, parameters);
		send((Request<R, S>) request);

	}

	/**
	 * 
	 * @param httpMethod
	 * @param url
	 * @param body
	 * @param parameters
	 * @return
	 */
	protected abstract Request<R, S> createRequest(final HttpMethod httpMethod, final String url, final R body,
			final Parameter... parameters);

	/* (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#send(br.com.softctrl.net.rest.Request)
	 */
	@Override
	public void send(final Request<R, S> request) {
		final Response<S> response = perform(request);
		final HTTPStatusCode.StatusCode statusCode = (response == null ? null : response.getStatusCode());
		final S result = (response == null ? null : response.getResult());
		this.mRequestFinishedListener.onRequestFinished(statusCode, result);
	}

	/**
	 * 
	 * @param url
	 * @param parameters
	 */
	public synchronized final void get(final String url, final Parameter... parameters) {
		this.send(HttpMethod.GET, url, null, parameters);
	}

	/**
	 * 
	 * @param url
	 * @param body
	 * @param parameters
	 */
	public synchronized final void get(final String url, final R body, final Parameter... parameters) {
		this.send(HttpMethod.GET, url, body, parameters);
	}

	/**
	 * @param url
	 * @param body
	 * @param parameters
	 */
	public synchronized final void post(final String url, final R body, final Parameter... parameters) {
		this.send(HttpMethod.POST, url, body, parameters);
	}

	/**
	 * @param url
	 * @param body
	 * @param parameters
	 */
	public synchronized final void delete(final String url, final R body, final Parameter... parameters) {
		this.send(HttpMethod.DELETE, url, body, parameters);
	}


}
