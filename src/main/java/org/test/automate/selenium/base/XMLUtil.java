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
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.test.automate.selenium.Constants;
import org.test.automate.selenium.template.Test;
import org.test.automate.selenium.template.TestSuite;


public class XMLUtil{


	private static final ThreadLocal<XMLUtil> threadLocal = new ThreadLocal<XMLUtil>() {
		protected XMLUtil initialValue() {
			return new XMLUtil();
		}
	};
	
	
	private XMLUtil() {
    }
    
    public static XMLUtil getInstance() {
        return threadLocal.get();
    }
    
    
    
    public Object readXML(final String xml, final Class className) throws JAXBException{
    	
    	
    	
    	InputStream is = FileHandler.getResourceAsStream(xml);
    	
    	JAXBContext jaxbContext = JAXBContext.newInstance(className);
    	Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    	
    	return jaxbUnmarshaller.unmarshal(is);
    }
    
    public static void main(String ...arg) throws Exception{
    	
    	
    	Test test = (Test) getInstance().readXML("Test.xml", Test.class);
    	
    	RegexHandler regexHandler = RegexHandler.getInstance();
    	
    	
        	try {
        		regexHandler.domainConfig.putAll(FileHandler.loadResourceProperties(Constants.DOMAIN_CONFIG_FILE));
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        
    	
    	for (TestSuite suite : test.getTestSuites()) {
    		
			System.out.println("file path : " + Constants.TEST_SOURCE_LOCATION + Constants.TEST_PACKAGE.replace(".", "/") + "/"+suite.getId().trim());
			regexHandler.createTestSuite(suite);
		}
    }
	
}
