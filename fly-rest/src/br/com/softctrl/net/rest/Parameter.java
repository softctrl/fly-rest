package br.com.softctrl.net.rest;

import static br.com.softctrl.net.util.Constants.UTF_8;
import static java.net.URLEncoder.encode;

import java.io.UnsupportedEncodingException;

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
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public final class Parameter {

	private String mName;
	private String mValue;

	/**
	 * 
	 * @param name
	 * @param value
	 * @throws UnsupportedEncodingException 
	 */
	public Parameter(String name, Object value) throws UnsupportedEncodingException {
		this.mName = encode(name, UTF_8.name());
		this.mValue = encode(value + "", UTF_8.name());
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return mName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return this.mValue;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return Objects.equals(this.mName, ((Parameter) o).mName);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (new StringBuilder(this.mName)).append('=').append(this.mValue).toString();
	}

}
