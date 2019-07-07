package edu.aman.projects.docker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class BaseTest {

	// Declare ThreadLocal Driver (ThreadLocalMap) for ThreadSafe Tests
	protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();

	@BeforeMethod
	@Parameters(value = { "browser" })
	public void setupTest(String browser) throws MalformedURLException {
		// Set DesiredCapabilities
		DesiredCapabilities capabilities = new DesiredCapabilities();

		// Set BrowserName
		capabilities.setCapability("browserName", browser);

		// Set the Hub url (Docker exposed hub URL)
		driver.set(new RemoteWebDriver(new URL("http://localhost:4446/wd/hub"), capabilities));
	}

	public WebDriver getDriver() {
		// Get driver from ThreadLocalMap
		return driver.get();
	}

	@AfterMethod
	public void tearDown() throws Exception {
		getDriver().quit();
	}

	@AfterClass
	void terminate() {
		// Remove the ThreadLocalMap element
		driver.remove();
	}

	@BeforeSuite
	public void setupSuite() throws IOException, InterruptedException {
		executeCommand("docker-compose up -d --scale chrome=2 --scale firefox=2");
		verifyGridSetup();
	}

	@AfterSuite
	public void teardownSuite() throws IOException {
		executeCommand("docker-compose down");
	}

	private void executeCommand(String command) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
		File dir = new File("src/main/resources");
		pb.directory(dir);
		pb.redirectErrorStream(true);
		Process p = pb.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
		}
	}

	private void verifyGridSetup() throws IOException, InterruptedException {
		HttpURLConnection con = null;
		URL url = new URL("http://localhost:4446/grid/console");
		while (true) {
			try {
				con = (HttpURLConnection) url.openConnection();
				int responseCode = con.getResponseCode();
				if (responseCode == 200) {
					System.out.println("Successful response from grid setup");
				} else {
					System.out.println("Response other than 200, need to wait for setup");
					con.disconnect();
					Thread.sleep(2000);
					continue;
				}
			} catch (Exception e) {
				System.out.println("Error in response, need to wait for setup");
				Thread.sleep(2000);
				continue;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
			StringBuilder sb = new StringBuilder();
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			if (sb.toString().contains("browserName=chrome")) {
				System.out.println("Setup done");
				break;
			}
			Thread.sleep(2000);
		}
	}

}
