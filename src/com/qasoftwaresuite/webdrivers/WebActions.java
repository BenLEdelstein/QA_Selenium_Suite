package com.qasoftwaresuite.webdrivers;

//import java.awt.AWTException;
//import java.awt.Robot;
//import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;

public abstract class WebActions {

	//#REGION FULLY FUNCTIONAL
	//debug
	boolean debug = true;
	//download count
	private int downloadCount;//, totalDownloadCount;
	//private Date time;
	private File directory;
	//TODO check OS for windows or solaris based filepath system -- see main
	 WebActions(String downloadDirectory){		
		//count number of '.crdownload' files in /home/Downloads
		directory = new File(downloadDirectory);
		String [] files = directory.list();
		//totalDownloadCount = 0;
		downloadCount = 0;
		for(int i=0;i<files.length; i++)
			if(files[i].contains(".crdownload"))
				downloadCount++;
	}
	
	 boolean testURL(WebDriver driver, URL url) {
		return testURL(driver, url.toString());
	}
	
	 boolean testURL(WebDriver driver, String url){
		driver.get(url);
		boolean result = false;
		if(driver.getCurrentUrl().equals(url)) {
			if(debug)
				System.out.println("Webpage \""+url+"\" successfully fetched");
			result = true;
		}
		if(debug)
			System.out.println("Webpage \""+url+"\" successfully fetched");
		
		return result;
	}
	
	 boolean testURLTitle(WebDriver driver, URL url, String expectedName) {
		return testURLTitle(driver, url.toString(), expectedName);
	}
	
	 boolean testURLTitle(WebDriver driver, String url, String expectedName) {
		driver.get(url);
		boolean result = false;
		if(driver.getTitle().equals(expectedName)) {
			if(debug)
				System.out.println("Webpage \""+url+"\" successfully fetched");
			result = true;
		}
		if(debug)
			System.out.println("Webpage \""+url+"\" successfully fetched");
		
		return result;
	}
	
	 List<LinkData> testPageLinksByURL(WebDriver driver, URL url) {
		return testPageLinksByURL(driver, url.toString());
	}
	
	 List<LinkData> testPageLinksByURL(WebDriver driver, String url) {
		driver.get(url);
		List<WebElement> pageLinks = driver.findElements(By.tagName("a"));
		List<LinkData> linkResults = new ArrayList<LinkData>();
		for(int i =0; i<pageLinks.size(); i++) {
			WebElement link = pageLinks.get(i);
			String pageTitle = driver.getTitle();
			String pageURL = driver.getCurrentUrl();
			String linkURL = link.getAttribute("href");
			String linkText = link.getText();
			boolean pass = false;
			if(link.isDisplayed()) {
				link.click();
				String linkPageTitle = driver.getTitle();
				if(!driver.getCurrentUrl().equals(pageURL) 
						&& driver.getCurrentUrl().equals(linkURL))
					pass = true;
				LinkData linkData = new LinkData(linkURL, pageTitle, pageURL, linkText, linkPageTitle, pass);
				linkResults.add(linkData);
				if(debug) {
					String passed = "unsuccessfully";
					if(linkData.getPass())
						passed = "successfully";
					System.out.println("\""+linkData.getText()+"\" link to: \""+linkData.getURL()+"\" \n	on the page titled: \""
					+linkData.getPageTitle()+"\" with url: \""+linkData.getPageURL()+"\"\n	"+passed+" navigated to the page \n	on URL: \""
							+linkData.getURL()+"\" titled: \""+linkData.getLinkPageTitle()+"\"");
				}
			}
			driver.navigate().back();
			pageLinks = driver.findElements(By.tagName("a"));
		}
		
		return linkResults;
	}

	 void searchText(WebDriver driver, String url, String textboxXPath, String searchButtonXPath, String word){
		driver.get(url);
		WebElement textbox = driver.findElement(By.xpath(textboxXPath));
		WebElement searchButton = driver.findElement(By.xpath(searchButtonXPath));
		textbox.sendKeys(word);
		searchButton.click();
	}
	
