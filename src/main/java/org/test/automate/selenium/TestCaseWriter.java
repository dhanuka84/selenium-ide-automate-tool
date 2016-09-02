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
import java.io.InputStream;
import java.io.Serializable;

import org.test.automate.selenium.base.FileHandler;
import org.test.automate.selenium.base.RegexHandler;
import org.test.automate.selenium.base.XMLUtil;
import org.test.automate.selenium.template.Test;
import org.test.automate.selenium.template.TestSuite;


public class TestCaseWriter {

	public static void writeTestCases() throws Exception {

		XMLUtil xmlUtil = XMLUtil.getInstance();
		Test test = (Test) xmlUtil.readXML(Constants.TEST_CASES_CONFIG, Test.class);

		RegexHandler regexHandler = RegexHandler.getInstance();

		

		for (TestSuite suite : test.getTestSuites()) {
			System.out.println("Test Class : " + suite.getId());
			regexHandler.createTestSuite(suite);
		}
	}

	public static void main(String... arg) throws Exception {
		try {
			RegexHandler regexHandler = RegexHandler.getInstance();
			//FileHandler fileHandler = FileHandler.getInstance();
			/*InputStream is = fileHandler.getResourceAsStream(Constants.DOMAIN_CONFIG_FILE);
			regexHandler.domainConfig.load(is);*/
			regexHandler.domainConfig.load(FileHandler.getResourceAsStream(Constants.DOMAIN_CONFIG_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeTestCases();
	}

}
