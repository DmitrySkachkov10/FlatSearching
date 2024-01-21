package by.skachkovdmitry.audit.service.api;

import by.skachkovdmitry.audit.core.dto.Audit;
import by.skachkovdmitry.audit.core.dto.PageOfAudit;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

public interface IAuditService {

    void save(Audit audit);

    Audit getAudit(UUID uuid);

    PageOfAudit getPageOfAudit(Pageable pageable);
}
