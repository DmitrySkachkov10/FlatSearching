package by.skachkovdmitry.audit.controller;

import by.skachkovdmitry.audit.core.dto.InputInfo;
import by.skachkovdmitry.audit.service.api.IAuditService;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<String> saveData(@RequestBody InputInfo inputInfo) {
        auditService.save(inputInfo);
        return new ResponseEntity<>("cool", HttpStatusCode.valueOf(200));

    }
}
