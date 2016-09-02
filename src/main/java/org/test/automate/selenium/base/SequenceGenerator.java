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
import java.util.Calendar;

public class SequenceGenerator {

	private static long initialValue = 0;
	private static Calendar cal = Calendar.getInstance();
	private static String prefix = "" + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DATE)
			+ cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE);
	private static long currentValue;

	public synchronized static long getInitialValue() {

		return initialValue;
	}
	
	public synchronized static void setInitialValue(long value){
		initialValue = value;
	}

	public synchronized static long increment(long current) {
		currentValue = current;
		return ++currentValue;
	}
	
	public synchronized static String getPrefix(){
		return prefix;
	}

	public static String createId(String prefix, long currentTime, int maximumLength) {

		StringBuffer id = new StringBuffer();
		if (prefix != null) {
			id.append(prefix).append(currentTime);
		}

		String createdId = id.toString();
		if (id.length() > maximumLength) {
			createdId = id.substring(0, maximumLength);
		} else if (id.length() < maximumLength) {
			createdId = createId(createdId, currentTime, maximumLength);
		}
		return createdId;
	}

	public static void main(String... arg) throws Exception {
		long currentTime = System.nanoTime();
		System.out.println(currentTime);
		Calendar cal = Calendar.getInstance();
		String imei = createId(prefix,getInitialValue(), 15);
		System.out.println(imei);
		long id = Long.parseLong(imei);
		id = increment(id);
		System.out.println(id);
		
		FileHandler fileHandler = FileHandler.getInstance();
		
		FileHandler.writingToFile("D:/dhanuka/dev/workspaces/mentanance/eb-selenium-ide-automate/src/main/config/autogenerate.txt",""+id);
		InputStream is = FileHandler.getResourceAsStream("D:/dhanuka/dev/workspaces/mentanance/eb-selenium-ide-automate/src/main/config/autogenerate.txt");
		String read = fileHandler.readInputStream(is);
		System.out.println(read);
	}

}
