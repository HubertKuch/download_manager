package com.hubert.downloader.external.coreapplication.utils;

public class HttpUtils {

	public static final String API_VERSION_V3 = "api/v3";
	public static final String CLIENT_VERSION = "3.5";
	public static final String HEADER_KEY_AUTHORIZATION = "Api-Key";
	public static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_TOKEN = "Token";
	public static final int MAX_FILE_NAME_LENGT = 200;
	public static final int MAX_FOLDER_NAME_LENGT = 300;
	public static final String SECRET_KEY = "wzrwYua$.DSe8suk!`'2";
	public static final String SERVICE_ADDRESS = "https://mobile.chomikuj.pl";

	public enum HttpStatus {
		HTTP_NO_NETWORK(-101, "R.string.toast_no_network"),
		HTTP_GENERAL_ERROR(400, "R.string.error_forbidden"),
		HTTP_SESSION_TIMEOUT(401, "R.string.error_session_timeout"),
		HTTP_FORBIDDEN(403, "R.string.error_forbidden"),
		HTTP_BAD_ACCESS(404, "R.string.error_no_access"),
		HTTP_SERVER_NOT_ALLOWED(405, "R.string.error_no_access"),
		HTTP_ACCOUNT_BLOCKED(423, "R.string.error_account_blocked"),
		HTTP_ERROR_UNKNOWN(500, "R.string.error_no_access"),
		HTTP_ERROR_UNAVAILABLE(503, "R.string.error_no_access"),
		HTTP_CLIENT_NOT_SUPPORTED(505, "R.string.toast_update_application");

		private final int mCode;
		private final String mMessageId;

		HttpStatus(int i, String i2) {
			this.mCode = i;
			this.mMessageId = i2;
		}

		public static HttpStatus fromCode(int i) {
			HttpStatus[] values;
			for (HttpStatus httpStatus : values()) {
				if (httpStatus.getCode() == i) {
					return httpStatus;
				}
			}
			return HTTP_SERVER_NOT_ALLOWED;
		}

		public int getCode() {
			return this.mCode;
		}

		public String getMessage() {
			return this.mMessageId;
		}
	}