	 void openNewTab(WebDriver driver){
		//open new page
		String pageURL = driver.getCurrentUrl();
		String oldWindow = driver.getWindowHandle();
		String newWindow = "";
		((JavascriptExecutor)driver).executeScript("window.open()");
		ArrayList<String> handles = new ArrayList<String>(driver.getWindowHandles());
		for (int i = 0; i<handles.size(); i++)
			if(handles.get(i).equals(oldWindow)){
				newWindow = handles.get(i+1);
				break;
			}
		
		driver.switchTo().window(newWindow);
		driver.get(pageURL);
	}
	
	 void closeNewTab(WebDriver driver){
		//TODO put last tab check
		String currentHandle = driver.getWindowHandle();
		String previousHandle = "";
		ArrayList<String> handles = new ArrayList<String>(driver.getWindowHandles());
		for(int i = 0; i<handles.size();i++)
			if(handles.get(i).equals(currentHandle))
				previousHandle = handles.get(i-1);
		
		driver.close();
		driver.switchTo().window(previousHandle);
	}
	
	 void delayDownloadQueue(WebDriver driver, int downloadLimit, int secondsDelay){
		int currentDownloads;
		do{
			String[] files = directory.list();
			currentDownloads = 0;
			for (int i = 0; i<files.length; i++)
				if(files[i].contains(".crdownload"))
					currentDownloads++;
		
			if(currentDownloads>downloadLimit)
				driver.manage().timeouts().implicitlyWait(secondsDelay, TimeUnit.SECONDS);
		
		}while(currentDownloads>downloadLimit);
	}
	
	 void login(WebDriver driver, String url, String login, String password){
		driver.get(url);
		//input text into login
		WebElement we = driver.findElement(By.id("login"));
		we.sendKeys(login);
		//input text into password
		we = driver.findElement(By.id("password"));
		we.sendKeys(password);
		we = driver.findElement(By.cssSelector("button[type='submit']"));
		we.click();
	}

	 void clickLinkByText(WebDriver driver, String text){
		WebElement we = driver.findElement(By.linkText(text));
		we.click();
	}
	/**
	 * 
	 * @param driver
	 * @param elementXPath
	 */
	 void recursiveTraverseLinks(WebDriver driver, String elementXPath){
		
		try{//check if there are multiple pages here
				driver.findElement(By.xpath("//img[@title='Next page']"));
				openNewTab(driver);
				traversePages(driver, elementXPath);
				closeNewTab(driver);
		}catch(Exception e){clickPageDownloadLinks(driver, elementXPath);}//if not, continue
		//return to previous page
		driver.navigate().back();
	}
	
	 void clickPageDownloadLinks(WebDriver driver, String elementXPath){
		//check download queue
		delayDownloadQueue(driver, 3,5);
		//grab all links
		List <WebElement> we = driver.findElements(By.xpath(elementXPath));
		//Grab links in table
		for (int i=0; i<we.size(); i++){
			WebElement link = we.get(i);
			try{
				link = driver.findElement(By.xpath("//*/tbody/tr["+(i+2)+"]/td[*]/a[*]/img[@title='Download']"));
			}
			catch(Exception e){}
			String url = driver.getCurrentUrl();
			link.click();
			//totalDownloadCount++;
			
			if(!url.equals(driver.getCurrentUrl()))
				recursiveTraverseLinks(driver, elementXPath);
			we = driver.findElements(By.xpath("//*/tbody/tr[*]/td[1]/a"));
		}
	}
	
	//TODO make parameter for next and previous page xPaths
	 void traversePages(WebDriver driver, String elementXPath){
		
		//download all links
		clickPageDownloadLinks(driver, elementXPath);
		
		try{//go to next page
			driver.findElement(By.xpath("//img[@title='Next page']")).click();
			//repeat
			traversePages(driver, elementXPath);
		}catch(Exception e){}//no more next page links
		
		
		try{//go to previous page
			driver.findElement(By.xpath("//img[@title='Previous page']")).click();
		}catch(Exception f){}
		
	}

	//#ENDREGION
	
