import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataProviderTest {

	@Test
	void TestDataProvider() throws IOException {
		Application.Configuration configuration = new Application.Configuration();

		var header = new ArrayList<String>();
		List<List<String>> data = new ArrayList<>();

		var provider = new DataProvider(new File("src/test/java/sample.csv"), configuration.interpreterPath, header);

		provider.loadRows(2, data);
		assertEquals(data.size(), 2);
		provider.loadRows(10, data);
		assertEquals(data.size(), 3);
		provider.loadRows(10, data);
		assertEquals(data.size(), 3);

		assertEquals(header.get(2), "Weight, kg");

		assertEquals(Arrays.asList("2", "Name with spaces", "NA"), data.get(1));
		assertEquals(Arrays.asList("3", "Name, with,commas", "65.1"), data.get(2));
	}
}
