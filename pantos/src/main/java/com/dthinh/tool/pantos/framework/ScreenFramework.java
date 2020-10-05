package com.dthinh.tool.pantos.framework;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dthinh.tool.pantos.entity.Screen;
import com.dthinh.tool.pantos.utils.FileUtils;
import com.dthinh.tool.pantos.utils.RegexUtils;
import com.dthinh.tool.pantos.utils.StringUtils;

public class ScreenFramework {
	public static String OMM_DIRS = "C:\\Users\\thinh.tran\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\ldp";
	public static String SYS_DIRS = "C:\\Users\\thinh.tran\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\ldp\\eusu.clt.syscommon";

	public static void run(Screen sc, String targetPath) {
		String txt = FileUtils.readFileToString(sc.getHTMLPath());
		Matcher m = RegexUtils.matcher(txt, "\\b(\\w*omms?)\\b", Pattern.CASE_INSENSITIVE);
		List<String> ommNameList = new ArrayList<String>();
		while (m.find()) {
			String g = m.group();
			if ("ds".toUpperCase().equals(g.toUpperCase().substring(0, 2))) {
				g = g.substring(2, g.length());
			}
			ommNameList.add(g);
		}
		ommNameList.add("ReportAttatchInfoOMM");
		sc.setOmmNameList(ommNameList);

		StringUtils.removeDuplicate(ommNameList);

		List<Path> ommFilePathList = new ArrayList<Path>();
		ommFilePathList.addAll(FileUtils.findOMMFiles(OMM_DIRS, ommNameList, 1));
		ommFilePathList.addAll(FileUtils.findOMMFiles(SYS_DIRS, ommNameList, Integer.MAX_VALUE));
		sc.setOmmFilePathList(ommFilePathList);

		String ommTarget = targetPath + "\\omms\\";
		try {
			FileUtils.copyFilesToDirPath(ommFilePathList, ommTarget);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<String> dtoNameList = new ArrayList<String>();
		List<Path> dtoFilePathList = new ArrayList<Path>();
		String dtoTarget = targetPath + "\\dtos\\";
		for (Path p : ommFilePathList) {
			String ommContent = FileUtils.readFileToString(p.toString());
			try {
				Path dtoPath = FileUtils.writeDto(p.getFileName().toString(), ommContent, dtoTarget);
				dtoNameList.add(dtoPath.getFileName().toString().replaceAll("\\.java", ""));
				dtoFilePathList.add(dtoPath);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		sc.setDtoFilePathList(dtoFilePathList);
		sc.setDtoNameList(dtoNameList);
		
		sc.setTargetPath(targetPath);
		
		System.out.println(StringUtils.replaceHTMLOMMWithDto(sc));
	}
}
