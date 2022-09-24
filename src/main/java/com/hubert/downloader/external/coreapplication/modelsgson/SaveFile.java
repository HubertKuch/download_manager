package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveFile extends ApiError {

	@JsonProperty("FileId")
	public long fileId;

	@JsonProperty("FileName")
	public String fileName;

}