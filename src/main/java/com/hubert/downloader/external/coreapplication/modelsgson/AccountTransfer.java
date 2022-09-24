package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountTransfer {

	@JsonProperty("BalanceInfo")
	public String balanceInfo;

	@JsonProperty("BalanceInfoShort")
	public String balanceInfoShort;

	@JsonProperty("DownloadsAvailable")
	public boolean downloadsAvailable;

	@JsonProperty("DownloadsQouta")
	public long downloadsQouta;

	@JsonProperty("HasUnlimitedTransfer")
	public boolean hasUnlimitedTransfer;

	@JsonProperty("Points")
	public int points;

	@JsonProperty("PointsAvailable")
	public boolean pointsAvailable;

	@JsonProperty("PointsInfo")
	public String pointsInfo;

	@JsonProperty("QuotaAdditional")
	public long quotaAdditional;

	@JsonProperty("QuotaLeft")
	public long quotaLeft;

	@JsonProperty("QuotaPeriodical")
	public long quotaPeriodical;

}