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

package org.test.automate.selenium;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.test.automate.selenium.base.FileHandler;

public class BrowserSelector{

	
	private static final ThreadLocal<BrowserSelector> threadLocal = new ThreadLocal<BrowserSelector>() {
		protected BrowserSelector initialValue() {
			return new BrowserSelector();
		}
	};
	
	
	private BrowserSelector() {
		if(browserConfig.isEmpty()){
			try {
				browserConfig.putAll(FileHandler.loadResourceProperties(Constants.BROWSER_CONFIG_FILE));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
    }
    
    public static BrowserSelector getInstance() {
        return threadLocal.get();
    }
    
    public static Properties browserConfig = new Properties();
    
    private WebDriver driver = null;
	private DesiredCapabilities  capabilities = null;
	
	public WebDriver getWebDriver(boolean isRemote, String hubURL, boolean enableNative) throws Exception{
		String browserName = browserConfig.getProperty("browserName");
		BrowserType type = BrowserType.getType(browserName);
		
		capabilities = getDesiredCapabilities();
		
		if(isRemote){
			if(enableNative){
				FirefoxProfile profile = new FirefoxProfile(); 
				// forcefully enable native events 
				profile.setEnableNativeEvents(true); 
				capabilities.setCapability(FirefoxDriver.PROFILE, profile);
			}
			 
			driver = new RemoteWebDriver(new URL(hubURL), capabilities);
		}else{

			switch(type){
			case FIREFOX :
				if(enableNative){
					FirefoxProfile profile = new FirefoxProfile(); 
					// forcefully enable native events 
					profile.setEnableNativeEvents(true); 
					capabilities.setCapability(FirefoxDriver.PROFILE, profile);
				} 
				driver = new FirefoxDriver(capabilities);
				break;
			case SAFARI :
				driver = new SafariDriver(capabilities);
				break;
			case GOOGLECHROME :
				
				break;
			case OPERA :
			
				break;
			case IEXPLORE :
				driver = new InternetExplorerDriver(capabilities);
				break;
			case IEXPLORE_PROXY :
				break;
			case CHROME :
				driver = new ChromeDriver(capabilities);
				break;
			case KONQUEROR :
				break;
			case MOCK :
				break;
			case IE_HTA :
				break;
			case ANDROID :
				break;
			case HTMLUNIT :
				break;
			case IE :
				break;
			case IPHONE :
				break;
			case IPAD :
				break;
			case PHANTOMJS :
				break;
			}
		}
		
		
		
		
		return driver;
	}
	
	public DesiredCapabilities getDesiredCapabilities(){
		
		String browserName = browserConfig.getProperty("browserName");
		BrowserType type = BrowserType.getType(browserName);
		
		switch(type){
		case FIREFOX :
			capabilities = DesiredCapabilities.firefox();
			break;
		case SAFARI :
			capabilities = DesiredCapabilities.safari();
			break;
		case GOOGLECHROME :
			
			break;
		case OPERA :
			capabilities = DesiredCapabilities.opera();
			break;
		case IEXPLORE :
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			break;
		case IEXPLORE_PROXY :
			break;
		case CHROME :
			capabilities = DesiredCapabilities.chrome();
			break;
		case KONQUEROR :
			
			break;
		case MOCK :
			break;
		case IE_HTA :
			break;
		case ANDROID :
			capabilities = DesiredCapabilities.android();
			break;
		case HTMLUNIT :
			capabilities = DesiredCapabilities.htmlUnit();
			break;
		case IE :
			break;
		case IPHONE :
			capabilities = DesiredCapabilities.iphone();
			break;
		case IPAD :
			capabilities = DesiredCapabilities.ipad();
			break;
		case PHANTOMJS :
			break;
		}
		
		capabilities.setJavascriptEnabled(true);
		
		 Set<Entry<Object, Object>> entries = browserConfig.entrySet();
		 if(entries != null){
			 for(Entry<Object, Object> entry : entries){
				 capabilities.setCapability((String) entry.getKey(), entry.getValue());
			 }
		 }
		
		 
		 
		return capabilities;
	}

}
