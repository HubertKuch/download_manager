package com.hubert.downloader.domain.aggregators;

import com.hubert.downloader.domain.models.report.Report;
import com.hubert.downloader.domain.models.report.ResponseReportsEntity;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportAggregator {

    private final UserService userService;

    public ResponseReportsEntity responseEntity(Report report) {
        User user = userService.findById(report.getReportingUserId());

        return ResponseReportsEntity.from(report, user);
    }

    public List<ResponseReportsEntity> responseEntity(List<Report> reports) {
        return reports.stream().map(this::responseEntity).toList();
    }
}
