package com.hubert.downloader.external.coreapplication.requestsgson.async;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.downloader.external.coreapplication.requestsgson.BaseGsonRequest;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.Constants;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;

public class GetAccountInfo extends BaseGsonRequest<GetAccountInfo.AccountInfo> {

	private static final String PATH = "/account/info";

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AccountInfo {

		@JsonProperty("AvatarUrl")
		public String avatarUrl;

		@JsonProperty("BalanceInfo")
		public String balanceInfo;

		@JsonProperty("HasUnlimitedTransfer")
		public boolean hasUnlimitedTransfer;

		@JsonProperty("Points")
		public int points;

		@JsonProperty("PointsAvailable")
		public boolean pointsAvailable;

		@JsonProperty("PointsInfo")
		public String pointsinfo;

		@JsonProperty("Transfer")
		public int transfer;

		@Override
		public String toString() {
			return "AccountInfo{" +
					"avatarUrl='" + avatarUrl + '\'' +
					", balanceInfo='" + balanceInfo + '\'' +
					", hasUnlimitedTransfer=" + hasUnlimitedTransfer +
					", points=" + points +
					", pointsAvailable=" + pointsAvailable +
					", pointsinfo='" + pointsinfo + '\'' +
					", transfer=" + transfer +
					'}';
		}

	}

	public GetAccountInfo() {
		super(PATH, AccountInfo.class);
	}

	@Override
	public void prepareHttpRequest(HttpRequest httpRequest) {
	}

	@Override
	public AccountInfo getResponse() throws Exception, PasswordRequiredException {
		AccountInfo response = super.getResponse();
		if (getResponseCode() == 401) {
			throw new Exception(Constants.ERROR_RELOGIN_REQUIRED);
		}
		return response;
	}
}