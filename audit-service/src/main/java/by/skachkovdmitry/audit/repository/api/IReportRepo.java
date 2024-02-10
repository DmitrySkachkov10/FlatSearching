package by.skachkovdmitry.audit.repository.api;

import by.skachkovdmitry.audit.repository.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IReportRepo extends JpaRepository<ReportEntity, UUID> {

}
