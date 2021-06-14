package st.assignment;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class CSVUtilUnitTest {

    @Test
    public void testReadAndWriteCSV1() {
        CSVUtil csvUtil = new CSVUtil();
        String[] stringToBeWritten = {"header","first line,123,Hello,Good", "second line,456,bye,bad"};

        csvUtil.writeCSV(Arrays.asList(stringToBeWritten), "dummyFile.csv");

        String[][] dataReaded = csvUtil.readCSV("dummyFile.csv");

        String[] expectedData = {"header", "header","first line,123,Hello,Good", "second line,456,bye,bad"};
        String[][] expectedDataRead = Arrays.stream(expectedData)
                .map(e-> Arrays.stream(e.split(",")).toArray(String[]::new))
                .toArray(String[][]::new);

        assertArrayEquals(expectedDataRead, dataReaded);
    }

    @Test
    public void testAppendCSV2(){
        CSVUtil csvUtil = new CSVUtil();
        String[] stringToBeAppend = {"a new line", "second new line"};

        csvUtil.appendCSV(Arrays.asList(stringToBeAppend), "dummyFile.csv");

        String[][] dataReaded = csvUtil.readCSV("dummyFile.csv");
        String[] expectedData = {"header", "header","first line,123,Hello,Good", "second line,456,bye,bad","a new line", "second new line"};

        String[][] expectedDataRead = Arrays.stream(expectedData)
                .map(e-> Arrays.stream(e.split(",")).toArray(String[]::new))
                .toArray(String[][]::new);

        assertArrayEquals(expectedDataRead, dataReaded);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidReadCsv3(){
        CSVUtil csvUtil = new CSVUtil();
        csvUtil.readCSV("Path that does not exist");
    }
}