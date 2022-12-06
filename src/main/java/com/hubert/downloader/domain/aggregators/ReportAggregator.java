package com.hubert.downloader.domain.aggregators;

import com.hubert.downloader.domain.models.report.Report;
import com.hubert.downloader.domain.models.report.ResponseReportEntity;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportAggregator {

    private final UserService userService;

    public ResponseReportEntity responseEntity(Report report) {
        User user = userService.findById(report.getReportingUserId());

        return ResponseReportEntity.from(report, user);
    }

    public List<ResponseReportEntity> responseEntity(List<Report> reports) {
        return reports.stream().map(this::responseEntity).toList();
    }
}
