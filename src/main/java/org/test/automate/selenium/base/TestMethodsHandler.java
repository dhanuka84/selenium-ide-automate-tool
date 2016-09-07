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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.test.automate.selenium.Constants;
import org.test.automate.selenium.template.Parameter;
import org.test.automate.selenium.template.TestCase;
import org.test.automate.selenium.template.TestSuite;


public class TestMethodsHandler {
	
	private static final ThreadLocal<TestMethodsHandler> threadLocal = new ThreadLocal<TestMethodsHandler>() {
		protected TestMethodsHandler initialValue() {
			return new TestMethodsHandler();
		}
	};
	
	public static final Map<String,Map<String,Set<String>>> fieldsPerPageObject = new ConcurrentHashMap<String,Map<String,Set<String>>>(); 
	
	
	private TestMethodsHandler() {
		
    }
    
    public static TestMethodsHandler getInstance() {
        return threadLocal.get();
    }
    
    private static Pattern testMethodPattern = Pattern.compile("\\s@Test+[\\w|\\W|\\n|\\s]*@After");
    private static Pattern baseClassPatternTestng = Pattern.compile(Constants.TESTNG_BASE_CLASS);
    private static Pattern baseClassPatternJuit = Pattern.compile(Constants.JUNIT_BASE_CLASS);
    private static Pattern parameterPattern = Pattern.compile("String\\s*[\\w]*\\s*=\\s*\"[\\w]+\";");
    private static Pattern seleniumTypePattern = Pattern.compile("private\\s*static\\s*boolean\\s*useRC\\s*=\\s*false;");
    
    public static Properties domainConfig = new Properties();
    
    private static String sequence = SequenceGenerator.createId(SequenceGenerator.getPrefix(), SequenceGenerator.getInitialValue(), 15);
    
     
    
	public String findParametersAndEdit(final String method, final Map<String, Parameter> suiteParamMap,
			final Map<String, Parameter> testMethodParamMap) {
				String finalMethod = method;
    	
    	Map<String,String> parameterMap = new HashMap<String,String>();
    	
    	Matcher testMethodMatcher = parameterPattern.matcher(method);
        StringBuffer sb = new StringBuffer();
        while (testMethodMatcher.find()) {
        	String parameter = testMethodMatcher.toMatchResult().group();
        	int startRefIndex = parameter.indexOf("String");
        	int endRefIndex = parameter.indexOf("=");
        	int endParametherIndex = parameter.indexOf(";");
        	String key = parameter.substring(startRefIndex + 6, endRefIndex).trim();
        	String value = parameter.substring(endRefIndex + 1,endParametherIndex).trim();
        	parameterMap.put(key.trim(), value.trim());
        	
        	//if key exist
        	String newValue = "";
        	
        	if(testMethodParamMap.containsKey(key)){
				
				Parameter param = testMethodParamMap.get(key);
				newValue =  param.getValue();
				
				if(param.isAutogenerate()){
					long seqVal = Long.parseLong(sequence);
					testMethodMatcher.appendReplacement(sb, "String "+key+" = "+"\""+seqVal+"\";\n");
					sequence = ""+SequenceGenerator.increment(seqVal);
				}else
				if(StringUtils.isNotEmpty(newValue)){
					testMethodMatcher.appendReplacement(sb, "String "+key+" = "+"\""+newValue+"\";\n");
				} 
				
			}else if(suiteParamMap.containsKey(key)){
				Parameter param = suiteParamMap.get(key);
				newValue =  param.getValue();
				if(StringUtils.isNotEmpty(newValue)){
					testMethodMatcher.appendReplacement(sb, "String "+key+" = "+"\""+newValue+"\";\n");
				} 
					
			}
        	
        	
             
        }
        
        finalMethod = testMethodMatcher.appendTail(sb).toString();
    	
    	return finalMethod;
    }
    
