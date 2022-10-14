package com.hubert.downloader.external.pl.kubikon.chomikmanager.api;

import com.hubert.downloader.external.pl.kubikon.chomikmanager.Constants;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.SessionManagerImpl;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpResponse;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import pl.kubikon.shared.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class WebApi {

	public static final long SAVE_FILE_TRY_MANY_TIMES_SLEEP_INTERVAL_MS = 1500;
	public static final long SAVE_FILE_TRY_MANY_TIMES_MAX_RETRIES = 20;
	public static final long TRY_AGAIN_INTERVAL_MS = 11000;

	public static final String BASE_URL = "https://chomikuj.pl";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0";
	public static String requestVerificationToken;
	public static final Object savingFilesLock = new Object();
	public static long lastFileSaveRequest;

	public static void login(String userName, String password) throws Exception, CopyingForbiddenException, ReloginRequiredException, TryAgainException, TooFastRequestsException {
		callApi(getBaseHttpRequest("/action/Login/TopBarLogin")
				.addBodyParam("Login", userName)
				.addBodyParam("Password", password), JSONObject.class);
	}

	public static void postPassword(String accountName, String folderId, String folderName, String password) throws Exception, CopyingForbiddenException, ReloginRequiredException, TryAgainException, TooFastRequestsException {
		if (folderId.equals("0"))
			postUserPassword(accountName, password);
		else
			postFolderPassword(accountName, folderId, folderName, password);
	}

	public static void postFolderPassword(String accountName, String folderId, String folderName, String password) throws Exception, CopyingForbiddenException, ReloginRequiredException, TryAgainException, TooFastRequestsException {
		callApiAndRetryIfBadToken(getBaseHttpRequest("/action/Files/LoginToFolder")
				.addBodyParam("ChomikName", accountName)
				.addBodyParam("FolderId", folderId)
				.addBodyParam("FolderName", folderName)
				.addBodyParam("Password", password)
				.addBodyParam("Remember", true), String.class);
	}

	public static void postUserPassword(String accountName, String password) throws Exception, CopyingForbiddenException, ReloginRequiredException, TryAgainException, TooFastRequestsException {
		HttpResponse response = callApiAndRetryIfBadToken(getBaseHttpRequest("/action/UserAccess/LoginToProtectedWindow")
				.addBodyParam("TargetChomikName", accountName)
				.addBodyParam("AccountName", accountName)
				.addBodyParam("Password", password)
				.addBodyParam("OK", "OK")
				.addBodyParam("RemeberMe", true)
				.doNotFollowRedirects(), HttpResponse.class);
		if (response.responseCode != 302)
			throw new Exception(Constants.ERROR_INVALID_PASSWORD);
	}

	public static HttpRequest getBaseHttpRequest(String path) throws Exception {
		HttpRequest httpRequest = new HttpRequest(BASE_URL + path)
				.addHeaders(getHeaders())
				.addBodyParam("__RequestVerificationToken", getRequestVerificationToken());
		return httpRequest;
	}

	public static <T> T callApiAndRetryIfBadToken(HttpRequest httpRequest, Class<T> type) throws Exception, CopyingForbiddenException, ReloginRequiredException, TryAgainException, TooFastRequestsException {
		try {
			return callApi(httpRequest, type);
		} catch (ReloginRequiredException e) {
			login(SessionManagerImpl.getLoggedInUserName(), SessionManagerImpl.getLoggedInPassword());
			return callApi(httpRequest, type);
		} catch (TryAgainException e) {
			Thread.sleep(TRY_AGAIN_INTERVAL_MS);
			return callApi(httpRequest, type);
		}
	}

	public static <T> T callApi(HttpRequest httpRequest, Class<T> type) throws Exception, ReloginRequiredException, TooFastRequestsException, TryAgainException, CopyingForbiddenException {
		HttpResponse httpResponse = httpRequest.fetchResponse();
		if (httpResponse.responseBody != null) {
			if (httpResponse.responseBody.contains("musisz być zalogowany"))
				throw new ReloginRequiredException();
			if (httpResponse.responseBody.contains("Nie możesz tak szybko chomikować plików"))
				throw new TooFastRequestsException();
			if (httpResponse.responseBody.contains("Prosimy spróbować ponownie za chwilę"))
				throw new TryAgainException();
			if (httpResponse.responseBody.contains("Właściciel chomik nie zgadza się na przechomikowywanie plików od niego"))
				throw new CopyingForbiddenException();
		}
		String chomikSession = httpResponse.responseCookies.get("ChomikSession");
		String rememberMe = httpResponse.responseCookies.get("RememberMe");
		String foldersAccess = httpResponse.responseCookies.get("FoldersAccess");
		if (chomikSession != null) {
			SessionManagerImpl.setParam(SessionManagerImpl.SESSION_AUTHORIZATION_TOKEN_WEB_API, chomikSession);
		}
		if (rememberMe != null) {
			SessionManagerImpl.setParam(SessionManagerImpl.SESSION_AUTHORIZATION_TOKEN_REMEMBER_ME_WEB_API, rememberMe);
		}
		if (foldersAccess != null) {
			SessionManagerImpl.setParam(SessionManagerImpl.SESSION_AUTHORIZATION_FOLDERS_ACCESS_COOKIE_WEB_API, foldersAccess);
		}
		if (type == JSONObject.class) {
			JSONObject response = new JSONObject(httpResponse.responseBody);
			if (response.has("isSuccess")) {
				if (!response.getBoolean("IsSuccess"))
					throw new Exception(String.format(Constants.ERROR_WEB_API_WITH_DETAILS, response.optString("Message")));
				if (!response.has("Data") || response.isNull("Data")) {
					if (response.has("Content") && !response.isNull("Content"))
						throw new Exception(String.format(Constants.ERROR_WEB_API_WITH_DETAILS, response.getString("Content")));
					else
						throw new Exception(String.format(Constants.ERROR_WEB_API_WITH_DETAILS, response));
				}
			}
			return (T) response;
		}
		if (type == String.class)
			return (T) httpResponse.responseBody;
		if (type == HttpResponse.class)
			return (T) httpResponse;
		throw new Exception("WebApi.callApi(): invalid type " + type.getName());
	}

	public static Map<String, String> getHeaders() throws Exception {
		Map<String, String> headers = new HashMap<>();
		headers.put("Referer", "https://chomikuj.pl");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("User-Agent", USER_AGENT);
		headers.put("Cookie", getRequestCookiesString());
		return headers;
	}

	public static String getRequestCookiesString() throws Exception {
		Map<String, String> requestsCookies = new HashMap<>();
		requestsCookies.put("__RequestVerificationToken_Lw__", getRequestVerificationToken());
		if (getChomikSessionWebApi() != null)
			requestsCookies.put("ChomikSession", getChomikSessionWebApi());
		if (getRememberMeTokenWebApi() != null)
			requestsCookies.put("RememberMe", getRememberMeTokenWebApi());
		if (getFoldersAccessCookie() != null)
			requestsCookies.put("FoldersAccess", getFoldersAccessCookie());
		StringBuilder sb = new StringBuilder();
		for (String key : requestsCookies.keySet()) {
			if (sb.length() > 0)
				sb.append(";");
			//sb.append(key).append("=").append(URLEncoder.encode(requestsCookies.get(key), StandardCharsets.UTF_8));
			sb.append(key).append("=").append(requestsCookies.get(key));
		}
		return sb.toString();
	}

	public static String getRequestVerificationToken() throws Exception {
		if (requestVerificationToken != null)
			return requestVerificationToken;
		HttpRequest httpRequest = new HttpRequest(BASE_URL)
				.addHeader("User-Agent", USER_AGENT);
		String html = httpRequest.run();
		Element element = Jsoup.parse(html).getElementsByAttributeValue("name", "__RequestVerificationToken").get(0);
		requestVerificationToken = element.attr("value");
		return requestVerificationToken;
	}

	public static boolean isAccountPasswordProtected(String accountName) throws Exception {
		HttpRequest httpRequest = new HttpRequest(BASE_URL + "/" + accountName)
				.addHeader("User-Agent", USER_AGENT);
		String html = httpRequest.run();
		return html.contains("Dostęp do tego konta wymaga podania hasła");
	}

	public static boolean isLoggedIn() {
		return getChomikSessionWebApi() != null && getRememberMeTokenWebApi() != null;
	}

	public static String getChomikSessionWebApi() {
		return SessionManager.getParam(SessionManagerImpl.SESSION_AUTHORIZATION_TOKEN_WEB_API);
	}

	public static String getRememberMeTokenWebApi() {
		return SessionManager.getParam(SessionManagerImpl.SESSION_AUTHORIZATION_TOKEN_REMEMBER_ME_WEB_API);
	}

	public static String getFoldersAccessCookie() {
		return SessionManager.getParam(SessionManagerImpl.SESSION_AUTHORIZATION_FOLDERS_ACCESS_COOKIE_WEB_API);
	}

}