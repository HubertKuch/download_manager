package com.hubert.downloader.external.coreapplication.requestsgson.async;

import com.hubert.downloader.external.coreapplication.modelsgson.ApiError;
import com.hubert.downloader.external.coreapplication.requestsgson.BaseGsonRequest;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.Constants;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;
import org.json.JSONObject;

public class PostFoldersPasswordRequest extends BaseGsonRequest<ApiError> {

	private static final String ACCOUNT_ID = "AccountId";
	public static final int ERROR_WRONG_PASSWORD = 1;
	private static final String FOLDER_ID = "FolderId";
	public static final String PASSWORD = "Password";
	public static final String PATH = "/folders/password";

	private final String accountId, folderId, password;

	public PostFoldersPasswordRequest(String accountId, String folderId, String password) {
		super(PATH, ApiError.class);
		this.accountId = accountId;
		this.folderId = folderId;
		this.password = password;
	}

	@Override
	public void prepareHttpRequest(HttpRequest httpRequest) {
		JSONObject body = new JSONObject();
		body.put(ACCOUNT_ID, accountId);
		body.put(FOLDER_ID, folderId);
		body.put(PASSWORD, password);

		System.out.println(body);

		httpRequest.setBodyJson(body);
	}

	@Override
	public ApiError getResponse() throws Exception, PasswordRequiredException {
		ApiError apiError = super.getResponse();

		if (getResponseCode() == 401 && (apiError.code == 12 || apiError.code == 2))
			throw new Exception(Constants.ERROR_INVALID_PASSWORD);
		return apiError;
	}
}