    public String findTestMethod(final String file, final TestCase testCase, final Map<String,Parameter> suiteParamMap) throws Exception{
    	
    	
    	FileHandler fileHandler = FileHandler.getInstance();
    	String content = fileHandler.readInputStream(FileHandler.getResourceAsStream(file));
    	
    	List<Parameter> testMethodParameters = testCase.getParameters();
		Map<String,Parameter> testMethodParamMap = new HashMap<String,Parameter>();
		if(testMethodParameters != null){
			for(Parameter parameter : testMethodParameters){
				testMethodParamMap.put(parameter.getId(), parameter);
			}
		}
		
    	
    	Matcher testMethodMatcher = testMethodPattern.matcher(content);
        StringBuilder sb = new StringBuilder();
        while (testMethodMatcher.find()) {
        	String method = testMethodMatcher.toMatchResult().group();
        	
        	method = findParametersAndEdit(method, suiteParamMap, testMethodParamMap);
        	/*Set<Entry<String, String>> entries = parameters.entrySet();
        	if(entries != null){
        		for(Entry<String, String> entry : entries){
        			String key = entry.getKey();
        			String value = entry.getValue();
        			
        			if(testMethodParamMap.containsKey(key)){
        				
        				Parameter parameter = testMethodParamMap.get(key);
        				
        			}else if(suiteParamMap.containsKey(key)){
        				Parameter parameter = suiteParamMap.get(key);
        			}
        		}
        	}*/
        	
        	
        	
        	
             sb.append(method);
        }
        
        int end = sb.indexOf("@After");
        String finalMethod = sb.substring(0, end);
    	
    	return finalMethod;
    }
    
	public String createTestSuite(final TestSuite suite) throws Exception {
		
		boolean isJunit = false;
		boolean useRC = suite.isUseRC();
		
		if (domainConfig.containsKey(Constants.TEST_FRAMEWORK_CONFIG)
				&& Constants.TEST_FRAMEWORK_JUNIT.equalsIgnoreCase(domainConfig
						.getProperty(Constants.TEST_FRAMEWORK_CONFIG))) {
			isJunit = true;
		}

		String packageName = Constants.TEST_PACKAGE;
		String className = suite.getId();
		String testFilePath = Constants.TEST_SOURCE_LOCATION + Constants.TEST_PACKAGE.replace(".", "/") + "/"+suite.getId().trim()+".java";
		String pageobjectPath = Constants.TEST_SOURCE_LOCATION + Constants.PAGE_OBJECT_PACKAGE.replace(".", "/") + "/";
		
		List<TestCase> testMethods = suite.getTestCases();
		
		List<Parameter> suiteParameters = suite.getParameters();
		Map<String,Parameter> suiteParamMap = new HashMap<String,Parameter>();
		for(Parameter parameter : suiteParameters){
			if(parameter.isAutogenerate()){
				long seqVal = Long.parseLong(sequence);
				parameter.setValue(""+seqVal);
				sequence = ""+SequenceGenerator.increment(seqVal);
			}
			suiteParamMap.put(parameter.getId(), parameter);
		}

		FileHandler fileHandler = FileHandler.getInstance();
		String baseClassName = Constants.TESTNG_BASE_CLASS + ".java";
		if(isJunit){
			baseClassName = Constants.JUNIT_BASE_CLASS + ".java";
		}
		String content = fileHandler.readInputStream(FileHandler.getResourceAsStream(baseClassName));

		// add test method

		int classEnd = content.lastIndexOf("}");
		String baseClass = content.substring(0, classEnd);
		StringBuilder finalClass = new StringBuilder();
		finalClass.append(baseClass).append("\n");
		for (TestCase method : testMethods) {
			finalClass.append("\n").append(findTestMethod(method.getId().trim()+".java", method, suiteParamMap)).append("\n");
		}

		finalClass.append("}");

		// replace package

		int packageEndIndex = finalClass.indexOf(";");
		int packageStartIndex = finalClass.indexOf("package");
		finalClass.delete(packageStartIndex, packageEndIndex);
		packageEndIndex = finalClass.indexOf(";");
		finalClass.replace(0, packageEndIndex, "package " + packageName);

		// replace class name
		Pattern pattern = baseClassPatternTestng;
		if(isJunit){
			pattern = baseClassPatternJuit;
		}
		
		Matcher classMatcher = pattern.matcher(finalClass);
		String finalOutput = null;
		while (classMatcher.find()) {

			finalOutput = classMatcher.replaceFirst(className);
			break;
		}

		if(useRC){
			finalOutput = changeUseRC(finalOutput);
		}
		
		CustomMethodEditor methodEditor = CustomMethodEditor.getInstance();
		if(! useRC){
			finalOutput = methodEditor.replaceFindMethod(finalOutput);

		}
		
		FileHandler.deleteAndWriteToFile(testFilePath,finalOutput);
		
		//createOrupdate PageObject class
		
		methodEditor.extractPageObject(finalOutput,pageobjectPath);

		return finalOutput;
	}
	
	private String changeUseRC(final String defaultClass){
		
		Matcher seleniumTypeMatcher = seleniumTypePattern.matcher(defaultClass);
        StringBuffer sb = new StringBuffer();
        while (seleniumTypeMatcher.find()) {
        	seleniumTypeMatcher.appendReplacement(sb, Constants.USE_RC);
        }
		return seleniumTypeMatcher.appendTail(sb).toString();
	}

}
