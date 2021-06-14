package st.assignment;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * This regression test for all unit test which includes
 * unit test for class OrderApplication, MyScanner, CSVUtils
 * and ApplicationData
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = {
        ApplicationDataTest.class,
        CSVUtilUnitTest.class,
        MyScannerUnitTest.class,
        OrderApplicationCalculateOrderTotalTest.class,
        OrderApplicationCheckOrderTest.class,
        OrderApplicationGetDeliveryRate.class,
        OrderApplicationLoginAddressTest.class,
        OrderApplicationMakePaymentTest.class,
        OrderApplicationPlaceOrderTest.class,
        OrderApplicationPrintOrderDetailsTest.class,
        OrderApplicationViewItemMenuTest.class
})
public class RegressionUnitTestSuite {
}
