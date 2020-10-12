package com.dthinh.tool.pantos.framework;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dthinh.tool.pantos.entity.Screen;
import com.dthinh.tool.pantos.utils.FileUtils;
import com.dthinh.tool.pantos.utils.RegexUtils;
import com.dthinh.tool.pantos.utils.StringUtils;

public class ScreenFramework {
	public static String MAIN_DIRS = "C:\\Users\\thinh.tran\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\ldp";
	public static String SYS_DIRS = "C:\\Users\\thinh.tran\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\ldp\\eusu.clt.syscommon";
	public static boolean isInitial = true;

	public static void run(Screen sc) throws IOException, URISyntaxException {
		step1(sc);
		step2(sc);
	}

	public static void step1(Screen sc) throws IOException, URISyntaxException {
		searchOMM(sc);
		readOMMAndWriteOMMAndDto(sc);
		searchSvcAndOperationInHTML(sc);
		searchSvcAndWriteSvc(sc);

		FileUtils.copyFilesToDirPath(sc.getHTMLPath(), sc.getTargetPath() + "\\html\\original");
		FileUtils.writeStringToFile(
				sc.getTargetPath() + "\\html\\" + Paths.get(sc.getHTMLPath()).getFileName().toString(),
				StringUtils.formatHTML(sc));
	}

	public static void step2(Screen sc) throws IOException, URISyntaxException {
		searchBizAndWriteBiz(sc);
		searchDaoAndWriteDao(sc);
		searchDBIOAndWriteDBIO(sc);
	}

	public static List<Path> searchOMM(Screen sc) throws IOException {
		searchOMMNameInHTML(sc);
		List<String> ommNameList = sc.getOmmNameList();
		List<Path> ommFilePathList = new ArrayList<Path>();
		ommFilePathList.addAll(FileUtils.findOMMFiles(MAIN_DIRS, ommNameList, 1));
		ommFilePathList.addAll(FileUtils.findOMMFiles(SYS_DIRS, ommNameList, Integer.MAX_VALUE));
		sc.setOmmFilePathList(ommFilePathList);

		StringUtils.removeDuplicate(sc.getOmmNameList());
		if (isInitial) {
			sc.getOmmFilePathList().addAll(searchChildOMMInOMM(sc));
			StringUtils.removePathDuplicate(sc.getOmmFilePathList());
		}

		return ommFilePathList;
	}

	public static List<String> searchOMMNameInHTML(Screen sc) {
		String txt = FileUtils.readFileToString(sc.getHTMLPath());
		Matcher m = RegexUtils.matcher(txt, "\\b(\\w*omms?)\\b", Pattern.CASE_INSENSITIVE);
		List<String> ommNameList = new ArrayList<String>();
		if (isInitial)
			sc.setOmmNameList(ommNameList);
		while (m.find()) {
			String g = m.group();
			if ("comm".equals(g))
				continue;
			if ("ds".toUpperCase().equals(g.toUpperCase().substring(0, 2))) {
				g = g.substring(2, g.length());
			}
			sc.getOmmNameList().add(g);
		}
		StringUtils.removeDuplicate(sc.getOmmNameList());
		return ommNameList;
	}

	public static void searchSvcAndOperationInHTML(Screen sc) {
		if (isInitial) {
			String txt = FileUtils.readFileToString(sc.getHTMLPath());
			Matcher m = RegexUtils.matcher(txt, "\"service\":\"(.*)\",");
			List<String> svcNameList = new ArrayList<String>();
			sc.setSvcNameList(svcNameList);
			while (m.find()) {
				String g = m.group(1);
				sc.getSvcNameList().add(g);
			}
			StringUtils.removeDuplicate(sc.getSvcNameList());

			m = RegexUtils.matcher(txt, "\"operation\":\"(.*)\"");
			List<String> svcMethodList = new ArrayList<String>();
			sc.setSvcMethodList(svcMethodList);
			while (m.find()) {
				String g = m.group(1);
				sc.getSvcMethodList().add(g);
			}
			StringUtils.removeDuplicate(sc.getSvcMethodList());
		}
	}

	public static List<Path> searchChildOMMInOMM(Screen sc) throws IOException {
		List<Path> ommFilePathList = sc.getOmmFilePathList();
		List<Path> additionalOMMList = new ArrayList<Path>();

		for (int i = 0; i < ommFilePathList.size(); i++) {
			Path p = ommFilePathList.get(i);
			String ommContent = FileUtils.readFileToString(p.toString());
			Matcher m = RegexUtils.matcher(ommContent, "	.*\\.(\\w*omms?) ", Pattern.CASE_INSENSITIVE);
			while (m.find()) {
				String g = m.group(1);
				List<String> tempArr = new ArrayList<String>();
				tempArr.add(g);
				additionalOMMList.addAll(FileUtils.findOMMFiles(MAIN_DIRS, tempArr, Integer.MAX_VALUE));
			}
		}
		return additionalOMMList;
	}

