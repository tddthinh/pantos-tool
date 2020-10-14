package com.dthinh.tool.pantos.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileUtils {
	public static final int READ_AHEAD_LIMIT = 100000;

	public static String readFileToString(String filepath) {
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(filepath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static void writeStringToFile(String filepath, String str) {
		try {
			Files.write(Paths.get(filepath), str.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Path> findOMMFiles(String dirPath, List<String> nameList, int depthFindDirs) throws IOException {
//		List<Path> ommFilePathList = new ArrayList<Path>();
//
//		try {
//			List<Path> ommDirPathList = Files.find(Paths.get(dirPath), depthFindDirs,
//					(filePath, fileAttr) -> fileAttr.isDirectory() && filePath.getFileName().toString().endsWith("omm"))
//					.collect(Collectors.toList());
//			for (Path p : ommDirPathList) {
//				ommFilePathList
//						.addAll(Files
//								.find(Paths.get(p.toString()), Integer.MAX_VALUE,
//										(filePath, fileAttr) -> fileAttr.isRegularFile()
//												&& filePath.getFileName().toString().endsWith("omm"))
//								.filter(filePath -> {
//									boolean matched = false;
//									for (String ommName : ommNameList) {
//										if ((ommName + ".omm").equalsIgnoreCase(filePath.getFileName().toString()))
//											matched = true;
//									}
//									return matched;
//								}).collect(Collectors.toList()));
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return findFiles(dirPath, nameList, "omm", null, "omm", Integer.MAX_VALUE);
	}

	public static void copyFilesToDirPath(List<Path> pathList, String dirPath) throws IOException {
		Path target = Paths.get(dirPath);
		File dir = target.toFile();
		dir.mkdirs();
		for (Path source : pathList) {
			Files.copy(source, Paths.get(target.toString(), source.getFileName().toString()),
					StandardCopyOption.REPLACE_EXISTING);
		}

	}

	public static void copyFilesToDirPath(String filePath, String dirPath) throws IOException {
		Path target = Paths.get(dirPath);
		File dir = target.toFile();
		dir.mkdirs();
		Path source = Paths.get(filePath);
		Files.copy(source, Paths.get(target.toString(), source.getFileName().toString()),
				StandardCopyOption.REPLACE_EXISTING);

	}

	public static Path writeDto(String ommName, String ommContent, String dirPath) throws URISyntaxException {
		if (ommName.endsWith(".omm"))
			ommName = ommName.substring(0, ommName.length() - 4);
		String templateName;
		if (ommName.endsWith("CondOMM")) {
			templateName = "dto-cond-template.txt";
		} else if (ommName.endsWith("GrpOMM")) {
			templateName = "dto-grp-template.txt";
		} else if (ommName.endsWith("ListOMM")) {
			templateName = "dto-list-template.txt";
		} else {
			templateName = "dto--template.txt";
		}
		String templateContent = readFileToString(getFileFromResource(templateName).getAbsolutePath());
		String dtoName = ommName.substring(0, ommName.length() - 3) + "Dto";
		String fields = StringUtils.extractOMMFields(ommContent);
		String dtoContent = templateContent.replaceAll("dtoClassName", dtoName).replaceAll("dtoFields", fields);
		(new File(dirPath)).mkdirs();
		String dtoPath = dirPath + "\\" + dtoName + ".java";
		writeStringToFile(dtoPath, dtoContent);
		return Paths.get(dtoPath);
	}

	public static Path writeDbio(String dbioName, String dbioContent, String dirPath) throws URISyntaxException {
		return writeBasedOnTemplate("mybatis-xml-template.txt", dbioName + ".xml", dbioContent, dirPath);
	}

	public static Path writeController(String svcName, String svcContent, String dirPath) throws URISyntaxException {
		return writeBasedOnTemplate("ctl-template.txt", svcName + ".java", svcContent, dirPath);
	}

	public static Path writeBiz(String bizName, String bizContent, String dirPath) throws URISyntaxException {
		return writeBasedOnTemplate("biz-template.txt", bizName + ".java", bizContent, dirPath);
	}

	public static Path writeDao(String daoName, String daoContent, String dirPath) throws URISyntaxException {
		return writeBasedOnTemplate("dao-template.txt", daoName + ".java", daoContent, dirPath);
	}

	public static Path writeBasedOnTemplate(String templateName, String targetFileNameAndExtension, String contents,
			String dirPath) throws URISyntaxException {
		String dbioPath = dirPath + "\\" + targetFileNameAndExtension;
		String templateContent = readFileToString(getFileFromResource(templateName).getAbsolutePath());
		String TargetContent = templateContent.replace("contents", contents);
		(new File(dirPath)).mkdirs();

		writeStringToFile(dbioPath, TargetContent);
		return Paths.get(dbioPath);
	}

	public static File getFileFromResource(String fileName) {
		return new File("src/main/resources/" + fileName);
	}

	public static String searchUsingBufferedReader(String filePath, String searchQuery) throws IOException {
		searchQuery = searchQuery.trim();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(searchQuery)) {
					return line;
				} else {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String searchSQLInDBIO(String filePath, List<String> searchQueryList) throws IOException {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				for (String searchQuery : searchQueryList) {
					if (line == null)
						break;
					if (line.contains(searchQuery.trim())) {
						sb.append(line + "\n");
						Matcher m = RegexUtils.matcher(line, "<(select|insert|update|delete)",
								Pattern.CASE_INSENSITIVE);
						if (m.find()) {
							String endTag = "</" + m.group(1) + ">";
							while ((line = br.readLine()) != null) {
								sb.append(line + "\n");
								if (line.contains(endTag)) {
									break;
								}
							}
						}
					}
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String searchBlockMethodInSvc(String filePath, List<String> searchQueryList,
			List<String> bizListToStore) throws IOException {
		String contents = readFileToString(filePath);
		String n = System.lineSeparator();
		String endTag = System.currentTimeMillis() + "";
		contents = contents.replace("@BxmServiceOperation", endTag + n + "@BxmServiceOperation");
		try (BufferedReader br = new BufferedReader(new StringReader(contents))) {
			String line;
			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				if (line.contains("@Autowired")) {
					sb.append(line + "\n");
					sb.append((line = br.readLine()) + "\n");
					Matcher m = RegexUtils.matcher(line, "private (.*) ", Pattern.CASE_INSENSITIVE);
					if (m.find()) {
						bizListToStore.add(m.group(1));
					}
				} else {
					for (String searchQuery : searchQueryList) {
						if (line == null)
							break;
						if (line.contains("@BxmServiceOperation(\"" + searchQuery.trim() + "\")")) {
							sb.append(line + "\n");
							while ((line = br.readLine()) != null) {
								if (line.contains(endTag)) {
									break;
								}
								sb.append(line + "\n");
							}
						}
					}
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String searchBlockMethodInBiz(String filePath, List<String> searchQueryList,
			List<String> daoListToStore) throws IOException {
		String contents = readFileToString(filePath);
		String n = System.lineSeparator();
		String endTag = System.currentTimeMillis() + "";
		contents = contents.replace("public", endTag + n + "public");
		contents = contents.replace("private", endTag + n + "private");

		try (BufferedReader br = new BufferedReader(new StringReader(contents))) {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				if (line.contains("@Autowired")) {
					sb.append(line + "\n");
					br.readLine();
					sb.append((line = br.readLine()) + "\n");
					Matcher m = RegexUtils.matcher(line, "private (.*) ", Pattern.CASE_INSENSITIVE);
					if (m.find()) {
						daoListToStore.add(m.group(1));
					}
				} else {
					for (String searchQuery : searchQueryList) {
						if (line == null)
							break;
						if ((line.contains("public ") || line.contains("private "))
								&& line.contains(searchQuery)) {
							sb.append(line + "\n");
							while ((line = br.readLine()) != null) {
								if (line.contains(endTag)) {
									break;
								}
								sb.append(line + "\n");
							}
						}
					}
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String searchBlockMethodInDao(String filePath, List<String> searchQueryList,
			List<String> dbioListToStore) throws IOException {
		String contents = readFileToString(filePath);
		String n = System.lineSeparator();
		String endTag = System.currentTimeMillis() + "";
		contents = contents.replace("public", endTag + n + "public");
		contents = contents.replace("private", endTag + n + "private");

		try (BufferedReader br = new BufferedReader(new StringReader(contents))) {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				if (line.contains("@Autowired")) {
					sb.append(line + "\n");
					br.readLine();
					sb.append((line = br.readLine()) + "\n");
					Matcher m = RegexUtils.matcher(line, "private (.*) ", Pattern.CASE_INSENSITIVE);
					if (m.find()) {
						dbioListToStore.add(m.group(1));
					}
				} else {
					for (String searchQuery : searchQueryList) {
						if (line == null)
							break;
						if ((line.contains("public ") || line.contains("private ")) && line.contains(searchQuery)) {
							sb.append(line + "\n");
							while ((line = br.readLine()) != null) {
								if (line.contains(endTag)) {
									break;
								}
								sb.append(line + "\n");
							}
						}
					}
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static List<Path> findDaoFiles(String dirPath, List<String> nameList) throws IOException {
		return findFiles(dirPath, nameList, "dao", "src", "java", Integer.MAX_VALUE);
	}

	public static List<Path> findDBIOFiles(String dirPath, List<String> nameList) throws IOException {
		return findFiles(dirPath, nameList, "dao", "src", "dbio", Integer.MAX_VALUE);
	}

	public static List<Path> findSvcFiles(String dirPath, List<String> nameList) throws IOException {
		return findFiles(dirPath, nameList, "service", "src", "java", Integer.MAX_VALUE);
	}

	public static List<Path> findBizFiles(String dirPath, List<String> nameList) throws IOException {
		return findFiles(dirPath, nameList, "biz", "src", "java", Integer.MAX_VALUE);
	}

	public static List<Path> findFiles(String dirPath, List<String> nameList, String parentDirName,
			String anySubDirName, String fileExtensionName, int depthFindDirs) throws IOException {
		List<Path> dirPathList = Files
				.find(Paths.get(dirPath), depthFindDirs,
						(filePath, fileAttr) -> fileAttr.isDirectory()
								&& filePath.getFileName().toString().endsWith(parentDirName))
				.collect(Collectors.toList());
		List<Path> filePathList = new ArrayList<Path>();
		for (Path p : dirPathList) {
			filePathList.addAll(Files
					.find(Paths.get(p.toString()), depthFindDirs,
							(filePath, fileAttr) -> fileAttr.isRegularFile()
									&& filePath.getFileName().toString().endsWith(fileExtensionName))
					.filter(filePath -> {
						if (anySubDirName != null && filePath.toString().indexOf("\\" + anySubDirName + "\\") == -1)
							return false;
						boolean matched = false;
						for (String name : nameList) {
							if ((name + "." + fileExtensionName).equalsIgnoreCase(filePath.getFileName().toString()))
								matched = true;
						}
						return matched;
					}).collect(Collectors.toList()));
		}
		return filePathList;
	}
}
