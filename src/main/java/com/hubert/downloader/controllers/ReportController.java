package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.models.report.Report;
import com.hubert.downloader.domain.models.report.ReportPayload;
import com.hubert.downloader.domain.models.report.ResponseReportsEntity;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.services.ReportService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports/")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    @GetMapping
    public List<ResponseReportsEntity> getReports() {
        return reportService.getAggregatedReports();
    }

    @PostMapping
    public ResponseReportsEntity newReport(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ReportPayload reportPayload
    ) {
        User user = userService.findByToken(new Token(token.replace("Bearer ", "")));
        Report report = reportService.addReport(reportPayload, user);

        return reportService.aggregate(report);
    }
}
