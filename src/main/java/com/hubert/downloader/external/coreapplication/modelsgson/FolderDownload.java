package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderDownload extends ApiError {

	@JsonProperty("Code")
	public Integer code;

	@JsonProperty("Files")
	public ArrayList<FolderDownloadChFile> files;

	@JsonProperty("FilesCount")
	public int filesCount;

	@JsonProperty("FilesSize")
	public long filesSize;

	@JsonProperty("QuotaRequired")
	public long quotaRequired;

}