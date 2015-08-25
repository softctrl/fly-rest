package br.com.softctrl.net.util;

import java.util.concurrent.ConcurrentHashMap;

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
public final class HTTPStatusCode {

	public static final class StatusCode {
		private int mValue;
		private String mDescription;
		private String mReference;

		private StatusCode(int value, String description, String reference) {
			this.mValue = value;
			this.mDescription = description;
			this.mReference = reference;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return mDescription;
		}

		/**
		 * @return the reference
		 */
		public String getReference() {
			return mReference;
		}

		/**
		 * @return the value
		 */
		public int getValue() {
			return mValue;
		}

		/**
		 * 
		 * @return true if that status code was about Timeout.
		 */
		public boolean isTimeoutError() {
			// 408 - Request Timeout - [RFC7231, Section 6.5.7]
			// 504 - Gateway Timeout - [RFC7231, Section 6.6.5]
			return (this.mValue == 408 || this.mValue == 504);
		}

		/**
		 *
		 * @return
		 */
		public boolean isOk() {
			// 200 - OK - [RFC7231, Section 6.3.1]
			return (this.mValue == 200);
		}

		@Override
		public String toString() {
			return String.format("Code: [%d]\nDescription: %s\nReference: %s", this.getValue(), this.getDescription(),
					this.getReference());
		}

	}

	private static final ConcurrentHashMap<Integer, StatusCode> STATUS_CODE_MAP = new ConcurrentHashMap<Integer, StatusCode>();

