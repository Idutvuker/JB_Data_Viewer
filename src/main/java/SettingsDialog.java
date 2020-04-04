import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class SettingsDialog extends JDialog {
	private static final Icon directoryIcon = UIManager.getIcon("FileView.directoryIcon");

	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField pathField;
	private JButton browse;

	private Application.Configuration configuration;

	public SettingsDialog(Application.Configuration configuration) {
		this.configuration = configuration;

		setupUI();
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonCancel);

		buttonOK.addActionListener(e -> onOK());

		buttonCancel.addActionListener(e -> onCancel());

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(
				e -> onCancel(),
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);

		pack();
		setTitle("Settings");
		setResizable(false);
		setVisible(true);
	}

	private File openFileChooser() {
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog(this);

		return jfc.getSelectedFile();
	}

	private void onOK() {
		configuration.interpreterPath = pathField.getText();
		dispose();
	}

	private void onCancel() {
		dispose();
	}

	private void setupUI() {
		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		final JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(panel1);

		final JLabel label1 = new JLabel();
		label1.setText("Python Interpreter:");
		panel1.add(label1);

		pathField = new JTextField(30);
		pathField.setText(configuration.interpreterPath);
		panel1.add(pathField);

		browse = new JButton();
		browse.setIcon(directoryIcon);
		browse.setPreferredSize(new Dimension(25, 25));
		browse.addActionListener(e -> {
			File file = openFileChooser();
			if (file != null)
				pathField.setText(file.getAbsolutePath());
		});
		panel1.add(browse);

		contentPane.add(Box.createVerticalStrut(100));

		final JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		contentPane.add(panel2);

		buttonCancel = new JButton();
		buttonCancel.setText("Cancel");
		panel2.add(buttonCancel);

		buttonOK = new JButton();
		buttonOK.setHideActionText(false);
		buttonOK.setText("OK");
		panel2.add(buttonOK);
	}
}
