package br.com.softctrl.net.rest.ssl.pinning;

import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import br.com.softctrl.net.util.Constants;
import static br.com.softctrl.net.util.StringUtils.byteArrayToHexString;

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
public class SCPinningTrustManager implements X509TrustManager {

	private static final String TAG = SCPinningTrustManager.class.getSimpleName();

	private final List<byte[]> mPins = new LinkedList<byte[]>();
	private String[] mAuthType;
	private final Set<X509Certificate> mCache = Collections.synchronizedSet(new HashSet<X509Certificate>());

	public SCPinningTrustManager(List<byte[]> pins) {
		this(pins, new String[]{Constants.RSA, Constants.ECDHE_RSA, Constants.ECDHE_ECDSA});
		
	}

	public SCPinningTrustManager(List<byte[]> pins, String[] authType) {
		this.getPins().addAll(pins);
		this.setAuthType(authType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.
	 * X509Certificate[], java.lang.String)
	 */
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		if (chain == null) {
			throw new IllegalArgumentException(TAG + ": X509Certificate array is null");
		}

		if (chain.length < 0) {
			throw new IllegalArgumentException(TAG + ": X509Certificate is empty");
		}

		if (this.getCache().contains(chain[0])) {
			return;
		}

		if (null != authType && !isValidAuthType(authType)) {
			throw new CertificateException(TAG + ": AuthType is not RSA[" + authType + "]");
		}
		try {
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(Constants.X_509);
			tmf.init((KeyStore) null);
			for (TrustManager trustManager : tmf.getTrustManagers()) {
				((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
			}
		} catch (Exception e) {
			throw new CertificateException(e);
		}
		// Pin it!
		validatePin(chain[0]);
		this.getCache().add(chain[0]);
	}
	
	/**
	 * 
	 * @param authType
	 * @return
	 */
	protected boolean isValidAuthType(final String authType) {
		for (String at : getAuthType()) {
			if (at.equalsIgnoreCase(authType)) return true;
		}
		return false;
	}

	/**
	 * 
	 * @param certificate
	 * @return
	 * @throws CertificateException
	 */
	protected boolean validatePin(X509Certificate certificate) throws CertificateException {
		try {
			final MessageDigest digest = MessageDigest.getInstance(Constants.SHA1);
			final byte[] spki = certificate.getPublicKey().getEncoded();
			final byte[] pin = digest.digest(spki);
			System.out.println();
			for (byte[] validPin : this.getPins()) {
				if (Arrays.equals(validPin, pin)) {
					return true;
				}
			}
			throw new CertificateException(TAG + ": Invalid public key:" + byteArrayToHexString(pin));
		} catch (NoSuchAlgorithmException nsae) {
			throw new CertificateException(TAG, nsae);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.
	 * X509Certificate[], java.lang.String)
	 */
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String arg1) throws CertificateException { }

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers() { return null; }

	/**
	 * 
	 */
	protected void clearCache() {
		this.getCache().clear();
	}

	/**
	 * @return the mPins
	 */
	public List<byte[]> getPins() {
		return mPins;
	}

	/**
	 * @return the mAuthType
	 */
	public String[] getAuthType() {
		return mAuthType;
	}

	/**
	 * @param mAuthType the mAuthType to set
	 */
	public void setAuthType(String[] mAuthType) {
		this.mAuthType = mAuthType;
	}

	/**
	 * @return the mCache
	 */
	public Set<X509Certificate> getCache() {
		return mCache;
	}

}
