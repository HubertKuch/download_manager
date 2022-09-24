package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Modules {

	@JsonProperty("PaymentsEnabled")
	public boolean paymentsEnabled = true;

	@JsonProperty("FolderDownloadEnabled")
	public boolean folderDownloadEnabled = true;

	@JsonProperty("SubscriptionsEnabled")
	public boolean subscriptionsEnabled = true;

}