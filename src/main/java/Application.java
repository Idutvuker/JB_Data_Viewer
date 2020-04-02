import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class Application {
	private JFrame frame;
	private JTable jtable;

	public static final int CHUNK_SIZE = 100;

	private StringTable loadTable(File file) {
		try {
			DataProvider provider = new DataProvider(file, CHUNK_SIZE);

			return new StringTable(provider);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void showTable(File file) {
		StringTable table = loadTable(file);

		if (table != null) {
			if (jtable == null) {
				jtable = new JTable();

				jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				frame.add(jtable);

				JScrollPane pane = new JScrollPane(jtable);
				frame.add(pane);
			}
			jtable.setModel(table);

			frame.setVisible(true);
		}
	}

	public Application() {
		frame = new JFrame("FrameDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Menu");
		JMenuItem open_csv = new JMenuItem("Open CSV");
		open_csv.addActionListener(e -> {
			JFileChooser jfc = new JFileChooser(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma Separated Values", "csv");
			//jfc.addChoosableFileFilter(filter);
			jfc.setFileFilter(filter);

			int returnValue = jfc.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();
				System.out.println(selectedFile.getAbsolutePath());
				showTable(selectedFile);
			}
		});

		JMenuItem set_python_path = new JMenuItem("Set python path");

		menu.add(open_csv);
		menu.add(set_python_path);
		menuBar.add(menu);

		frame.setJMenuBar(menuBar);
		frame.setPreferredSize(new Dimension(400, 300));
		frame.pack();
		frame.setVisible(true);
	}
}
