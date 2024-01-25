package by.skachkovdmitry.audit.service.api;

import by.skachkovdmitry.audit.core.dto.PageOfReport;
import by.skachkovdmitry.audit.core.dto.Report;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReportService {
    void addReport(Report report);

    PageOfReport getReports(Pageable pageable);

    boolean exist(UUID uuid);

    InputStreamResource download(UUID uuid);
}
