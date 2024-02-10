package by.skachkovdmitry.audit.service;

import by.dmitryskachkov.entity.ValidationError;
import by.skachkovdmitry.audit.core.enums.EssenceType;
import by.skachkovdmitry.audit.core.mapper.DtoUserToEntityMapper;
import by.skachkovdmitry.audit.core.dto.Audit;
import by.skachkovdmitry.audit.core.dto.LogInfo;
import by.skachkovdmitry.audit.core.dto.PageOfAudit;
import by.skachkovdmitry.audit.repository.api.IAuditRepo;
import by.skachkovdmitry.audit.repository.entity.AuditEntity;
import by.skachkovdmitry.audit.service.api.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.Reflection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AuditService implements IAuditService {

    private final IAuditRepo auditRepo;

    private final DtoUserToEntityMapper dtoUserToEntityMapper;

    public AuditService(IAuditRepo auditRepo, DtoUserToEntityMapper dtoUserToEntityMapper) {
        this.auditRepo = auditRepo;
        this.dtoUserToEntityMapper = dtoUserToEntityMapper;
    }

    @Override
    @Transactional
    public void save(LogInfo logInfo) {
        auditRepo.save(new AuditEntity(UUID.randomUUID(),
                LocalDateTime.now(),
                dtoUserToEntityMapper.mapDtoToEntity(logInfo.getUser()), //todo может быть null решить этот вопрос
                logInfo.getText(),
                EssenceType.valueOf(logInfo.getEssenceType()),
                logInfo.getId()));

        log.info("аудит добавлен в базу");
    }

    @Override
    public Audit getAudit(UUID uuid) {
        AuditEntity auditEntity = auditRepo.getById(uuid);
        return new Audit(auditEntity.getUuid().toString(),
                auditEntity.getCreateDate(),
                dtoUserToEntityMapper.mapEntityToDto(auditEntity.getUser()),
                auditEntity.getText(),
                auditEntity.getEssenceType(),
                auditEntity.getId());

    }

    @Override
    public PageOfAudit getPageOfAudit(Pageable pageable) {
        Page<AuditEntity> auditEnityPage = auditRepo.findAll(pageable);
        PageOfAudit page = new PageOfAudit();

        page.setNumber(auditEnityPage.getNumber());
        page.setSize(auditEnityPage.getSize());
        page.setTotalPages(auditEnityPage.getTotalPages());
        page.setFirst(auditEnityPage.isFirst());
        page.setNumberOfElements(auditEnityPage.getNumberOfElements());
        page.setLast(auditEnityPage.isLast());

        page.setContent(auditEnityPage
                .getContent()
                .stream()
                .map(elem -> new Audit(elem.getUuid().toString(),
                        elem.getCreateDate().truncatedTo(ChronoUnit.MILLIS),
                        dtoUserToEntityMapper.mapEntityToDto(elem.getUser()),
                        elem.getText(),
                        elem.getEssenceType(),
                        elem.getId())).toList());
        return page;
    }

    @Override//можно через aop тут удобно будте
    public List<AuditEntity> getAuditsForUserBetweenDates(List<UUID> usersUuid, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return auditRepo.findAuditsForUsersBetweenDates(usersUuid, startDate, endDate);
        } catch (Exception e){
            throw new ValidationError("Неверный формат 'from' or 'to'");
        }
    }
}
