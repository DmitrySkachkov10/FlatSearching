package by.skachkovdmitry.audit.service.api;

import by.skachkovdmitry.audit.core.dto.PageOfReport;
import by.skachkovdmitry.audit.core.dto.UserActionAuditParam;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IReportService {
    void addReport(UserActionAuditParam actionAuditParam);

    PageOfReport getReports(Pageable pageable);

    boolean exist(UUID id);

    InputStreamResource download(UUID uuid);
}