	static {
		// I will not manage unassigned codes
		// Source:
		// http://www.iana.org/assignments/http-status-codes/http-status-codes.xml
		STATUS_CODE_MAP.put(100, new StatusCode(100, "Continue", "[RFC7231, Section 6.2.1]"));
		STATUS_CODE_MAP.put(101, new StatusCode(101, "Switching Protocols", "[RFC7231, Section 6.2.2]"));
		STATUS_CODE_MAP.put(102, new StatusCode(102, "Processing", "[RFC2518]"));
		STATUS_CODE_MAP.put(200, new StatusCode(200, "OK", "[RFC7231, Section 6.3.1]"));
		STATUS_CODE_MAP.put(201, new StatusCode(201, "Created", "[RFC7231, Section 6.3.2]"));
		STATUS_CODE_MAP.put(202, new StatusCode(202, "Accepted", "[RFC7231, Section 6.3.3]"));
		STATUS_CODE_MAP.put(203, new StatusCode(203, "Non-Authoritative Information", "[RFC7231, Section 6.3.4]"));
		STATUS_CODE_MAP.put(204, new StatusCode(204, "No Content", "[RFC7231, Section 6.3.5]"));
		STATUS_CODE_MAP.put(205, new StatusCode(205, "Reset Content", "[RFC7231, Section 6.3.6]"));
		STATUS_CODE_MAP.put(206, new StatusCode(206, "Partial Content", "[RFC7233, Section 4.1]"));
		STATUS_CODE_MAP.put(207, new StatusCode(207, "Multi-Status", "[RFC4918]"));
		STATUS_CODE_MAP.put(208, new StatusCode(208, "Already Reported", "[RFC5842]"));
		STATUS_CODE_MAP.put(226, new StatusCode(226, "IM Used", "[RFC3229]"));
		STATUS_CODE_MAP.put(300, new StatusCode(300, "Multiple Choices", "[RFC7231, Section 6.4.1]"));
		STATUS_CODE_MAP.put(301, new StatusCode(301, "Moved Permanently", "[RFC7231, Section 6.4.2]"));
		STATUS_CODE_MAP.put(302, new StatusCode(302, "Found", "[RFC7231, Section 6.4.3]"));
		STATUS_CODE_MAP.put(303, new StatusCode(303, "See Other", "[RFC7231, Section 6.4.4]"));
		STATUS_CODE_MAP.put(304, new StatusCode(304, "Not Modified", "[RFC7232, Section 4.1]"));
		STATUS_CODE_MAP.put(305, new StatusCode(305, "Use Proxy", "[RFC7231, Section 6.4.5]"));
		STATUS_CODE_MAP.put(306, new StatusCode(306, "(Unused)", "[RFC7231, Section 6.4.6]"));
		STATUS_CODE_MAP.put(307, new StatusCode(307, "Temporary Redirect", "[RFC7231, Section 6.4.7]"));
		STATUS_CODE_MAP.put(308, new StatusCode(308, "Permanent Redirect", "[RFC7538]"));
		STATUS_CODE_MAP.put(400, new StatusCode(400, "Bad Request", "[RFC7231, Section 6.5.1]"));
		STATUS_CODE_MAP.put(401, new StatusCode(401, "Unauthorized", "[RFC7235, Section 3.1]"));
		STATUS_CODE_MAP.put(402, new StatusCode(402, "Payment Required", "[RFC7231, Section 6.5.2]"));
		STATUS_CODE_MAP.put(403, new StatusCode(403, "Forbidden", "[RFC7231, Section 6.5.3]"));
		STATUS_CODE_MAP.put(404, new StatusCode(404, "Not Found", "[RFC7231, Section 6.5.4]"));
		STATUS_CODE_MAP.put(405, new StatusCode(405, "Method Not Allowed", "[RFC7231, Section 6.5.5]"));
		STATUS_CODE_MAP.put(406, new StatusCode(406, "Not Acceptable", "[RFC7231, Section 6.5.6]"));
		STATUS_CODE_MAP.put(407, new StatusCode(407, "Proxy Authentication Required", "[RFC7235, Section 3.2]"));
		STATUS_CODE_MAP.put(408, new StatusCode(408, "Request Timeout", "[RFC7231, Section 6.5.7]"));
		STATUS_CODE_MAP.put(409, new StatusCode(409, "Conflict", "[RFC7231, Section 6.5.8]"));
		STATUS_CODE_MAP.put(410, new StatusCode(410, "Gone", "[RFC7231, Section 6.5.9]"));
		STATUS_CODE_MAP.put(411, new StatusCode(411, "Length Required", "[RFC7231, Section 6.5.10]"));
		STATUS_CODE_MAP.put(412, new StatusCode(412, "Precondition Failed", "[RFC7232, Section 4.2]"));
		STATUS_CODE_MAP.put(413, new StatusCode(413, "Payload Too Large", "[RFC7231, Section 6.5.11]"));
		STATUS_CODE_MAP.put(414, new StatusCode(414, "URI Too Long", "[RFC7231, Section 6.5.12]"));
		STATUS_CODE_MAP.put(415, new StatusCode(415, "Unsupported Media Type", "[RFC7231, Section 6.5.13]"));
		STATUS_CODE_MAP.put(416, new StatusCode(416, "Range Not Satisfiable", "[RFC7233, Section 4.4]"));
		STATUS_CODE_MAP.put(417, new StatusCode(417, "Expectation Failed", "[RFC7231, Section 6.5.14]"));
		STATUS_CODE_MAP.put(421, new StatusCode(421, "Misdirected Request", "[RFC7540, Section 9.1.2]"));
		STATUS_CODE_MAP.put(422, new StatusCode(422, "Unprocessable Entity", "[RFC4918]"));
		STATUS_CODE_MAP.put(423, new StatusCode(423, "Locked", "[RFC4918]"));
		STATUS_CODE_MAP.put(424, new StatusCode(424, "Failed Dependency", "[RFC4918]"));
		STATUS_CODE_MAP.put(426, new StatusCode(426, "Upgrade Required", "[RFC7231, Section 6.5.15]"));
		STATUS_CODE_MAP.put(428, new StatusCode(428, "Precondition Required", "[RFC6585]"));
		STATUS_CODE_MAP.put(429, new StatusCode(429, "Too Many Requests", "[RFC6585]"));
		STATUS_CODE_MAP.put(431, new StatusCode(431, "Request Header Fields Too Large", "[RFC6585]"));
		STATUS_CODE_MAP.put(500, new StatusCode(500, "Internal Server Error", "[RFC7231, Section 6.6.1]"));
		STATUS_CODE_MAP.put(501, new StatusCode(501, "Not Implemented", "[RFC7231, Section 6.6.2]"));
		STATUS_CODE_MAP.put(502, new StatusCode(502, "Bad Gateway", "[RFC7231, Section 6.6.3]"));
		STATUS_CODE_MAP.put(503, new StatusCode(503, "Service Unavailable", "[RFC7231, Section 6.6.4]"));
		STATUS_CODE_MAP.put(504, new StatusCode(504, "Gateway Timeout", "[RFC7231, Section 6.6.5]"));
		STATUS_CODE_MAP.put(505, new StatusCode(505, "HTTP Version Not Supported", "[RFC7231, Section 6.6.6]"));
		STATUS_CODE_MAP.put(506, new StatusCode(506, "Variant Also Negotiates", "[RFC2295]"));
		STATUS_CODE_MAP.put(507, new StatusCode(507, "Insufficient Storage", "[RFC4918]"));
		STATUS_CODE_MAP.put(508, new StatusCode(508, "Loop Detected", "[RFC5842]"));
		STATUS_CODE_MAP.put(510, new StatusCode(510, "Not Extended", "[RFC2774]"));
		STATUS_CODE_MAP.put(511, new StatusCode(511, "Network Authentication Required", "[RFC6585]"));
	}

	/**
	 * 
	 * @param statusCode
	 * @return
	 */
	public static final StatusCode resolveStatusCode(int statusCode) {
		if (STATUS_CODE_MAP.containsKey(statusCode)) {
			return STATUS_CODE_MAP.get(statusCode);
		} else {
			return null;
		}

	}

}
