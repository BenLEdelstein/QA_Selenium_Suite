package com.qasoftwaresuite.webdrivers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

public class FirefoxBrowser extends WebActions{

	private WebDriver driver;
	private Path geckoDriver;
	
	public FirefoxBrowser(String downloadDirectory){
		super(downloadDirectory);
		//this needs fixing
		System.setProperty("webdriver.gecko.driver", "drivers/geckodriver");
		
		//Set system driver path
		//get location outside JAR
		File jar = null;
		try {
			jar = new File(WebActions.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e1) { e1.printStackTrace(); }
		String parent = jar.getParent();
		//copy driver out of JAR
		File driverFile = new File("drivers/geckodriver");
		try {
			
			geckoDriver = Files.copy(driverFile.toPath(), 
					Paths.get(parent, driverFile.getName()), 
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) { e.printStackTrace(); }
		//Using Profile LennyX stored in ~/.mozilla/firefox/33dfjsf7.LennyX

		//disabled pdf viewer in firefox and auto download
//		ProfilesIni allProfs = new ProfilesIni();
//		FirefoxProfile profile = allProfs.getProfile("LennyX");
//		profile.setPreference("browser.download.folderList", 2);
//		profile.setPreference("browser.download.dir", downloadDirectory);
//		profile.setPreference("browser.download.useDownloadDir", true);
//		profile.setPreference("pdfjs.disabled", true);
//		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,application/x-pdf,application/octet-stream");
//		FirefoxOptions options = new FirefoxOptions();
//		options.setProfile(profile);
		driver = new FirefoxDriver();//options);
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
			Files.delete(geckoDriver);
		} catch (IOException e) { e.printStackTrace(); }
	}
}
