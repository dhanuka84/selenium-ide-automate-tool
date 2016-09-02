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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum BrowserType {

	FIREFOX("firefox"), GOOGLECHROME("googlechrome"), SAFARI("safari"), OPERA("opera"), IEXPLORE("iexplore"), IEXPLORE_PROXY(
			"iexploreproxy"), SAFARI_PROXY("safariproxy"), CHROME("chrome"), KONQUEROR("konqueror"), MOCK("mock"), IE_HTA(
			"iehta"), ANDROID("android"), HTMLUNIT("htmlunit"), IE("internet explorer"), IPHONE("iPhone"), IPAD("iPad"), PHANTOMJS(
			"phantomjs"), UNKNOWN(""),LOCAL("local"),REMOTE("remote");

	private final String name;

	private static final Map<String, BrowserType> nameMap = new HashMap<String, BrowserType>();

	static {
		for (BrowserType s : EnumSet.allOf(BrowserType.class)) {

			nameMap.put(s.getName(), s);
		}
	}

	private BrowserType(String name) {
		this.name = name;

	}

	public String getName() {
		return name;
	}

	public static BrowserType getType(String name) {
		if (nameMap.containsKey(name))
			return nameMap.get(name);
		else
			return UNKNOWN;
	}
}
