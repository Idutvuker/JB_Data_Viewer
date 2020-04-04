import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents tabular data from associated data provider
 */
public class StringTable implements TableModel, Closeable {
	// Amount of bottom rows which will ask to load more rows below, when they are displayed
	private static final int LOAD_THRESHOLD = 2;
	// Amount of rows loaded below the asking row
	private static final int LOAD_BUFFER_SIZE = 100;

	private final List<String> header = new ArrayList<>();
	private final List<List<String>> data = new ArrayList<>();
	private final int numCols;

	private DataProvider provider;

	private int cacheSize = 0;

	/**
	 * Creates string table model based on given provider
	 *
	 * @param dataProviderBuilder a builder for data provider
	 * @throws IOException if provider could not be build
	 */
	public StringTable(DataProvider.Builder dataProviderBuilder) throws IOException {
		provider = dataProviderBuilder.build(header);
		numCols = header.size();

		loadCache(0);
	}

	/**
	 * Tries to load more rows into cache
	 *
	 * @param position indicates from where cache buffer begins
	 */
	private void loadCache(int position) {
		int loadEnd = position + LOAD_BUFFER_SIZE;
		provider.loadRows(loadEnd - cacheSize, data);

		cacheSize = loadEnd;
	}

	/**
	 * Closes provider
	 */
	@Override
	public void close() {
		provider.close();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= cacheSize - LOAD_THRESHOLD)
			loadCache(rowIndex);

		assert rowIndex < cacheSize;

		return data.get(rowIndex).get(columnIndex);
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return numCols;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return header.get(columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}
}
