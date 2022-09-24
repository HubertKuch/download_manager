package com.hubert.downloader.external.coreapplication.requestsgson.async;

import com.hubert.downloader.external.coreapplication.modelsgson.FolderDownload;
import com.hubert.downloader.external.coreapplication.requestsgson.BaseGsonRequest;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;

public class GetFolderDetailsRequest extends BaseGsonRequest<FolderDownload> {

	public static final int ERROR_FOLDER_WITH_PASSWORD = 0;
	private static final String PATH = "/folders/download/items";

	private final String accountId, folderId, folderName;

	public GetFolderDetailsRequest(String accountId, String folderId, String folderName) {
		super(PATH, FolderDownload.class);
		this.accountId = accountId;
		this.folderId = folderId;
		this.folderName = folderName;
	}

	@Override
	public void prepareHttpRequest(HttpRequest httpRequest) {
		if (accountId != null)
			httpRequest.addUrlParam("accountId", accountId);
		httpRequest.addUrlParam("folderId", folderId);
	}

	@Override
	public FolderDownload getResponse() throws Exception, PasswordRequiredException {
		FolderDownload folderDownloadResponse = super.getResponse();
		if (folderDownloadResponse.code == null) {
			throw new Exception("GetFolderDetailsRequest error " + folderDownloadResponse.message);
		}
		if (folderDownloadResponse.code == 0) {
			return folderDownloadResponse;
		} else if (folderDownloadResponse.code == 12) {
			throw new PasswordRequiredException(folderName, accountId, folderId);
		} else {
			throw new Exception("GetFolderDetailsRequest error " + folderDownloadResponse.code);
		}
	}

}