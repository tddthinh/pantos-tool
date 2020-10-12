package com.dthinh.tool.pantos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.dthinh.tool.pantos.controller.*;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Scene.fxml"));
			primaryStage.setTitle("Copying Related LDP's Files");
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
			MainController.stage = primaryStage;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
