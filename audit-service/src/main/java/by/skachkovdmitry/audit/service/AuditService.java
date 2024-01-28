package by.skachkovdmitry.audit.service;

import by.skachkovdmitry.audit.core.enums.EssenceType;
import by.skachkovdmitry.audit.core.mapper.DtoUserToEntityMapper;
import by.skachkovdmitry.audit.core.dto.Audit;
import by.skachkovdmitry.audit.core.dto.LogInfo;
import by.skachkovdmitry.audit.core.dto.PageOfAudit;
import by.skachkovdmitry.audit.repository.api.IAuditRepo;
import by.skachkovdmitry.audit.repository.entity.AuditEntity;
import by.skachkovdmitry.audit.service.api.IAuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuditService implements IAuditService {

    private final IAuditRepo auditRepo;

    private final DtoUserToEntityMapper dtoUserToEntityMapper;

    public AuditService(IAuditRepo auditRepo, DtoUserToEntityMapper dtoUserToEntityMapper) {
        this.auditRepo = auditRepo;
        this.dtoUserToEntityMapper = dtoUserToEntityMapper;
    }

    @Override
    public void save(LogInfo logInfo) {

        auditRepo.save(new AuditEntity(UUID.randomUUID(),
                LocalDateTime.now(),
                dtoUserToEntityMapper.mapDtoToEntity(logInfo.getUser()), //todo может быть null решить этот вопрос
                logInfo.getText(),
                EssenceType.valueOf(logInfo.getEssenceType()),
                logInfo.getId()));

        System.out.println("аудит добавлен в базу");

    }

    @Override
    public Audit getAudit(UUID uuid) {
        AuditEntity auditEntity = auditRepo.getById(uuid);
        return new Audit(auditEntity.getUuid().toString(),
                auditEntity.getDt_create(),
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
                        elem.getDt_create().truncatedTo(ChronoUnit.MILLIS),
                        dtoUserToEntityMapper.mapEntityToDto(elem.getUser()),
                        elem.getText(),
                        elem.getEssenceType(),
                        elem.getId())).toList());
        return page;
    }
}
