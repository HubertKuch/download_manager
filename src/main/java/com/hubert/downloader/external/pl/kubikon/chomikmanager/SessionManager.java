package pl.kubikon.shared;

import com.hubert.downloader.external.pl.kubikon.shared.utils.Utils;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class SessionManager implements Serializable {

	public static void setParam(String paramName, String paramValue) throws Exception {
		JSONObject jsonObject = getSessionData();
		jsonObject.put(paramName, paramValue);
		setSessionData(jsonObject);
	}

	public static String getParam(String paramName) {
		return getSessionData().optString(paramName, null);
	}

	public static String getParam(String paramName, String defaultValue) {
		return getSessionData().optString(paramName, defaultValue);
	}

	public static boolean hasParam(String paramName) {
		return getSessionData().has(paramName);
	}

	public static JSONObject getSessionData() {
		File file = Utils.getLocalTokenDataFile();
		if (file.exists()) {
			try (FileInputStream inputStream = new FileInputStream(file)) {
				return new JSONObject(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
			} catch (Exception e) {
			}
		}
		return new JSONObject();
	}

	public static void setSessionData(JSONObject sessionData) {
		try (FileOutputStream outputStream = new FileOutputStream(Utils.getLocalTokenDataFile())) {
			outputStream.write(sessionData.toString().getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
		}
	}

	public static void clearLocalTokenData() {
		setSessionData(new JSONObject());
	}

}