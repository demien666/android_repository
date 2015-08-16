package com.demien.weather.ua.service;

import java.io.Serializable;

public class RestClientResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2518502868448582522L;
	private String response;
	private String errorMessage;
	
	public RestClientResult() {
		
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	

}
