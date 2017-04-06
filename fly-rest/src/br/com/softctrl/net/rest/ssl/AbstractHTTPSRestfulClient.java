/**
 * 
 */
package br.com.softctrl.net.rest.ssl;

import static br.com.softctrl.net.util.Constants.ACCEPT_ENCODING;
import static br.com.softctrl.net.util.Constants.CONTENT_TYPE;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.com.softctrl.net.rest.AbstractHTTPRestfulClient;
import br.com.softctrl.net.rest.HttpMethod;
import br.com.softctrl.net.rest.listener.RequestFinishedListener;
import br.com.softctrl.net.rest.listener.ResponseErrorListener;
import br.com.softctrl.net.rest.listener.ResponseListener;
import br.com.softctrl.net.rest.ssl.pinning.SCPinningTrustManager;
import br.com.softctrl.net.util.Constants;
import br.com.softctrl.net.util.HTTPStatusCode.StatusCode;
import br.com.softctrl.utils.Objects;

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
public abstract class AbstractHTTPSRestfulClient<R, S> extends AbstractHTTPRestfulClient<R, S> {

	private static final String TAG = AbstractHTTPSRestfulClient.class.getSimpleName();

	private SSLContext mSslContext;

	/**
	 * Default constructor with basic listeners.
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public AbstractHTTPSRestfulClient() {
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
			if (Objects.isNull(this.mProxy))
				connection = (HttpsURLConnection) uri.openConnection();
			else
				connection = (HttpsURLConnection) uri.openConnection(mProxy);
			if (Objects.nonNull(this.mSslContext))
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

	/**
	 * Base on:
	 * https://www.owasp.org/index.php/Certificate_and_Public_Key_Pinning#
	 * Android
	 * 
	 * @param pins
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public AbstractHTTPSRestfulClient<R, S> setupPinningManager(List<byte[]> pins)
			throws KeyManagementException, NoSuchAlgorithmException {
		TrustManager tm[] = { new SCPinningTrustManager(pins) };
		return this.setupTrustManager(tm);
	}

	/**
	 * 
	 * @param trustManagers
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public AbstractHTTPSRestfulClient<R, S> setupTrustManager(TrustManager[] trustManagers)
			throws KeyManagementException, NoSuchAlgorithmException {
		this.mSslContext = SSLContext.getInstance(Constants.TLS);
//		this.mSslContext.createSSLEngine().set.setEnabledProtocols(new String[]{"TLSv1", "SSLv3"});
		this.mSslContext.init(null, trustManagers, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(this.mSslContext.getSocketFactory());
		return this;
	}
	
	/**
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public AbstractHTTPSRestfulClient<R, S> accpectSelfsignedCertificate() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
		} };
		this.setupTrustManager(trustAllCerts);
		return this;
	}

}
