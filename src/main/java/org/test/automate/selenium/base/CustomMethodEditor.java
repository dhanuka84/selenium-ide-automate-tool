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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    private static Pattern pageObjectPattern = Pattern.compile("@pageobject\\(name='\\w+.+'(,replace=true|false)*\\)");
    private static Pattern functionPattern = Pattern.compile("@function\\(name='\\w+.+'(,replace=true|false)*\\)");
    private static Pattern replacePattern = Pattern.compile("(replace=true|false)+\\)");
    private static Pattern endOfPagePattern = Pattern.compile("//\\s*@\\s*endPage");
    private static Pattern endOfFunctionPattern = Pattern.compile("//\\s*@\\s*endFunction");
    private static Pattern parameterPattern = Pattern.compile("\\s*String\\s+.*\\s*=\\s*\".*\";\\n*");
    
    //By.id("lst-ib")
    private static Pattern findElementPattern = Pattern.compile("By.[a-zA-Z]+\\(\".*\"\\)");
    
    //private static final By ELEMENT
    private static Pattern elementNamePattern = Pattern.compile("\\s*private\\s+static\\s+final\\s+By\\s+ELEMENT\\d+");
    
    //describer.findElementExplicitWait( driver , By.id("lst-ib")).clear();
    static Pattern elementPattern = Pattern.compile("describer.findElementExplicitWait\\s*\\(.*\\);\\n*");
    
	private static Pattern findFieldPattern = Pattern
			.compile("\\s*private\\s+static\\s+final\\s+By\\s+ELEMENT\\d+\\s*=\\s*By.[a-zA-Z]+\\(\".*\"\\);\\n*");
    
    //constrants
    private static final String FIELD_NAME_CONSTANT = "ELEMENT";
    private static final String FIELD_DECLARATION_CONSTANT = "private static final By "+FIELD_NAME_CONSTANT;   
    
    private static final int ELEMENT_START_NO = 1;
    private static final String FIND_ELEMENT_CUSTOM_METHOD = "describer.findElementExplicitWait(";
    
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
    
	public String extractPageObject(final StringBuilder testClass, final String filePath) throws Exception {

		String finalPage = null;
		Matcher pageobjectMatcher = pageObjectPattern.matcher(testClass.toString());
		
		FileHandler fileHandler = FileHandler.getInstance();
		String pageObjTemplate = fileHandler.readInputStream(FileHandler.getResourceAsStream(Constants.PAGE_OBJECT_BASE_CLASS+".java"));

		while (pageobjectMatcher.find()) {
			boolean replacePageObject = false;
			String expression = pageobjectMatcher.toMatchResult().group();
			// extract all the lines related to this pageobject
			int indexOfExp = testClass.indexOf(expression);
			String pageTxt = testClass.substring(indexOfExp,testClass.indexOf("@endPage", indexOfExp));
			pageTxt = pageTxt.replaceAll(pageObjectPattern.pattern(), "");
			//System.out.println("***********************   " + pageTxt);

			// create pageobject file
			expression = StringUtil.removeSpaces(expression);
			// extract replace condition
			replacePageObject = getReplaceValue(expression);
			String pageClassName = expression.substring(expression.indexOf("name='") + 6, expression.lastIndexOf("'"));
			String exactFile = filePath + pageClassName + ".java";
			
			//create file if not exist or need to replace
			StringBuilder fileContent = new StringBuilder();
			
			if(replacePageObject || !FileHandler.isFileExist(exactFile)){
				/*String pkgClassDec = "package "+ Constants.PAGE_OBJECT_PACKAGE +
						";\n\nimport org.openqa.selenium.By;\n\n" + "public final class " + pageClassName + " {";*/
				String pkgClassDec = pageObjTemplate.replaceAll(Constants.PAGE_OBJECT_BASE_CLASS, pageClassName);
				
				//start of class body
				FileHandler.createAndUpdateFile(replacePageObject, (exactFile),(pkgClassDec));
			}
			
			//get file content
			fileContent.append(FileHandler.getFileContent(exactFile));
			
			
			

			// extract function
			Matcher functionMatcher = functionPattern.matcher(pageTxt);
			while (functionMatcher.find()) {
				boolean replaceFunction = false;
				String functionExpress = functionMatcher.toMatchResult().group();
				
				// extract all the lines/body related to this function
				int indexOfFuncExp = pageTxt.indexOf(functionExpress);
				String functionBody = pageTxt.substring(indexOfFuncExp,pageTxt.indexOf("@endFunction", indexOfFuncExp));
				functionBody = functionBody.replaceAll(functionPattern.pattern(), "");
				
				//find parameters
				Matcher parameterMatcher = parameterPattern.matcher(functionBody);
				StringBuilder parameters = new StringBuilder();
				while(parameterMatcher.find()){
					if(parameters.length() > 0){
						parameters.append(",");
					}
					String parameterExp = parameterMatcher.toMatchResult().group();
					parameterExp = parameterExp.trim();
					parameterExp = parameterExp.substring(0, parameterExp.indexOf("="));
					parameters.append(parameterExp);
				}
				
				
				functionExpress = StringUtil.removeSpaces(functionExpress);
				String functionName = functionExpress.substring(functionExpress.indexOf("name='") + 6,
						functionExpress.lastIndexOf("'"));
				replaceFunction = getReplaceValue(functionExpress);
				
				StringBuilder javaMethod = new StringBuilder();
				javaMethod.append(" public void "+functionName+"("+parameters.toString()+ "){\n");
				functionBody = functionBody.replaceAll(parameterPattern.pattern(), "");
				javaMethod.append(functionBody).append("\n }");
				
				
				
				if(!replacePageObject && replaceFunction){
					//TODO replace function
					//\s*public\s+\w+.+\s+(login1)\(\)\s*\{(.*\s*.*)*\}[^\s]
					
				}
				
				if(!replacePageObject && !replaceFunction){
					//TODO do not replace function
				}
				
				//create functions to pageobject mapping
				if(TestMethodsHandler.methodSignaturePerPO.containsKey(Constants.PAGE_OBJECT_PACKAGE+"."+pageClassName)){
					Set<String> funtions = TestMethodsHandler.methodSignaturePerPO.get(Constants.PAGE_OBJECT_PACKAGE+"."+pageClassName);
					funtions.add(functionName+"("+parameters.toString()+ ")");
				}else{
					Set<String> funtions = new HashSet<String>();
					funtions.add(functionName+"("+parameters.toString()+ ")");
					TestMethodsHandler.methodSignaturePerPO.putIfAbsent(Constants.PAGE_OBJECT_PACKAGE+"."+pageClassName, funtions);
				}
				//replace page object
				if(replacePageObject){	
					constructFieldsAndFunctions(fileContent, pageClassName, javaMethod);
					}else{
					//TODO
				}
				
				

			}
			
			if(replacePageObject){
				fileContent.append("\n }");//end of class body
				FileHandler.createAndUpdateFile(replacePageObject, (exactFile), fileContent.toString());
			}
			//edit test class
		}
		
		

		return finalPage;
	}
	
	private void constructFieldsAndFunctions(final StringBuilder fileContent, final String pageClassName,final StringBuilder javaMethod)
			throws Exception {

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
		Matcher elementExtractMatcher = elementPattern.matcher(javaMethod.toString());
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
						
						javaMethod.replace(javaMethod.indexOf(element), javaMethod.indexOf(element) + element.length(),
								FIELD_NAME_CONSTANT + nextElementNo);
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
					
					javaMethod.replace(javaMethod.indexOf(element), javaMethod.indexOf(element) + element.length(),
							FIELD_NAME_CONSTANT + nextElementNo);
					++nextElementNo;
				}

			}
		}

		fileContent.append("\n").append(javaMethod.toString());
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
	
	private boolean getReplaceValue(final String expression){
		//extract replace condition
		boolean replace = false;
		Matcher replaceMatcher = replacePattern.matcher(expression);
		while (replaceMatcher.find()) {
			String replaceExpress = replaceMatcher.toMatchResult().group();
			String repalceValue = replaceExpress.substring(replaceExpress.indexOf("replace=")+8,replaceExpress.lastIndexOf(")"));
			replace = Boolean.parseBoolean(repalceValue);
		}
		
		return replace;
	}
	
	public String addFunctionsToTest(final String testClass){
		
		String finalOutput = testClass;
		finalOutput = finalOutput.replaceAll(elementPattern.pattern(), "");
		Set<Entry<String, Set<String>>> entries = TestMethodsHandler.methodSignaturePerPO.entrySet();
		Iterator<Entry<String, Set<String>>> iter = entries.iterator();
		
		while(iter.hasNext()){
			Entry<String, Set<String>> iterNext = iter.next();
			//org.test.automate.selenium.testng.pageobject.GooglePO2
			String pageObjectName = iterNext.getKey();
			String pageObjectClassName = pageObjectName.substring(pageObjectName.lastIndexOf(".")+1, pageObjectName.length());
			String firstLetter = pageObjectClassName.substring(0, 1);
			String pageObjVariable = pageObjectClassName.replaceFirst(firstLetter, firstLetter.toLowerCase());
			//GooglePO2 googlePO2 = new GooglePO2(driver,describer);
			String objectConstruct = pageObjectClassName +" "+pageObjVariable+"= new "+pageObjectClassName+"(driver,describer);";
			String pageObjPattern = "@pageobject\\(name='"+pageObjectClassName+"'(,replace=true|false)*\\)";
			Matcher pageObjMatcher = Pattern.compile(pageObjPattern).matcher(finalOutput);
			if(pageObjMatcher.find()){
				// @pageobject(name='GooglePO1',replace=true)
				String pageObjAnnotate = pageObjMatcher.group();
				//googlePO2.login1(nameParam1 )
				finalOutput = finalOutput.replaceFirst(pageObjPattern,							
						(pageObjAnnotate+"\n\n"+objectConstruct));
			}
			
			Set<String> methtodSignatures = iterNext.getValue();
			
			for(String method : methtodSignatures){
				//login1(String nameParam1 )
				String methodName = method.substring(0, method.indexOf("("));
				// @function(name='login1',replace=true)
				//String functionPattern = "@function\\(name='"+methodName+"'(,replace=true|false)*\\)";
				String functionPattern = "//\\s*@endFunction\\(name='"+methodName+"'\\)";
				Matcher funcAnnoMatcher = Pattern.compile(functionPattern).matcher(finalOutput);
				if(funcAnnoMatcher.find()){
					String functionAnnotate = funcAnnoMatcher.group();
					//googlePO2.login1(nameParam1 )
					finalOutput = finalOutput.replaceFirst(functionPattern,							
							(pageObjVariable+"."+method.replaceAll("String\\s+", "")+";")+"\n"+functionAnnotate);
				}
				
			}
		}

		return finalOutput;
	} 
}
