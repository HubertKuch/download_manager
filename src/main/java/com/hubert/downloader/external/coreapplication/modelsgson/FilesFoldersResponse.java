package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.downloader.external.coreapplication.models.AccountsListItem;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilesFoldersResponse {

	@JsonProperty("Code")
	public Integer code;

	@JsonProperty("Files")
	public ArrayList<ChFile> files;

	public String folderId;

	public String folderName;

	@JsonProperty("Folders")
	public ArrayList<ChFolder> folders;

	@JsonProperty("IsNextPageAvailable")
	public boolean isNextPageAvailable;

	@JsonProperty("Owner")
	public Owner owner;

	@JsonProperty("ParentId")
	public String parentId;

	@JsonProperty("ParentName")
	public String parentName;

	@JsonProperty("Message")
	public String Message;

	@Override
	public String toString() {
		return "FilesFoldersResponse{" +
				"code=" + code +
				", files=" + files +
				", folderId='" + folderId + '\'' +
				", folderName='" + folderName + '\'' +
				", folders=" + folders +
				", isNextPageAvailable=" + isNextPageAvailable +
				", owner=" + owner +
				", parentId='" + parentId + '\'' +
				", parentName='" + parentName + '\'' +
				'}';
	}

	public ChFolder getParentFolder() {
		if (parentId != null && parentName != null) {
			ChFolder parentFolder = new ChFolder();
			parentFolder.accountId = owner.f502id;
			parentFolder.name = "";
			parentFolder.f500id = parentId;
			parentFolder.isParentFolderPointer = true;
			return parentFolder;
		}
		return null;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Owner {

		@JsonProperty("AvatarUrl")
		public String avatarUrl;

		@JsonProperty("Id")
		/* renamed from: id */
		public String f502id;

		@JsonProperty("IsFriend")
		public boolean isFriend;

		@JsonProperty("Name")
		public String name;

	}

	public static abstract class ChFileOrFolder {

		public abstract long getId();

		public abstract String getName();

		public abstract String getDisplayName();

		public abstract String getFileName();

		public abstract boolean isFolder();

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ChFolder extends ChFileOrFolder {

		@JsonProperty("AccountId")
		public String accountId;

		@JsonProperty("HasPassword")
		public boolean hasPassword;

		@JsonProperty("Id")
		/* renamed from: id */
		public String f500id;

		@JsonProperty("IsAdult")
		public boolean isAdult;

		@JsonProperty("IsHidden")
		public boolean isHidden;

		@JsonProperty("IsPublicUploadEnabled")
		public boolean isUploadEnabled;

		@JsonProperty("Name")
		public String name;

		@JsonProperty("ParentId")
		public String parentId;

		public boolean isParentFolderPointer;

		public ChFolder() {
		}

		public ChFolder(String accountId, String id) {
			this.accountId = accountId;
			this.f500id = id;
		}

		public ChFolder(String accountId, String id, String name) {
			this.accountId = accountId;
			this.f500id = id;
			this.name = name;
		}

		@Override
		public String toString() {
			return getDisplayName();
		}

		@Override
		public long getId() {
			return Long.parseLong(f500id);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getDisplayName() {
			return isParentFolderPointer ? ".." : getName().replaceAll("\\s+", " ");
		}

		@Override
		public String getFileName() {
			return null;
		}

		@Override
		public boolean isFolder() {
			return true;
		}

		public static ChFolder accountRootFolder(AccountsListItem account) {
			return accountRootFolder(account.getAccountId(), account.getAccountName());
		}

		public static ChFolder accountRootFolder(String accountId, String accountName) {
			ChFolder folderResponse = new ChFolder();
			folderResponse.accountId = accountId;
			folderResponse.f500id = "0";
			folderResponse.parentId = "0";
			folderResponse.name = accountName;
			return folderResponse;
		}

		public static ChFolder addParentFolderPointerText(ChFolder folderResponse) {
			ChFolder newFolderResponse = new ChFolder();
			newFolderResponse.accountId = folderResponse.accountId;
			newFolderResponse.hasPassword = folderResponse.hasPassword;
			newFolderResponse.f500id = folderResponse.f500id;
			newFolderResponse.isAdult = folderResponse.isAdult;
			newFolderResponse.isHidden = folderResponse.isHidden;
			newFolderResponse.isUploadEnabled = folderResponse.isUploadEnabled;
			newFolderResponse.parentId = folderResponse.parentId;
			newFolderResponse.name = folderResponse.name;
			newFolderResponse.isParentFolderPointer = true;
			return newFolderResponse;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ChFile extends ChFileOrFolder {

		@JsonProperty("FileType")
		public String fileType;

		@JsonProperty("FileId")
		/* renamed from: id */
		public long f498id;

		@JsonProperty("IsCopied")
		public boolean isCopied;

		@JsonProperty("IsCopyable")
		public boolean isCopyable;

		@JsonProperty("IsFileFreeForUser")
		public boolean isFree;

		@JsonProperty("IsHidden")
		public boolean isHidden;

		@JsonProperty("MediaType")
		public String mediaType;

		@JsonProperty("FileName")
		public String name;

		@JsonProperty("Path")
		public String path;

		@JsonProperty("Size")
		public long size;

		@JsonProperty("SmallThumbnailImg")
		public String smallThumbUrl;

		@JsonProperty("SourceAccount")
		public Owner sourceAccount;

		@JsonProperty("StreamingUrl")
		public String streamUrl;

		@JsonProperty("ThumbnailImg")
		public String thumbUrl;

		@Override
		public String toString() {
			return "FileResponse{" +
					"fileType='" + fileType + '\'' +
					", f498id=" + f498id +
					", isCopied=" + isCopied +
					", isCopyable=" + isCopyable +
					", isFree=" + isFree +
					", isHidden=" + isHidden +
					", mediaType='" + mediaType + '\'' +
					", name='" + name + '\'' +
					", path='" + path + '\'' +
					", size=" + size +
					", smallThumbUrl='" + smallThumbUrl + '\'' +
					", sourceAccount=" + sourceAccount +
					", streamUrl='" + streamUrl + '\'' +
					", thumbUrl='" + thumbUrl + '\'' +
					'}';
		}

		@Override
		public long getId() {
			return f498id;
		}

		@Override
		public String getName() {
			String fileName = this.name;
			if (fileType != null && !fileType.isEmpty())
				fileName += "." + fileType;
			return fileName;
		}

		@Override
		public String getDisplayName() {
			return getName().replaceAll("\\s+", " ");
		}

		@Override
		public String getFileName() {
			return null;
		}

		@Override
		public boolean isFolder() {
			return false;
		}

	}

}