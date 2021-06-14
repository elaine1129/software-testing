package st.assignment;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class GetDeliveryRateIntegrationTest {
	
	private Address add = new Address ("1","1","34000","Alor Gajah", "1","Melaka");

	private Member aMember = new Member("M01","Lim","123","0123456789",add);
    
	private List<DeliveryRates> deliveryRates= List.of(new DeliveryRates("Alor Gajah", 2.50));

		
	@Test
	public void testGetDeliveryRate() {
		ApplicationData ad = new ApplicationData();
		ad.setDeliveryRate(deliveryRates);
		OrderApplication oa = new OrderApplication ().setApplicationData(ad);
		
		
		double actR = oa.get_delivery_rate(aMember);
		
		assertEquals (2.50,actR,0.0);
		
	}
	
	

}
