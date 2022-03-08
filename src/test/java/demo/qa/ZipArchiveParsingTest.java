package demo.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ZipArchiveParsingTest {
    ClassLoader classLoader = getClass().getClassLoader();

    // Constants
    private static final String
            CSV_FILE = "price.csv",
            XLS_FILE = "price.xlsx",
            PDF_FILE = "kubik.pdf";

    @Test
    void parseZipArchiveTest() throws Exception {
        ZipFile zf = new ZipFile("src/test/resources/files/Archive.zip");
        for (Enumeration<? extends ZipEntry> iter = zf.entries(); iter.hasMoreElements(); ) {
            ZipEntry entryFile = iter.nextElement();
            if (entryFile.getName().contains("pdf")) {
                assertThat(entryFile.getName()).isEqualTo(PDF_FILE);
                parsePdfTest(zf.getInputStream(entryFile));
            } else if (entryFile.getName().contains("xlsx")) {
                assertThat(entryFile.getName()).isEqualTo(XLS_FILE);
                parseXlsTest(zf.getInputStream(entryFile));
            } else if (entryFile.getName().contains("csv")) {
                assertThat(entryFile.getName()).isEqualTo(CSV_FILE);
                parseCsvTest(zf.getInputStream(entryFile));
            }
        }
    }

        void parsePdfTest (InputStream pdfFile) throws Exception {
            PDF pdf = new PDF(pdfFile);
            assertThat(pdf.text).contains(
                    "Как собрать кубик Рубика"
            );
        }

        void parseXlsTest (InputStream xlsFile) throws Exception {
            XLS xls = new XLS(xlsFile);
            assertThat(xls.excel
                    .getSheetAt(0)
                    .getRow(0)
                    .getCell(0)
                    .getStringCellValue()).contains("product_id");

        }

        void parseCsvTest (InputStream csvFile) throws Exception {
            try (CSVReader reader = new CSVReader(new InputStreamReader(csvFile));) {
                List<String[]> strA = reader.readAll();
                assertThat(strA.get(0)).contains(
                        "product_id",
                        "Артикул",
                        "Фото",
                        "Название",
                        "Описание",
                        "Цена",
                        "link");
            }
        }
    }