	/* loaded from: classes.dex */
	public enum ResponseStatus {
		RESPONSE_OK(0, "R.string.button_ok"),
		RESPONSE_OLD_OK(200, "R.string.button_ok"),
		RESPONSE_REGISTER_ERROR(400, "R.string.error_register"),
		RESPONSE_WRONG_PASSWORD(401, "R.string.error_wrong_password"),
		RESPONSE_WRONG_TOKEN(402, "R.string.error_couldnt_extend_session"),
		RESPONSE_ACCOUNT_DOESNT_EXIST(404, "R.string.error_account_doesnt_exist"),
		RESPONSE_UNKNOWN(500, "R.string.error_unknown"),
		RESPONSE_CLIENT_NOT_SUPPORTED(505, "R.string.toast_update_application"),
		RESPONSE_FILE_NOT_FOUND(601, "R.string.error_file_not_found"),
		RESPONSE_ACCESS_DENIED(602, "R.string.error_access_denied"),
		RESPONSE_ACCOUNT_BLOCKED(603, "R.string.error_downloading_account_blocked"),
		RESPONSE_FILE_INVALID(604, "R.string.error_file_invalid"),
		RESPONSE_NOT_ENOUGH_TRANSFER(605, "R.string.error_not_enough_transfer"),
		RESPONSE_DOWNLOAD_ERROR(606, "R.string.error_download_error"),
		RESPONSE_DIR_INVALID(610, "R.string.error_dir_invalid"),
		RESPONSE_NO_NETWORK_ERROR(666, "R.string.toast_no_network"),
		RESPONSE_INVALID_PARAMETERS(2000, "R.string.error_invalid_parametres"),
		RESPONSE_SAME_NAME(2001, "R.string.error_same_name"),
		RESPONSE_EMPTY_NAME(2002, "R.string.error_empty_name"),
		RESPONSE_NAME_ALREADY_EXISTS(2003, "R.string.error_name_already_exists"),
		RESPONSE_INTERNAL_ERROR(2004, "R.string.error_internal"),
		RESPONSE_NAME_EXISTS_AT_DESTINATION(2005, "R.string.error_name_already_exists"),
		RESPONSE_FOLDER_NOT_EXISTS(2006, "R.string.error_folder_not_exists"),
		RESPONSE_INVALID_START_OR_END(2007, "R.string.error_invalid_start_or_end"),
		RESPONSE_INVALID_CHARS(2008, "R.string.error_invalid_chars"),
		RESPONSE_FORBIDDEN_WORDS(2009, "R.string.error_forbiden_words"),
		RESPONSE_NAME_TO_LONG(2010, "R.string.error_name_to_long"),
		RESPONSE_EMPTY_FILE_NAME(3002, "R.string.error_empty_name"),
		RESPONSE_FILE_NAME_TO_LONG(3003, "R.string.error_name_to_long"),
		RESPONSE_FILE_NAME_ENDS_DOT(3004, "R.string.error_invalid_start_or_end"),
		RESPONSE_FILE_NAME_INVALID(3005, "R.string.error_invalid_chars"),
		RESPONSE_FILE_CHANGE_NAME_UNAUTHORIZED(3401, "R.string.error_unauthorized"),
		RESPONSE_FILE_DOESNT_EXIST(3404, "R.string.error_file_delted_from_server"),
		RESPONSE_PARENT_FOLDER_DOESNT_EXIST(2404, "R.string.error_parent_folder_doesnt_exist"),
		RESPONSE_INVALID_CHARACTERS(4001, "R.string.error_register_invalid_character"),
		RESPONSE_ACCOUNT_ALREADY_EXISTS(4002, "R.string.error_account_already_exists"),
		RESPONSE_EMPTY_REGISTER_NAME(4003, "R.string.toast_invalid_account_name"),
		RESPONSE_FORBIDDEN_NAME(4004, "R.string.error_register_forbidden_email"),
		RESPONSE_SYSTEM_NAME(4005, "R.string.error_register_forbidden_name"),
		RESPONSE_RESERVED_NAME(4006, "R.string.error_register_forbidden_name"),
		RESPONSE_REGISTER_NAME_TOO_SHORT(4007, "R.string.error_register_name_to_short"),
		RESPONSE_REGISTER_NAME_TOO_LONG(4008, "R.string.error_register_name_to_long"),
		RESPONSE_PASWORD_TO_EASY(4021, "R.string.error_invalid_password"),
		RESPONSE_PASSWORD_NOT_STRONG_ENOUGH(4022, "R.string.error_invalid_password"),
		RESPONSE_PASSWORD_FORBIDDEN(4023, "R.string.error_invalid_password"),
		RESPONSE_INVALID_EMAIL(4041, "R.string.toast_invalid_email"),
		STATUS_ACCOUNT_NOT_FOUND(5001, "R.string.error_account_doesnt_exist"),
		STATUS_EMAIL_NO_MATCH(5002, "R.string.error_email_not_match"),
		STATUS_ERROR(5003, "R.string.error_internal"),
		SETPASSWORD_PASSWORD_NOT_EQUAL(6001, "R.string.error_password_not_match"),
		SETPASSWORD_WRONG_TICKET(6002, "R.string.error_internal"),
		SETPASSWORD_INVALID_PASSWORD(6003, "R.string.error_invalid_password");

		private final int mCode;
		private final String mMessageId;

		ResponseStatus(int i, String i2) {
			this.mCode = i;
			this.mMessageId = i2;
		}

		public static ResponseStatus fromCode(int i) {
			ResponseStatus[] values;
			for (ResponseStatus responseStatus : values()) {
				if (responseStatus.getCode() == i) {
					return responseStatus;
				}
			}
			return RESPONSE_UNKNOWN;
		}

		public int getCode() {
			return this.mCode;
		}

		public String getMessage() {
			return this.mMessageId;
		}
	}

}
