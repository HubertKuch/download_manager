package com.hubert.downloader.domain.models.report;

public record ReportPayload(
        String content,
        BugRefersTo bugRefersTo
) {}
