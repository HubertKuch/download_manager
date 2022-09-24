package com.hubert.downloader.external.coreapplication.models;

import com.coreapplication.enums.MediaType;

import java.util.Date;

/* loaded from: classes.dex */
public class FileListItem extends ListItem {
	public static final String DOWNLOAD_TO_FOLDER = "to_folder";
	public static final String EXPIRATION_DATE = "expiration_date";
	public static final String FILE_EXTENSION = "file_extension";
	public static final String FILE_ID = "file_id";
	public static final String FILE_NAME = "file_name";
	public static final String FILE_URL = "file_url";
	public static final String FOLDER_NAME = "folder_name";
	public static final String ICON_ID = "icon_id";
	public static final String LOCAL_FILE_ID = "local_file_id";
	public static final String MEDIA_TYPE = "media_type";
	public static final String SIZE = "size";
	public boolean downloadToFolder;
	public Date expirationDate;
	public String fileExtension;
	public long fileId;
	public String fileName;
	public String fileSize;
	public long fileSizeInBytes;
	public String fileUrl;
	public String folderName;
	public int iconId;
	public boolean isChecked;
	public boolean isCopyable;
	public boolean isFromOtherUser;
	public int localFileId;
	public MediaType mediaType;

	/* JADX INFO: Access modifiers changed from: protected */
	public FileListItem() {
		this.downloadToFolder = false;
	}

	/* JADX INFO: Access modifiers changed from: protected */
	public FileListItem(String str) {
		this.downloadToFolder = false;
		this.itemType = 0;
		this.fileName = str;
	}

	public FileListItem(int i) {
		super(i);
		this.downloadToFolder = false;
	}

	/* JADX INFO: Access modifiers changed from: protected */
	public FileListItem(int i, String str) {
		this.downloadToFolder = false;
		this.itemType = i;
		this.folderName = str;
	}

	/* JADX INFO: Access modifiers changed from: protected */
	public FileListItem(String str, String str2) {
		this.downloadToFolder = false;
		this.itemType = 0;
		this.fileName = str;
		this.thumbUrl = str2;
	}

	/* JADX INFO: Access modifiers changed from: protected */
	public FileListItem(boolean z, String str) {
		this.downloadToFolder = false;
		this.itemType = 0;
		this.fileName = str;
	}

	/* JADX INFO: Access modifiers changed from: protected */
	public FileListItem(String str, boolean z) {
		this.downloadToFolder = false;
		this.itemType = 3;
		this.folderName = str;
	}

	public MediaType getMediaType() {
		return this.mediaType;
	}

	public boolean isPreviewSupported() {
		return this.mediaType == MediaType.VIDEO || this.mediaType == MediaType.IMAGE || this.mediaType == MediaType.MUSIC || this.mediaType == MediaType.ANIMATED_GIF;
	}

	public boolean isCopyable() {
		return this.isCopyable;
	}

	public void setIsCopyable(boolean z) {
		this.isCopyable = z;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public String getFileName() {
		return this.fileName;
	}

	@Override // com.coreapplication.models.ListItem
	public String getThumbUrl() {
		if (this.thumbUrl == null || this.thumbUrl.equals("")) {
			return null;
		}
		return this.thumbUrl;
	}

	public boolean isFile() {
		return getFileId() > 0;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public boolean getIsFromOtherUser() {
		return this.isFromOtherUser;
	}

	public void setIsFromOtherUser(boolean z) {
		this.isFromOtherUser = z;
	}

	public void setFileName(String str) {
		this.fileName = str;
	}

	public void setFolderName(String str) {
		this.folderName = str;
	}

	public void setNewFileUrl(String str) {
		this.fileUrl = str;
	}

	public void setNewMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public boolean isDownloadToFolder() {
		return this.downloadToFolder;
	}

	public void setDownloadToFolder(boolean z) {
		this.downloadToFolder = z;
	}

	/* JADX INFO: Access modifiers changed from: protected */
	/* renamed from: a */
	public void m2311a(String str) {
		MediaType[] values = MediaType.values();
		int length = values.length;
		int i = 0;
		while (true) {
			if (i >= length) {
				break;
			}
			MediaType mediaType = values[i];
			if (mediaType.getName().equals(str)) {
				this.mediaType = mediaType;
				break;
			}
			i++;
		}
		if (this.mediaType == null) {
			this.mediaType = MediaType.UNDEFINED;
		}
	}

	public int getIconId() {
		return this.iconId;
	}

	public void setIconId(int i) {
		this.iconId = i;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Date date) {
		this.expirationDate = date;
	}

	public String getFileExtension() {
		return this.fileExtension;
	}

	public void setFileExtension(String str) {
		this.fileExtension = str;
	}

	public long getFileId() {
		return this.fileId;
	}

	public void setFileId(long j) {
		this.fileId = j;
	}

	public boolean isChecked() {
		return this.isChecked;
	}

	public void setChecked(boolean z) {
		this.isChecked = z;
	}

	public long getFileSizeInBytes() {
		return this.fileSizeInBytes;
	}

	public void setFileSizeInBytes(long j) {
		this.fileSizeInBytes = j;
	}

	public String getFileSize() {
		/*if (this.fileSize == null) {
			this.fileSize = MemoryUtils.convertBytesToStringRepresentation(this.fileSizeInBytes);
		}*/
		return this.fileSize;
	}

	public void setFileSize(String str) {
		this.fileSize = str;
	}

	public String getFileFullName() {
		return getFileName() + "." + getFileExtension();
	}

}
