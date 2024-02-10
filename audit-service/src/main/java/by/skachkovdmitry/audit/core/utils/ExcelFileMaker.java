package by.skachkovdmitry.audit.core.utils;

import by.dmitryskachkov.entity.NoContentException;
import by.dmitryskachkov.entity.SystemError;
import by.skachkovdmitry.audit.repository.entity.AuditEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@Slf4j
public class ExcelFileMaker {

    private String fileName;

    @Value("${app.reports.directory}")
    private String DIRECTORY;

    private final String EXTENSION = ".xlsx";
    public ExcelFileMaker() {
    }

    public byte[] loadFile(String fileName)  { //todo aop
        try{
            return Files.readAllBytes(Path.of(DIRECTORY + fileName + EXTENSION));
        } catch (IOException e){
            throw new NoContentException("Сервер, по предоставленному uuid, не смог найти информацию");
        }
    }

    public void createFile(List<AuditEntity> auditEntityList) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("AuditData");
        Row headerRow = sheet.createRow(0);
        String[] columns = {"uuid", "dtCreate", "user_uuid", "text", "essenceType", "id", "mail", "fio", "role"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (AuditEntity audit : auditEntityList){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(audit.getUuid().toString());
            row.createCell(1).setCellValue(audit.getCreateDate().toString());
            row.createCell(2).setCellValue(audit.getUser().getUuid().toString());
            row.createCell(3).setCellValue(audit.getText());
            row.createCell(4).setCellValue(audit.getEssenceType().toString());
            row.createCell(5).setCellValue(audit.getId());
            row.createCell(6).setCellValue(audit.getUser().getMail());
            row.createCell(7).setCellValue(audit.getUser().getFio());
            row.createCell(8).setCellValue(audit.getUser().getRole().toString());
        }


        try (FileOutputStream fileOut = new FileOutputStream(DIRECTORY + fileName + EXTENSION)) {
            workbook.write(fileOut);
            log.info("Файл успешно создан с именем - " + fileName);
        } catch (IOException e) {
            log.error("Ошибка в создании файла");
           throw new SystemError();
        }
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
