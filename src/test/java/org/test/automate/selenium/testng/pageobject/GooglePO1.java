package org.test.automate.selenium.testng.pageobject;

import org.openqa.selenium.WebDriver;
import org.test.automate.selenium.template.CustomMethodDescriber;
import org.openqa.selenium.By;

public final class GooglePO1 {

private static final By ELEMENT1 = By.id("lst-ib");
private static final By ELEMENT2 = By.name("btnG");

	private final WebDriver driver;
	private final CustomMethodDescriber describer;
	
	public GooglePO1(final WebDriver driver, final CustomMethodDescriber describer){
		this.driver = driver;
		this.describer = describer;
	}
	
	

	 public void login1(String nameParam1 ,String nameParam3 ){
    
    describer.findElementExplicitWait( driver , ELEMENT1).clear();
    describer.findElementExplicitWait( driver , ELEMENT1).sendKeys(nameParam1);
    describer.findElementExplicitWait( driver , ELEMENT2).click();

    // 
 }

	 public void login3(){

    
    describer.findElementExplicitWait( driver , ELEMENT1).clear();
    describer.findElementExplicitWait( driver , ELEMENT2).click();

    // 
 }

	 public void login4(String nameParam4 ,String nameParam5 ){
    
    describer.findElementExplicitWait( driver , ELEMENT1).clear();
    describer.findElementExplicitWait( driver , ELEMENT1).sendKeys(nameParam4);
    describer.findElementExplicitWait( driver , ELEMENT2).click();

    // 
 }
}