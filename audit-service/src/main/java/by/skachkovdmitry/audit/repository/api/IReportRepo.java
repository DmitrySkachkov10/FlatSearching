package by.skachkovdmitry.audit.repository.api;

import by.skachkovdmitry.audit.repository.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IReportRepo extends JpaRepository<ReportEntity, UUID> {

}
