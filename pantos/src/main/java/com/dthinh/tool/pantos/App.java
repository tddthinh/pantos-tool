package com.dthinh.tool.pantos;

import com.dthinh.tool.pantos.entity.Screen;
import com.dthinh.tool.pantos.framework.ScreenFramework;
import com.dthinh.tool.pantos.utils.StringUtils;

public class App {
	public static void main(String[] args) {
		String htmlPath = "C:\\Users\\thinh.tran\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\ldp\\MDM_M1003.html";
		String targetPath = "C:\\Users\\thinh.tran\\Google Drive\\MyComShared\\Ideas\\Pantos Tools\\sample\\ldp\\pantos";
		Screen sc = new Screen(htmlPath);
		ScreenFramework.run(sc, targetPath);
		//System.out.println(sc.toString());
		System.out.println(StringUtils.formatHTML(sc));
	}
}
