import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs a script with a chosen interpreter
 * Communicates with running script via its output and input.
 * Provides table data by parts.
 */
public class DataProvider implements Closeable {
	public static class Builder {
		private File file;
		private String interpreterPath;

		public Builder(File file, String interpreterPath) {
			this.file = file;
			this.interpreterPath = interpreterPath;
		}

		public DataProvider build(List<String> header) throws IOException {
			return new DataProvider(file, interpreterPath, header);
		}
	}

	private static final String SCRIPT_PATH = "src/main/resources/script.py";

	private final int numCols;

	private PrintStream scriptIn;
	private Scanner scriptOutScanner;

	private Process process;

	/**
	 * Starts a process that runs script.
	 * Then reads table header and stores it in given list.
	 *
	 * @param file            a file that should be opened
	 * @param interpreterPath a path to interpreter
	 * @param header          a list where table header will be stored
	 * @throws IOException if couldn't start the process or file wasn't found
	 */
	public DataProvider(File file, String interpreterPath, List<String> header) throws IOException {
		ProcessBuilder processBuilder = new ProcessBuilder(interpreterPath, SCRIPT_PATH);
		process = processBuilder.start();

		scriptIn = new PrintStream(process.getOutputStream());
		scriptIn.println(file.getAbsolutePath());
		scriptIn.flush();

		BufferedReader scriptOut = new BufferedReader(new InputStreamReader(process.getInputStream()));

		scriptOutScanner = new Scanner(scriptOut).useDelimiter("\\R+");

		// if file is not loaded
		if (!scriptOutScanner.nextBoolean())
			throw new FileNotFoundException(file.getAbsolutePath());

		numCols = scriptOutScanner.nextInt();

		for (int i = 0; i < numCols; i++) {
			header.add(scriptOutScanner.next());
		}
	}

	/**
	 * Tells python to load given number of rows.
	 * Then puts loaded rows in the list
	 *
	 * @param numRows Number of rows to load
	 * @param data    List in which data will be stored
	 */
	public void loadRows(int numRows, List<List<String>> data) {
		scriptIn.println(numRows);
		scriptIn.flush();

		int realNumRows = scriptOutScanner.nextInt();

		for (int i = 0; i < realNumRows; i++) {
			ArrayList<String> row = new ArrayList<>(numCols);
			for (int j = 0; j < numCols; j++) {
				row.add(scriptOutScanner.next());
			}
			data.add(row);
		}
	}

	/**
	 * Destroys the process and closes streams
	 */
	@Override
	public void close() {
		process.destroy();
		scriptIn.close();
		scriptOutScanner.close();
	}
}
