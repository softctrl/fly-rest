/**
 * 
 */
package br.com.softctrl.net.rest;

import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

import br.com.softctrl.net.rest.listener.RequestFinishedListener;
import br.com.softctrl.net.rest.listener.ResponseErrorListener;
import br.com.softctrl.net.rest.listener.ResponseListener;
import br.com.softctrl.net.util.HTTPStatusCode.StatusCode;
import br.com.softctrl.utils.Objects;

/*
The MIT License (MIT)

Copyright (c) 2016 Carlos Timoshenko Rodrigues Lopes
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
 * 
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public class Sync<R, S> implements IRestfulClient<R, S> {
	
	private static class Wrapper<S> {
		private S value;
		public void setValue(S value) {
			this.value = value;
		}
		public S getValue() {
			return value;
		}
	}
	
	/**
	 * 
	 */
	private IRestfulClient<R, S> mRestfulClient;
	
	/**
	 * 
	 */
	public Sync(){}

	/**
	 * 
	 * @param restfulClient
	 */
	public Sync(IRestfulClient<R, S> restfulClient){
		this.mRestfulClient = Objects.requireNonNull(restfulClient, "You need to inform a valid Restful Client.");
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#getResponseListener()
	 */
	@Override public ResponseListener<S> getResponseListener() {
		return Objects.requireNonNull(this.mRestfulClient).getResponseListener();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setResponseListener(br.com.softctrl.net.rest.listener.ResponseListener)
	 */
	public IRestfulClient<R, S> setResponseListener(ResponseListener<S> responseListener) {
		Objects.requireNonNull(this.mRestfulClient).setResponseListener(responseListener);
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#getResponseErrorListener()
	 */
	@Override public ResponseErrorListener getResponseErrorListener() {
		return Objects.requireNonNull(this.mRestfulClient).getResponseErrorListener();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setResponseErrorListener(br.com.softctrl.net.rest.listener.ResponseErrorListener)
	 */
	public IRestfulClient<R, S> setResponseErrorListener(ResponseErrorListener responseErrorListener) {
		Objects.requireNonNull(this.mRestfulClient).setResponseErrorListener(responseErrorListener);
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#getRequestFinishedListener()
	 */
	@Override public RequestFinishedListener<S> getRequestFinishedListener() {
		return Objects.requireNonNull(this.mRestfulClient).getRequestFinishedListener();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setRequestFinishedListener(br.com.softctrl.net.rest.listener.RequestFinishedListener)
	 */
	public IRestfulClient<R, S> setRequestFinishedListener(RequestFinishedListener<S> requestFinishedListener) {
		Objects.requireNonNull(this.mRestfulClient).setRequestFinishedListener(requestFinishedListener);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setConnectTimeout(int)
	 */
	@Override public IRestfulClient<R, S> setConnectTimeout(int connectTimeout) {
		return Objects.requireNonNull(this.mRestfulClient).setConnectTimeout(connectTimeout);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setReadTimeout(int)
	 */
	@Override public IRestfulClient<R, S> setReadTimeout(int readTimeout) {
		return Objects.requireNonNull(this.mRestfulClient).setReadTimeout(readTimeout);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setCharset(java.nio.charset.Charset)
	 */
	@Override public IRestfulClient<R, S> setCharset(Charset charset) {
		return Objects.requireNonNull(this.mRestfulClient).setCharset(charset);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setContentType(java.lang.String)
	 */
	@Override public IRestfulClient<R, S> setContentType(String contentType) {
		return Objects.requireNonNull(this.mRestfulClient).setContentType(contentType);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setBasicAuthentication(java.lang.String, java.lang.String)
	 */
	@Override public IRestfulClient<R, S> setBasicAuthentication(String username, String password) {
		return Objects.requireNonNull(this.mRestfulClient).setBasicAuthentication(username, password);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setProxy(java.net.Proxy)
	 */
	@Override public IRestfulClient<R, S> setProxy(Proxy proxy) {
		return Objects.requireNonNull(this.mRestfulClient).setProxy(proxy);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setProxy(java.lang.String, int)
	 */
	@Override public IRestfulClient<R, S> setProxy(String hostname, int port) {
		return Objects.requireNonNull(this.mRestfulClient).setProxy(hostname, port);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setProxy(java.net.Proxy.Type, java.lang.String, int)
	 */
	@Override public IRestfulClient<R, S> setProxy(Type type, String hostname, int port) {
		return Objects.requireNonNull(this.mRestfulClient).setProxy(type, hostname, port);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setProxy(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override public IRestfulClient<R, S> setProxy(String username, String password, String hostname, int port) {
		return Objects.requireNonNull(this.mRestfulClient).setProxy(username, password, hostname, port);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#setProxy(java.net.Proxy.Type, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override public IRestfulClient<R, S> setProxy(Type type, String username, String password, String hostname, int port) {
		return Objects.requireNonNull(this.mRestfulClient).setProxy(type, username, password, hostname, port);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#newURLConnection(br.com.softctrl.net.rest.HttpMethod, java.lang.String)
	 */
	@Override public URLConnection newURLConnection(HttpMethod httpMethod, String url) {
		return Objects.requireNonNull(this.mRestfulClient).newURLConnection(httpMethod, url);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#send(br.com.softctrl.net.rest.Request)
	 */
	@Override public void send(Request<R, S> request) {
		Objects.requireNonNull(this.mRestfulClient).send(request);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#add(br.com.softctrl.net.rest.Parameter)
	 */
	@Override public IRestfulClient<R, S> add(Parameter parameter) {
		return Objects.requireNonNull(this.mRestfulClient).add(parameter);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#add(br.com.softctrl.net.rest.Property)
	 */
	@Override public IRestfulClient<R, S> add(Property property) {
		return Objects.requireNonNull(this.mRestfulClient).add(property);
	}
	
	/*
	 * 
	 */
	@Override public R getBody() {
		return Objects.requireNonNull(this.mRestfulClient).getBody();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#getParameters()
	 */
	@Override public List<Parameter> getParameters() {
		return Objects.requireNonNull(this.mRestfulClient).getParameters();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#getProperties()
	 */
	@Override public List<Property> getProperties() {
		return Objects.requireNonNull(this.mRestfulClient).getProperties();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#send(br.com.softctrl.net.rest.HttpMethod, java.lang.String, java.lang.Object, br.com.softctrl.net.rest.Parameter[])
	 */
	@Override public void send(HttpMethod httpMethod, String url, R body, Parameter... parameters) {
		Objects.requireNonNull(this.mRestfulClient).send(httpMethod, url, body, parameters);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#send(br.com.softctrl.net.rest.HttpMethod, java.lang.String, java.lang.Object, br.com.softctrl.net.rest.Property[])
	 */
	@Override public void send(HttpMethod httpMethod, String url, R body, Property... properties) {
		Objects.requireNonNull(this.mRestfulClient).send(httpMethod, url, body, properties);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.softctrl.net.rest.IRestfulClient#send(br.com.softctrl.net.rest.HttpMethod, java.lang.String, java.lang.Object, br.com.softctrl.net.rest.Parameter[], br.com.softctrl.net.rest.Property[])
	 */
	@Override public void send(HttpMethod httpMethod, String url, R body, Parameter[] parameters, Property[] property) {
		Objects.requireNonNull(this.mRestfulClient).send(httpMethod, url, body, parameters, property);
	}

	/**
	 * 
	 * @param url
	 */
	public synchronized final S get(final String url) {
		return this.get(url, this.getBody(),
				(Objects.isNullOrEmpty(this.getParameters()) ? null : this.getParameters().toArray(new Parameter[] {})),
				(Objects.isNullOrEmpty(this.getProperties()) ? null : this.getProperties().toArray(new Property[] {})));
	}

	/**
	 * 
	 * @param url
	 * @param parameters
	 */
	public synchronized final S get(final String url, final Parameter... parameters) {
		return this.get(url, this.getBody(),
				        parameters,
				        (Objects.isNullOrEmpty(this.getProperties()) ? null : this.getProperties().toArray(new Property[] {})));
	}

	/**
	 * 
	 * @param url
	 * @param body
	 * @param parameters
	 */
	public synchronized final S get(final String url, final R body, final Parameter... parameters) {
		return this.get(url, body,
				        parameters,
				        (Objects.isNullOrEmpty(this.getProperties()) ? null : this.getProperties().toArray(new Property[] {})));
	}

	/**
	 * 
	 * @param url
	 * @param body
	 * @param parameters
	 * @param properties
	 */
	public synchronized final S get(final String url, final R body, final Parameter[] parameters, final Property[] properties) {

		final Wrapper<S> wrapper = new Wrapper<S>(); 
		final RequestFinishedListener<S> rfl1 =  this.getRequestFinishedListener();
		final RequestFinishedListener<S> rfl2 = new RequestFinishedListener<S>() {
			@Override public void onRequestFinished(StatusCode statusCode, S response) {
				rfl1.onRequestFinished(statusCode, response);
				wrapper.setValue(response);
			}
		};
		this.setRequestFinishedListener(rfl2);
		this.send(HttpMethod.GET, url, body, parameters, properties);
		return wrapper.getValue();
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public synchronized final S post(final String url) {
		return this.post(url, this.getBody(),
				(Objects.isNullOrEmpty(this.getParameters()) ? null : this.getParameters().toArray(new Parameter[] {})),
				(Objects.isNullOrEmpty(this.getProperties()) ? null : this.getProperties().toArray(new Property[] {})));
		
	}

	/**
	 * 
	 * @param url
	 * @param body
	 * @param parameters
	 * @return
	 */
	public synchronized final S post(final String url, final R body, final Parameter... parameters) {
		return this.post(url, body,
				         parameters,
				         (Objects.isNullOrEmpty(this.getProperties()) ? null : this.getProperties().toArray(new Property[] {})));
	}

	/**
	 * 
	 * @param url
	 * @param body
	 * @param parameters
	 * @param properties
	 * @return
	 */
	public synchronized final S post(final String url, final R body, final Parameter[] parameters, final Property[] properties) {

		final Wrapper<S> wrapper = new Wrapper<S>(); 
		final RequestFinishedListener<S> rfl1 =  this.getRequestFinishedListener();
		final RequestFinishedListener<S> rfl2 = new RequestFinishedListener<S>() {
			@Override public void onRequestFinished(StatusCode statusCode, S response) {
				rfl1.onRequestFinished(statusCode, response);
				wrapper.setValue(response);
			}
		};
		this.setRequestFinishedListener(rfl2);
		this.send(HttpMethod.POST, url, body, parameters, properties);
		return wrapper.getValue();

	}

}