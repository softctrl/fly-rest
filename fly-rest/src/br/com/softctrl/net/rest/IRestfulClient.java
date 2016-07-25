/**
 * 
 */
package br.com.softctrl.net.rest;

import java.net.Proxy;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

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
 * 
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public interface IRestfulClient<R, S> {
	
	/**
	 * 
	 * @param responseListener
	 * @return
	 */
	public IRestfulClient<R, S> setResponseListener(ResponseListener<S> responseListener);
	
	/**
	 * 
	 * @return
	 */
	public ResponseListener<S> getResponseListener();

	/**
	 * 
	 * @param responseErrorListener
	 * @return
	 */
	public IRestfulClient<R, S> setResponseErrorListener(ResponseErrorListener responseErrorListener);

	/**
	 * 
	 * @return
	 */
	public ResponseErrorListener getResponseErrorListener();
	
	/**
	 * 
	 * @param requestFinishedListener
	 * @return
	 */
	public IRestfulClient<R, S> setRequestFinishedListener(RequestFinishedListener<S> requestFinishedListener);

	/**
	 * 
	 * @return
	 */
	public RequestFinishedListener<S> getRequestFinishedListener();
	

	/**
	 * 
	 * @param connectTimeout
	 * @return
	 */
	public IRestfulClient<R, S> setConnectTimeout(int connectTimeout);

	/**
	 * 
	 * @param readTimeout
	 *            in seconds
	 * @return
	 */
	public IRestfulClient<R, S> setReadTimeout(int readTimeout);

	/**
	 * 
	 * @param charset
	 * @return
	 */
	public IRestfulClient<R, S> setCharset(Charset charset);

	/**
	 * 
	 * @param contentType
	 *            the content type to set
	 * @return
	 */
	public IRestfulClient<R, S> setContentType(String contentType);

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public IRestfulClient<R, S> setBasicAuthentication(String username, String password);

	/**
	 * 
	 * @param proxy
	 * @return
	 */
	public IRestfulClient<R, S> setProxy(Proxy proxy);

	/**
	 * 
	 * @param hostname
	 * @param port
	 * @return
	 */
	public IRestfulClient<R, S> setProxy(String hostname, int port);

	/**
	 * 
	 * @param type
	 * @param hostname
	 * @param port
	 * @return
	 */
	public IRestfulClient<R, S> setProxy(Proxy.Type type, String hostname, int port);

	/**
	 * 
	 * @param username
	 * @param password
	 * @param hostname
	 * @param port
	 * @return
	 */
	public IRestfulClient<R, S> setProxy(final String username, final String password, final String hostname,
			final int port);

	/**
	 * 
	 * @param type
	 * @param username
	 * @param password
	 * @param hostname
	 * @param port
	 * @return
	 */
	public IRestfulClient<R, S> setProxy(Proxy.Type type, final String username, final String password, String hostname,
			int port);

	/**
	 * 
	 * @param httpMethod
	 * @param url
	 * @return
	 */
	URLConnection newURLConnection(HttpMethod httpMethod, String url);

	/**
	 * 
	 * @param request
	 */
	public void send(final Request<R, S> request);

	/**
	 * 
	 * @param parameter
	 * @return
	 */
	public IRestfulClient<R, S> add(Parameter parameter);

	/**
	 * 
	 * @param property
	 * @return
	 */
	public IRestfulClient<R, S> add(Property property);
	
	/**
	 * 
	 * @return
	 */
	public R getBody();
	
	/**
	 * 
	 * @return
	 */
	public List<Parameter> getParameters();
	
	/**
	 * 
	 * @return
	 */
	public List<Property> getProperties();

	/**
	 * 
	 * @param httpMethod
	 * @param url
	 * @param body
	 * @param parameters
	 */
	void send(HttpMethod httpMethod, String url, R body, Parameter... parameters);

	void send(HttpMethod httpMethod, String url, R body, Property... properties);

	void send(HttpMethod httpMethod, String url, R body, Parameter[] parameters, Property[] property);

}
