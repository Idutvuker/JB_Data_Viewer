import java.io.*;
import java.util.Scanner;

public class DataProvider {
	private final int chunkSize;
	private final int numRows;
	private final int numCols;

	private PrintStream pythonIn;
	private Scanner pythonOutScanner;

	private String[] header;

	public DataProvider(File file, int chunkSize) throws IOException {
		this.chunkSize = chunkSize;

		ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/script.py");
		Process process = processBuilder.start();

		pythonIn = new PrintStream(process.getOutputStream());
		pythonIn.println(file.getAbsolutePath());
		//pythonIn.println(chunkSize);
		pythonIn.flush();

		BufferedReader pythonOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader pythonErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		pythonOutScanner = new Scanner(pythonOut).useDelimiter("\\s*\\R+\\s*");

		numRows = pythonOutScanner.nextInt();
		numCols = pythonOutScanner.nextInt();

		header = new String[numCols];
		for (int i = 0; i < numCols; i++)
			header[i] = pythonOutScanner.next();
	}

	public void loadRows(int from, int len, String[][] data) {
		//System.out.println("load from: " + from + " " + len);
		pythonIn.println(from);
		pythonIn.println(len);
		pythonIn.flush();

		for (int i = 0; i < len; i++) {
			for (int j = 0; j < numCols; j++) {
				data[i][j] = pythonOutScanner.next();
			}
		}
	}

	public String[] getHeader() {
		return header;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}
}
