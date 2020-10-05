package com.dthinh.tool.pantos;

import com.dthinh.tool.pantos.entity.Screen;
import com.dthinh.tool.pantos.framework.ScreenFramework;

public class App {
	public static void main(String[] args) {
		String htmlPath = "C:\\Users\\thinh.tran\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\ldp\\ADM_P1024.html";
		String targetPath = "C:\\Users\\thinh.tran\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\ldp\\pantos";
		Screen sc = new Screen(htmlPath);
		ScreenFramework.run(sc, targetPath);
		System.out.println(sc.toString());
	}
}
