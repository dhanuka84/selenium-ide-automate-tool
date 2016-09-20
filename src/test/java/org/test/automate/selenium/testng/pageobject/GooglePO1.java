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
	
	

	 public void login1(String nameParam ){
    describer.findElementExplicitWait( driver , ELEMENT1).clear();
    describer.findElementExplicitWait( driver , ELEMENT1).sendKeys(nameParam);
    describer.findElementExplicitWait( driver , ELEMENT2).click();
    // 
 }
}