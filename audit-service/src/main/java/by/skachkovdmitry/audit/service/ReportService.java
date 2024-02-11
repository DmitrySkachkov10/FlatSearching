package by.skachkovdmitry.audit.service;


import by.dmitryskachkov.entity.SystemError;
import by.dmitryskachkov.entity.ValidationError;
import by.skachkovdmitry.audit.core.dto.PageOfReport;
import by.skachkovdmitry.audit.core.dto.Report;
import by.skachkovdmitry.audit.core.dto.UserActionAuditParam;
import by.skachkovdmitry.audit.core.enums.ReportStatus;
import by.skachkovdmitry.audit.core.enums.ReportType;
import by.skachkovdmitry.audit.core.utils.ExcelFileMaker;
import by.skachkovdmitry.audit.repository.api.IReportRepo;
import by.skachkovdmitry.audit.repository.entity.ReportEntity;
import by.skachkovdmitry.audit.service.api.IAuditService;
import by.skachkovdmitry.audit.service.api.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class ReportService implements IReportService {
    private final IReportRepo reportRepo;

    private final TaskExecutor taskExecutor;
    private final ExcelFileMaker excelFileMaker;
    private final IAuditService auditService;
    private final Lock lock = new ReentrantLock();
    private final String FROM = "from: ";
    private final String TO = " to: ";

    public ReportService(IReportRepo reportRepo, TaskExecutor taskExecutor, ExcelFileMaker excelFileMaker, IAuditService auditService) {
        this.reportRepo = reportRepo;
        this.taskExecutor = taskExecutor;
        this.excelFileMaker = excelFileMaker;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public void addReport(UserActionAuditParam userActionAuditParam) {

        ReportEntity reportEntity = new ReportEntity(UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                ReportStatus.LOADED,
                ReportType.JOURNAL_AUDIT,
                FROM + userActionAuditParam.getFrom() + TO + userActionAuditParam.getTo(),
                userActionAuditParam.getUserUuid(),
                userActionAuditParam.getFrom().atStartOfDay(),
                userActionAuditParam.getTo().atStartOfDay());
        try {
            reportRepo.saveAndFlush(reportEntity);
            taskExecutor.execute(() -> create(reportEntity));
        } catch (DataIntegrityViolationException e) {
            log.error("Попытка создания дубликата отчета");
            throw new ValidationError("Данный отчет уже создан");
        }
    }

    @Override
    public PageOfReport getReports(Pageable pageable) {
        Page<ReportEntity> reportEntities = reportRepo.findAll(pageable);

        return new PageOfReport(reportEntities.getNumber(),
                reportEntities.getSize(),
                reportEntities.getTotalPages(),
                reportEntities.getTotalElements(),
                reportEntities.isFirst(),
                reportEntities.getNumberOfElements(),
                reportEntities.isLast(),
                reportEntities.getContent().stream()
                        .map(elem -> new Report(elem.getUuid().toString(),
                                elem.getCreateDate(),
                                elem.getUpdateDate(),
                                elem.getStatus(),
                                elem.getDescription(), new UserActionAuditParam(elem.getUserUuid(),
                                elem.getFromDate().toLocalDate(),
                                elem.getToDate().toLocalDate()))).toList());
    }

    @Override
    public boolean exist(UUID id) {
        ReportEntity report = reportRepo.findById(id).orElse(null);
        return report != null && report.getStatus().equals(ReportStatus.DONE);
    }

    @Override
    public InputStreamResource download(UUID uuid) {
        return new InputStreamResource(new ByteArrayInputStream(excelFileMaker.loadFile(String.valueOf(uuid))));
    }


    @Transactional
    private void create(ReportEntity reportEntity) {
        boolean shouldMakeFile = false;
        try {
            lock.lock();
            if (reportEntity.getStatus().equals(ReportStatus.LOADED)) {
                reportEntity.setStatus(ReportStatus.PROGRESS);
                reportRepo.saveAndFlush(reportEntity);
                shouldMakeFile = true;
            }
        } finally {
            lock.unlock();
        }
        if (shouldMakeFile) {
            excelFileMaker.setFileName(reportEntity.getUuid().toString());
            excelFileMaker.createFile(auditService.getAuditsForUserBetweenDates(reportEntity.getUserUuid(), reportEntity.getFromDate(), reportEntity.getToDate()));
            reportEntity.setStatus(ReportStatus.DONE);
            reportRepo.saveAndFlush(reportEntity);
        }
    }
}
