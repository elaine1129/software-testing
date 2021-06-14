package st.assignment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import st.assignment.lib.ScannerWrapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class MyScannerUnitTest {
    private MyScanner testMyScanner;
    private ScannerWrapper mockScanner;

    @Before
    public void setUp(){
        mockScanner = mock(ScannerWrapper.class);
        testMyScanner = new MyScanner().setScannerWrapper(mockScanner);
    }

    @Test
    @Parameters({"1", "20"})
    public void testNextItemID(int expectedId) {
        when(mockScanner.nextLine()).thenReturn(String.valueOf(expectedId));
        int actualId = testMyScanner.nextItemID();
        assertEquals(expectedId, actualId);
    }

    @Test (expected = IllegalArgumentException.class)
    @Parameters({"0", "21"})
    public void testInvalidItemID(int invalidId) {
        when(mockScanner.nextLine()).thenReturn(String.valueOf(invalidId));
        testMyScanner.nextItemID();
    }

    @Test
    public void testNextLine() {
        when(mockScanner.nextLine()).thenReturn("A line");
        assertEquals("A line", testMyScanner.nextLine());
    }

    @Test
    public void testNext() {
        when(mockScanner.nextLine()).thenReturn("A line");
        assertEquals("A", testMyScanner.next());
    }

    @Test
    @Parameters({"1", "100"})
    public void testNextQuantity(int expectedQuantity) {
        when(mockScanner.nextLine()).thenReturn(String.valueOf(expectedQuantity));
        assertEquals(expectedQuantity, testMyScanner.nextQuantity());
    }

    @Test (expected = IllegalArgumentException.class)
    @Parameters({"0", "-1"})
    public void testInvalidNextQuantity(int expectedQuantity) {
        when(mockScanner.nextLine()).thenReturn(String.valueOf(expectedQuantity));
        testMyScanner.nextQuantity();
    }

    @Test
    public void testNextInt() {
        when(mockScanner.nextLine()).thenReturn("1");
        assertEquals(1, testMyScanner.nextInt());
    }

    @Test (expected = IllegalArgumentException.class)
    @Parameters ({"a", " ", "$-%"})
    public void testNextInt(String input) {
        when(mockScanner.nextLine()).thenReturn(input);
        testMyScanner.nextInt();
    }
}