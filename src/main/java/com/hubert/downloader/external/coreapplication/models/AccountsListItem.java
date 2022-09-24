package com.hubert.downloader.external.coreapplication.models;

public class AccountsListItem extends ListItem {

	private String mAccountId;
	private String mAccountName;
	private int mTotalFilesCount;

	public AccountsListItem(String mAccountId, String mAccountName) {
		this(mAccountId, mAccountName, "", 0);
	}

	public AccountsListItem(String mAccountId, String mAccountName, String thumbUrl, int mTotalFilesCount) {
		this.mAccountId = mAccountId;
		this.mAccountName = mAccountName;
		this.mTotalFilesCount = mTotalFilesCount;
		setThumbUrl(thumbUrl);
	}

	public AccountsListItem(int i) {
		this.itemType = i;
	}

	public String getAccountId() {
		return this.mAccountId;
	}

	public void setAccountId(String str) {
		this.mAccountId = str;
	}

	public String getAccountName() {
		return this.mAccountName;
	}

	public void setAccountName(String str) {
		this.mAccountName = str;
	}

	public int getTotalFilesCount() {
		return this.mTotalFilesCount;
	}

	public void setTotalFilesCount(int i) {
		this.mTotalFilesCount = i;
	}

}