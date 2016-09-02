/*******************************************************************************
 * Copyright [2016] [Dhanuka Ranasinghe]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.test.automate.selenium.template;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class CustomMethodDescriber {

	
	private static final ThreadLocal<CustomMethodDescriber> threadLocal = new ThreadLocal<CustomMethodDescriber>() {
		protected CustomMethodDescriber initialValue() {
			return new CustomMethodDescriber();
		}
	};
	
	
	private CustomMethodDescriber() {
    }
    
    public static CustomMethodDescriber getInstance() {
        return threadLocal.get();
    }

	public  WebElement findElementExplicitWait(final WebDriver driver,final By by)  
	{  
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)  
	             .withTimeout(60, TimeUnit.SECONDS)  
	             .pollingEvery(500, TimeUnit.MILLISECONDS)  
	             .ignoring(NoSuchElementException.class); 

	     WebElement element= wait.until(new Function<WebDriver, WebElement>() {  
	           public WebElement apply(WebDriver driver) {  
	             return driver.findElement(by);  
	            }  
	      });  
	  return element;  
	 } 
}
