package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDownloadUrl {

	@JsonProperty("AccountBalance")
	public AccountTransfer accountTransfer;

	@JsonProperty("Code")
	public Integer code;

	@JsonProperty("FileUrl")
	public String fileUrl;

	@JsonProperty("LicenseValidTo")
	public Date licenseValidTo;

	@JsonProperty("Message")
	public String message;

}