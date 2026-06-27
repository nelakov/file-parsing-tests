package demo.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

class ZipArchiveParsingTest {

    private static final String ARCHIVE_PATH = "src/test/resources/files/Archive.zip";
    private static final String PDF_ENTRY = "kubik.pdf";
    private static final String XLS_ENTRY = "price.xlsx";
    private static final String CSV_ENTRY = "price.csv";

    @Test
    void parsesPdfFromArchive() throws Exception {
        try (ZipFile archive = new ZipFile(ARCHIVE_PATH);
             InputStream pdfEntry = entryStream(archive, PDF_ENTRY)) {
            PDF pdf = new PDF(pdfEntry);

            assertThat(pdf.text).contains("Как собрать кубик Рубика");
        }
    }

    @Test
    void parsesXlsFromArchive() throws Exception {
        try (ZipFile archive = new ZipFile(ARCHIVE_PATH);
             InputStream xlsEntry = entryStream(archive, XLS_ENTRY)) {
            XLS xls = new XLS(xlsEntry);

            assertThat(firstCellValue(xls)).contains("product_id");
        }
    }

    @Test
    void parsesCsvFromArchive() throws Exception {
        try (ZipFile archive = new ZipFile(ARCHIVE_PATH);
             InputStream csvEntry = entryStream(archive, CSV_ENTRY);
             CSVReader csvReader = new CSVReader(new InputStreamReader(csvEntry, StandardCharsets.UTF_8))) {
            List<String[]> csvRows = csvReader.readAll();

            assertThat(csvRows.get(0)).contains(
                    "product_id", "Артикул", "Фото", "Название", "Описание", "Цена", "link");
        }
    }

    private static InputStream entryStream(ZipFile archive, String entryName) throws Exception {
        ZipEntry entry = archive.getEntry(entryName);
        assertThat(entry).as("entry %s present in archive", entryName).isNotNull();
        return archive.getInputStream(entry);
    }

    private static String firstCellValue(XLS xls) {
        return xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
    }
}
