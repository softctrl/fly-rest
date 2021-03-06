package br.com.softctrl.net.util;

import java.nio.charset.Charset;

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
public final class Constants {

	public static final Charset UTF_8 = Charset.forName("UTF-8");
	public static final int CONNECT_TIMEOUT = 5000; // 5 seconds
	public static final int READ_TIMEOUT = CONNECT_TIMEOUT;

	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";
	
	//
	public static final String TLS = "TLS";
	public static final String RSA = "RSA";
	public static final String ECDHE_RSA = "ECDHE_RSA";
	public static final String ECDHE_ECDSA = "ECDHE_ECDSA";
	public static final String X_509 = "X509";
	public static final String SHA1 = "SHA1";

}
