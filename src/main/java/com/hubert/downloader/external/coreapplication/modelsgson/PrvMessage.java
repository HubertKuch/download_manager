package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrvMessage {

	@JsonProperty("Body")
	public String body;

	@JsonProperty("Date")
	public Date date;

	@JsonProperty("From")
	public PrvMessageFrom from;

	@JsonProperty("Id")
	/* renamed from: id */
	public Integer f505id;

	@JsonProperty("Intro")
	public String intro;

	@JsonProperty("Properties")
	public PrvMessageProperties properties;

	@JsonProperty("To")
	public PrvMessageTo sentTo;

	@JsonProperty("Title")
	public String title;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class PrvMessageTo {

		@JsonProperty("AvatarUrl")
		public String avatarUrl;

		@JsonProperty("Id")
		/* renamed from: id */
		public String f508id;

		@JsonProperty("Name")
		public String name;

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class PrvMessageProperties {

		@JsonProperty("IsAdministriveMessage")
		public boolean isAdministriveMessage;

		@JsonProperty("IsFraud")
		public boolean isFraud;

		@JsonProperty("IsFraudReported")
		public boolean isFraudReported;

		@JsonProperty("IsFraudSuspect")
		public boolean isFraudSuspect;

		@JsonProperty("IsRead")
		public boolean isRead;

		@JsonProperty("IsSpam")
		public boolean isSpam;

	}

	public String getDateFormatted() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.date);
	}

}