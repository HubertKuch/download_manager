package com.hubert.downloader.external.coreapplication.requestsgson.async;

import com.coreapplication.modelsgson.FilesFoldersResponse;
import com.coreapplication.requestsgson.BaseGsonRequest;
import com.hubert.downloader.external.coreapplication.modelsgson.FilesFoldersResponse;
import com.hubert.downloader.external.coreapplication.requestsgson.BaseGsonRequest;
import com.hubert.downloader.external.pl.kubikon.shared.utils.HttpRequest;
import pl.kubikon.chomikmanager.model.exception.PasswordRequiredException;
import pl.kubikon.shared.utils.HttpRequest;

public class TreeRequest extends BaseGsonRequest<FilesFoldersResponse> {

	private static final String ACCOUNT_ID = "AccountId";
	public static final int ERROR_FOLDER_NOT_EXISTS = 11;
	public static final int ERROR_FOLDER_WITH_PASSWORD_OPEN_FAILED = 10;
	public static final int ERROR_USER_WITH_PASSWORD_OPEN_FAILED = 12;
	private static final String FOLDER_ID = "Parent";
	private static final String PAGE = "page";
	private static final String PATH = "/folders";
	private String accountId;
	private final String folderId;
	private final String folderName;
	private final int page;

	public TreeRequest(String folderId, String folderName, String accountId, int page) {
		this(folderId, folderName, page);
		this.accountId = accountId;
	}

	public TreeRequest(String folderId, String folderName, int page) {
		super(PATH, FilesFoldersResponse.class);
		this.folderId = folderId;
		this.page = page;
		this.folderName = folderName;
	}

	@Override
	public void prepareHttpRequest(HttpRequest httpRequest) {
		if (accountId != null)
			httpRequest.addUrlParam(ACCOUNT_ID, accountId);
		httpRequest.addUrlParam(FOLDER_ID, folderId);
		httpRequest.addUrlParam(PAGE, page);
	}

	@Override
	public FilesFoldersResponse getResponse() throws Exception {
		FilesFoldersResponse filesFoldersResponse = super.getResponse();
		if (filesFoldersResponse.code != null && filesFoldersResponse.code == 0) {
			filesFoldersResponse.folderId = this.folderId;
			filesFoldersResponse.folderName = this.folderName;
			return filesFoldersResponse;
		} else if (filesFoldersResponse.code != null && (filesFoldersResponse.code == 12 || filesFoldersResponse.code == 2)) {
			throw new PasswordRequiredException(folderName, accountId, folderId);
		} else
			throw new Exception("FilesFoldersResponse: responseCode=" + getResponseCode() + " errorCode= " + filesFoldersResponse.code + " message= " + filesFoldersResponse.Message);
	}

	public String getFolderId() {
		return this.folderId;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public int getPage() {
		return this.page;
	}

	public String getAccountId() {
		return this.accountId;
	}

	/*@Override // com.coreapplication.requestsgson.BaseGsonRequest
	public void handleError(RequestListener<FilesFoldersResponse> requestListener, int i, ApiError apiError, int i2) {
		if (i == 401 && apiError != null && apiError.code == 12) {
			requestListener.onError(ERROR_FOLDER_WITH_PASSWORD_OPEN_FAILED);
		} else if (i == 401 && apiError != null && apiError.code == 2) {
			requestListener.onError(ERROR_USER_WITH_PASSWORD_OPEN_FAILED);
		} else if (i == 404) {
			requestListener.onError(ERROR_FOLDER_NOT_EXISTS);
		} else {
			super.handleError(requestListener, i, apiError, i2);
		}
	}*/

	public static class Builder {
		private String mAccountId;
		private String mFolderId;
		private String mFolderName;
		private int mPage = 1;
		private Type mType;

		public enum Type {
			OTHER_FILES,
			USER_FILES
		}

		public Builder setAccountId(String str) {
			this.mAccountId = str;
			return this;
		}

		public Builder setFolderId(String str) {
			this.mFolderId = str;
			return this;
		}

		public Builder setPage(int i) {
			this.mPage = i;
			return this;
		}

		public Builder setType(Type type) {
			this.mType = type;
			return this;
		}

		public String toString() {
			return "Type=" + this.mType + " folderId=" + this.mFolderId + " accountId=" + this.mAccountId + " page=" + this.mPage;
		}

		public TreeRequest create() {
			if (this.mType == Type.OTHER_FILES) {
				return new TreeRequest(this.mFolderId, this.mFolderName, this.mAccountId, this.mPage);
			}
			return new TreeRequest(this.mFolderId, this.mFolderName, this.mPage);
		}

		public void setFolderName(String str) {
			this.mFolderName = str;
		}

	}

}