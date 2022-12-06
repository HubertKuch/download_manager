package com.hubert.downloader.domain.models.report;

import com.hubert.downloader.domain.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    private String id;
    @Field
    private String reportingUserId;
    @Field
    private String content;
    @Field
    private Date addedAt;
    @Field
    private BugStatus bugStatus;
    @Field
    private BugRefersTo refersTo;

    public static Report from(ReportPayload reportPayload, User reporter) {
        return new Report(
                null,
                reporter.getId(),
                reportPayload.content(),
                new Date(System.currentTimeMillis()),
                BugStatus.NOT_STARTED,
                reportPayload.bugRefersTo()
        );
    }

    public Report update(ReportUpdatePayload reportUpdatePayload) {
        this.bugStatus = reportUpdatePayload.bugStatus();

        return this;
    }
}
