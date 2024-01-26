package by.skachkovdmitry.audit.controller;

import by.skachkovdmitry.audit.core.dto.PageOfReport;
import by.skachkovdmitry.audit.core.dto.UserActionAuditParam;
import by.skachkovdmitry.audit.core.enums.ReportType;
import by.skachkovdmitry.audit.service.api.IReportService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final IReportService reportService;

    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/{type}")
    public ResponseEntity<String> addReport(@PathVariable ReportType type,
                                            @RequestBody UserActionAuditParam userActionAuditParam) {
        reportService.addReport(userActionAuditParam);
        return new ResponseEntity<>("Отчёт запущен", HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<PageOfReport> getReports(int page, int size) {
        PageOfReport pageOfReport = reportService.getReports(PageRequest.of(page, size));
        return new ResponseEntity<>(pageOfReport, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{uuid}/export", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headExportReport(@PathVariable UUID uuid) {
        if (reportService.exist(uuid)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

//    @GetMapping("/{uuid}/export")
//    public ResponseEntity<byte[]> getExportedReport(@PathVariable UUID uuid) {
//
//    }
}
