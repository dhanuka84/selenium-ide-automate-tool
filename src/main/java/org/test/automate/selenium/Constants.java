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

import java.io.Serializable;


public class Constants {
	
	public static final String TEST_CASES_CONFIG = "Test.xml";
	public static final String TEST_PACKAGE = "org.test.automate.selenium.testng";
	public static final String PAGE_OBJECT_PACKAGE = "org.test.automate.selenium.testng.pageobject";
	public static final String 	TEST_SOURCE_LOCATION = "src/test/java/";
	public static final String DOMAIN_CONFIG_FILE = "domain.properties";
	public static final String BROWSER_CONFIG_FILE = "browser.properties";
	public static final String BASE_URL = "base.url";
	public static final String DRIVER_MODE = "driver.mode";
	public static final String HUB_URL = "hub.url";
	public static final String NATIVE_ENABLE = "enable.native.events";
	
	
	public static final String TEST_FRAMEWORK_CONFIG ="test.framework";
	public static final String TEST_FRAMEWORK_TESTNG ="testng";
	public static final String TEST_FRAMEWORK_JUNIT ="junit";
	public static final String TESTNG_BASE_CLASS = "SeleniumBase";
	public static final String JUNIT_BASE_CLASS = "JUnitSeleniumBase";
	public static final String PAGE_OBJECT_BASE_CLASS = "PageObjectBase";
	
	public static final String USE_RC = "private static boolean useRC = true;\n";
	

}
