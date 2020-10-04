package com.dthinh.tool.pantos.entity;

import java.nio.file.Path;
import java.util.List;

import com.dthinh.tool.pantos.utils.FileUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Screen {
	private String HTMLPath;
	private List<String> ommNameList;
	private List<Path> ommFilePathList;
	private List<String> dtoNameList;
	private List<Path> dtoFilePathList;
	private String targetPath;
	public Screen(String HTMLPath) {
		this.HTMLPath = HTMLPath;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OMM Name List:\n");
		for (String s : ommNameList) {
			sb.append(s+"\n");
		}
		sb.append("OMM Path File List:\n");
		for(Path p : ommFilePathList) {
			sb.append(p.toString()+"\n");
		}
		sb.append("OMM Contents:\n");
		for(Path p : ommFilePathList) {
			sb.append(FileUtils.readFileToString(p.toString())+"\n");
		}
		sb.append("DTO Name List:\n");
		for (String s : dtoNameList) {
			sb.append(s+"\n");
		}
		sb.append("DTO Path File List:\n");
		for(Path p : dtoFilePathList) {
			sb.append(p.toString()+"\n");
		}
		sb.append("DTO Contents:\n");
		for(Path p : dtoFilePathList) {
			sb.append(FileUtils.readFileToString(p.toString())+"\n");
		}
		
		return sb.toString();
	}
}
