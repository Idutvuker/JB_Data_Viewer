import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;


public class Application extends JFrame {
	public static class Configuration {
		private boolean isWindowsOS() {
			return System.getProperty("os.name").toLowerCase().contains("windows");
		}

		public String interpreterPath =  isWindowsOS() ? "python" : "python3";
	}

	private Configuration configuration = new Configuration();
	private JTable jtable;
	private StringTable tableModel;

	private void openFileNotFoundDialog(String filepath) {
		JOptionPane.showMessageDialog(
				this,
				"File not found:\n" + filepath,
				"Error",
				JOptionPane.ERROR_MESSAGE
		);
	}

	private void openInterpreterNotFoundDialog() {
		String[] options = {"Open Settings", "Close"};

		int result = JOptionPane.showOptionDialog(
				this,
				"Python interpreter not found.\n" +
						"Set the path in Settings",
				"Error",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.ERROR_MESSAGE,
				null,
				options,
				options[0]
		);

		if (result == JOptionPane.OK_OPTION) {
			new SettingsDialog(configuration);
		}
	}

	private void loadTable(File file) {
		var providerBuilder = new DataProvider.Builder(file, configuration.interpreterPath);

		try {
			StringTable newTableModel = new StringTable(providerBuilder);

			if (tableModel != null)
				tableModel.close();

			tableModel = newTableModel;
		} catch (FileNotFoundException e) {
			openFileNotFoundDialog(e.getMessage());
		} catch (IOException e) {
			openInterpreterNotFoundDialog();
		}
	}

	private void showTable() {
		if (tableModel != null) {
			if (jtable == null) {
				jtable = new JTable();

				jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				add(jtable);

				JScrollPane pane = new JScrollPane(jtable);
				add(pane);
			}

			jtable.setModel(tableModel);
			setVisible(true);
		}
	}

	private File openCSVFileChooser() {
		JFileChooser jfc = new JFileChooser(new File("."));
		jfc.setFileFilter(new FileNameExtensionFilter("Comma Separated Values", "csv"));
		jfc.showOpenDialog(this);

		return jfc.getSelectedFile();
	}

	private void setupMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");

		JMenuItem openCSV = new JMenuItem("Open CSV");
		openCSV.addActionListener(e -> {
			File file = openCSVFileChooser();
			if (file != null)
				loadTable(file);
			showTable();
		});

		JMenuItem settings = new JMenuItem("Settings");
		settings.addActionListener(e -> new SettingsDialog(configuration));

		menu.add(openCSV);
		menu.add(settings);
		menuBar.add(menu);

		setJMenuBar(menuBar);
	}

	public Application() {
		super("CSV Viewer");

		setupMenu();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		pack();
		setVisible(true);
	}
}
