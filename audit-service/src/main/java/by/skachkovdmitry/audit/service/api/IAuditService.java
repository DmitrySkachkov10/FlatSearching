package by.skachkovdmitry.audit.service.api;

import by.skachkovdmitry.audit.core.dto.Audit;
import by.skachkovdmitry.audit.core.dto.LogInfo;
import by.skachkovdmitry.audit.core.dto.PageOfAudit;
import by.skachkovdmitry.audit.repository.entity.AuditEntity;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IAuditService {

    void save(LogInfo logInfo);

    Audit getAudit(UUID uuid);

    PageOfAudit getPageOfAudit(Pageable pageable);

    List<AuditEntity> getAuditsForUserBetweenDates(List<UUID> usersUuid, LocalDateTime startDate, LocalDateTime endDate);
}
