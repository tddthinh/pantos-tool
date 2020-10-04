package com.dthinh.tool.pantos;

import com.dthinh.tool.pantos.entity.Screen;
import com.dthinh.tool.pantos.framework.ScreenFramework;

public class App {
	public static void main(String[] args) {
		String htmlPath = "C:\\Users\\khoai\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\html\\CFG_M4002.html";
		String targetPath = "C:\\Users\\khoai\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\ommcopyied\\";
		Screen sc = new Screen(htmlPath);
		ScreenFramework.run(sc, targetPath);
		System.out.println(sc.toString());
	}
}
