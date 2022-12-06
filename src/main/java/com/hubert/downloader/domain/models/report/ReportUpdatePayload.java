package com.hubert.downloader.domain.models.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReportUpdatePayload(
        BugStatus bugStatus
) {}
