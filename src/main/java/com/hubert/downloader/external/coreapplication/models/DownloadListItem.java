package com.hubert.downloader.external.coreapplication.models;

import com.hubert.downloader.external.coreapplication.modelsgson.FilesFoldersResponse;
import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownloadChFile;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class DownloadListItem extends FileListItem {
	private String mFolderId;
	private String mFolderParentId;
	private boolean mFreeTransfer;
	private boolean mHasPassword;
	private boolean mIsForAdult;
	private String mOwnerAvatarUrl;
	private String mOwnerId;
	private String mOwnerName;
	private String mSourceAccountId;
	private String mSourceAccountName;
	private String mSourceAvatarUrl;
	private String mStreamingUrl;

	/* loaded from: classes.dex */
	public enum TREE_MODE {
		ALL,
		FOLDERS,
		FILES
	}

	private long convertFileSize(long j) {
		return j * (1 << 10);
	}

	public DownloadListItem() {
	}

	public DownloadListItem(int i) {
		super(i);
	}

	public DownloadListItem(int i, int i2, String str) {
		super(i, str);
		setIconId(i2);
	}

	public DownloadListItem(int i, long j, String str, String str2, long j2) {
		super(str);
		setFileSizeInBytes(convertFileSize(j2));
		this.mHasPassword = false;
		setFileId(j);
		setIconId(i);
		setFileExtension(str2);
	}

	public DownloadListItem(int i, long j, String str, String str2, String str3, long j2, String str4, String str5, Boolean bool) {
		super(str, str4);
		this.mStreamingUrl = str5;
		setFileSizeInBytes(convertFileSize(j2));
		this.mHasPassword = false;
		setFileId(j);
		setIconId(i);
		m2311a(str2);
		setFileExtension(str3);
	}

	public DownloadListItem(int i, long j, String str, String str2, long j2, boolean z, boolean z2) {
		super(z, str);
		setFileSizeInBytes(convertFileSize(j2));
		this.mFreeTransfer = z;
		this.mHasPassword = false;
		setFileId(j);
		setIconId(i);
		setIsFromOtherUser(z2);
		setFileExtension(str2);
	}

	public DownloadListItem(int i, String str, String str2, String str3, boolean z) {
		super(str3, z);
		this.mFolderId = str;
		this.mFolderParentId = str2;
		this.mHasPassword = z;
		setFileSizeInBytes(0L);
		setIconId(i);
	}

	public String getStreamUrl() {
		return this.mStreamingUrl;
	}

	public boolean isForAdult() {
		return this.mIsForAdult;
	}

	public void setMediaType(String str) {
		m2311a(str);
	}

	public void setStreamingUrl(String str) {
		this.mStreamingUrl = str;
	}

	public void setForAdult(boolean z) {
		this.mIsForAdult = z;
	}

	public boolean isFreeTransfer() {
		return this.mFreeTransfer;
	}

	public boolean isHasPassword() {
		return this.mHasPassword;
	}

	public String getFolderId() {
		return this.mFolderId;
	}

	public void setFolderId(String str) {
		this.mFolderId = str;
	}

	public String getFolderParentId() {
		return this.mFolderParentId;
	}

	public void setFolderParentId(String str) {
		this.mFolderParentId = str;
	}

	public void setHasPassword(boolean z, int i) {
		this.mHasPassword = z;
		setIconId(i);
	}

	public String getSourceAvatarUrl() {
		return this.mSourceAvatarUrl;
	}

	public void setSourceAvatarUrl(String str) {
		this.mSourceAvatarUrl = str;
	}

	public String getSourceAccountName() {
		return this.mSourceAccountName;
	}

	public void setSourceAccountName(String str) {
		this.mSourceAccountName = str;
	}

	public String getOwnerAvatarUrl() {
		return this.mOwnerAvatarUrl;
	}

	public void setOwnerAvatarUrl(String str) {
		this.mOwnerAvatarUrl = str;
	}

	public String getOwnerName() {
		return this.mOwnerName;
	}

	public void setOwnerName(String str) {
		this.mOwnerName = str;
	}

	public String getSourceAccountId() {
		return this.mSourceAccountId;
	}

	public void setSourceAccountId(String str) {
		this.mSourceAccountId = str;
	}

	public String getOwnerId() {
		return this.mOwnerId;
	}

	public void setOwnerId(String str) {
		this.mOwnerId = str;
	}

	public void setParentId(String str) {
		this.mFolderParentId = str;
	}

	public void setFreeTransfer(boolean z) {
		this.mFreeTransfer = z;
	}
	public static DownloadListItem fromFolderDownloadFile(FolderDownloadChFile folderDownloadFile) {
		if (folderDownloadFile != null) {
			DownloadListItem downloadListItem = new DownloadListItem();
			downloadListItem.setFileId(folderDownloadFile.fileId);
			downloadListItem.setFileName(folderDownloadFile.fileName);
			String cleanFileExtension = "?";//StringHelper.getCleanFileExtension(folderDownloadFile.fileType);
			downloadListItem.setMediaType(folderDownloadFile.mediaType);
			downloadListItem.setFileExtension(cleanFileExtension);
			downloadListItem.setIsFromOtherUser(folderDownloadFile.isCopied);
			downloadListItem.setIsCopyable(folderDownloadFile.isCopyable);
			downloadListItem.setFreeTransfer(folderDownloadFile.isFileFreeForUser);
			downloadListItem.setFileSize(folderDownloadFile.size + "");
			downloadListItem.setFileSizeInBytes(folderDownloadFile.size * 1000);
			return downloadListItem;
		}
		throw new IllegalArgumentException("Null DownloadListItem object");
	}

	public String toString() {
		if (this.fileName != null) {
			return "File:" + this.fileId + "/" + this.fileName;
		}
		return "Folder:" + this.mFolderId + "/" + this.folderName;
	}

	public static ArrayList<DownloadListItem> parseTreeRequest(FilesFoldersResponse filesFoldersResponse) {
		return parseTreeRequest(filesFoldersResponse, TREE_MODE.ALL);
	}

	public static ArrayList<DownloadListItem> parseTreeRequest(FilesFoldersResponse filesFoldersResponse, TREE_MODE tree_mode) {
		String str;
		String str2;
		FilesFoldersResponse.Owner owner = filesFoldersResponse.owner;
		String str3 = null;
		if (owner != null) {
			str3 = owner.f502id;
			str2 = owner.name;
			str = owner.avatarUrl;
		} else {
			str2 = null;
			str = null;
		}
		ArrayList<DownloadListItem> arrayList = new ArrayList<>();
		ArrayList<FilesFoldersResponse.ChFolder> arrayList2 = filesFoldersResponse.folders;
		ArrayList<FilesFoldersResponse.ChFile> arrayList3 = filesFoldersResponse.files;
		if (tree_mode == TREE_MODE.ALL || tree_mode == TREE_MODE.FOLDERS) {
			Iterator<FilesFoldersResponse.ChFolder> it = arrayList2.iterator();
		}
		if (tree_mode == TREE_MODE.ALL || tree_mode == TREE_MODE.FILES) {
			for (FilesFoldersResponse.ChFile next2 : arrayList3) {
				DownloadListItem downloadListItem2 = new DownloadListItem(2, next2.f498id, next2.name, next2.fileType, next2.size, next2.isFree, next2.isCopied);
				downloadListItem2.setFolderParentId(filesFoldersResponse.folderId);
				downloadListItem2.setIsCopyable(next2.isCopyable);
				downloadListItem2.setMediaType(next2.mediaType);
				if (next2.sourceAccount != null) {
					downloadListItem2.setSourceAccountId(next2.sourceAccount.f502id);
					downloadListItem2.setSourceAccountName(next2.sourceAccount.name);
					downloadListItem2.setSourceAvatarUrl(next2.sourceAccount.avatarUrl);
				}
				if (owner != null) {
					downloadListItem2.setOwnerId(str3);
					downloadListItem2.setOwnerName(str2);
					str = str;
					downloadListItem2.setOwnerAvatarUrl(str);
				}
				arrayList.add(downloadListItem2);
			}
		}
		return arrayList;
	}
}
