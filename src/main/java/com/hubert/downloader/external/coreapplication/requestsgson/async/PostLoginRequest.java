package com.hubert.downloader.external.coreapplication.requestsgson.async;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.downloader.external.coreapplication.modelsgson.AccountTransfer;
import com.hubert.downloader.external.coreapplication.requestsgson.BaseGsonRequest;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;
import org.json.JSONObject;

public class PostLoginRequest extends BaseGsonRequest<PostLoginRequest.LoginResponse> {

	public static final int ERROR_ACCOUNT_DOESNT_EXIST = 1;
	public static final int ERROR_WRONG_PASSWORD = 2;

	private final String userName;
	private final String password;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class LoginResponse {

		@JsonProperty("AccountId")
		public String accountId;

		@JsonProperty("AccountName")
		public String accountName;

		@JsonProperty("ApiKey")
		public String apiKey;

		@JsonProperty("AvatarUrl")
		public String avatarUrl;

		@JsonProperty("AccountBalance")
		public AccountTransfer balance;

		@JsonProperty("Code")
		public Integer responseCode;

	}

	public PostLoginRequest(String userName, String password) {
		super("/account/login", LoginResponse.class);
		this.userName = userName;
		this.password = password;
	}

	@Override
	public void prepareHttpRequest(HttpRequest httpRequest) {
		JSONObject body = new JSONObject();
		body.put("AccountName", userName);
		body.put("Password", password);
		httpRequest.setBodyJson(body);
	}
}