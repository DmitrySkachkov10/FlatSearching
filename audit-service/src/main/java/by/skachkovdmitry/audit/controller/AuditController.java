package by.skachkovdmitry.audit.controller;

import by.skachkovdmitry.audit.core.dto.Audit;
import by.skachkovdmitry.audit.core.dto.PageOfAudit;
import by.skachkovdmitry.audit.service.api.IAuditService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/audit")
public class AuditController {

    private final IAuditService auditService;

    public AuditController(IAuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<PageOfAudit> getPage(@RequestParam int page, @RequestParam int size) {
        PageOfAudit pageOfAudit = auditService.getPageOfAudit(PageRequest.of(page - 1, size));
        return new ResponseEntity<>(pageOfAudit, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Audit> getByUuid(@PathVariable UUID uuid) {
        return new ResponseEntity<>(auditService.getAudit(uuid), HttpStatusCode.valueOf(200));
    }
}
