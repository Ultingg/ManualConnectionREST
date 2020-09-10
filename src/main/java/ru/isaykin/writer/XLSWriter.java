package ru.isaykin.writer;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.isaykin.reader.Author;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

@Slf4j
@Component
public class XLSWriter {

    @Value("${user.exclepath}")
    String excelPath;

    public CellStyle dateStyle(XSSFWorkbook workbook) {

        XSSFCellStyle style;
        style = workbook.createCellStyle();
        CreationHelper helper = workbook.getCreationHelper();
        style.setDataFormat(helper.createDataFormat().getFormat("yyyy-mm-dd"));

        return style;
    }

    public void writeToXLS(List<Author> authors) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("AuthorsUnder30");

        int rowNum = 0;
        Cell cell; //

        // задать стиль даты
        CellStyle dateCellStyle = dateStyle(workbook);
        Row row = sheet.createRow(rowNum);

        cell = row.createCell(0, NUMERIC);
        cell.setCellValue("Id");

        cell = row.createCell(1, STRING);
        cell.setCellValue("First name");

        cell = row.createCell(2, STRING);
        cell.setCellValue("Last name");

        cell = row.createCell(3, STRING);
        cell.setCellValue("E-mail");

        cell = row.createCell(4, STRING);
        cell.setCellValue("Birth date");
        cell.setCellStyle(dateCellStyle);

        for (Author au : authors) {
            rowNum++;
            row = sheet.createRow(rowNum);

            //idCell
            cell = row.createCell(0, NUMERIC);
            cell.setCellValue(au.getId());
            //first_name_Cell
            cell = row.createCell(1, STRING);
            cell.setCellValue(au.getFirstName());
            //last_name_Cell
            cell = row.createCell(2, STRING);
            cell.setCellValue(au.getLastName());
            //email
            cell = row.createCell(3, STRING);
            cell.setCellValue(au.getEmail());
            //birth_date_Cell
            cell = row.createCell(4, NUMERIC);
            cell.setCellValue(au.getBirthDate());
            cell.setCellStyle(dateCellStyle);
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(excelPath);
        FileOutputStream outFile;
        try {
            outFile = new FileOutputStream(file);
            workbook.write(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("XLSFile created");
    }
}

