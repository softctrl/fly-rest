/**
 * 
 */
package br.com.softctrl.net.rest.ssl;

import static br.com.softctrl.net.util.Constants.ACCEPT_ENCODING;
import static br.com.softctrl.net.util.Constants.CONTENT_TYPE;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import br.com.softctrl.net.rest.AbstractHTTPRestfulClient;
import br.com.softctrl.net.rest.HttpMethod;
import br.com.softctrl.net.rest.listener.RequestFinishedListener;
import br.com.softctrl.net.rest.listener.ResponseErrorListener;
import br.com.softctrl.net.rest.listener.ResponseListener;
import br.com.softctrl.net.rest.ssl.pinning.SCPinningTrustManager;
import br.com.softctrl.net.util.Constants;
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
 * [R]equest Re[S]ponse
 * 
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public abstract class AbstractHTTPSRestfulClient<R, S> extends AbstractHTTPRestfulClient<R, S> {

	private static final String TAG = AbstractHTTPSRestfulClient.class.getSimpleName();

	private SSLContext mSslContext;

	/**
	 * @param responseListener
	 * @param responseErrorListener
	 * @param requestFinishedListener
	 * @throws NoSuchAlgorithmException
	 */
	public AbstractHTTPSRestfulClient(final ResponseListener<S> responseListener,
			final ResponseErrorListener responseErrorListener,
			final RequestFinishedListener<S> requestFinishedListener) {
		this.mResponseListener = responseListener;
		this.mResponseErrorListener = responseErrorListener;
		this.mRequestFinishedListener = requestFinishedListener;
		this.validateListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.net.rest.IRestfulClient#newHttpConnection(br.com.softctrl
	 * .net.rest.HttpMethod, java.lang.String)
	 */
	@Override
	public HttpsURLConnection newURLConnection(HttpMethod httpMethod, String url) {

		URL uri;
		HttpsURLConnection connection = null;
		try {
			uri = new URL(url);
			if (this.mProxy == null)
				connection = (HttpsURLConnection) uri.openConnection();
			else
				connection = (HttpsURLConnection) uri.openConnection(mProxy);
			if (this.mSslContext != null)
				connection.setSSLSocketFactory(this.mSslContext.getSocketFactory());
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
	
}
