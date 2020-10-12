package com.dthinh.tool.pantos.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;

import com.dthinh.tool.pantos.entity.Screen;


public class StringUtils {
	public static void removeDuplicate(List<String> strs) {
		TreeSet<String> set = new TreeSet<String>();
		for (String s : strs) {
			set.add(s);
		}
		strs.clear();
		strs.addAll(set);
	}

	public static void removePathDuplicate(List<Path> strs) {
		TreeSet<String> set = new TreeSet<String>();
		for (Path s : strs) {
			set.add(s.toString());
		}
		strs.clear();
		for (String s : set) {
			strs.add(Paths.get(s));
		}
	}

	public static String convertOMMVarToDtoVar(String ommVar) {
		String dtoVar;
		if (ommVar.toUpperCase().endsWith("OMM")) {
			dtoVar = ommVar.substring(0, ommVar.length() - 3) + "Dto";
		} else if (ommVar.toUpperCase().endsWith("OMMS")) {
			dtoVar = ommVar.substring(0, ommVar.length() - 4) + "ListDto";
		} else {
			dtoVar = ommVar;
		}
		return dtoVar;
	}

	public static String formatHTML(Screen sc) {
		String content = formatHTMLOMMSyntax(sc);
		content = formatHTMLMsgbox(sc);
		content = formatHTMLOMMSyntax(sc);
		return content;

	}

	public static String formatHTMLOMMSyntax(Screen sc) {
		String content = FileUtils.readFileToString(sc.getHTMLPath());
		for (String ommStr : sc.getOmmNameList()) {
			if ("comm".equals(ommStr))
				continue;
			String dtoStr = convertOMMVarToDtoVar(ommStr);
			content = content.replaceAll(ommStr, dtoStr);
		}
		return content;
	}

	public static String formatHTMLMsgbox(Screen sc) {
		String content = FileUtils.readFileToString(sc.getHTMLPath());
		Matcher m = RegexUtils.matcher(content, "(onsite\\.messageBox\\(.*\\))");
		while (m.find()) {
//			String msgbox = m.group();
//			int i = msgbox.indexOf('(');
//			msgbox.substring(0, i)+""
		}
		return content;
	}

	public static String extractOMMFields(String ommContent) {
		StringBuilder sb = new StringBuilder();
		Matcher m = RegexUtils.matcher(ommContent, "(?:\\t|\\.)(\\w*) (\\w*)<");
		while (m.find()) {
			String type = convertOMMVarToDtoVar(m.group(1));
			String name = convertOMMVarToDtoVar(m.group(2));
			sb.append("\t private " + type + " " + name + ";\n");
		}
		return sb.toString();
	}
}
