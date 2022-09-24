package com.hubert.downloader.external.coreapplication.models;

/* loaded from: classes.dex */
public class ListItem implements Cloneable {
	public static final String ITEM_TYPE = "media_type_b";
	public static final int ITEM_TYPE_BACK = 5;
	public static final int ITEM_TYPE_DOWNLOADABLE_ITEM = 0;
	public static final int ITEM_TYPE_FOLDER = 3;
	public static final int ITEM_TYPE_FOLDER_LOCKED = 4;
	public static final String SMALL_THUMB = "small_thumb";
	public static final String THUMB = "thumb";
	public int itemType;
	public String smallThumbUrl;
	public String thumbUrl;

	public ListItem(int i) {
		this.itemType = i;
	}

	public ListItem() {
		this.itemType = 0;
	}

	public String getThumbUrl() {
		if (this.thumbUrl == null || this.thumbUrl.equals("")) {
			return null;
		}
		return this.thumbUrl;
	}

	public int getItemType() {
		return this.itemType;
	}

	public void setThumbUrl(String str) {
		this.thumbUrl = str;
	}

	public String getSmallThumbUrl() {
		return this.smallThumbUrl;
	}

	public void setSmallThumbUrl(String str) {
		this.smallThumbUrl = str;
	}

	public void setItemType(int i) {
		this.itemType = i;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
