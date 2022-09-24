package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalance {

	@JsonProperty("AccountBalance")
	public AccountTransfer accountBalance;

	@JsonProperty("Code")
	public int code;

	@JsonProperty("LicenseValidTo")
	public String licenseValidTo;

}