	public static void readOMMAndWriteOMMAndDto(Screen sc) throws IOException {
		List<String> dtoNameList = new ArrayList<String>();
		List<Path> dtoFilePathList = new ArrayList<Path>();
		List<Path> ommFilePathList = sc.getOmmFilePathList();
		String target = sc.getTargetPath() + "\\dtos";
		for (Path p : ommFilePathList) {
			String ommContent = FileUtils.readFileToString(p.toString());
			try {
				Path dtoPath = FileUtils.writeDto(p.getFileName().toString(), ommContent, target);
				dtoNameList.add(dtoPath.getFileName().toString().replaceAll("\\.java", ""));
				dtoFilePathList.add(dtoPath);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		sc.setDtoFilePathList(dtoFilePathList);
		StringUtils.removeDuplicate(dtoNameList);
		sc.setDtoNameList(dtoNameList);
		FileUtils.copyFilesToDirPath(sc.getOmmFilePathList(), target + "\\original");
	}

	public static void searchSvcAndWriteSvc(Screen sc) throws URISyntaxException, IOException {
		String target = sc.getTargetPath() + "\\service";
		List<Path> filePathList = FileUtils.findSvcFiles(ScreenFramework.MAIN_DIRS, sc.getSvcNameList());
		sc.setSvcFilePathList(filePathList);

		List<String> bizNameList = new ArrayList<String>();
		if (isInitial) {
			sc.setBizNameList(bizNameList);
			sc.getBizMethodList().addAll(sc.getSvcMethodList());
		}
		for (Path p : filePathList) {
			String contents = FileUtils.searchBlockMethodInSvc(p.toString(), sc.getSvcMethodList(), bizNameList);
			String name = p.getFileName().toString();
			name = name.substring(0, name.length() - 5);
			FileUtils.writeController(name, contents, target);
		}
		FileUtils.copyFilesToDirPath(filePathList, target + "\\original");
	}

	public static void searchBizAndWriteBiz(Screen sc) throws URISyntaxException, IOException {
		String target = sc.getTargetPath() + "\\biz";
		List<Path> filePathList = FileUtils.findBizFiles(ScreenFramework.MAIN_DIRS, sc.getBizNameList());
		sc.setBizFilePathList(filePathList);
		List<String> daoNameList = new ArrayList<String>();
		if (isInitial) {
			sc.setDaoNameList(daoNameList);
			sc.getDaoMethodList().addAll(sc.getBizMethodList());
		}
		for (Path p : filePathList) {
			String contents = FileUtils.searchBlockMethodInBiz(p.toString(), sc.getBizMethodList(), daoNameList);
			String name = p.getFileName().toString();
			name = name.substring(0, name.length() - 5);
			FileUtils.writeBiz(name, contents, target);
		}
		FileUtils.copyFilesToDirPath(filePathList, target + "\\original");
	}

	public static void searchDaoAndWriteDao(Screen sc) throws URISyntaxException, IOException {
		String target = sc.getTargetPath() + "\\mybatis";
		List<Path> filePathList = FileUtils.findDaoFiles(ScreenFramework.MAIN_DIRS, sc.getDaoNameList());
		sc.setDaoFilePathList(filePathList);
		List<String> dbioNameList = new ArrayList<String>();
		if (isInitial) {
			sc.setDbioNameList(dbioNameList);
		}
		for (Path p : filePathList) {
			String contents = FileUtils.searchBlockMethodInDao(p.toString(), sc.getDaoMethodList(), dbioNameList);
			String name = p.getFileName().toString();
			name = name.substring(0, name.length() - 5);
			FileUtils.writeDao(name, contents, target);
		}
		FileUtils.copyFilesToDirPath(filePathList, target + "\\original");
	}

	public static void searchDBIOAndWriteDBIO(Screen sc) throws URISyntaxException, IOException {
		String target = sc.getTargetPath() + "\\mybatis";
		List<Path> filePathList = FileUtils.findDBIOFiles(ScreenFramework.MAIN_DIRS, sc.getDbioNameList());
		sc.setDbioFilePathList(filePathList);
		for (Path p : filePathList) {
			String dbioContents = FileUtils.searchSQLInDBIO(p.toString(), sc.getDbioMethodList());
			String dbioName = p.getFileName().toString();
			dbioName = dbioName.substring(0, dbioName.length() - 5);
			FileUtils.writeDbio(dbioName, dbioContents, target);
		}
		FileUtils.copyFilesToDirPath(filePathList, target + "\\original");
	}
}
