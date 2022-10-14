package com.hubert.downloader.external.coreapplication.requestsgson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.coreapplication.utils.HttpUtils;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.SessionManagerImpl;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.AndroidApi;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpResponse;
import com.hubert.downloader.external.pl.kubikon.shared.utils.Utils;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseGsonRequest<T> {

	public final ObjectMapper mapper = new ObjectMapper();
	private final HttpRequest httpRequest;
	private final String url;
	private final Class<T> valueType;

	public BaseGsonRequest(String uri, Class<T> valueType) {
		this.url = AndroidApi.BASE_URL + uri;
		this.valueType = valueType;
		httpRequest = new HttpRequest(url).setLogEnabled();
	}

	public abstract void prepareHttpRequest(HttpRequest httpRequest);

	public String getResponseAsString() throws Exception {
		prepareHttpRequest(httpRequest);
		if (AndroidApi.isLoggedIn())
			httpRequest.addHeader("Api-Key", SessionManagerImpl.getParam(SessionManagerImpl.SESSION_AUTHORIZATION_TOKEN_ANDROID_API));
		httpRequest.addHeaders(getHeaders(httpRequest));
		HttpResponse httpResponse = httpRequest.fetchResponse();


		int retryIndex = 0;
		while (httpResponse.responseCode == 522 && retryIndex < 5) {//522: Connection timed out
			Thread.sleep(8000);
			httpResponse = httpRequest.fetchResponse();
			retryIndex++;
		}
		return httpResponse.responseBody;
	}

	public T getResponse() throws Exception, PasswordRequiredException {
		String response = getResponseAsString();
		if (response == null || response.isEmpty())
			return null;
		return mapper.readValue(response, valueType);
	}

	public JSONObject getResponseAsJsonObject() throws Exception {
		return new JSONObject(getResponseAsString());
	}

	public static Map<String, String> getHeaders(HttpRequest httpRequest) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Token", signRequest(httpRequest));
		headers.put("User-Agent", "android/3.5 (" + AndroidApi.getDeviceId() + "; Samsung SM-G991)");

		return headers;
	}

	public int getResponseCode() {
		return httpRequest.getResponseCode();
	}

	//com.coreapplication.managers.RequestManager
	public static String signRequest(HttpRequest httpRequest) {
		try {
			URL url = new URL(httpRequest.getUrl());
			String path = url.getPath();
			String query = url.getQuery();
			String fullPath;
			if (query != null)
				fullPath = path + "?" + query;
			else
				fullPath = path;
			StringBuilder sb = new StringBuilder().append(fullPath);
			if (httpRequest.getBody() != null)
				sb.append(httpRequest.getBody());
			sb.append(HttpUtils.SECRET_KEY);
			return Utils.md5(sb.substring(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}