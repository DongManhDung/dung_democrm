package com.dung.democrm.util;

import com.dung.democrm.entity.Customer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class CustomerExcelExporter {
    public CustomerExcelExporter(){

    }

    private static final String SHEET_NAME = "Customers";

    private static final String[] HEADERS = {
        "ID", "Name", "Company", "Phone", "Email", "Owner", "Employee Code", "Create At"
    };
    public static ByteArrayInputStream export(List<Customer> customers){
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()){
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            createHeader(sheet, workbook);

            sheet.setAutoFilter(new CellRangeAddress(0,0,0, HEADERS.length - 1));

            int rowIndex = 1;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            for (Customer customer : customers){
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(getValue(customer.getId().toString()));
                row.createCell(1).setCellValue(getValue(customer.getName()));
                row.createCell(2).setCellValue(getValue(customer.getCompany()));
                row.createCell(3).setCellValue(getValue(customer.getPhone()));
                row.createCell(4).setCellValue(getValue(customer.getEmail()));
                row.createCell(5).setCellValue(
                        customer.getOwner() != null ? customer.getOwner().getFullName() : ""
                );
                row.createCell(6).setCellValue(
                        customer.getOwner() != null ? customer.getOwner().getEmployeeCode() : ""
                );
                row.createCell(7).setCellValue(
                        customer.getCreatedAt().format(formatter)
                );
            }

            // Auto size column
            for (int i = 0; i < HEADERS.length; i++){
                sheet.autoSizeColumn(i);
            }

            sheet.createFreezePane(0,1);

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e){
            throw new RuntimeException("Failed to export customers to Excel.", e);
        }
    }

    private static void createHeader(Sheet sheet, Workbook workbook){
        Row header = sheet.createRow(0);

        header.setHeightInPoints(22);

        CellStyle headerStyle = workbook.createCellStyle();

        // Font
        Font font = workbook.createFont();
        font.setBold(true);

        headerStyle.setFont(font);

        // Background
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Alignment
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Border
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        for(int i = 0; i < HEADERS.length; i++){
            Cell cell = header.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }



    private static String getValue(String value){
        return value == null ? "" : value;
    }
}
