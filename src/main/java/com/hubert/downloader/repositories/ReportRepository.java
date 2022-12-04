package com.hubert.downloader.repositories;

import com.hubert.downloader.domain.models.report.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {}
