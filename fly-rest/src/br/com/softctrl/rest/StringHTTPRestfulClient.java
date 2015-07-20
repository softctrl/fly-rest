/**
 * 
 */
package br.com.softctrl.rest;

import br.com.softctrl.rest.listener.RequestFinishedListener;
import br.com.softctrl.rest.listener.ResponseErrorListener;
import br.com.softctrl.rest.listener.ResponseListener;

/**
 * @author timoshenko
 *
 */
public final class StringHTTPRestfulClient extends AbstractHTTPRestfulClient<String> {

	/**
	 * @param responseListener
	 * @param responseErrorListener
	 * @param requestFinishedListener
	 */
	public StringHTTPRestfulClient(ResponseListener<String> responseListener, ResponseErrorListener responseErrorListener,
			RequestFinishedListener<String> requestFinishedListener) {
		super(responseListener, responseErrorListener, requestFinishedListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.rest.BasicHTTPRestClient#createRequest(br.com.softctrl.
	 * rest.HttpMethod, java.lang.String, java.lang.Object,
	 * br.com.softctrl.rest.Parameter[])
	 */
	@Override
	protected Request<String> createRequest(HttpMethod httpMethod, String url, String body, Parameter... parameters) {
		final Request<String> request = new Request<String>(httpMethod, url, body) {
			@Override
			public Response<String> parseResponse(int statusCode, String result) {
				return new Response<String>(statusCode, result);
			}
		};
		if (parameters != null && parameters.length > 0) {
			for (Parameter parameter : parameters) {
				request.addParameter(parameter);
			}
		}
		return request;
	}

}
