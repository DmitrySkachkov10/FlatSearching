package by.skachkovdmitry.audit.controller.upload;

import by.skachkovdmitry.audit.core.dto.LogInfo;
import by.skachkovdmitry.audit.service.api.IAuditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit")
public class UploadController {

    private final IAuditService auditService;

    public UploadController(IAuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> saveData(@RequestBody LogInfo logInfo) {
        auditService.save(logInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
