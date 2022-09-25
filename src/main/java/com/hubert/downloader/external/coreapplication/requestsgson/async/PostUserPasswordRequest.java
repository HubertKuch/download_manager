package com.hubert.downloader.external.coreapplication.requestsgson.async;

import com.hubert.downloader.external.coreapplication.modelsgson.ApiError;
import com.hubert.downloader.external.coreapplication.requestsgson.BaseGsonRequest;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.Constants;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;
import net.minidev.json.JSONObject;

public class PostUserPasswordRequest extends BaseGsonRequest<ApiError> {

	private static final String ACCOUNT_ID = "AccountId";
	public static final int ERROR_WRONG_PASSWORD = 0;
	private static final String PASSWORD = "Password";
	public static final String PATH = "/account/passwords/read";

	private final String accountId, password;

	public PostUserPasswordRequest(String accountId, String password) {
		super(PATH, ApiError.class);
		this.accountId = accountId;
		this.password = password;
	}

	@Override
	public void prepareHttpRequest(HttpRequest httpRequest) {
		org.json.JSONObject body = new org.json.JSONObject();
		body.put(ACCOUNT_ID, accountId);
		body.put(PASSWORD, password);
		httpRequest.setBodyJson(body);
	}

	@Override
	public ApiError getResponse() throws Exception, PasswordRequiredException {
		ApiError apiError = super.getResponse();
		if (getResponseCode() == 401 && apiError.code == 2)
			throw new Exception(Constants.ERROR_INVALID_PASSWORD);
		return apiError;
	}

}