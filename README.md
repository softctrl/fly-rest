#I need to make some changings to a better suport of tls/ssl.

# fly-rest
Restfull Library to use on Android mobile projects 

To use, just make those imports:
```java
import br.com.softctrl.http.rest.StringHTTPRestfulClient;
import br.com.softctrl.http.rest.listener.RequestFinishedListener;
import br.com.softctrl.http.rest.listener.ResponseErrorListener;
import br.com.softctrl.http.rest.listener.ResponseListener;
import br.com.softctrl.http.util.HTTPStatusCode.StatusCode;
```
And create this listeners:
```java
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
```
You just need create a better client that you need, or maybe you can create your own(and send a pull request =D ):

```java
		StringHTTPRestfulClient restfulClient = new StringHTTPRestfulClient(responseListener, responseErrorListener,
				requestFinishedListener);
```
And finally:
```java
		String url = "http://www.0x09.com.br/";
		String body = null;
		restfulClient.get(url, body);
```
