package br.com.softctrl.net.util;

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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public class Objects {

	public static class StringHelper {

		private StringBuilder mDescription = new StringBuilder();
		private java.util.HashMap<String, Object> mFields = new java.util.HashMap<String, Object>();

		/**
		 *
		 * @param clazz
		 */
		private StringHelper(final Class<?> clazz) {
			this.mDescription.append("Class<").append(clazz.getSimpleName()).append('>').append(':').append(' ');
		}

		public StringHelper add(String name, Object value) {
			this.mFields.put(name, value);
			return this;
		}

		@Override
		public String toString() {
			StringBuilder result = new StringBuilder('{');
			for (Map.Entry<String, Object> field : this.mFields.entrySet()) {
				result.append(field.getKey()).append(':').append(' ').append(field.getValue()).append(' ');
			}
			return this.mDescription.append(result).append('}').toString();
		}
	}

	/**
	 *
	 * @param clazz
	 * @return
	 */
	public static StringHelper toStringHelper(final Class<?> clazz) {
		return new StringHelper(clazz);
	}

	/**
	 *
	 * @param objects
	 * @return
	 */
	public static int hashCode(Object... objects) {
		return Arrays.hashCode(objects);
	}

	/**
	 *
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean equals(Object obj1, Object obj2) {
		return obj1 == obj2 || (obj1 != null && obj1.equals(obj2));
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> boolean isNull(T obj) {
		return (obj == null);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(String value) {
		return ((value + "").trim().length() > 0);
	}

	/**
	 * 
	 * @param items
	 * @return
	 */
	public static <T> boolean isNullOrEmpty(T[] items) {
		return (isNull(items) ? true : (items.length == 0));
	}

	/**
	 * 
	 * @param items
	 * @return
	 */
	public static <T> boolean isNullOrEmpty(List<T> items) {
		return (isNull(items) ? true : (items.size() == 0));
	}

	/**
	 * 
	 * @param obj
	 * @param message
	 * @return
	 */
	public static <T> T requireNonNull(T obj, String message) {
		if (isNull(obj)) throw new IllegalArgumentException(message);
		return obj;
	}

	/**
	 * 
	 * @param obj
	 * @param defaultObj
	 * @return
	 */
	public static <T> T nonNullOrDefault(T obj, T defaultObj) {
		return (isNull(obj) ? requireNonNull(defaultObj, "Default value is not null.") : obj);
	}

}
