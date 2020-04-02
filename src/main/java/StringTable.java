import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Iterator;

public class StringTable implements TableModel {
	private final String[] header;
	private final String[][] data;

	private DataProvider provider;

	// todo: rename
	private int dFrom = -1;
	private int dTo = -1;

	private void loadChunk(int position) {
		dFrom = position - provider.getChunkSize();
		dTo = position + provider.getChunkSize();

		// Clamp values
		if (dFrom < 0)
			dFrom = 0;

		if (dTo >= provider.getNumRows())
			dTo = provider.getNumRows();

		provider.loadRows(dFrom, dTo - dFrom + 1, data);
	}

	public StringTable(DataProvider provider) {
		this.provider = provider;

		this.data = new String[2 * provider.getChunkSize() + 1][provider.getNumCols()];
		this.header = provider.getHeader();
		//loadChunk(0);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex < dFrom || rowIndex > dTo)
			loadChunk(rowIndex);

		assert !(rowIndex < dFrom || rowIndex > dTo);

		return data[rowIndex - dFrom][columnIndex];
	}

	@Override
	public int getRowCount() {
		return provider.getNumRows();
	}

	@Override
	public int getColumnCount() {
		return provider.getNumCols();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return header[columnIndex];
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
