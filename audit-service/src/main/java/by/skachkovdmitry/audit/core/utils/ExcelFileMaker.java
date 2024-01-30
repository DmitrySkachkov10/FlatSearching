package by.skachkovdmitry.audit.core.utils;

import by.skachkovdmitry.audit.repository.entity.AuditEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelFileMaker {

    private String fileName;

    private final String EXTENSION = ".xlsx";
    public ExcelFileMaker() {
    }

    public ExcelFileMaker(String fileName) {
        this.fileName = fileName;
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
            System.out.println(audit.getId());
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


        try (FileOutputStream fileOut = new FileOutputStream(fileName + EXTENSION)) {
            workbook.write(fileOut);
            System.out.println("Excel файл успешно сохранен локально: " + "first");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении Excel файла: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
