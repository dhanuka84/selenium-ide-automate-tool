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
@XmlRootElement(name="TestCase")
public class TestCase  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
    private String id;
	
    
	private List<Parameter> parameters;

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

	public void setId(String id) {
		this.id = id;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	

}
