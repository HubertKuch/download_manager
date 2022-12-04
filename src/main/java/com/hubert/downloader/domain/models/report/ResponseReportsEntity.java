package com.hubert.downloader.domain.models.report;

import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.models.user.UserPayload;

import java.util.Date;

public record ResponseReportsEntity(
        String id,
        UserPayload userPayload,
        String content,
        Date addedAt,
        BugStatus bugStatus,
        BugRefersTo bugRefersTo
) {
    public static ResponseReportsEntity from(Report report, User user) {
        return new ResponseReportsEntity(
                report.getId(),
                user.toPayload(),
                report.getContent(),
                report.getAddedAt(),
                report.getBugStatus(),
                report.getRefersTo()
        );
    }
}
