package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrvMessageFrom {

	@JsonProperty("AvatarUrl")
	public String avatarUrl;

	@JsonProperty("Id")
	/* renamed from: id */
	public String f509id;

	@JsonProperty("Name")
	public String name;

}
