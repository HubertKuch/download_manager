package com.hubert.downloader.external.pl.kubikon.shared.utils;

import com.hubert.downloader.external.pl.kubikon.chomikmanager.Constants;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class HttpRequest {

	private final String requestId;
	private final String url;
	private final Map<String, Object> urlParams = new HashMap<>();
	private final Map<String, Object> bodyParams = new HashMap<>();
	private final Map<String, String> headers = new HashMap<>();
	private Method method = Method.GET;
	private String jsonBody;
	private String rawBody;
	private HttpURLConnection connection;
	private int connectionTimeoutMs = 30000;
	private String contentType;
	private boolean log;
	private boolean doNotFollowRedirects;
	private boolean trustEveryone;
	private int responseCode;
	private Certificate certificate;
	private final Map<String, String> responseCookies = new HashMap<>();

	public HttpRequest(String url) {
		this.requestId = "HTTP" + System.nanoTime();
		this.url = url;
	}

	public HttpRequest setMethod(Method method) {
		this.method = method;
		return this;
	}

	public HttpRequest addUrlParam(String key, Object value) {
		this.urlParams.put(key, value);
		return this;
	}

	public HttpRequest addBodyParam(String key, Object value) {
		this.bodyParams.put(key, value);
		setMethod(Method.POST);
		contentType = "application/x-www-form-urlencoded";
		return this;
	}

	public HttpRequest doNotFollowRedirects() {
		doNotFollowRedirects = true;
		return this;
	}

	public HttpRequest setLogEnabled() {
		log = true;
		return this;
	}

	public HttpRequest setBodyJson(JSONObject jsonObject) {
		setMethod(Method.POST);
		jsonBody = jsonObject.toString();
		contentType = "application/json";
		return this;
	}

	public HttpRequest setBodyJson(JSONArray jsonArray) {
		setMethod(Method.POST);
		jsonBody = jsonArray.toString();
		contentType = "application/json";
		return this;
	}

	public HttpRequest setBodyRaw(String body) {
		setMethod(Method.POST);
		rawBody = body;
		return this;
	}

	private String encodeUrlParams(Map<String, Object> postparams) {
		StringBuilder sb = new StringBuilder();
		try {
			for (String key : postparams.keySet()) {
				if (sb.length() > 0)
					sb.append("&");
				Object value = postparams.get(key);
				if (value instanceof String)
					sb.append(key).append("=").append(URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8));
				else
					sb.append(key).append("=").append(value);
			}
		} catch (Exception ignored) {
		}
		return sb.toString();
	}

	public HttpRequest addHeader(String key, String value) {
		this.headers.put(key, value);
		return this;
	}

	public HttpRequest addHeaders(Map<String, String> headers) {
		this.headers.putAll(headers);
		return this;
	}

	public HttpRequest setConnectionTimeout(int connectionTimeoutMs) {
		this.connectionTimeoutMs = connectionTimeoutMs;
		return this;
	}

	public HttpRequest pinCertificate(Certificate certificate) {
		this.certificate = certificate;
		return this;
	}

	public InputStream inputStream() throws Exception {
		try {
			String url = getUrl();
			this.connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod(method.name());
			connection.setConnectTimeout(connectionTimeoutMs);
			for (String key : headers.keySet()) {
				connection.setRequestProperty(key, headers.get(key));
			}
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			//trustEveryone(connection);
			if (connection instanceof HttpsURLConnection) {
				HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
				if (trustEveryone) {
					httpsURLConnection.setHostnameVerifier((hostname, session) -> true);
					TrustManager[] trustAllCerts = new TrustManager[]{
							new X509TrustManager() {
								public X509Certificate[] getAcceptedIssuers() {
									return new X509Certificate[0];
								}

								public void checkClientTrusted(X509Certificate[] certs, String authType) {
								}

								public void checkServerTrusted(X509Certificate[] certs, String authType) {
								}
							}
					};
					SSLContext sslContext = SSLContext.getInstance("SSL");
					sslContext.init(null, trustAllCerts, new SecureRandom());
					sslContext.getProtocol();
					sslContext.getSupportedSSLParameters();
					sslContext.getDefaultSSLParameters();
					//SSLEngine engine = sslContext.createSSLEngine();
					httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
				} else if (certificate != null) {
					KeyStore ks = KeyStore.getInstance("BKS");
					ks.load(null, null);
					ks.setCertificateEntry("cert", certificate);
					TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					tmf.init(ks);
					SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
					sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
					httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
				}
			}
			if (doNotFollowRedirects)
				connection.setInstanceFollowRedirects(false);
			if (method == Method.POST) {
				String body = getBody();
				if (body != null) {
					connection.setDoOutput(true);
					byte[] postData = body.getBytes(StandardCharsets.UTF_8);
					if (contentType != null)
						connection.setRequestProperty("Content-Type", contentType);
					connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
					connection.setUseCaches(false);
					DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
					wr.write(postData);
				}
			}
			responseCode = connection.getResponseCode();
			InputStream stream;
			if (responseCode >= 400)
				stream = connection.getErrorStream();
			else
				stream = connection.getInputStream();
			String responseEncoding = connection.getHeaderField("Content-Encoding");
			if (responseEncoding != null && responseEncoding.trim().equals("gzip")) {
				stream = new GZIPInputStream(stream);
			}
			Map<String, List<String>> headerFields = connection.getHeaderFields();
			for (String headerName : headerFields.keySet()) {
				if (headerName != null && headerName.equalsIgnoreCase("Set-Cookie")) {
					List<String> cookiesHeader = headerFields.get(headerName);
					if (cookiesHeader != null) {
						for (String cookie : cookiesHeader)
							for (HttpCookie httpCookie : HttpCookie.parse(cookie))
								responseCookies.put(httpCookie.getName(), httpCookie.getValue());
					}
				}
			}
			return stream;
		} catch (SSLHandshakeException e) {
			e.printStackTrace();
			throw new Exception(Constants.ERROR_SSL_ERROR);
		}
	}

	public String run() throws Exception {
		return fetchResponse().responseBody;
	}

	public HttpResponse fetchResponse() throws Exception {
		InputStream stream = inputStream();
		String responseBody = null;

		if (stream != null) {

			responseBody = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
			stream.close();
		}
		return new HttpResponse(connection.getResponseCode(), responseBody, responseCookies);
	}

	public HttpRequest trustEveryone() {
		trustEveryone = true;
		return this;
	}

	public String getUrl() {
		String fullUrl = url;
		if (!urlParams.isEmpty()) {
			//if (!fullUrl.endsWith("/"))
			//	fullUrl += "/";
			fullUrl += "?" + encodeUrlParams(urlParams);
		}
		return fullUrl;
	}

	public String getBody() {
		String body = null;
		if (!bodyParams.isEmpty())
			body = encodeUrlParams(bodyParams);
		else if (jsonBody != null)
			body = jsonBody;
		else if (rawBody != null)
			body = rawBody;
		return body;
	}

	public int getResponseCode() {
		return responseCode;
	}

}