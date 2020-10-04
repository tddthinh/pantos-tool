package com.dthinh.tool.pantos.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
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

	public static List<Path> findOMMFiles(String dirPath, List<String> ommNameList, int depthFindDirs) {
		List<Path> ommFilePathList = new ArrayList<Path>();

		try {
			List<Path> ommDirPathList = Files.find(Paths.get(dirPath), depthFindDirs,
					(filePath, fileAttr) -> fileAttr.isDirectory() && filePath.getFileName().toString().endsWith("omm"))
					.collect(Collectors.toList());
			for (Path p : ommDirPathList) {
				ommFilePathList
						.addAll(Files
								.find(Paths.get(p.toString()), Integer.MAX_VALUE,
										(filePath, fileAttr) -> fileAttr.isRegularFile()
												&& filePath.getFileName().toString().endsWith("omm"))
								.filter(filePath -> {
									boolean matched = false;
									for (String ommName : ommNameList) {
										if ((ommName + ".omm").equalsIgnoreCase(filePath.getFileName().toString()))
											matched = true;
									}
									return matched;
								}).collect(Collectors.toList()));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ommFilePathList;
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
	public static Path writeDto(String ommName, String ommContent, String dirPath) throws URISyntaxException {
		if(ommName.endsWith(".omm")) ommName = ommName.substring(0, ommName.length()-4);
		String templateName;
		if(ommName.endsWith("CondOMM")) {
			templateName = "dto-cond-template.txt";
		}else if(ommName.endsWith("GrpOMM")) {
			templateName = "dto-grp-template.txt";
		}else if(ommName.endsWith("ListOMM")) {
			templateName = "dto-list-template.txt";
		}else {
			templateName = "dto--template.txt";
		}
		String templateContent = readFileToString(getFileFromResource(templateName).getAbsolutePath());
		String dtoName = ommName.substring(0,ommName.length()-3)+"Dto";
		String fields = StringUtils.extractOMMFields(ommContent);
		String dtoContent = templateContent.replaceAll("dtoClassName", dtoName).replaceAll("dtoFields", fields);
		(new File(dirPath)).mkdirs();
		String dtoPath = dirPath+dtoName+".java";
		writeStringToFile(dirPath+dtoName+".java", dtoContent);
		return Paths.get(dtoPath);
	}
	
	public static File getFileFromResource(String fileName) throws URISyntaxException{

        ClassLoader classLoader = FileUtils.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }

    }
}
