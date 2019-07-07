package edu.aman.projects.selenium;
/*package com.aman.projects.selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumProgram {
	
	public static void main(String[] args) throws MalformedURLException {
		// System.setProperty("webdriver.chrome.driver","C:\\Drivers\\chromedriver.exe");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", "chrome");
		// WebDriver driver = new ChromeDriver();
		WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4446/wd/hub"), capabilities);
		driver.manage().window().maximize();

		driver.get("https://www.seleniumhq.org/");
		System.out.println(driver.getTitle());
		System.out.println(driver.getWindowHandle());
		driver.close();


		}

	
	
	
	
	
	
	
	
	
	
	

}
*/