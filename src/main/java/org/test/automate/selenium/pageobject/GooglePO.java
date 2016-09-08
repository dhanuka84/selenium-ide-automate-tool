package org.test.automate.selenium.pageobject;

import org.test.automate.selenium.template.CustomMethodDescriber;
import org.openqa.selenium.By;

public final class GooglePO {
	
	private final CustomMethodDescriber describer;

public GooglePO(final CustomMethodDescriber describer){
	this.describer = describer;
}

private static final By ELEMENT1 = By.id("lst-ib");
private static final By ELEMENT2 = By.name("btnG");



 public void login1(String nameParam1 ){

    
    describer.findElementExplicitWait( driver , ELEMENT1).clear();
    describer.findElementExplicitWait( driver , ELEMENT1).sendKeys(nameParam1);
    describer.findElementExplicitWait( driver , ELEMENT2).click();

    // 
 }
 }