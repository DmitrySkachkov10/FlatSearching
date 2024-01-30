package by.skachkovdmitry.audit.repository.api;

import by.skachkovdmitry.audit.repository.entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IAuditRepo extends JpaRepository<AuditEntity, UUID> {

    @Query("SELECT a FROM AuditEntity a WHERE a.user.uuid = :userUuid AND a.createDate BETWEEN :startDate AND :endDate")
    List<AuditEntity> findAuditsForUserBetweenDates(
            @Param("userUuid") UUID userUuid,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}
