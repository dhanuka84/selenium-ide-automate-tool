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

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.test.automate.selenium.BrowserSelector;
import org.test.automate.selenium.BrowserType;
import org.test.automate.selenium.Constants;

import com.thoughtworks.selenium.Selenium;

/**
 
 *
 * Author: Dhanuka Ranasinghe
 * Created: April 02, 2013, 2:24:48 PM
 */
public class BaseDriverSetup {
	
	private static Properties domainConfig = new Properties();
	
	
	public static class JUnitSelenium {
		
		private Selenium selenium;
		private WebDriver driver;
		private String baseUrl;
		
		
		
		public JUnitSelenium(Selenium selenium, WebDriver driver, String baseUrl) {
			super();
			this.selenium = selenium;
			this.driver = driver;
			this.baseUrl = baseUrl;
		}
		
		
		public Selenium getSelenium() {
			return selenium;
		}
		
		public WebDriver getDriver() {
			return driver;
		}


		public String getBaseUrl() {
			return baseUrl;
		}


		
	}
	
	public static JUnitSelenium setUpDriver(final boolean useRC) throws Exception{
		
		if(domainConfig.isEmpty()){
			domainConfig.putAll(FileHandler.loadResourceProperties(Constants.DOMAIN_CONFIG_FILE));
		}
		
		String mode = domainConfig.getProperty(Constants.DRIVER_MODE);
		BrowserType type = BrowserType.getType(mode);
		
		BrowserSelector selector = BrowserSelector.getInstance();

		String baseUrl = domainConfig.getProperty(Constants.BASE_URL);
		String hubUrl = domainConfig.getProperty(Constants.HUB_URL);
		boolean enableNative = Boolean.parseBoolean(domainConfig.getProperty(Constants.NATIVE_ENABLE));
		
		WebDriver driver = null;
		Selenium selenium = null;
		
		switch(type){
		case LOCAL:
			driver = selector.getWebDriver(false,hubUrl,enableNative);
			break;
		case REMOTE:
			driver = selector.getWebDriver(true,hubUrl,enableNative);
			break;
		}
		
		
		
		
		/*DesiredCapabilities  capabilities = DesiredCapabilities.firefox();
		capabilities.setPlatform(org.openqa.selenium.Platform.WINDOWS);
		//capabilities.setVersion("9");
		
		capabilities.setJavascriptEnabled(true);
		
        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);*/
		
		
		/*RemoteWebDriver driver = new InternetExplorerDriver();*/
		
		/*DesiredCapabilities  capabilities = DesiredCapabilities.internetExplorer();*/
		/*capabilities = DesiredCapabilities.firefox();
		//capabilities.setPlatform(org.openqa.selenium.Platform.WINDOWS);
		//capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setJavascriptEnabled(true);
		WebDriver driver = new InternetExplorerDriver(capabilities);
		driver = new FirefoxDriver(capabilities);*/
		
	
		if(useRC){
			selenium = new WebDriverBackedSelenium(driver, baseUrl);
		}
		
		
		/*String baseUrl = "https://asa-test.com";*/
		
		
		//selenium.open("/");
		
		JUnitSelenium seleniumMapper = new JUnitSelenium(selenium, driver, baseUrl);
		return seleniumMapper;
		
	}

	
	

}
