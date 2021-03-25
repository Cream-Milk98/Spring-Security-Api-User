package com.viettel.campaign.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author anhvd_itsol  0ad5d5fb7aa141b4ac383bcb096e2ec3faba0b1b
 */
public class WorkBookBuilder {
    public static Font buildDefaultFont(XSSFWorkbook workbook) {
        Font defaultFont = workbook.createFont();
        defaultFont.setFontHeightInPoints((short) 13);
        defaultFont.setFontName("Times New Roman");
        defaultFont.setColor(IndexedColors.BLACK.getIndex());
        return defaultFont;
    }

    public static Font buildDefaultTitleFont(XSSFWorkbook workbook) {
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setFontName("Times New Roman");
        titleFont.setColor(IndexedColors.BLACK.getIndex());
        titleFont.setBold(true);
        return titleFont;
    }

    public static Font buildDefaultHeaderFont(XSSFWorkbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 13);
        headerFont.setFontName("Times New Roman");
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        headerFont.setBold(true);
        return headerFont;
    }

    public static CellStyle buildDefaultStyleTitle(XSSFWorkbook workbook) {
        CellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setFont(buildDefaultTitleFont(workbook));
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        return styleTitle;
    }

    public static CellStyle buildDefaultStyleRowHeader(XSSFWorkbook workbook) {
        CellStyle styleRowHeader = workbook.createCellStyle();
        styleRowHeader.setFont(buildDefaultHeaderFont(workbook));
        styleRowHeader.setAlignment(HorizontalAlignment.CENTER);
        styleRowHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styleRowHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleRowHeader.setBorderRight(BorderStyle.THIN);
        styleRowHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
        styleRowHeader.setBorderBottom(BorderStyle.THIN);
        styleRowHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styleRowHeader.setBorderLeft(BorderStyle.THIN);
        styleRowHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        styleRowHeader.setBorderTop(BorderStyle.THIN);
        styleRowHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return styleRowHeader;
    }

    public static CellStyle buildDefaultStyleRow(XSSFWorkbook workbook) {
        CellStyle styleRow = workbook.createCellStyle();
        styleRow.setFont(buildDefaultFont(workbook));
        return styleRow;
    }

    public static void writeCellContent(Row row, CellStyle rowStyle, int colNo, Object content) {
        Cell cell = row.createCell(colNo);
        if (content == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(String.valueOf(content));
        }
        cell.setCellStyle(rowStyle);
    }

}
