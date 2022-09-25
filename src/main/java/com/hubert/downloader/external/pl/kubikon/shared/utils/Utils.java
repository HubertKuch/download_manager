package com.hubert.downloader.external.pl.kubikon.shared.utils;
import org.apache.tomcat.util.buf.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static Matcher matcher(String string, String regex) {
		return Pattern.compile(regex).matcher(string);
	}

	public static boolean matches(String string, String regex) {
		return matcher(string, regex).find();
	}

	public static String getSafeFileName(String filename) {
		return filename.replaceAll("[\\\\/:*?\"<>|]", "_");
	}

	public static File getAppDataDirectory() {
		File systemAppData = getSystemAppDataDirectory();
		File appDataDir = new File(systemAppData, System.getProperty("user.dir"));
		appDataDir.mkdirs();
		return appDataDir;
	}

	public static File getLocalTokenDataFile() {
		return new File(getAppDataDirectory(), "sessionnew.dat");
	}

	public static String formatBytes(long bytes, int decimals) {
		if (bytes == 0)
			return "0 B";
		int k = 1024;
		String[] sizes = new String[]{"B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
		int i = (int) Math.floor(Math.log(bytes) / Math.log(k));
		return String.format("%." + decimals + "f", bytes / Math.pow(k, i)) + " " + sizes[i];
	}

	public static String formatKiloBytes(long bytes, int decimals) {
		//chomikuj api podaje w kB
		return formatBytes(bytes * 1024, decimals);
	}

	public static String formatTime(long millis) {
		return String.format("%02d:%02d:%02d", millis / 60 / 60 / 1000, (millis / 60 / 1000) % 60, (millis / 1000) % 60);
	}

	public static String formatDateTime(long millis) {
		return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(millis));
	}

	public static String toString(int[] longArray) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < longArray.length; i++) {
			if (i > 0)
				sb.append(",");
			sb.append(longArray[i]);
		}
		return sb.append("]").toString();
	}

	public static File getSystemAppDataDirectory() {
		String appDataPath = System.getenv("APPDATA");
		if (appDataPath != null)
			return new File(appDataPath);
		File appData = new File(System.getProperty("user.home"), "AppData");
		appData.mkdirs();
		return appData;
	}


	//com.coreapplication.encryption.MD5Encryption
	public static String md5(String str) {
		byte[] digest = md5Bytes(str.getBytes(StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder(digest.length * 2);
		for (byte b : digest)
			sb.append(String.format("%02x", b & 0xFF));
		return sb.toString();
	}

	//com.coreapplication.encryption.MD5Encryption
	public static byte[] md5Bytes(byte[] bytes) {
		try {
			return MessageDigest.getInstance("MD5").digest(bytes);
		} catch (NoSuchAlgorithmException e2) {
			throw new RuntimeException("MD5 should be supported?", e2);
		}
	}

	public static String trimTrailingSlash(String path) {
		if (!path.isEmpty() && path.charAt(path.length() - 1) == '/') {
			return path.substring(0, path.length() - 1);
		} else {
			return path;
		}
	}

	public static String[] getPathSegments(String path) {
		path = trimTrailingSlash(path);
		while (path.startsWith("/"))
			path = path.substring(1);
		String[] pathSegments = path.split("/");
		if (pathSegments.length == 0)
			pathSegments = new String[]{""};
		return pathSegments;
	}

	public static String getParentFolderPath(String path) {
		String[] pathSegments = Utils.getPathSegments(path);
		if (pathSegments.length > 1)
			return "/" + StringUtils.join(Arrays.asList(pathSegments).subList(0, pathSegments.length - 1), '/');
		return "/";
	}

	public static String fixPathWithParentDots(String path) {
		int parentFolderCount = 0;
		while (path.endsWith("/..")) {
			path = path.substring(0, path.length() - 3);
			parentFolderCount++;
		}
		for (int i = 0; i < parentFolderCount; i++)
			path = getParentFolderPath(path);
		return path;
	}

	public static String getFileBaseName(String path) {
		path = trimTrailingSlash(path);
		String[] pathSegments = path.split("/");
		if (pathSegments.length > 1)
			return pathSegments[pathSegments.length - 1];
		else
			return path;
	}

	public static String reverse(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = str.length() - 1; i >= 0; i--)
			sb.append(str.charAt(i));
		return sb.toString();
	}

	public static byte[] reverse(byte[] bytes) {
		byte[] newBytes = new byte[bytes.length];
		for (int i = bytes.length - 1; i >= 0; i--)
			newBytes[i] = bytes[bytes.length - 1 - i];
		return newBytes;
	}

	public static byte[] hexStringToByteArray(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

	private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] longToByteArray(long value) {
		return new byte[]{
				(byte) (value >> 56),
				(byte) (value >> 48),
				(byte) (value >> 40),
				(byte) (value >> 32),
				(byte) (value >> 24),
				(byte) (value >> 16),
				(byte) (value >> 8),
				(byte) value
		};
	}

}