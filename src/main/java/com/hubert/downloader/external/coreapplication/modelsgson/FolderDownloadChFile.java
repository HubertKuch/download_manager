package com.hubert.downloader.external.coreapplication.modelsgson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.io.FilenameUtils;
import org.jdom2.Element;
import pl.kubikon.chomikmanager.api.AndroidApi;
import pl.kubikon.shared.model.BaseDownloadFile;

import static pl.kubikon.chomikmanager.api.ChomikBoxApi.chomikujNamespace1;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderDownloadChFile extends FilesFoldersResponse.ChFileOrFolder implements BaseDownloadFile {

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

	public FolderDownloadChFile() {
	}

	public FolderDownloadChFile(Element element) {
		fileId = Long.parseLong(element.getChild("id", chomikujNamespace1).getText());
		fileName = element.getChild("name", chomikujNamespace1).getText();
		size = Long.parseLong(element.getChild("size", chomikujNamespace1).getText()) / 1000L;
	}

	public FolderDownloadChFile(long fileId, String fileNameWithExt) {
		this.fileId = fileId;
		this.fileName = FilenameUtils.getBaseName(fileNameWithExt);
		this.fileType = FilenameUtils.getExtension(fileNameWithExt);
	}

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

	public String toString2() {
		return "FolderDownloadFile{" +
				"fileId=" + fileId +
				", fileName='" + fileName + '\'' +
				", fileType='" + fileType + '\'' +
				", isCopied=" + isCopied +
				", isCopyable=" + isCopyable +
				", isFileFreeForUser=" + isFileFreeForUser +
				", isHidden=" + isHidden +
				", mediaType='" + mediaType + '\'' +
				", path='" + path + '\'' +
				", size=" + size +
				", smallThumbnailImg='" + smallThumbnailImg + '\'' +
				", streamingUrl='" + streamingUrl + '\'' +
				", thumbnailImg='" + thumbnailImg + '\'' +
				'}';
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
	public DownloadSource getDownloadSource() throws Exception {
		GetDownloadUrl getDownloadUrl = AndroidApi.getDownloadUrl(fileId);
		return new DirectDownloadSource(getDownloadUrl.fileUrl);
	}

	@Override
	public long getSize() {
		return size * 1000;
	}

	@Override
	public boolean isFolder() {
		return false;
	}

}