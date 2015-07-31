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
public final class JSONObjectHTTPRestfulClient extends AbstractHTTPRestfulClient<JSONObject> {

	/**
	 * @param responseListener
	 * @param responseErrorListener
	 * @param requestFinishedListener
	 */
	public JSONObjectHTTPRestfulClient(ResponseListener<JSONObject> responseListener,
			ResponseErrorListener responseErrorListener, RequestFinishedListener<JSONObject> requestFinishedListener) {
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
	protected Request<JSONObject> createRequest(HttpMethod httpMethod, String url, JSONObject body,
			Parameter... parameters) {
		final Request<JSONObject> request = new Request<JSONObject>(httpMethod, url, body) {
			@Override
			public Response<JSONObject> parseResponse(int statusCode, String result) {
                Response<JSONObject> response = null;
                try {
                    response = new Response<JSONObject>(statusCode, new JSONObject(result));
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
