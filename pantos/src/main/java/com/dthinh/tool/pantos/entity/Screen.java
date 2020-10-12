package com.dthinh.tool.pantos.entity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Screen {
	private String HTMLPath;
	
	private List<String> ommNameList = new ArrayList<String>();
	private List<Path> ommFilePathList = new ArrayList<Path>();
	
	private List<String> dtoNameList = new ArrayList<String>();
	private List<Path> dtoFilePathList = new ArrayList<Path>();
	
	private List<String> dbioNameList = new ArrayList<String>();
	private List<Path> dbioFilePathList = new ArrayList<Path>();
	private List<String> dbioMethodList = new ArrayList<String>();
	
	private List<String> daoNameList = new ArrayList<String>();
	private List<Path> daoFilePathList = new ArrayList<Path>();
	private List<String> daoMethodList = new ArrayList<String>();
	
	private List<String> svcNameList = new ArrayList<String>();
	private List<Path> svcFilePathList = new ArrayList<Path>();
	private List<String> svcMethodList = new ArrayList<String>();
	
	private List<String> bizNameList = new ArrayList<String>();
	private List<Path> bizFilePathList = new ArrayList<Path>();
	private List<String> bizMethodList = new ArrayList<String>();
	
	private String targetPath;
	public Screen(String HTMLPath, String targetPath) {
		this.HTMLPath = HTMLPath;
		this.targetPath = targetPath;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("OMM Name List:"+System.lineSeparator());
		for (String s : ommNameList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		sb.append("OMM Path File List:"+System.lineSeparator());
		for(Path p : ommFilePathList) {
			sb.append("\t"+p.toString()+System.lineSeparator());
		}
//		sb.append("OMM Contents:"+System.lineSeparator());
//		for(Path p : ommFilePathList) {
//			sb.append("\t"+FileUtils.readFileToString(p.toString())+System.lineSeparator());
//		}
		
		sb.append("DTO Name List:"+System.lineSeparator());
		for (String s : dtoNameList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		sb.append("DTO Path File List:"+System.lineSeparator());
		for(Path p : dtoFilePathList) {
			sb.append("\t"+p.toString()+System.lineSeparator());
		}
//		sb.append("DTO Contents:"+System.lineSeparator());
//		for(Path p : dtoFilePathList) {
//			sb.append("\t"+FileUtils.readFileToString(p.toString())+System.lineSeparator());
//		}
		
		sb.append("Svc Name List:"+System.lineSeparator());
		for (String s : svcNameList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		sb.append("Svc Path File List:"+System.lineSeparator());
		for(Path p : svcFilePathList) {
			sb.append("\t"+p.toString()+System.lineSeparator());
		}
		sb.append("Svc Methods:"+System.lineSeparator());
		for(String s : svcMethodList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		
		sb.append("Biz Name List:"+System.lineSeparator());
		for (String s : bizNameList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		sb.append("Biz Path File List:"+System.lineSeparator());
		for(Path p : bizFilePathList) {
			sb.append("\t"+p.toString()+System.lineSeparator());
		}
		sb.append("Biz Methods:"+System.lineSeparator());
		for(String s : bizMethodList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		
		sb.append("DAO Name List:"+System.lineSeparator());
		for (String s : daoNameList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		sb.append("DAO Path File List:"+System.lineSeparator());
		for(Path p : daoFilePathList) {
			sb.append("\t"+p.toString()+System.lineSeparator());
		}
		sb.append("DAO Methods:"+System.lineSeparator());
		for(String s : daoMethodList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		
		sb.append("DBIO Name List:"+System.lineSeparator());
		for (String s : dbioNameList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		sb.append("DBIO Path File List:"+System.lineSeparator());
		for(Path p : dbioFilePathList) {
			sb.append("\t"+p.toString()+System.lineSeparator());
		}
		
		sb.append("DBIO Methods:"+System.lineSeparator());
		for(String s : dbioMethodList) {
			sb.append("\t"+s+System.lineSeparator());
		}
		
		sb.append("HTML Path File:"+System.lineSeparator());
		sb.append("\t"+HTMLPath);
		sb.append(System.lineSeparator());
		sb.append("Target Path Dir:"+System.lineSeparator());
		sb.append("\t"+targetPath+System.lineSeparator());
		
		return sb.toString();
	}
}
