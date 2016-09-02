package org.test.automate.selenium.template;

import java.io.Serializable;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.test.automate.selenium.base.BaseDriverSetup;
import org.test.automate.selenium.base.BaseDriverSetup.JUnitSelenium;
import org.test.automate.selenium.template.CustomMethodDescriber;
import com.thoughtworks.selenium.Selenium;



public class JUnitSeleniumBase implements Serializable{
	
	/**
	 * Please don't change this template class
	 * 
	 * **/

	private static final long serialVersionUID = 1L;
	
	private static Selenium selenium;
	private static WebDriver driver;
	private static boolean useRC = false;
	private static String baseUrl;
	private static CustomMethodDescriber describer;
	
	

	@BeforeClass
	public static void setUp() throws Exception {
		
		
		JUnitSelenium seleniumDriver = BaseDriverSetup.setUpDriver(useRC);	
		baseUrl = seleniumDriver.getBaseUrl();
		describer = CustomMethodDescriber.getInstance();
		
		if(useRC){
			selenium = seleniumDriver.getSelenium();
		}else{
			driver = seleniumDriver.getDriver();
		}
		
	}
	
	@After
	public void tearDown() throws Exception {
		
		System.out.println(" tear Down ");

		Thread.sleep(2000);
		
		if(useRC){
			selenium.click("//img[@alt='Logout']");
		}else{
			driver.findElement(By.xpath("//img[@alt='Logout']")).click();
		}
		
		
		Thread.currentThread().sleep(2000);
	}
	
	@AfterClass
	public  static void closeBrowser() throws InterruptedException{
		
		if(useRC){
			selenium.stop();
		}else{
			driver.quit();
		}
		
		Thread.currentThread().sleep(2000);
	}
	
	

}
