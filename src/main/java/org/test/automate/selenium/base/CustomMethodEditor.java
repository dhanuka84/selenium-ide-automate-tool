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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.util.ConcurrentHashSet;
import org.test.automate.selenium.Constants;

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
    
    private static FileHandler fileHandler = FileHandler.getInstance();
    private static ConcurrentHashSet<String> newlyCreatedPageObjects = new ConcurrentHashSet<String>();
    private static ConcurrentHashSet<String> newlyCreatedFunctions = new ConcurrentHashSet<String>();
    
    
    private static Pattern findByXpathPattern = Pattern.compile("driver.findElement\\(By.xpath\\(\"//\\w+.+\"\\)\\)");
    private static Pattern findByIdPattern = Pattern.compile("driver.findElement\\(By.id\\(\"\\w+.+\"\\)\\)");
    private static Pattern findByNamePattern = Pattern.compile("driver.findElement\\(By.name\\(\"\\w+.+\"\\)\\)");
    //private static Pattern findElementPattern = Pattern.compile("driver.findElement\\(*.\\)");
    private static Pattern ByXpathPattern = Pattern.compile("By.xpath\\(\"//\\w+.+\"\\)");
    private static Pattern ByIdPattern = Pattern.compile("By.id\\(\"\\w+.+\"\\)");
    private static Pattern ByNamePattern = Pattern.compile("By.name\\(\"\\w+.+\"\\)");
    
    private static Pattern threadSleepPattern = Pattern.compile("Thread.sleep\\([0-9]+\\);");
    private static Pattern threadErrorPattern = Pattern.compile("//.*ERROR.*\\[Thread.sleep\\([0-9]+\\);\\]\\]");
  //.*ERROR.*\[Thread.sleep\([0-9]+\);\]]
  //driver.findElement(By.xpath("//input[@name='username']")).clear();
 //describer.findElementExplicitWait(driver, By.xpath("//input[@name='username']")).clear();
    
    private static Pattern pageObjectPattern = Pattern.compile("//\\s*@pageobject\\(name='\\w+.+'\\s*\\)");
    private static Pattern functionStartPattern = Pattern.compile("//\\s*@function\\(name='\\w+.+'\\s*,*\\s*(manualClass=')*\\w*.*\\s*\\)");
    private static Pattern endOfPagePattern = Pattern.compile("//\\s*@\\s*endPage");
    private static Pattern endOfFunctionPattern = Pattern.compile("//\\s*@\\s*endFunction");
    private static Pattern parameterPattern = Pattern.compile("\\s*String\\s+.*\\s*=\\s*\".*\";\\n*");
    private static Pattern emptyLinePattern = Pattern.compile("\\s*//\\s*\\n*");
    
    //By.id("lst-ib")
    private static Pattern findElementPattern = Pattern.compile("By.[a-zA-Z]+\\(\".*\"\\)");
    
    //private static final By ELEMENT
    private static Pattern elementNamePattern = Pattern.compile("\\s*private\\s+static\\s+final\\s+By\\s+ELEMENT\\d+");
    
    //describer.findElementExplicitWait( driver , By.id("lst-ib")).clear();
    static Pattern functExpLinePattern = Pattern.compile("describer.findElementExplicitWait\\s*\\(.*\\);\\n*");
    
	private static Pattern findFieldPattern = Pattern
			.compile("\\s*private\\s+static\\s+final\\s+By\\s+ELEMENT\\d+\\s*=\\s*By.[a-zA-Z]+\\(\".*\"\\);\\n*");
    
    //constrants
    private static final String FIELD_NAME_CONSTANT = "ELEMENT";
    private static final String FIELD_DECLARATION_CONSTANT = "private static final By "+FIELD_NAME_CONSTANT;   
    
    private static final int ELEMENT_START_NO = 1;
    private static final String FIND_ELEMENT_CUSTOM_METHOD = "describer.findElementExplicitWait(";
    private static final String MANUAL_FUNCTION_ATTRIBUTE = "manualClass='";
    
    public String replaceFindMethod(final String testClass){
		    	
    	String finalEdited = testClass;
    	finalEdited = replaceFindMethod(findByXpathPattern.matcher(testClass),ByXpathPattern);
    	finalEdited = replaceFindMethod(findByIdPattern.matcher(finalEdited),ByIdPattern);
    	finalEdited = replaceFindMethod(findByNamePattern.matcher(finalEdited),ByNamePattern);
    	
    	return finalEdited;
	}
    
    private String replaceFindMethod(Matcher firstLevel, Pattern secondLevelPattern){
    	
        StringBuffer sb = new StringBuffer();
        while (firstLevel.find()) {
        	String expression = firstLevel.toMatchResult().group();
        	firstLevel.appendReplacement(sb, changeFindMethodInput(secondLevelPattern.matcher(expression)));
        }
        
        String editedClass = firstLevel.appendTail(sb).toString();
		return applyThreadSleepError(editedClass);
    	
    }
    
    private String changeFindMethodInput(Matcher elementMatcher){
    	    	
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
    
    public void createPageObject(final StringBuilder testClass, final String filePath) throws Exception{
    	String pageObjTemplate = fileHandler.readInputStream(FileHandler.getResourceAsStream(Constants.PAGE_OBJECT_BASE_CLASS+".java"));
    	
    	String[] testMethods = testClass.toString().split("@Test");
    	int index = 0;
    	for(String test : testMethods){
    		if(index == 0){
    			//we ignore first block
    			++index;
    			continue;
    		}
    		StringBuilder testMethod = new StringBuilder(test);
    		extractPageObject(testMethod, filePath, pageObjTemplate);
    		
    		
    		++index;
    		
    	}
    	
    }
    
/**   
 * This method use to extract information related to pageobject. 
 * 	driver.get(baseUrl + "");
    // @pageobject(name='GooglePO1',replace=true)
    // @function(name='login1',replace=true)
    String nameParam = "dhanuka";
    driver.findElement(By.id("lst-ib")).clear();
    driver.findElement(By.id("lst-ib")).sendKeys(nameParam);
    driver.findElement(By.name("btnG")).click();
    // @endFunction
 	// @endPage
 * @throws Exception 
    
*/	
    
	public String extractPageObject(final StringBuilder testCase, final String filePath, String pageObjTemplate) throws Exception {

		String finalPage = null;
		Matcher pageobjectMatcher = pageObjectPattern.matcher(testCase.toString());

		while (pageobjectMatcher.find()) {
			boolean replacePageObject = true;
			String expression = pageobjectMatcher.toMatchResult().group();
			// extract all the lines related to this pageobject
			int indexOfExp = testCase.indexOf(expression);
			String pageTxt = testCase.substring(indexOfExp,testCase.indexOf("@endPage", indexOfExp));
			pageTxt = pageTxt.replaceAll(pageObjectPattern.pattern(), "");
			//System.out.println("***********************   " + pageTxt);

			// create pageobject file
			expression = StringUtil.removeSpaces(expression);
			// extract replace condition
			//replacePageObject = getReplaceValue(expression);
			String pageClassName = expression.substring(expression.indexOf("name='") + 6, expression.lastIndexOf("'"));
			String exactFile = filePath + pageClassName + ".java";
			
			//create file if not exist or need to replace
			StringBuilder fileContent = new StringBuilder();
			//already created means: created by this tool at particular run
			boolean pageAlreadyCreated = false;
			if(newlyCreatedPageObjects.contains(exactFile)){
				pageAlreadyCreated = true;
			}
			
			if((replacePageObject || !FileHandler.isFileExist(exactFile)) && !pageAlreadyCreated){
				/*String pkgClassDec = "package "+ Constants.PAGE_OBJECT_PACKAGE +
						";\n\nimport org.openqa.selenium.By;\n\n" + "public final class " + pageClassName + " {";*/
				String pkgClassDec = pageObjTemplate.replaceAll(Constants.PAGE_OBJECT_BASE_CLASS, pageClassName);
				
				//start of class body
				FileHandler.createAndUpdateFile(replacePageObject, (exactFile),(pkgClassDec));
				newlyCreatedPageObjects.add(exactFile);
			}
			
			//get file content
			fileContent.append(FileHandler.getFileContent(exactFile));
			
			
			

			// extract function
			Matcher functionMatcher = functionStartPattern.matcher(pageTxt);
			while (functionMatcher.find()) {
				boolean isFuncAlreadyCreated = false;
				String functionExpress = functionMatcher.toMatchResult().group();
				//find whether manualfunction
				if(functionExpress.contains(MANUAL_FUNCTION_ATTRIBUTE)){
					continue;
				}
				
				// extract all the lines/body related to this function
				int indexOfFuncExp = pageTxt.indexOf(functionExpress);
				String functionBody = pageTxt.substring(indexOfFuncExp,pageTxt.indexOf("@endFunction", indexOfFuncExp));
				functionBody = functionBody.replaceAll(functionStartPattern.pattern(), "");
				
				//find parameters
				Matcher parameterMatcher = parameterPattern.matcher(functionBody);
				StringBuilder parameters = new StringBuilder();
				StringBuilder methodSignatureParams = new StringBuilder();
				while(parameterMatcher.find()){
					if(parameters.length() > 0){
						parameters.append(",");
						methodSignatureParams.append(",");
					}
					String parameterExp = parameterMatcher.toMatchResult().group();
					parameterExp = parameterExp.trim();
					parameterExp = parameterExp.substring(0, parameterExp.indexOf("="));
					parameters.append(parameterExp);
					methodSignatureParams.append("String");
				}
				
				
				functionExpress = StringUtil.removeSpaces(functionExpress);
				String functionName = functionExpress.substring(functionExpress.indexOf("name='") + 6,
						functionExpress.lastIndexOf("'"));
	/*			replaceFunction = getReplaceValue(functionExpress);*/
				
				StringBuilder javaMethod = new StringBuilder();
				//GooglePO1.function1(String param1,String param2)
				//(String,String)
				String pageFunctionSignature = pageClassName +"."+functionName+"("+methodSignatureParams.toString()+ ")";
				pageFunctionSignature = pageFunctionSignature.trim();
				
				javaMethod.append(" public void "+functionName+"("+parameters.toString()+ "){\n");
				functionBody = functionBody.replaceAll(parameterPattern.pattern(), "");
				javaMethod.append(functionBody).append("\n }");
				
				
				//create functions to pageobject mapping
				if(TestMethodsHandler.methodSignaturePerPO.containsKey(Constants.PAGE_OBJECT_PACKAGE+"."+pageClassName)){
					Set<String> funtions = TestMethodsHandler.methodSignaturePerPO.get(Constants.PAGE_OBJECT_PACKAGE+"."+pageClassName);
					//function1(String param1, String param2)
					
					funtions.add(functionName+"("+buildParameterList(parameters.toString())+ ")");
				}else{
					Set<String> funtions = new HashSet<String>();
					funtions.add(functionName+"("+buildParameterList(parameters.toString())+ ")");
					TestMethodsHandler.methodSignaturePerPO.putIfAbsent(Constants.PAGE_OBJECT_PACKAGE+"."+pageClassName, funtions);
				}
				//replace page object
				if(replacePageObject){	
					constructFieldsAndFunctions(fileContent, pageClassName, javaMethod, pageFunctionSignature);
					}else{
					//TODO
				}
				
				newlyCreatedFunctions.add(pageFunctionSignature);

			}//end of function creation while loop
			

			FileHandler.createAndUpdateFile(replacePageObject, (exactFile), fileContent.toString());
			//edit test class
		}//end of pageobject creation while loop
		
		

		return finalPage;
	}
	
	//(param1,param2)
	private String constructFunctionCall(final Matcher functionMatcher, final String functionPattern,
			String pageObjTxt, String pageObjVariable, final StringBuilder testConstruct, final Set<String> pageObjInit){
		MatchResult result = functionMatcher.toMatchResult();
		int indexOfFunction = result.start();
		String functionExpress = result.group();
		boolean isManualFunction = false;
		String manualClassName = null;
		//find whether manualfunction
		if(functionExpress.contains(MANUAL_FUNCTION_ATTRIBUTE)){
			isManualFunction = true;
			manualClassName = functionExpress.substring(functionExpress.indexOf(MANUAL_FUNCTION_ATTRIBUTE)+MANUAL_FUNCTION_ATTRIBUTE.length(),
					functionExpress.lastIndexOf("'"));
			
			String firstLetter = manualClassName.substring(0, 1);
			pageObjVariable = manualClassName.replaceFirst(firstLetter, firstLetter.toLowerCase());
			// GooglePO2 googlePO2 = new GooglePO2(driver,describer);
			String objectConstruct = manualClassName + " " + pageObjVariable + "= new " + manualClassName
					+ "(driver,describer);";
			objectConstruct = objectConstruct.replaceAll("\\s*//\\s*", "");
			if(!pageObjInit.contains(manualClassName)){
				testConstruct.append("\n"+objectConstruct);
				pageObjInit.add(manualClassName);
			}
		}
		
		
		// extract all the lines/body related to this function
		String endFunc = "@endFunction";
		String functionBody = pageObjTxt.substring(indexOfFunction, pageObjTxt.indexOf(endFunc, indexOfFunction)+endFunc.length());
		//String parameterSet = functionBody.replaceAll(functionPattern, "");
		
		//find parameters
		Matcher parameterMatcher = parameterPattern.matcher(functionBody);
		StringBuilder parameters = new StringBuilder();
		while(parameterMatcher.find()){
			if(parameters.length() > 0){
				parameters.append(",");
			}
			String parameterExp = parameterMatcher.toMatchResult().group();
			parameterExp = parameterExp.trim();
			parameterExp = parameterExp.substring(parameterExp.indexOf("String")+"String".length(), parameterExp.indexOf("="));
			parameters.append(parameterExp);
		}
		
		functionExpress = StringUtil.removeSpaces(functionExpress);
		String functionName = null;
		if(isManualFunction){
			functionName = functionExpress.substring(functionExpress.indexOf("name='") + 6,functionExpress.indexOf(",")-1);
		}else{
			functionName = functionExpress.substring(functionExpress.indexOf("name='") + 6,functionExpress.lastIndexOf("'"));
		}
		
		
		Matcher funcEndMatcher = endOfFunctionPattern.matcher(pageObjTxt);
		if(funcEndMatcher.find()){
			String functionAnnotate = funcEndMatcher.toMatchResult().group();
			//googlePO2.login1(nameParam1 )
			functionBody = functionBody.replace("@endFunction",							
					(pageObjVariable+"."+functionName+"("+parameters+");\n"+functionAnnotate));
			
			
		}
		
		
		/*System.out.println(functionBody);
		System.out.println(functionName);
		System.out.println(functionBody);*/
		functionBody = functionBody.replaceAll(functionStartPattern.pattern(), "");
		functionBody = functionBody.replaceAll(endOfFunctionPattern.pattern(), "");
		functionBody = functionBody.replaceAll(emptyLinePattern.pattern(), "");
		
		System.out.println(functionBody);
		return functionBody;
		
	}
	
	private String buildParameterList(final String parameters){
		String[] parameterList = parameters.split(",");
		StringBuilder parameterBuilder = new StringBuilder();
		if(parameterList != null && parameterList.length > 0){
			for(int index = 0 ; index < parameterList.length ; ++index){
				if(index > 0){
					parameterBuilder.append(",");
				}
				parameterBuilder.append("String");
			}
		}
		
		return parameterBuilder.toString();
	}
	
	private void constructFieldsAndFunctions(final StringBuilder fileContent,final String pageClassName,final StringBuilder javaMethod,
			String pageFunctionSignature)throws Exception {

		Matcher fieldMatcher = elementNamePattern.matcher(fileContent);
		String lastField = returnLastMatch(fieldMatcher);

		int nextElementNo = ELEMENT_START_NO;
		String nextElement = "";
		if (lastField != null) {
			// TODO need to verify
			// get last number
			int lastIndex = fileContent.lastIndexOf(lastField);
			String lastNo = lastField.substring(lastField.indexOf(FIELD_NAME_CONSTANT) + FIELD_NAME_CONSTANT.length(),lastField.length());
			nextElementNo = Integer.parseInt(lastNo) + 1;
			nextElement = FIELD_DECLARATION_CONSTANT + nextElementNo + " = ";

		} else {

			nextElement = FIELD_DECLARATION_CONSTANT + nextElementNo + " = ";
		}

		// extract variables/elements
		Matcher elementExtractMatcher = functExpLinePattern.matcher(javaMethod.toString());
		while (elementExtractMatcher.find()) {
			// describer.findElementExplicitWait( driver ,
			// By.id("lst-ib")).clear();
			String elementExpression = elementExtractMatcher.toMatchResult().group();
			Matcher elementMatcher = findElementPattern.matcher(elementExpression);
			if (elementMatcher.find()) {
				// By.id("lst-ib")
				String element = elementMatcher.toMatchResult().group();

				if (TestMethodsHandler.fieldsPerPageObject.containsKey(pageClassName)) {
					Map<String, Set<String>> fieldsMap = TestMethodsHandler.fieldsPerPageObject.get(pageClassName);
					if (fieldsMap.containsKey(element)) {
						Set<String> elementSet = fieldsMap.get(element);
						if (elementSet.size() > 1) {
							throw new Exception(" Invalid mapping between expression and field name");
						}

						String fieldName = elementSet.iterator().next();
						
						javaMethod.replace(javaMethod.indexOf(element), javaMethod.indexOf(element) + element.length(),fieldName);

					} else {
						Set<String> elementSet = new HashSet<String>();
						elementSet.add(FIELD_NAME_CONSTANT + nextElementNo);
						fieldsMap.put(element, elementSet);
						String field = FIELD_DECLARATION_CONSTANT + nextElementNo + " = " + element + ";";
						// append field to pageobject fileContent.append(c)
						insertFields(fileContent, nextElementNo, pageClassName, field);
						
						/*javaMethod.replace(javaMethod.indexOf(element), javaMethod.indexOf(element) + element.length(),
								FIELD_NAME_CONSTANT + nextElementNo);*/
						replaceElementWithConstant(javaMethod, element, nextElementNo);
						++nextElementNo;
					}
				} else {
					Map<String, Set<String>> fieldsMap = new HashMap<String, Set<String>>();
					Set<String> elementSet = new HashSet<String>();
					elementSet.add(FIELD_NAME_CONSTANT + nextElementNo);
					fieldsMap.put(element, elementSet);
					TestMethodsHandler.fieldsPerPageObject.putIfAbsent(pageClassName, fieldsMap);
					String field = FIELD_DECLARATION_CONSTANT + nextElementNo + " = " + element + ";";
					// append field to pageobject fileContent.append(c)
					insertFields(fileContent, nextElementNo, pageClassName, field);
					//element == By.id("lst-ib")
					/*javaMethod.replace(javaMethod.indexOf(element), javaMethod.indexOf(element) + element.length(),
							FIELD_NAME_CONSTANT + nextElementNo);*/
					replaceElementWithConstant(javaMethod, element, nextElementNo);
					++nextElementNo;
				}

			}
		}

		if(!newlyCreatedFunctions.contains(pageFunctionSignature)){
			int indexOfClassEnd = fileContent.lastIndexOf("}");
			String insertingString = "\n\t"+javaMethod.toString()+"\n}";
			//insert new function to the end of the class body
			fileContent.replace(indexOfClassEnd, indexOfClassEnd+insertingString.length(),insertingString);
			//fileContent.append("\n").append(javaMethod.toString());
		}
		
	}
	
	private void replaceElementWithConstant(StringBuilder javaMethod, String element, int nextElementNo){
		javaMethod.replace(javaMethod.indexOf(element), javaMethod.indexOf(element) + element.length(),
				FIELD_NAME_CONSTANT + nextElementNo);
		++nextElementNo;
	}
	
	
	private void insertFields(final StringBuilder page, int nextElementNo, final String pageClassName, final String field){
		
		if(nextElementNo == ELEMENT_START_NO){
			//no feels added yet
			int bodyStart = page.indexOf("{");
			page.replace(bodyStart, bodyStart+1, ("{\n"+field));
			
		}else{
			Matcher fieldMatcher = findFieldPattern.matcher(page);
			String lastField = returnLastMatch(fieldMatcher);
			int lastIndex = page.lastIndexOf(lastField);
			page.replace(lastIndex, lastIndex+lastField.length()+1,("\n"+lastField+field+"\n"));
		}
		
	}
	
	private String returnLastMatch(final Matcher matcher){
		String matchingString = null;
		while (matcher.find()) {
		    matchingString = matcher.group();
		} 		
		return matchingString;
	}
	
	/*private boolean getReplaceValue(final String expression){
		//extract replace condition
		boolean replace = false;
		Matcher replaceMatcher = replacePattern.matcher(expression);
		while (replaceMatcher.find()) {
			String replaceExpress = replaceMatcher.toMatchResult().group();
			String repalceValue = replaceExpress.substring(replaceExpress.indexOf("replace=")+8,replaceExpress.lastIndexOf(")"));
			replace = Boolean.parseBoolean(repalceValue);
		}
		
		return replace;
	}*/
	
	public List<String> addFunctionsToTest(final String testClass) {

		List<String> testMethods = new ArrayList<String>();
		String finalOutput = testClass;
		finalOutput = finalOutput.replaceAll(functExpLinePattern.pattern(), "");
		String nameConstant = "name=";
		String manualClassConstant = "manualClass=";

		int indexOfTest = 0;
		int indexOfPage = 0;
		String[] testCases = finalOutput.split("@Test");
		for(String test : testCases){
			if(indexOfTest == 0){
				++indexOfTest;
				continue;
			}
			
			Set<String> pageObjInit = new HashSet<String>();
			
			
			System.out.println("**************************** ");
			Matcher pageObjMatcher = pageObjectPattern.matcher(test);
			StringBuilder testConstruct = new StringBuilder();
			while (pageObjMatcher.find()) {
				
				MatchResult pageResult = pageObjMatcher.toMatchResult();
				indexOfPage = pageResult.start();
				// @pageobject(name='GooglePO1',replace=true)
				String pageObjAnnotate = pageResult.group();
				pageObjAnnotate = StringUtil.removeSpaces(pageObjAnnotate);
				
				String pageObjName = pageObjAnnotate.substring(pageObjAnnotate.indexOf(nameConstant)+nameConstant.length(),
						pageObjAnnotate.indexOf(")"));
				// googlePO2.login1(nameParam1 )
				pageObjName = pageObjName.replaceAll("'", "");
				
				
				System.out.println("=========================== "+indexOfPage);
				String endPage = "@endPage";
				String pageObjTxt = test.substring(indexOfPage, test.indexOf(endPage, indexOfPage));
				
				
				
				String firstLetter = pageObjName.substring(0, 1);
				String pageObjVariable = pageObjName.replaceFirst(firstLetter, firstLetter.toLowerCase());
				// GooglePO2 googlePO2 = new GooglePO2(driver,describer);
				String objectConstruct = pageObjName + " " + pageObjVariable + "= new " + pageObjName
						+ "(driver,describer);";
				objectConstruct = objectConstruct.replaceAll("\\s*//\\s*", "");
				if(!pageObjInit.contains(pageObjName)){
					testConstruct.append("\n"+objectConstruct);
					pageObjInit.add(pageObjName);
				}
				
				//add between @page to @function: driver.get(baseUrl + "");
				String otherLines = pageObjTxt.substring(pageObjTxt.indexOf(")")+1, pageObjTxt.indexOf("@function"));
				testConstruct.append("\n").append(otherLines).append("\n");
				
				//add functions
				Matcher functionMatcher = functionStartPattern.matcher(pageObjTxt);
				while(functionMatcher.find()){					
					String funcBody = constructFunctionCall(functionMatcher, functionStartPattern.pattern(), pageObjTxt, pageObjVariable,
							testConstruct,pageObjInit);
					testConstruct.append("\n").append(funcBody);
				}

			}
			
			testConstruct = new StringBuilder(testConstruct.toString().replaceAll(emptyLinePattern.pattern(), ""));
			testMethods.add(testConstruct.toString());
			++indexOfTest;
			
		}

		return testMethods;
	}
}
