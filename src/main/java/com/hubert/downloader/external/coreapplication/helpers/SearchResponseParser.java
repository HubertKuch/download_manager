package com.hubert.downloader.external.coreapplication.helpers;

import com.hubert.downloader.external.coreapplication.models.AccountsListItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class SearchResponseParser {

	public static ArrayList<AccountsListItem> parseAccountsJsonToArrayList(JSONObject jsonObject) {
		System.out.println(jsonObject);
		JSONArray array = jsonObject.getJSONArray("Results");
		ArrayList<AccountsListItem> list = new ArrayList<>();
		int length = array.length();
		for (int i = 0; i < length; i++) {
			JSONObject object = array.optJSONObject(i);
			list.add(new AccountsListItem(object.optString("AccountId"), object.optString("AccountName"), object.optString("AccountAvatarUrl"), object.optInt("TotalFilesCount")));
		}
		return list;
	}
}
