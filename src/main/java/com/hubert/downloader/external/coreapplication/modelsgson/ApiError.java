package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiError {

	@JsonProperty("argExtra")
	public String argExtra;

	@JsonProperty("Code")
	public Integer code;

	@JsonProperty("Message")
	public String message;

}