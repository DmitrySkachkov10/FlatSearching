package by.skachkovdmitry.audit.service;

import by.skachkovdmitry.audit.core.dto.PageOfReport;
import by.skachkovdmitry.audit.core.dto.Report;
import by.skachkovdmitry.audit.core.dto.UserActionAuditParam;
import by.skachkovdmitry.audit.core.enums.ReportStatus;
import by.skachkovdmitry.audit.core.enums.ReportType;
import by.skachkovdmitry.audit.repository.api.IReportRepo;
import by.skachkovdmitry.audit.repository.entity.ReportEntity;
import by.skachkovdmitry.audit.service.api.IReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ReportService implements IReportService {
    private final IReportRepo reportRepo;

    private final Lock lock = new ReentrantLock();
    private final String FROM = "from: ";
    private final String TO = " to: ";

    public ReportService(IReportRepo reportRepo) {
        this.reportRepo = reportRepo;
    }

    @Override //todo сделать нормаьный маппер и выделить отдельно его
    public void addReport(UserActionAuditParam userActionAuditParam) {

        ReportEntity reportEntity = new ReportEntity(UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                ReportStatus.LOADED,
                ReportType.JOURNAL_AUDIT,
                FROM + userActionAuditParam.getFrom() + TO + userActionAuditParam.getTo(),
                userActionAuditParam.getUser(),
                userActionAuditParam.getFrom(),
                userActionAuditParam.getTo()
        );
        reportRepo.save(reportEntity);
        create(reportEntity.getUuid());
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
                reportEntities.getContent().stream().map(elem -> new Report(elem.getUuid().toString(),
                        elem.getCreateDate(),
                        elem.getUpdateDate(),
                        elem.getStatus(),
                        elem.getDescription(),
                        new UserActionAuditParam(elem.getUser(), elem.getFrom(), elem.getTo()))).toList());
    }

    @Override
    public boolean exist(UUID uuid) {
        return reportRepo.existsById(uuid);  //todo нужно брать статус из базы в не отчет(мы же будем качать файл)
    }

    @Override
    public InputStreamResource download(UUID uuid) {
        return null;
    }

    @Async
    private void create(UUID uuid) {

        ReportEntity existingReport = reportRepo.getReferenceById(uuid);
        if (existingReport.getStatus().equals(ReportStatus.LOADED)) {
            try {
                lock.lock();
                existingReport.setStatus(ReportStatus.PROGRESS);
                reportRepo.saveAndFlush(existingReport);
            } finally {
                lock.unlock();
                //todo создание файла самого
            }
        }
    }
}
