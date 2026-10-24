package com.lut.grade.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 读写工具（Apache POI）。
 * 读取支持 xls / xlsx；写出采用 SXSSF 流式模式，行量大时不吃内存。
 */
public final class ExcelUtil {

    private ExcelUtil() {
    }

    /** 单次导入的最大行数，避免一次性灌入过多数据。 */
    public static final int MAX_ROWS = 500;

    /**
     * 读取成绩导入模板（首行为表头，从第二行开始读）。
     * 期望列序：学号 | 姓名(可选) | 成绩 | 考试类型(可选)
     *
     * @return 行数据列表，姓名与分数列的解析结果都放在 GradeRow 里
     */
    public static List<GradeRow> readGradeRows(InputStream in) throws IOException {
        List<GradeRow> rows = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum() && rows.size() < MAX_ROWS; i++) {
                Row row = sheet.getRow(i);
                if (row == null || isBlankRow(row)) {
                    continue;
                }
                GradeRow item = new GradeRow();
                item.setRowNum(i + 1);
                item.setStudentNo(cellString(row.getCell(0)));
                item.setStudentName(cellString(row.getCell(1)));
                String scoreText = cellString(row.getCell(2));
                if (scoreText != null && !scoreText.trim().isEmpty()) {
                    item.setScore(new BigDecimal(scoreText.trim()));
                }
                item.setExamType(cellString(row.getCell(3)));
                rows.add(item);
            }
        } catch (NumberFormatException e) {
            throw new IOException("第 " + (rows.size() + 2) + " 行的成绩不是合法数字", e);
        }
        return rows;
    }

    /** 导出成绩表：列序 学号 | 姓名 | 课程 | 学分 | 成绩 | 等级 | 绩点。 */
    public static void writeGradeSheet(OutputStream out, String[] headers, List<String[]> rows)
            throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            Sheet sheet = workbook.createSheet("成绩表");
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            for (int r = 0; r < rows.size(); r++) {
                Row row = sheet.createRow(r + 1);
                String[] values = rows.get(r);
                for (int c = 0; c < values.length; c++) {
                    row.createCell(c).setCellValue(values[c] == null ? "" : values[c]);
                }
            }
            // SXSSF 流式模式不支持 autoSizeColumn，直接设定列宽（宽度单位 1/256 字符）
            int[] widths = {14, 12, 22, 12, 8, 8, 10, 8};
            for (int i = 0; i < headers.length; i++) {
                sheet.setColumnWidth(i, (i < widths.length ? widths[i] : 14) * 256);
            }
            workbook.write(out);
            workbook.dispose();
        }
    }

    private static boolean isBlankRow(Row row) {
        return cellString(row.getCell(0)).isEmpty();
    }

    private static String cellString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                double value = cell.getNumericCellValue();
                if (value == Math.floor(value) && !Double.isInfinite(value)) {
                    return String.valueOf((long) value);
                }
                return String.valueOf(value);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return cell.getStringCellValue().trim();
        }
    }

    /** 一行导入数据。 */
    public static class GradeRow {

        private int rowNum;
        private String studentNo;
        private String studentName;
        private BigDecimal score;
        private String examType;
        private String error;

        public int getRowNum() {
            return rowNum;
        }

        public void setRowNum(int rowNum) {
            this.rowNum = rowNum;
        }

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public BigDecimal getScore() {
            return score;
        }

        public void setScore(BigDecimal score) {
            this.score = score;
        }

        public String getExamType() {
            return examType;
        }

        public void setExamType(String examType) {
            this.examType = examType;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
