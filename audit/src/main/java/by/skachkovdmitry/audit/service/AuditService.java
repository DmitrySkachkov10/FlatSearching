package by.skachkovdmitry.audit.service;

import by.skachkovdmitry.audit.core.DtoEntityMapper;
import by.skachkovdmitry.audit.core.dto.Audit;
import by.skachkovdmitry.audit.core.dto.PageOfAudit;
import by.skachkovdmitry.audit.repository.api.IAuditRepo;
import by.skachkovdmitry.audit.repository.entity.AuditEntity;
import by.skachkovdmitry.audit.service.api.IAuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AuditService implements IAuditService {

    private final IAuditRepo auditRepo;

    private final DtoEntityMapper dtoEntityMapper;

    public AuditService(IAuditRepo auditRepo, DtoEntityMapper dtoEntityMapper) {
        this.auditRepo = auditRepo;
        this.dtoEntityMapper = dtoEntityMapper;
    }

    @Override
    public void save(Audit audit) {
        try {

            auditRepo.save(new AuditEntity(UUID.fromString(audit.getUuid()),
                    audit.getDt_create(),
                    dtoEntityMapper.mapDtoToEntity(audit.getUser()),
                    audit.getText(),
                    audit.getEssenceType(),
                    audit.getId()));

            System.out.println("аудит добавлен в базу");

        } catch (Exception e) {
            System.out.println("Ошибка подключение к базе audita");

        }
    }

    @Override
    public Audit getAudit(UUID uuid) {
        AuditEntity auditEntity = auditRepo.getById(uuid);
        return new Audit(auditEntity.getUuid().toString(),
                auditEntity.getDt_create(),
                dtoEntityMapper.mapEntityToDto(auditEntity.getUser()),
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
                        elem.getDt_create(),
                        dtoEntityMapper.mapEntityToDto(elem.getUser()),
                        elem.getText(),
                        elem.getEssenceType(),
                        elem.getId())).toList());
        return page;
    }
}
