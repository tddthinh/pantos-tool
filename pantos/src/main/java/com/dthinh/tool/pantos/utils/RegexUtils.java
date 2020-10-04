package com.dthinh.tool.pantos.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	public static Matcher matcher(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher;
	}
	public static Matcher matcher(String str, String regex, int flags) {
		Pattern pattern = Pattern.compile(regex,flags);
		Matcher matcher = pattern.matcher(str);
		return matcher;
	}
	
}
