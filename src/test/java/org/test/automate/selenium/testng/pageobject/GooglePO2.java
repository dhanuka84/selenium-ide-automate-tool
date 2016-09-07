package org.test.automate.selenium.testng.pageobject;

import org.openqa.selenium.By;

public final class GooglePO2 {



private static final By ELEMENT1 = By.id("lst-ib");
private static final By ELEMENT2 = By.name("btnG");


 public void login2(){

    driver.get(baseUrl + "");
    describer.findElementExplicitWait( driver , ELEMENT1).clear();
    describer.findElementExplicitWait( driver , ELEMENT1).sendKeys(nameParam);
    describer.findElementExplicitWait( driver , ELEMENT2).click();
    
 
    // 
 }
 }