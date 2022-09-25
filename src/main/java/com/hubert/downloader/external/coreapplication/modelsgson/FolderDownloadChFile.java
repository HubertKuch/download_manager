package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderDownloadChFile extends FilesFoldersResponse.ChFileOrFolder {

	@JsonProperty("FileId")
	public long fileId;

	@JsonProperty("FileName")
	public String fileName;

	@JsonProperty("FileType")
	public String fileType;

	@JsonProperty("IsCopied")
	public boolean isCopied;

	@JsonProperty("IsCopyable")
	public boolean isCopyable;

	@JsonProperty("IsFileFreeForUser")
	public boolean isFileFreeForUser;

	@JsonProperty("IsHidden")
	public boolean isHidden;

	@JsonProperty("MediaType")
	public String mediaType;

	@JsonProperty("Path")
	public String path;

	@JsonProperty("Size")
	public long size;

	@JsonProperty("SmallThumbnailImg")
	public String smallThumbnailImg;

	@JsonProperty("StreamingUrl")
	public String streamingUrl;

	@JsonProperty("ThumbnailImg")
	public String thumbnailImg;

	public String getFullFileName() {
		String fullFileName = fileName;
		if (fileType != null && !fileType.isEmpty())
			fullFileName += "." + fileType;
		return fullFileName;
	}

	@Override
	public String toString() {
		return fileName;
	}

	@Override
	public long getId() {
		return fileId;
	}

	@Override
	public String getName() {
		return getFullFileName();
	}

	@Override
	public String getDisplayName() {
		return getName().replaceAll("\\s+", " ");
	}

	@Override
	public String getFileName() {
		return getFullFileName();
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}