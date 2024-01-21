package by.skachkovdmitry.audit.controller;

import by.skachkovdmitry.audit.core.dto.Audit;
import by.skachkovdmitry.audit.service.api.IAuditService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/audit")
public class UploadController {

    private final IAuditService auditService;

    public UploadController(IAuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping("/upload")
    public void saveData(@RequestBody Audit audit) {
        auditService.save(audit);
    }
}
