package com.hubert.downloader.domain.models.file;

public interface IncomingDataRequestWithPassword extends RequiringPassword {
    String getUrl();
}
