package br.com.softctrl.rest;

import br.com.softctrl.http.rest.StringHTTPRestfulClient;
import br.com.softctrl.http.rest.listener.RequestFinishedListener;
import br.com.softctrl.http.rest.listener.ResponseErrorListener;
import br.com.softctrl.http.rest.listener.ResponseListener;
import br.com.softctrl.http.util.HTTPStatusCode.StatusCode;
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
public class SCMain {

	public static void main(String[] args) {
		ResponseListener<String> responseListener = new ResponseListener<String>() {			
			@Override
			public void onResponse(String response) {
				System.out.println("####ResponseListener");
				System.out.println(response);
			}
		};
		ResponseErrorListener responseErrorListener = new ResponseErrorListener() {			
			@Override
			public void onResponseError(StatusCode statusCode, String serverMessage, Throwable throwable) {
				System.out.println("####ResponseErrorListener");
				System.out.println(statusCode);
				System.out.println(serverMessage);
				System.out.println(throwable);
			}
		};
		RequestFinishedListener<String> requestFinishedListener = new RequestFinishedListener<String>() {			
			@Override
			public void onRequestFinished(StatusCode statusCode, String response) {
				System.out.println("####RequestFinishedListener");
				System.out.println(statusCode);
				System.out.println(response);
			}
		};
		
		StringHTTPRestfulClient restfulClient = new StringHTTPRestfulClient(responseListener, responseErrorListener,
				requestFinishedListener);
		String url = "http://www.0x09.com.br/";
		String body = null;
		restfulClient.get(url, body);
	}

}



