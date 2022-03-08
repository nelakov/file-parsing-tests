![LOGO](https://fs-thb03.getcourse.ru/fileservice/file/thumbnail/h/b635b6cb9478bb87c77e9c070ee6e122.png/s/x50/a/159627/sc/207)

# Tests of parsing files

*_Lesson topics_*
- File download
- File upload
- Parsing pdf files
- Parsing xls files
- Parsing csv file 
- Parsing zip file
- Working with json files
___
## Examples

1. ZIP archive (pdf, csv, xls files)

``` java
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
```
___
2. Parsing JSON files with Jackson

```java
public class SimpleJsonTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void jsonTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HockeyPlayer hockeyPlayer = objectMapper.readValue(Paths.get("src/test/resources/json/player.json").toFile(), HockeyPlayer.class);
        assertThat(hockeyPlayer.name).isEqualTo("Jaromir");
        assertThat(hockeyPlayer.lastname).isEqualTo("Jagr");
        assertThat(hockeyPlayer.isCapitan).isEqualTo(true);
        assertThat(hockeyPlayer.teams).contains("Hawks", "Pinguins");

    }
}
```