package st.assignment;

import org.junit.Test;
import st.assignment.lib.TablePrinter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class OrderApplicationPrintOrderDetailsTest {

	@Test
	public void testPrintOrderDetailsMockito() {
		TablePrinter tp = new TablePrinter();
		TablePrinter tpSpy = spy(tp);
		String[] header = {"Item","Quantity","Price"};
		
		
		Item item = new Item (1, "Butter cake", "Cake", 46, 46.8, false);
		List<OrderDetails> odList = new ArrayList<OrderDetails>();
		odList.add(new OrderDetails("od1", item, 3, 46));
		User u = new Member("M01","Elaine","123","0123456789"
				,new Address("1","1","1","Alor Gajah","1","Melaka"));
		Order o = new Order (u,odList, "o1");

	
		OrderApplication oa = new OrderApplication ();
		oa.setTablePrinter(tpSpy);
		oa.print_order_details(o);

		verify (tpSpy).printTable(odList, header);
	}
	
	
	}