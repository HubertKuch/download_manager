package com.hubert.downloader.services;

import com.hubert.downloader.domain.aggregators.ReportAggregator;
import com.hubert.downloader.domain.models.report.Report;
import com.hubert.downloader.domain.models.report.ReportPayload;
import com.hubert.downloader.domain.models.report.ResponseReportsEntity;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportAggregator reportAggregator;

    public Report addReport(ReportPayload reportPayload, User user) {
        Report report = Report.from(reportPayload, user);

        return reportRepository.save(report);
    }

    public List<Report>  getReports() {
        return reportRepository.findAll();
    }

    public ResponseReportsEntity aggregate(Report report) {
        return reportAggregator.responseEntity(report);
    }

    public List<ResponseReportsEntity> getAggregatedReports() {
        List<Report> reports = getReports();

        return reportAggregator.responseEntity(reports);
    }
}
