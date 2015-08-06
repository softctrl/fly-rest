/**
 * 
 */
package br.com.softctrl.http.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
public final class StreamUtils {

	private static final class Constants {
		private static final String UTF_8 = "UTF-8";
	}

	private StreamUtils() {
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 */
	public static final String inputStreamToString(InputStream inputStream) {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		if (inputStream != null) {
			final byte[] buffer = new byte[1024];
			int length = 0;
			try {
				while ((length = inputStream.read(buffer)) != -1) {
					byteArrayOutputStream.write(buffer, 0, length);
				}
				return byteArrayOutputStream.toString(Constants.UTF_8);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public static final byte[] streamToByteArray(InputStream inputStream) {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		if (inputStream != null) {
			final byte[] buffer = new byte[1024];
			int length = 0;
			try {
				while ((length = inputStream.read(buffer)) != -1) {
					byteArrayOutputStream.write(buffer, 0, length);
				}
				return byteArrayOutputStream.toByteArray();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public static final void streamCopy(final InputStream in, final OutputStream out) {

		if (in == null || out == null)
			throw new IllegalArgumentException("You need to provide input and output streams.");
		final byte[] buffer = new byte[8192];
		int len = 0;
		try {
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
				out.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
