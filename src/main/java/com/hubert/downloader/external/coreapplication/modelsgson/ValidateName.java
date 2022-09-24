package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateName {

	@JsonProperty("ErrorCode")
	public int errorCode;

	@JsonProperty("ErrorMessage")
	public String errorMessage;

	@JsonProperty("IsValid")
	public boolean isValid;

}