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

package org.test.automate.selenium.base;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomMethodEditor {

	
	private static final ThreadLocal<CustomMethodEditor> threadLocal = new ThreadLocal<CustomMethodEditor>() {
		protected CustomMethodEditor initialValue() {
			return new CustomMethodEditor();
		}
	};
	
	
	private CustomMethodEditor() {
		
    }
    
    public static CustomMethodEditor getInstance() {
        return threadLocal.get();
    }
    
    
    private static Pattern findElementPattern = Pattern.compile("driver.findElement\\(By.xpath\\(\"//\\w+.+\"\\)\\)");
    private static Pattern ByXpathPattern = Pattern.compile("By.xpath\\(\"//\\w+.+\"\\)");
    private static Pattern threadSleepPattern = Pattern.compile("Thread.sleep\\([0-9]+\\);");
    private static Pattern threadErrorPattern = Pattern.compile("//.*ERROR.*\\[Thread.sleep\\([0-9]+\\);\\]\\]");
  //.*ERROR.*\[Thread.sleep\([0-9]+\);\]]
  //driver.findElement(By.xpath("//input[@name='username']")).clear();
 //describer.findElementExplicitWait(driver, By.xpath("//input[@name='username']")).clear();
    public static final String FIND_ELEMENT_CUSTOM_METHOD = "describer.findElementExplicitWait(";
    
    public String replaceFindMethod(final String testClass){
		
		Matcher elementMatcher = findElementPattern.matcher(testClass);
        StringBuffer sb = new StringBuffer();
        while (elementMatcher.find()) {
        	String expression = elementMatcher.toMatchResult().group();
        	elementMatcher.appendReplacement(sb, changeFindMethodInput(expression));
        }
        
        String editedClass = elementMatcher.appendTail(sb).toString();
		return applyThreadSleepError(editedClass);
	}
    
    private String changeFindMethodInput(final String expression){
    	    	
    	Matcher elementMatcher = ByXpathPattern.matcher(expression);
        StringBuffer sb = new StringBuffer();
        while (elementMatcher.find()) {
        	String argument = elementMatcher.toMatchResult().group();
        	sb.append(FIND_ELEMENT_CUSTOM_METHOD).append(" driver , ").append(argument).append(")");
        	
        }
		return sb.toString();
    }
    
    
    private String applyThreadSleepError(final String testClass){
    	Matcher elementMatcher = threadErrorPattern.matcher(testClass);
        StringBuffer sb = new StringBuffer();
        while (elementMatcher.find()) {
        	String expression = elementMatcher.toMatchResult().group();
        	elementMatcher.appendReplacement(sb, applyThreadSleep(expression));
        }
		return elementMatcher.appendTail(sb).toString();
    }
    
    private String applyThreadSleep(final String testClass){
    	Matcher elementMatcher = threadSleepPattern.matcher(testClass);
        StringBuffer sb = new StringBuffer();
        while (elementMatcher.find()) {
        	String expression = elementMatcher.toMatchResult().group();
        	sb.append(expression);
        }
		return sb.toString();
    }
}
