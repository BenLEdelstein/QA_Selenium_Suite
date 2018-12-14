package com.qasoftwaresuite.webdrivers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeBrowser extends WebActions{

	private WebDriver driver;
	private Path chromeDriver;
	
	public ChromeBrowser(String downloadDirectory){
		super(downloadDirectory);
		
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
		
		//Set system driver path
		//get location outside JAR
		File jar = null;
		try {
			jar = new File(WebActions.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e1) { e1.printStackTrace(); }
		String parent = jar.getParent();
		//copy driver out of JAR
		File driverFile = new File("drivers/chromedriver");
		try {
			
			chromeDriver = Files.copy(driverFile.toPath(), 
					Paths.get(parent, driverFile.getName()), 
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) { e.printStackTrace(); }
		

		//disabled pdf viewer in chrome
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("download.directory_upgrade", true);
		chromePrefs.put("download.prompt_for_download", false);
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
		chromePrefs.put("plugins.always_open_pdf_externally", true);
		//chromePrefs.put("plugins.plugins_disabled", new String [] {"Chrome PDF Viewer"});
		chromePrefs.put("download.default_directory", downloadDirectory);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		driver = new ChromeDriver(options);
	}
	
	public boolean testURL(String url){
		return super.testURL(driver, url);
	}
	
	public boolean testURL(URL url) {
		return super.testURL(driver, url);
	}
	
	public boolean testURLName(String url, String expectedName) {
		return super.testURLTitle(driver, url, expectedName);
	}
	
	public boolean testURLName(URL url, String expectedName) {
		return super.testURLTitle(driver, url, expectedName);
	}
	
	public List<LinkData> testPageLinksByURL(URL url) {
		return super.testPageLinksByURL(driver, url);
	}
	
	public List<LinkData> testPageLinksByURL(String url) {
		return super.testPageLinksByURL(driver, url);
	}
	
	public void searchText(String url, String textboxXPath, String searchButtonXPath, String word){
		super.searchText(driver, url, textboxXPath, searchButtonXPath, word);
	}
	
	public void login(String url, String login, String password){
		super.login(driver, url, login, password);
	}
	
	public void clickLinkByText(String text){
		super.clickLinkByText(driver, text);
	}
	
	public void traverseLinks(String elementXPath){
		super.recursiveTraverseLinks(driver, elementXPath);
	}
	
	public void twoTierTraverseLinks(String level1XPath, String level2XPath)
	{
		super.twoTierTraverseLinks(driver, level1XPath, level2XPath);
	}
	public void closeDriver() throws Throwable{
		finalize();	
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		super.closeDriver(driver);
		try {
			Files.delete(chromeDriver);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
}
