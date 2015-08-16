package com.demien.weather.ua.service;

import java.util.concurrent.Callable;

import android.util.Log;

import com.demien.weather.ua.service.RestClient.RequestMethod;

public class RestClientAsync implements Callable<RestClientResult> {
	
	private RequestMethod method;
	private String url; 
	
	public RestClientAsync(RequestMethod method, String url) {
		this.method=method;
		this.url=url;
		
	}


	@Override
	public RestClientResult call() {
		RestClientResult result=new RestClientResult();
		try {
			RestClient restClient=new RestClient();
			restClient.Execute(method, url, null, null);
			Log.i("RestClientAsync","request executed");
			result.setResponse(new String(restClient.response));
			//handler.processRestClientResult(null, restClient.response);
		} catch (Throwable e) {
			Log.e("RestClientAsync","request failed! "+e.getMessage());
			result.setErrorMessage(e.getMessage());
			//handler.processRestClientResult(e.getMessage(), null);
		} 
		
		return result;
	}

}
 