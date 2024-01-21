package by.skachkovdmitry.audit.repository.api;

import by.skachkovdmitry.audit.repository.entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IAuditRepo extends JpaRepository<AuditEntity, UUID> {

}
