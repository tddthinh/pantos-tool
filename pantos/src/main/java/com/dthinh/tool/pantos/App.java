package com.dthinh.tool.pantos;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.dthinh.tool.pantos.controller.MainController;
import com.dthinh.tool.pantos.entity.Screen;
import com.dthinh.tool.pantos.framework.ScreenFramework;
import com.dthinh.tool.pantos.utils.StringUtils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.dthinh.tool.pantos.controller.*;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
		
//		ScreenFramework.MAIN_DIRS = "C:\\Users\\thinh.tran\\Desktop\\Pantos Tools\\sample\\ldp";
//		ScreenFramework.SYS_DIRS = "C:\\Users\\thinh.tran\\Desktop\\Pantos Tools\\sample\\ldp\\eusu.clt.syscommon";
//
//		String htmlPath = "C:\\Users\\thinh.tran\\Desktop\\Pantos Tools\\sample\\ldp\\MDM_M8001.html";
//		String targetPath = "C:\\Users\\thinh.tran\\Desktop\\Pantos Tools\\sample\\pantos";
//
//		Screen sc = new Screen(htmlPath, targetPath);
//
//		// addtionOmms.add("MDMComIntfOMM");
//
//		sc.getOmmNameList().add("ComUDCOutListOMM");
//		sc.getOmmNameList().add("MDMComIntfCondOMM");
//		sc.getOmmNameList().add("MdmSvceLaneCodeCondOMM");
//		sc.getOmmNameList().add("MdmSvceLaneCodeListOMM");
//		sc.getOmmNameList().add("MdmSvceLaneCondOMM");
//		sc.getOmmNameList().add("MdmSvceLaneListOMM");
//		sc.getOmmNameList().add("mdmSvceLaneCodeOmms");
//		sc.getOmmNameList().add("mdmSvceLaneOmms");
//		sc.getOmmNameList().add("outUdcOmms");
//		sc.getOmmNameList().add("MDMComIntfOMM");
//
//		sc.getSvcNameList().add("SvcMDM0107");
//		sc.getSvcMethodList().add("searchMdmSvceLaneKeyValue");
//		sc.getSvcMethodList().add("searchMdmSvceLaneList");
//		sc.getSvcMethodList().add("searchMdmSvceLaneCodeList");
//		sc.getSvcMethodList().add("manageServiceLaneIntf");
//
////		sc.getDbioNameList().add("DBIOMDM1003");
////		sc.getDbioMethodList().add("searchMdmCurList");
////		sc.getDbioMethodList().add("updateMdmCur");
////		sc.getDbioMethodList().add("insertMdmCur");
//
//		try {
//			ScreenFramework.run(sc);
//			System.out.println(sc.toString());
//			// System.out.println(StringUtils.formatHTML(sc));
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Scene.fxml"));
			primaryStage.setTitle("Application Title");
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
			MainController.stage = primaryStage;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
