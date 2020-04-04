import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringTableTest {
	StringTable table;

	StringTableTest() throws IOException {
		Application.Configuration configuration = new Application.Configuration();

		table = new StringTable(new DataProvider.Builder(
				new File("src/test/java/sample.csv"), configuration.interpreterPath));
	}

	@Test
	void getValueAt() {
		assertEquals("NA", table.getValueAt(1, 2));
		assertEquals("Name, with,commas", table.getValueAt(2, 1));
		assertEquals("70.3", table.getValueAt(0, 2));
	}

	@Test
	void getRowCount() {
		assertEquals(3, table.getRowCount());
	}

	@Test
	void getColumnCount() {
		assertEquals(3, table.getColumnCount());
	}

	@Test
	void getColumnName() {
		assertEquals("Name", table.getColumnName(1));
		assertEquals("Weight, kg", table.getColumnName(2));
	}
}
