package com.hubert.downloader.external.coreapplication.requestsgson.async;

import com.hubert.downloader.external.coreapplication.modelsgson.GetDownloadUrl;
import com.hubert.downloader.external.coreapplication.requestsgson.BaseGsonRequest;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.Constants;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;

public class GetUrlDownloadRequest extends BaseGsonRequest<GetDownloadUrl> {

	public static final int ERROR_ACCESS_DENIED = 602;
	public static final int ERROR_FILE_INVALID = 603;
	public static final int ERROR_FILE_NOT_FOUND = 601;
	public static final int ERROR_NO_ENOUGH_TRANSFER = 605;
	public static final int ERROR_UNKNOW = 604;
	private static final String PATH = "/files/download";
	private final long fileId;

	public GetUrlDownloadRequest(long fileId) {
		super(PATH, GetDownloadUrl.class);
		this.fileId = fileId;
	}

	@Override
	public void prepareHttpRequest(HttpRequest httpRequest) {
		httpRequest.addUrlParam("FileId", fileId);
	}

	@Override
	public GetDownloadUrl getResponse() throws Exception {
		GetDownloadUrl getDownloadUrlResponse = super.getResponse();
		if (getResponseCode() == 404)
			throw new Exception(Constants.ERROR_FILE_NOT_FOUND);
		if (getDownloadUrlResponse.code == 0)// ???? || getDownloadUrlResponse.code == 605 ????
			return getDownloadUrlResponse;
		else if (getDownloadUrlResponse.code == ERROR_NO_ENOUGH_TRANSFER)
			throw new Exception(Constants.ERROR_NO_ENOUGH_TRANSFER);
		else
			throw new Exception("GetUrlDownloadRequest: error code " + getDownloadUrlResponse.code);
	}

}
