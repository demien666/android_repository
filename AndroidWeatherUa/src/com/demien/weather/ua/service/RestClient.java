package com.demien.weather.ua.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class RestClient {
	public enum RequestMethod {
		GET, POST
	}

	public int responseCode = 0;
	public String message;
	public String response;

	public void Execute(RequestMethod method, String url,
			ArrayList<org.apache.http.NameValuePair> headers, ArrayList<org.apache.http.NameValuePair> params)
			throws Exception {
		switch (method) {
		case GET: {
			// add parameters
			String combinedParams = "";
			if (params != null) {
				combinedParams += "?";
				for (org.apache.http.NameValuePair p : params) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1)
						combinedParams += "&" + paramString;
					else
						combinedParams += paramString;
				}
			}
			HttpGet request = new HttpGet(url + combinedParams);
			// add headers
			if (headers != null) {
				headers = addCommonHeaderField(headers);
				for (org.apache.http.NameValuePair h : headers)
					request.addHeader(h.getName(), h.getValue());
			}
			executeRequest(request, url);
			break;
		}
		case POST: {
			HttpPost request = new HttpPost(url);
			// add headers
			if (headers != null) {
				headers = addCommonHeaderField(headers);
				for (org.apache.http.NameValuePair h : headers)
					request.addHeader(h.getName(), h.getValue());
			}
			if (params != null)
				request.setEntity((HttpEntity) new UrlEncodedFormEntity(
						(List<? extends org.apache.http.NameValuePair>) params,
						HTTP.UTF_8));
			executeRequest(request, url);
			break;
		}
		}
	}

	private ArrayList<org.apache.http.NameValuePair> addCommonHeaderField(
			ArrayList<org.apache.http.NameValuePair> _header) {
		_header.add( new BasicNameValuePair(
				"Content-Type", "application/x-www-form-urlencoded"));
		return _header;
	}

	private void executeRequest(HttpUriRequest request, String url) throws ClientProtocolException, IOException {
		Log.i("Rest client - executeRequest", url);
		response=null;
		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;

			httpResponse = client.execute(request);
			Log.i("Rest client - executeRequest", "processing results");
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();
			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);
				instream.close();
			}
   /*
			response="ERROR:"+e.getMessage();
			if (e.getCause()!=null) {
				response+=" CAUSE "+e.getCause().getMessage();
				Log.e("executeRequest", response);
			}
		*/
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
		} catch (IOException e) {
		}
		return sb.toString();
	}
	
}
