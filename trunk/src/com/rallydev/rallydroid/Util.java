/*
 * Copyright 2009 Rally Software Development
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.rallydev.rallydroid;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	public static String slurp (InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public static String trim(String string, int maxLength) {
		if(string.length() > maxLength) {
			String substring = string.substring(0,maxLength-3);
			return substring + "...";
		} else {
			return string;
		}
	}
	
	public static String trim(String string) {
		return trim(string, 100);
	}
	
	public static String stripHTML(String htmlString) {
      String nonHtmlString = htmlString.replaceAll("\\<.*?\\>", "");
      nonHtmlString = nonHtmlString.replaceAll("<br/>", "\n");
      nonHtmlString = nonHtmlString.replaceAll("&#39;", "\'");
      nonHtmlString = nonHtmlString.replaceAll("&quot;", "\"");
      nonHtmlString = nonHtmlString.replaceAll("&nbsp;", " ");      
      return nonHtmlString;
  }

	public static Date utcToDate(String utcString) throws ParseException {
		String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.parse(utcString);
	}
	
}