package com.hubert.downloader.external.coreapplication.requestsgson.async;

import com.hubert.downloader.external.coreapplication.helpers.SearchResponseParser;
import com.hubert.downloader.external.coreapplication.models.AccountsListItem;
import com.hubert.downloader.external.coreapplication.requestsgson.BaseGsonRequest;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;
import net.minidev.json.JSONObject;

import java.util.ArrayList;

public class GetSearchAccountsRequest extends BaseGsonRequest<JSONObject> {

	public static final int ERROR_PARSING = 1;
	private static final String PATH = "/account/search";
	private final String query;
	private final int page;

	//@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SearchAccountsResult {

		//@JsonProperty("Friends")
		public ArrayList<AccountsListItem> data;

		//@JsonProperty("IsNextPageAvailable")
		public boolean isNextPageAvailable = false;

	}

	public GetSearchAccountsRequest(String query, int page) {
		super(PATH, JSONObject.class);
		this.query = query;
		this.page = page;
	}

	@Override
	public void prepareHttpRequest(HttpRequest httpRequest) {
		httpRequest.addUrlParam("Query", query);
		httpRequest.addUrlParam("PageNumber", page);
	}

	public SearchAccountsResult getParsedResponse() throws Exception {
		org.json.JSONObject jSONObject = getResponseAsJsonObject();
		SearchAccountsResult searchAccountsResult = new SearchAccountsResult();
		searchAccountsResult.isNextPageAvailable = jSONObject.optBoolean("IsNextPageAvailable", false);
		try {
			searchAccountsResult.data = SearchResponseParser.parseAccountsJsonToArrayList(jSONObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (searchAccountsResult.data != null)
			return searchAccountsResult;
		else
			throw new Exception("GetSearchAccountsRequest: parsing error");
	}

}