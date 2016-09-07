package org.test.automate.selenium.pageobject;

import org.openqa.selenium.By;

public class GooglePO {
	
	private static final By ELEMENT1 = By.id("lst-ib");
	
	/*public static void login(){
		describer.findElementExplicitWait( driver , element1).clear();
	}*/
	
	public By test(){
		return ELEMENT1;
	}
	

}