	 void twoTierTraverseLinks(WebDriver driver, String level1XPath, String level2XPath)
	{
		//Create lectures directory
		Path lecturesFolder = null;
		try {
			lecturesFolder = Files.createDirectory(Paths.get(directory.getPath(), "Lectures"));
		} catch (IOException e) { e.printStackTrace(); }
		
		//Start initial files array
		File[] initialFilesAndDirectories = directory.listFiles();
		ArrayList<File> ifad = new ArrayList<File>();
		for (File fi : initialFilesAndDirectories)
			ifad.add(fi);
		
		String backURL = driver.getCurrentUrl();
		//get all links
		List<WebElement> links = driver.findElements(By.xpath(level1XPath));
		
		//traverse links (nested)
		for (int i=0; i<links.size(); i++){
			
			//create folder with lectures section name
			Path folder = null;
			try {
				folder = Files.createDirectory(Paths.get(lecturesFolder.toString(), links.get(i).getText()));
			} catch (IOException e) { e.printStackTrace(); }
			
			//click link
			links.get(i).click();
			links = driver.findElements(By.xpath(level2XPath));
			
			for (int j = 0; j<links.size();j++){
				//pdfs should download
				links.get(j).click();
//				driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
//				Actions action = new Actions(driver);
//				action.contextClick().build().perform();
//				
//				try {
//					Robot r = new Robot();
//					r.keyPress(KeyEvent.VK_DOWN);
//					r.keyRelease(KeyEvent.VK_DOWN);
//					r.keyPress(KeyEvent.VK_DOWN);
//					r.keyRelease(KeyEvent.VK_DOWN);
//					r.keyPress(KeyEvent.VK_DOWN);
//					r.keyRelease(KeyEvent.VK_DOWN);
//					r.keyPress(KeyEvent.VK_DOWN);
//					r.keyRelease(KeyEvent.VK_DOWN);
//					r.keyPress(KeyEvent.VK_ENTER);
//					r.keyRelease(KeyEvent.VK_ENTER);
//					r.keyPress(KeyEvent.VK_ENTER);
//					r.keyRelease(KeyEvent.VK_ENTER);
//				} catch (AWTException e) {
//					e.printStackTrace();
//				}
				
				links = driver.findElements(By.xpath(level2XPath));
			}
			//wait until downloads complete
			delayDownloadQueue(driver, 0, 2);
			//move all files to lectures folder directory
			File[] endFiles = directory.listFiles();
			try {
				for (File fi:endFiles) {
					if(!ifad.contains(fi))
						Files.move(fi.toPath(), Paths.get(folder.toString(), fi.getName()));
				}
			} catch (IOException e) { e.printStackTrace(); }
			
			//go back and refresh links list
			driver.get(backURL);
			links = driver.findElements(By.xpath(level1XPath));
		}
	}
	
	 void closeDriver(WebDriver driver){
		
		int newCount;
		do{
			newCount =  0;
			String[] files = directory.list();
			for(int i=0; i<files.length; i++)
				if(files[i].contains(".crdownload"))
					newCount++;
			
			if(newCount>downloadCount)
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
		}while(newCount>downloadCount);
		
		driver.close();
		driver.quit();
	}
}

class LinkData{
	
	private String linkURL, pageTitle, pageURL, linkText, linkPageTitle;
	private boolean pass;
	
	LinkData(String linkURL, String pageTitle, String pageURL, 
			String linkText, String linkPageTitle, boolean pass){
		this.linkURL = linkURL;
		this.pageTitle = pageTitle;
		this.pageURL = pageURL;
		this.linkText = linkText;
		this.linkPageTitle = linkPageTitle;
		this.pass = pass;
	}
	
	void setURL(String url) {this.linkURL=url;}
	String getURL() {return linkURL;}
	void setPageTitle(String pageTitle) {this.pageTitle=pageTitle;}
	String getPageTitle() {return pageTitle;}
	void setPageURL(String pageURL) {this.pageURL=pageURL;}
	String getPageURL() {return pageURL;}
	void setText(String linkText) {this.linkText=linkText;}
	String getText() {return linkText;}
	void setLinkPageTitle(String linkPageTitle) {this.linkPageTitle=linkPageTitle;}
	String getLinkPageTitle() {return linkPageTitle;}
	void setPass(boolean pass) {this.pass = pass;}
	boolean getPass() {return pass;}
	
}
