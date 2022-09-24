package com.hubert.downloader.external.pl.kubikon.shared.utils;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

	public final int responseCode;
	public final String responseBody;
	public final Map<String, String> responseCookies;

	public HttpResponse(int responseCode, String responseBody, Map<String, String> responseCookies) {
		this.responseCode = responseCode;
		this.responseBody = responseBody;
		this.responseCookies = responseCookies;
	}

}