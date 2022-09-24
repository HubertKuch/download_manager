 package com.hubert.downloader.external.pl.kubikon.chomikmanager.api;

import com.hubert.downloader.external.coreapplication.models.AccountsListItem;
import com.hubert.downloader.external.coreapplication.modelsgson.ApiError;
import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownload;
import com.hubert.downloader.external.coreapplication.modelsgson.GetDownloadUrl;
import com.hubert.downloader.external.coreapplication.requestsgson.async.*;
import com.hubert.downloader.external.coreapplication.utils.HttpUtils;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.SessionManager;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.SessionManagerImpl;
import com.hubert.downloader.external.pl.kubikon.shared.utils.Utils;

import java.util.UUID;

//APK 3.5 (165)
public class AndroidApi {

	public static final String BASE_URL = HttpUtils.SERVICE_ADDRESS + "/" + HttpUtils.API_VERSION_V3;

	public static void login(String userName, String password) throws Exception, PasswordRequiredException {
		PostLoginRequest.LoginResponse response = new PostLoginRequest(userName, password).getResponse();
		if (response.responseCode != 0) {
			SessionManagerImpl.clearLocalTokenData();
			String errorMessage = "Error " + response.responseCode;
			Utils.log("Error while logging in: " + errorMessage);
			throw new Exception(errorMessage);
		} else {
			Utils.log("Succesfully logged in");
			SessionManagerImpl.setParam(SessionManagerImpl.SESSION_AUTHORIZATION_TOKEN_ANDROID_API, response.apiKey);
			SessionManagerImpl.setParam(SessionManagerImpl.SESSION_USERNAME, response.accountName);
			SessionManagerImpl.setParam(SessionManagerImpl.SESSION_PASSWORD, password);
			SessionManagerImpl.setParam(SessionManagerImpl.SESSION_USER_HAMSTER_ID, response.accountId);
		}
	}

	public static GetAccountInfo.AccountInfo getAccountInfo() throws Exception {
		return new GetAccountInfo().getResponse();
	}

	public static GetDownloadUrl getDownloadUrl(long fileId) throws Exception {
		return new GetUrlDownloadRequest(fileId).getResponse();
	}

	public static GetSearchAccountsRequest.SearchAccountsResult getSearchAccountResult(String query, int page) throws Exception {
		return new GetSearchAccountsRequest(query, page).getParsedResponse();
	}

	public static AccountsListItem searchForAccount(String accountName) throws Exception {
		GetSearchAccountsRequest.SearchAccountsResult searchAccountsResult;
		int page = 1;
		do {
			searchAccountsResult = getSearchAccountResult(accountName, page);
			for (AccountsListItem accountsListItem : searchAccountsResult.data) {
				if (accountsListItem.getAccountName().equals(accountName)) {
					return accountsListItem;
				}
			}
			page++;
		} while (searchAccountsResult.isNextPageAvailable);
		throw new Exception("getAccountByName(): no account found");
	}

	public static FolderDownload getFolderDownload(String accountId, String folderId, String folderName) throws Exception, PasswordRequiredException {
		return new GetFolderDetailsRequest(accountId, folderId, folderName).getResponse();
	}

	public static ApiError postPassword(String accountId, String folderId, String password) throws Exception {
		if (folderId.equals("0"))
			return postUserPassword(accountId, password);
		else
			return postFolderPassword(accountId, folderId, password);
	}

	public static ApiError postFolderPassword(String accountId, String folderId, String password) throws Exception {
		return new PostFoldersPasswordRequest(accountId, folderId, password).getResponse();
	}

	public static ApiError postUserPassword(String accountId, String password) throws Exception {
		return new PostUserPasswordRequest(accountId, password).getResponse();
	}

	public static boolean isLoggedIn() {
		return SessionManager.hasParam(SessionManagerImpl.SESSION_AUTHORIZATION_TOKEN_ANDROID_API) && SessionManager.hasParam(SessionManagerImpl.SESSION_USERNAME);
	}

	public static String getDeviceId() {
		String deviceId = SessionManager.getParam(SessionManagerImpl.SESSION_DEVICE_ID_ANDROID_API);
		if (deviceId == null) {
			deviceId = UUID.randomUUID().toString();
			try {
				SessionManager.setParam(SessionManagerImpl.SESSION_DEVICE_ID_ANDROID_API, deviceId);
			} catch (Exception e) {
				Utils.log(e);
			}
		}
		return deviceId;
	}
}