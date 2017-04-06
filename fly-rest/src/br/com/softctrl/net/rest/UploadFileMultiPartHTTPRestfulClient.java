package br.com.softctrl.net.rest;

import static br.com.softctrl.utils.Objects.nonNull;
import static br.com.softctrl.utils.io.StreamUtils.streamToByteArray;
import static br.com.softctrl.utils.io.StreamUtils.streamToString;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Set;
import java.util.UUID;

import br.com.softctrl.net.rest.listener.RequestFinishedListener;
import br.com.softctrl.net.rest.listener.ResponseErrorListener;
import br.com.softctrl.net.rest.listener.ResponseListener;
import br.com.softctrl.net.util.HTTPStatusCode;
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
public class UploadFileMultiPartHTTPRestfulClient extends AbstractHTTPRestfulClient<InputStream, String> {
	
	/**
	 * 
	 * @author carlostimoshenkorodrigueslopes@gmail.com
	 *
	 */	
	static final class Constants {
		public static final byte[] CR_LF = "\r\n".getBytes();
		public static final byte[] HYPHENS = "--".getBytes();
		
		static final class String {
			public static final java.lang.String BOUNDARY = "--%s\r\n";
			public static final java.lang.String CONTENT_DISPOSITION = "Content-Disposition: form-data; name=\"%s\"\r\n";
			public static final java.lang.String CONTENT_DISPOSITION_DATA = "Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\n";
			public static final java.lang.String CONTENT_TYPE = "Content-Type: text/plain;\r\n\r\n";
		}
	}
	
	private String mName = "file";
	private String mFileName = "file";

	/**
	 * 
	 */
	public UploadFileMultiPartHTTPRestfulClient() {
		super();
	}

	/**
	 * @param responseListener
	 * @param responseErrorListener
	 * @param requestFinishedListener
	 */
	public UploadFileMultiPartHTTPRestfulClient(ResponseListener<String> responseListener,
			ResponseErrorListener responseErrorListener, RequestFinishedListener<String> requestFinishedListener) {
		super(responseListener, responseErrorListener, requestFinishedListener);
	}
	
	/**
	 * 
	 * @param name
	 * @param filename
	 * @return
	 */
	public UploadFileMultiPartHTTPRestfulClient setMultiPartInfo(final String name, final String filename) {
		this.mName = Objects.requireNonNull(name);
		this.mFileName = Objects.requireNonNull(filename);
		return this;
	}
	
	/**
	 * 
	 * @param boundary
	 * @param dataOutputStream
	 * @param parameter
	 * @throws IOException
	 */
	private void addMultiPartParameter(final String boundary, final DataOutputStream dataOutputStream, final Parameter parameter)
			throws IOException {

		dataOutputStream.writeBytes(String.format(Constants.String.BOUNDARY, boundary));
		dataOutputStream.writeBytes(String.format(Constants.String.CONTENT_DISPOSITION, parameter.getName()));
		dataOutputStream.writeBytes(Constants.String.CONTENT_TYPE);
		dataOutputStream.writeBytes(parameter.getValue());
		dataOutputStream.write(Constants.CR_LF);

	}
	
	/**
	 * 
	 * @param boundary
	 * @param filename
	 * @param dataOutputStream
	 * @param data
	 * @throws IOException
	 */
	private void addMultiPartFile(final String boundary, final String name, final String filename, final DataOutputStream dataOutputStream, final byte[] data)
			throws IOException {

		dataOutputStream.writeBytes(String.format(Constants.String.BOUNDARY, boundary));
		dataOutputStream.writeBytes(String.format(Constants.String.CONTENT_DISPOSITION_DATA, name, filename));
		dataOutputStream.write(Constants.CR_LF);

		dataOutputStream.write(data);
		dataOutputStream.write(Constants.CR_LF);
		dataOutputStream.writeBytes(Constants.HYPHENS + boundary + Constants.HYPHENS + Constants.CR_LF);

	}

