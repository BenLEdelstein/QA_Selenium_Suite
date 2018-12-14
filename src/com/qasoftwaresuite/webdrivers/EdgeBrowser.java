package com.qasoftwaresuite.webdrivers;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.*;

public class EdgeBrowser extends WebActions{

	private WebDriver driver;
	
	EdgeBrowser(String downloadDirectory){
		super(downloadDirectory);
		driver = new EdgeDriver();
	}
	
	boolean testURL(String url){
		return super.testURL(driver, url);
	}
	
	boolean testURL(URL url) {
		return super.testURL(driver, url);
	}
	
	boolean testURLName(String url, String expectedName) {
		return super.testURLTitle(driver, url, expectedName);
	}
	
	boolean testURLName(URL url, String expectedName) {
		return super.testURLTitle(driver, url, expectedName);
	}
	
	List<LinkData> testPageLinksByURL(URL url) {
		return super.testPageLinksByURL(driver, url);
	}
	
	List<LinkData> testPageLinksByURL(String url) {
		return super.testPageLinksByURL(driver, url);
	}
	
	void searchText(String url, String textboxXPath, String searchButtonXPath, String word){
		super.searchText(driver, url, textboxXPath, searchButtonXPath, word);
	}
	
	void login(String url, String login, String password){
		super.login(driver, url, login, password);
	}
	
	void clickLinkByText(String text){
		super.clickLinkByText(driver, text);
	}
	
	
	void closeDriver(){
		super.closeDriver(driver);
	}
}
