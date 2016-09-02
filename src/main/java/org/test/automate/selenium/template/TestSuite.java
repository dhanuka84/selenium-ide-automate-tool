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
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


//@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="TestSuite")
public class TestSuite  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
    private List<TestCase> testCases;
	
    
	private List<Parameter> parameters;
	
	
    private String id;
    
	private boolean useRC;

	@XmlElement(name ="TestCase")
	public List<TestCase> getTestCases() {
		
		/*if(testCases == null){
			testCases = Collections.EMPTY_LIST;
		}*/
		return testCases;
	}

	@XmlAttribute
	public String getId() {
		return id;
	}

	@XmlElement(name ="Parameter")
	public List<Parameter> getParameters() {
		
		/*if(parameters == null){
			parameters = Collections.EMPTY_LIST;
		}*/
		return parameters;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	@XmlAttribute
	public boolean isUseRC() {
		return useRC;
	}
	public void setUseRC(boolean useRC) {
		this.useRC = useRC;
	}


	

	
}
