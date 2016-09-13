package org.test.automate.selenium.testng.pageobject;

import org.openqa.selenium.WebDriver;
import org.test.automate.selenium.template.CustomMethodDescriber;
import org.openqa.selenium.By;

public final class PageObjectBase {
	
	private final WebDriver driver;
	private final CustomMethodDescriber describer;
	
	public PageObjectBase(final WebDriver driver, final CustomMethodDescriber describer){
		this.driver = driver;
		this.describer = describer;
	}
	
	
}


