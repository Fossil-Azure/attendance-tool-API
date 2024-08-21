package com.fossil.attendancetracker.service;

import com.fossil.attendancetracker.model.Attendance;
import com.fossil.attendancetracker.model.MonthlyAttendance;
import com.fossil.attendancetracker.model.Users;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelGeneratorService {
    public ByteArrayInputStream generateExcel(List<Users> users, List<Attendance> attendances, int year, int month, List<MonthlyAttendance> monthlyAttendances) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Attendance");

            // Dates row
            Row dateRow = sheet.createRow(0);
            Row dayRow = sheet.createRow(1);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE");

            // Static Columns
            String[] columns = {"Emp ID", "SAP ID", "Emp Name", "Region", "Manager", "Work Location", "Shift"};
            CellStyle headerCellStyle = createHeaderCellStyle(workbook, IndexedColors.YELLOW.getIndex());
            for (int i = 0; i < columns.length; i++) {
                Cell cell = dayRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Date and Day Columns
            CellStyle dateDayCellStyle = createHeaderCellStyle(workbook, IndexedColors.SKY_BLUE.getIndex());
            int dayIndex = columns.length;
            for (int day = 1; day <= getDaysInMonth(year, month); day++) {
                LocalDate date = LocalDate.of(year, month, day);
                Cell dateCell = dateRow.createCell(dayIndex);
                dateCell.setCellValue(date.format(dateFormatter));
                dateCell.setCellStyle(dateDayCellStyle);

                Cell dayCell = dayRow.createCell(dayIndex);
                dayCell.setCellValue(date.format(dayFormatter));
                dayCell.setCellStyle(dateDayCellStyle);

                dayIndex++;
            }

            // Add additional columns
            String[] additionalColumns = {"Total days WFO", "Total Leaves", "Shift Allowance", "Meals Allowance", "Total Allowance"};
            for (String additionalColumn : additionalColumns) {
                Cell cell = dayRow.createCell(dayIndex++);
                cell.setCellValue(additionalColumn);
                cell.setCellStyle(headerCellStyle);
            }

            // Fill data
            int rowIdx = 2;
            for (Users user : users) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(user.getEmpId());
                row.createCell(1).setCellValue(user.getSapId());
                row.createCell(2).setCellValue(user.getName());
                row.createCell(3).setCellValue(user.getRegion());
                row.createCell(4).setCellValue(user.getManagerName());
                row.createCell(5).setCellValue(user.getWorkLocation());
                row.createCell(6).setCellValue(user.getShift());

                List<Attendance> userAttendances = filterAttendancesByUser(attendances, user.getEmailId());
                int dayColIndex = 7;
                for (int day = 1; day <= getDaysInMonth(year, month); day++) {
                    LocalDate currentDate = LocalDate.of(year, month, day);
                    String attendanceStatus = getAttendanceStatusForDay(userAttendances, year, month, day);

                    if ((currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) && "Not marked".equals(attendanceStatus)) {
                        attendanceStatus = "Weekly Off";
                    }

                    Cell cell = row.createCell(dayColIndex++);
                    cell.setCellValue(attendanceStatus);

                    // Add dropdown with color coding
                    addDropdownToCellWithColor(sheet, cell, workbook,
                            new String[]{"Shift A", "Shift B", "Shift C", "Shift D", "Shift E", "Shift F", "Absent", "Holiday", "Weekly Off"},
                            row.getRowNum(), cell.getColumnIndex());

                    if ("Weekly Off".equals(attendanceStatus)) {
                        XSSFColor customColor = new XSSFColor(new byte[]{(byte) 122, (byte) 122, (byte) 122}, null); // A darker grey
                        CellStyle customGreyCellStyle = createHeaderCellStyle(workbook, customColor);
                        cell.setCellStyle(customGreyCellStyle);
                    }
                }

                MonthlyAttendance userMonthlyAttendance = getMonthlyAttendanceByEmail(monthlyAttendances, user.getEmailId());

                if (userMonthlyAttendance != null) {
                    row.createCell(dayColIndex++).setCellValue(userMonthlyAttendance.getWfo() + userMonthlyAttendance.getWfoFriday()); // Total days WFO
                    row.createCell(dayColIndex++).setCellValue(userMonthlyAttendance.getLeaves()); // Total Leaves
                } else {
                    row.createCell(dayColIndex++).setCellValue("N/A"); // Total days WFO
                    row.createCell(dayColIndex++).setCellValue("N/A"); // Total Leaves
                }

                int firstDayColIndex = 8;
                int lastDayColIndex = firstDayColIndex + getDaysInMonth(year, month) - 1;
                String shiftAllowanceFormula = String.format(
                        "COUNTIF(%s%d:%s%d,\"Shift B\")*150+COUNTIF(%s%d:%s%d,\"Shift C\")*250+COUNTIF(%s%d:%s%d,\"Shift D\")*350+COUNTIF(%s%d:%s%d,\"Shift F\")*250+COUNTIF(%s%d:%s%d,\"Shift E\")*350",
                        getColumnLetter(firstDayColIndex), rowIdx,
                        getColumnLetter(lastDayColIndex), rowIdx,
                        getColumnLetter(firstDayColIndex), rowIdx,
                        getColumnLetter(lastDayColIndex), rowIdx,
                        getColumnLetter(firstDayColIndex), rowIdx,
                        getColumnLetter(lastDayColIndex), rowIdx,
                        getColumnLetter(firstDayColIndex), rowIdx,
                        getColumnLetter(lastDayColIndex), rowIdx,
                        getColumnLetter(firstDayColIndex), rowIdx,
                        getColumnLetter(lastDayColIndex), rowIdx
                );
                row.createCell(dayColIndex++).setCellFormula(shiftAllowanceFormula);

                String mealsAllowanceFormula = String.format(
                        "IF(G%d=\"Shift A\",%s%d*75,%s%d*100)",
                        rowIdx,  // For G column row reference
                        getColumnLetter(lastDayColIndex + 1), rowIdx,
                        getColumnLetter(lastDayColIndex + 1), rowIdx
                );
                row.createCell(dayColIndex++).setCellFormula(mealsAllowanceFormula);

                int shiftAllowanceColIndex = dayColIndex - 1;
                int mealsAllowanceColIndex = dayColIndex;
                String totalAllowanceFormula = String.format(
                        "%s%d+%s%d",
                        getColumnLetter(shiftAllowanceColIndex), rowIdx,
                        getColumnLetter(mealsAllowanceColIndex), rowIdx
                );
                row.createCell(dayColIndex++).setCellFormula(totalAllowanceFormula);
            }

            applyBorderToSheet(sheet, rowIdx, dayIndex);

            for (int i = 0; i < dayIndex; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }

    private void addDropdownToCellWithColor(Sheet sheet, Cell cell, Workbook workbook, String[] options, int rowIdx, int colIdx) {
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(options);
        CellRangeAddressList addressList = new CellRangeAddressList(rowIdx, rowIdx, colIdx, colIdx);
        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);

        // Apply conditional formatting to the cell based on the value
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        // Create a rule for each dropdown value with a specific color
        for (String option : options) {
            ConditionalFormattingRule rule = sheetCF.createConditionalFormattingRule(
                    String.format("TRIM($%s%d)=\"%s\"", getColumnLetter(colIdx + 1), rowIdx + 1, option)
            );
            PatternFormatting fill = rule.createPatternFormatting();
            fill.setFillBackgroundColor(getColorForOption(option, workbook));

            CellRangeAddress[] regions = {new CellRangeAddress(rowIdx, rowIdx, colIdx, colIdx)};
            sheetCF.addConditionalFormatting(regions, rule);
        }
    }

    private MonthlyAttendance getMonthlyAttendanceByEmail(List<MonthlyAttendance> monthlyAttendances, String emailId) {
        return monthlyAttendances.stream()
                .filter(monthlyAttendance -> monthlyAttendance.getEmailId().equals(emailId))
                .findFirst()
                .orElse(null);
    }

    private XSSFColor getColorForOption(String option, Workbook workbook) {
        return switch (option) {
            case "Shift A" -> new XSSFColor(new byte[]{(byte) 144, (byte) 238, (byte) 144}, null);
            case "Shift B" -> new XSSFColor(new byte[]{(byte) 250, (byte) 215, (byte) 160}, null);
            case "Shift C" -> new XSSFColor(new byte[]{(byte) 117, (byte) 117, (byte) 235}, null);
            case "Shift D" -> new XSSFColor(new byte[]{(byte) 240, (byte) 152, (byte) 115   }, null);
            case "Shift E" -> new XSSFColor(new byte[]{(byte) 0, (byte) 255, (byte) 0}, null);
            case "Shift F" -> new XSSFColor(new byte[]{(byte) 255, (byte) 0, (byte) 255}, null);
            case "Weekly Off" -> new XSSFColor(new byte[]{(byte) 122, (byte) 122, (byte) 122}, null);
            case "Holiday" -> new XSSFColor(new byte[]{(byte) 170, (byte) 170, (byte) 170}, null);
            case "Absent" -> new XSSFColor(new byte[]{(byte) 237, (byte) 88, (byte) 85}, null);
            default -> new XSSFColor(new byte[]{(byte) 245, (byte) 115, (byte) 245}, null);
        };
    }

    private CellStyle createHeaderCellStyle(Workbook workbook, short bgColor) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(bgColor);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Set border
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);

        return cellStyle;
    }

    private CellStyle createHeaderCellStyle(Workbook workbook, XSSFColor bgColor) {
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);

        // Set custom RGB background color
        cellStyle.setFillForegroundColor(bgColor);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Set border
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);

        return cellStyle;
    }

    private void applyBorderToSheet(Sheet sheet, int rowCount, int colCount) {
        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                    }
                    CellStyle style = cell.getCellStyle();
                    if (style == null) {
                        style = sheet.getWorkbook().createCellStyle();
                    }

                    // Apply border to each cell
                    style.setBorderBottom(BorderStyle.THIN);
                    style.setBorderTop(BorderStyle.THIN);
                    style.setBorderRight(BorderStyle.THIN);
                    style.setBorderLeft(BorderStyle.THIN);
                    cell.setCellStyle(style);
                }
            }
        }
    }

    private int getDaysInMonth(int year, int month) {
        return java.time.YearMonth.of(year, month).lengthOfMonth();
    }

    private List<Attendance> filterAttendancesByUser(List<Attendance> attendances, String emailId) {
        return attendances.stream().filter(att -> att.getEmailId().equals(emailId)).toList();
    }

    private String getAttendanceStatusForDay(List<Attendance> attendances, int year, int month, int day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        String formattedDate = LocalDate.of(year, month, day).format(formatter);

        return attendances.stream()
                .filter(att -> att.getDate().equals(formattedDate))
                .map(Attendance::getShift)
                .findFirst()
                .orElse("Not marked");
    }

    private void addDropdownToCell(Sheet sheet, Cell cell, Workbook workbook, String[] options) {
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(options);
        CellRangeAddressList addressList = new CellRangeAddressList(cell.getRowIndex(), cell.getRowIndex(),
                cell.getColumnIndex(), cell.getColumnIndex());
        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }

    private String getColumnLetter(int columnNumber) {
        StringBuilder columnLetter = new StringBuilder();
        while (columnNumber > 0) {
            int modulo = (columnNumber - 1) % 26;
            columnLetter.insert(0, (char) (modulo + 'A'));
            columnNumber = (columnNumber - modulo) / 26;
        }
        return columnLetter.toString();
    }

}