	/**
	 * @param request
	 * @return
	 */
	@Override
	protected Response<String> perform(Request<InputStream, String> request) {

		Objects.requireNonNull(request, "You need to send a request data.");
		Response<String> result = null;
		final HttpURLConnection connection = newURLConnection(request.getHttpMethod(), request.getUrl());
		try {
			
			final String boundary = UUID.randomUUID().toString();
			
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Expect", "100-continue");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			connection.setDoInput(true);
			final boolean isPOST = HttpMethod.POST.equals(request.getHttpMethod());
			connection.setDoOutput(isPOST);
			
			// Basic HTTP Authentication
			if (Objects.nonNull(this.mBasicHttpAuthentication)) {
				connection.setRequestProperty(this.mBasicHttpAuthentication.getKey(),
						this.mBasicHttpAuthentication.getValue());
			}
			
			// Request properties
			final Set<Property> properties = request.getProperties();
			if (properties != null && properties.size() > 0) {
				for (Property property : properties) {
					connection.setRequestProperty(property.getKey(), property.getValue());
				}
			}
			
			// If is a POST:
			if (isPOST) {

				DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
				final Set<Parameter> parameters = request.getParameters();

				// If exists parameters:
				if (!Objects.isNullOrEmpty(parameters)) {
					for (Parameter parameter : parameters) {
						this.addMultiPartParameter(boundary, dataOutputStream, parameter);
					}
				}
				// If exists body:
				if (Objects.nonNull(request.getBody())) {
					this.addMultiPartFile(boundary, this.mName, this.mFileName, dataOutputStream, request.bodyToByteArray());
				}
				dataOutputStream.flush();
				dataOutputStream.close();

			}
			connection.connect();
			result = request.parseResponse(connection.getResponseCode(), connection.getInputStream());
			if (nonNull(this.mResponseListener)) this.mResponseListener.onResponse(result == null ? null : result.getResult());
		} catch (java.net.SocketTimeoutException e) {
			try {
				final String response = streamToString(connection.getErrorStream());
				final HTTPStatusCode.StatusCode statusCode = HTTPStatusCode.resolveStatusCode(408);
				if (nonNull(this.mResponseErrorListener)) this.mResponseErrorListener.onResponseError(statusCode, response, e);
			} catch (Exception e1) {
				if (nonNull(this.mResponseErrorListener)) this.mResponseErrorListener.onResponseError(null, null, e1);
			}
		} catch (IOException e) {
			try {
				final String response = streamToString(connection.getErrorStream());
				final HTTPStatusCode.StatusCode statusCode = HTTPStatusCode
						.resolveStatusCode(connection.getResponseCode());
				if (nonNull(this.mResponseErrorListener)) this.mResponseErrorListener.onResponseError(statusCode, response, e);
			} catch (Exception e2) {
				if (nonNull(this.mResponseErrorListener)) this.mResponseErrorListener.onResponseError(null, null, e2);
			}
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.net.rest.AbstractHTTPRestfulClient#createRequest(br.com.
	 * softctrl.net.rest.HttpMethod, java.lang.String, java.lang.Object,
	 * br.com.softctrl.net.rest.Parameter[],
	 * br.com.softctrl.net.rest.Property[])
	 */
	@Override
	protected Request<InputStream, String> createRequest(HttpMethod httpMethod, String url, InputStream body,
			Parameter[] parameters, Property[] properties) {

		final Request<InputStream, String> request = new Request<InputStream, String>(httpMethod, url, body) {
			@Override
			public Response<String> parseResponse(int statusCode, InputStream result) {
				String _result = streamToString(result);
				return new Response<String>(statusCode, _result);
			}

			@Override
			public byte[] bodyToByteArray() {
				return streamToByteArray(getBody());
			}
		};
		this.loadData(request, parameters, properties);
		return request;

	}

}