package st.assignment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class CalculateOrderTestIntegrationTest {

	 private Guest aGuest = new Guest();
	    private Member aMember = new Member();

	    private Item promoItem11 = new Item(135,140,true);
	    private Item nonPromoItem8 = new Item(70,75,false);

	    private Object[] orderParam() {
	            return new Object[]{
	                    new Object[]{
	                            Arrays.asList(new OrderDetails(promoItem11, 1)), aMember, 128.25
	                    },
	                    new Object[]{
	                            Arrays.asList(new OrderDetails(nonPromoItem8, 1)), aMember, 70
	                    },
	                    new Object[]{
	                            Arrays.asList(new OrderDetails(nonPromoItem8, 1), new OrderDetails(promoItem11, 1)), aMember, 198.25
	                    },
	                    new Object[]{
	                            Arrays.asList(new OrderDetails(promoItem11, 1)), aGuest, 133
	                    },
	                    new Object[]{
	                            Arrays.asList(new OrderDetails(nonPromoItem8, 1)), aGuest, 75
	                    },
	                    new Object[]{
	                            Arrays.asList(new OrderDetails(promoItem11, 1), new OrderDetails(promoItem11, 1)), aGuest, 266
	                    }
	            };
	        }
	@Test
	@Parameters (method= "orderParam")
	public void testCalculateOrderIntegrationTest(List<OrderDetails> orderDetailsList, User user, double expectedTotal) {
		
		Order o = new Order (user, orderDetailsList, "O1");
		
		OrderApplication oa = new OrderApplication ();
		
		double actR = oa.calculate_order_total(o, user);
		
		assertEquals(expectedTotal, actR,0.0);
	}

}
