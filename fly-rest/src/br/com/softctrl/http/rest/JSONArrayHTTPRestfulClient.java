/**
 * 
 */
package br.com.softctrl.http.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.softctrl.http.rest.listener.RequestFinishedListener;
import br.com.softctrl.http.rest.listener.ResponseErrorListener;
import br.com.softctrl.http.rest.listener.ResponseListener;

/**
 * @author timoshenko
 *
 */
public final class JSONArrayHTTPRestfulClient extends AbstractHTTPRestfulClient<JSONArray> {

	/**
	 * @param responseListener
	 * @param responseErrorListener
	 * @param requestFinishedListener
	 */
	public JSONArrayHTTPRestfulClient(ResponseListener<JSONArray> responseListener,
			ResponseErrorListener responseErrorListener, RequestFinishedListener<JSONArray> requestFinishedListener) {
		super(responseListener, responseErrorListener, requestFinishedListener);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.http.rest.AbstractHTTPRestfulClient#createRequest(br.com.
	 * softctrl.rest.HttpMethod, java.lang.String, java.lang.Object,
	 * br.com.softctrl.http.rest.Parameter[])
	 */
	@Override
	protected Request<JSONArray> createRequest(HttpMethod httpMethod, String url, JSONArray body,
			Parameter... parameters) {
		final Request<JSONArray> request = new Request<JSONArray>(httpMethod, url, body) {
			@Override
			public Response<JSONArray> parseResponse(int statusCode, String result) {
                Response<JSONArray> response = null;
                try {
                    response = new Response<JSONArray>(statusCode, new JSONArray(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
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
