package com.hubert.downloader.external.pl.kubikon.chomikmanager;

import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.AndroidApi;

public class SessionManagerImpl extends SessionManager {

	public static final String SESSION_USERNAME = "username";
	public static final String SESSION_PASSWORD = "password";
	public static final String SESSION_AUTHORIZATION_TOKEN_ANDROID_API = "authTokenAndroid";
	public static final String SESSION_AUTHORIZATION_TOKEN_CHOMIK_BOX = "authTokenChomikBox";
	public static final String SESSION_AUTHORIZATION_TOKEN_WEB_API = "authTokenWebApi";
	public static final String SESSION_AUTHORIZATION_TOKEN_REMEMBER_ME_WEB_API = "authTokenRememberMeWebApi";
	public static final String SESSION_AUTHORIZATION_FOLDERS_ACCESS_COOKIE_WEB_API = "authFolderAccessCookie";
	public static final String SESSION_USER_HAMSTER_ID = "userHamsterId";
	public static final String SESSION_USER_ACCOUNT_ID = "userAccountId";
	public static final String SESSION_DEVICE_ID_ANDROID_API = "deviceId";

	public static boolean isLoggedIn() {
		//Jeżeli na któreś z API użytkownik nie jest zalogowany, wyloguj ze wszystkich
		if (AndroidApi.isLoggedIn() && !AndroidApi.isLoggedIn()) {
			return false;
		}
		return AndroidApi.isLoggedIn();
	}

	public static String getLoggedInUserName() {
		return getParam(SESSION_USERNAME);
	}

	public static String getLoggedInUserHamsterId() {
		return getParam(SESSION_USER_HAMSTER_ID);
	}

	public static String getLoggedInUserAccountId() {
		return getParam(SESSION_USER_ACCOUNT_ID);
	}

	public static String getLoggedInPassword() {
		return getParam(SESSION_PASSWORD);
	}

}