package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.exceptions.InvalidRequestDataException;
import com.hubert.downloader.domain.models.report.Report;
import com.hubert.downloader.domain.models.report.ReportPayload;
import com.hubert.downloader.domain.models.report.ReportUpdatePayload;
import com.hubert.downloader.domain.models.report.ResponseReportEntity;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.services.ReportService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getReports(@RequestHeader(name = "Authorization") String token) {
        User user = userService.findByToken(new Token(token));

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(reportService.getAggregatedReports());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseReportEntity newReport(
            @RequestHeader(name = "Authorization") String token, @RequestBody ReportPayload reportPayload) {
        User user = userService.findByToken(new Token(token));
        Report report = reportService.addReport(reportPayload, user);

        return reportService.aggregate(report);
    }

    @PatchMapping("/{reportId}/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseReportEntity changeReportStatus(
            @RequestBody ReportUpdatePayload reportUpdatePayload,
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "reportId") String reportId) throws InvalidRequestDataException {
        Report report = reportService.update(reportId, reportUpdatePayload);

        return reportService.aggregate(report);
    }

    @GetMapping("/own/")
    public ResponseEntity<?> getUserReports(@RequestHeader(name = "Authorization") String token) {
        User user = userService.findByToken(new Token(token));

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(reportService.getUserReports(user));
    }
}
