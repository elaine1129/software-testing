package st.assignment;

import org.junit.BeforeClass;
import org.junit.Test;
import st.assignment.lib.TablePrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OrderApplicationViewItemMenuTest {
	public static final String filePath = "dummyItemList.csv";
	private static Scanner fin;
	private static List<String[]> linesRead;

	@BeforeClass
	public static void readFile() {
		linesRead = new ArrayList<String[]>();
		try {
			fin = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			System.out.println("Error opening the file " + filePath);
			System.exit(0);
		}
		while (fin.hasNextLine()) {
			String singleLine = fin.nextLine();
			String[] singleItem = singleLine.split(",");
			linesRead.add(singleItem);
		}
	}


	@Test
	public void testViewItemMenuMockito() {
		
		TablePrinter tpMock = mock(TablePrinter.class);
		
		OrderApplication oa = new OrderApplication();
		oa.setTablePrinter(tpMock);
		
		List<Item>itemArray = new ArrayList<Item>();
		itemArray.add(new Item(1, "Butter Cake","Cake",46.00,46.80,false));
		itemArray.add(new Item(2, "Marble Cake","Cake",76.50,77.20,false));

		String[] header = {"Id","Name","Type","Member Price (RM)","Non-Member Price (RM)", "Promotional Item"};

		List<String[]>actualString = new ArrayList<String[]>();

		actualString.add(header);
		actualString.add(itemArray.get(0).toFile().split(","));
		actualString.add(itemArray.get(1).toFile().split(","));

		oa.view_item_menu(itemArray);
		verify(tpMock).printTable(itemArray,header);
		assertArrayEquals(linesRead.toArray(), actualString.toArray());
	}

}
