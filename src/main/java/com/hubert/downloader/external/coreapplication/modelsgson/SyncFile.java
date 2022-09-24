package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncFile {

	@JsonProperty("Chunk")
	public Long chunk;

	@JsonProperty("Code")
	public Integer code;

	@JsonProperty("UploadCompleted")
	public Boolean uploadCompleted;

	@JsonProperty("Url")
	public String url;

}