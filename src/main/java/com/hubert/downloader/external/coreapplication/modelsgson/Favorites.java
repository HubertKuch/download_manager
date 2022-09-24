package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Favorites {

	@JsonProperty("Friends")
	public ArrayList<Friend> friends;

	@JsonProperty("IsNextPageAvailable")
	public boolean isNextPageAvailable;

}