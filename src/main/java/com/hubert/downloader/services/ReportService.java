package com.hubert.downloader.services;

import com.hubert.downloader.domain.aggregators.ReportAggregator;
import com.hubert.downloader.domain.exceptions.InvalidRequestDataException;
import com.hubert.downloader.domain.models.report.Report;
import com.hubert.downloader.domain.models.report.ReportPayload;
import com.hubert.downloader.domain.models.report.ReportUpdatePayload;
import com.hubert.downloader.domain.models.report.ResponseReportEntity;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public ResponseReportEntity aggregate(Report report) {
        return reportAggregator.responseEntity(report);
    }

    public List<ResponseReportEntity> getAggregatedReports() {
        List<Report> reports = getReports();

        return reportAggregator.responseEntity(reports);
    }

    public Report update(String reportId, ReportUpdatePayload reportUpdatePayload) throws InvalidRequestDataException {
        Optional<Report> optionalReport = reportRepository.findById(reportId);

        if (optionalReport.isEmpty()) {
            throw new InvalidRequestDataException("Report id is not matched. Check reportId validity.");
        }

        Report report = optionalReport.get();

        report.update(reportUpdatePayload);

        return reportRepository.save(report);
    };
}
