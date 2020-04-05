import java.io.IOException;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class PythonEnvTest {
	public static void assumePythonIsInstalled() throws InterruptedException {
		Application.Configuration configuration = new Application.Configuration();

		ProcessBuilder processBuilder = new ProcessBuilder(configuration.interpreterPath, "-c", "import pandas");

		int exitCode;
		try {
			Process process = processBuilder.start();
			exitCode = process.waitFor();
		}
		catch (IOException e) {
			exitCode = -1;
		}

		assumeTrue(exitCode == 0, "Tests require python 3 with pandas to be installed.");
	}
}
