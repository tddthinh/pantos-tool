package com.dthinh.tool.pantos.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;

import com.dthinh.tool.pantos.entity.Screen;
import com.dthinh.tool.pantos.framework.ScreenFramework;
import com.dthinh.tool.pantos.utils.FileUtils;
import com.dthinh.tool.pantos.utils.StringUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainController implements Initializable {

	@FXML
	private TextArea daoNameField;

	@FXML
	private TextArea dbioMethodField;

	@FXML
	private TextArea logArea;

	@FXML
	private Button run1Btn;

	@FXML
	private TextArea ommNameField;

	@FXML
	private TextArea svcMethodField;

	@FXML
	private TextArea dtoNameField;

	@FXML
	private TextArea dbioNameField;

	@FXML
	private Button run2Btn;

	@FXML
	private Button openTargetBtn;

	@FXML
	private Button targetBtn;

	@FXML
	private TextField targetField;

	@FXML
	private TextArea daoMethodField;

	@FXML
	private TextArea bizMethodField;

	@FXML
	private TextArea svcNameField;

	@FXML
	private TextArea bizNameField;

	@FXML
	private TextField htmlField;

	@FXML
	private Button htmlBtn;

	@FXML
	private TextField ldpField;

	@FXML
	private Button ldpBtn;

	public static FileChooser fileChooser = new FileChooser();
	public static DirectoryChooser directoryChooser = new DirectoryChooser();
	public static String currentPath = System.getProperty("user.home");
	public static Stage stage;
	private Screen sc = null;

	@FXML
	void selectHTML(ActionEvent event) {
		configuringHTMLChooser();
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			currentPath = file.getParentFile().getAbsolutePath();
			htmlField.setText(file.getAbsolutePath());
		}
	}

	@FXML
	void selectDirTarget(ActionEvent event) {
		configuringTargetChooser();
		File file = directoryChooser.showDialog(stage);
		if (file != null) {
			currentPath = file.getParentFile().getAbsolutePath();
			targetField.setText(file.getAbsolutePath());
		}
	}

	@FXML
	void selectDirLDP(ActionEvent event) {
		configuringTargetChooser();
		File file = directoryChooser.showDialog(stage);
		if (file != null) {
			currentPath = file.getParentFile().getAbsolutePath();
			ldpField.setText(file.getAbsolutePath());
		}
	}

	@FXML
	void run1Btn(ActionEvent event) {
		if (htmlField.getText().isEmpty() || targetField.getText().isEmpty() || ldpField.getText().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter html, target and LDP field");
			alert.showAndWait();
		} else {
			ScreenFramework.MAIN_DIRS = ldpField.getText();
			ScreenFramework.SYS_DIRS = ldpField.getText() + "\\eusu.clt.syscommon";
			ScreenFramework.isInitial = true;

			String htmlPath = htmlField.getText();
			String targetPath = targetField.getText();
			sc = new Screen(htmlPath, targetPath);

			try {
				ScreenFramework.run(sc);
				logArea.setText(sc.toString());
				setTextFields(sc);

			} catch (IOException e) {
				logArea.setText(e.toString());
				clearTextFields();
				e.printStackTrace();
			} catch (URISyntaxException e) {
				logArea.setText(e.toString());
				clearTextFields();
				e.printStackTrace();
			}
		}
	}

	@FXML
	void run2Btn(ActionEvent event) {
		if (sc == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please run step 1 first");
			alert.showAndWait();
		} else {
			ScreenFramework.MAIN_DIRS = ldpField.getText();
			ScreenFramework.SYS_DIRS = ldpField.getText() + "\\eusu.clt.syscommon";
			ScreenFramework.isInitial = false;
			loadTextFieldsToScreen(sc);
			try {
				// System.out.println(sc.toString());
				ScreenFramework.run(sc);
				logArea.setText(sc.toString());
			} catch (IOException e) {
				logArea.setText(e.toString());
				clearTextFields();
				e.printStackTrace();
			} catch (URISyntaxException e) {
				logArea.setText(e.toString());
				clearTextFields();
				e.printStackTrace();
			}
		}
	}

	@FXML
	void openTargetDir(ActionEvent event) {

	}

	void setTextFields(Screen sc) {
		ommNameField.setText(String.join(System.lineSeparator(), sc.getOmmNameList()));
		dtoNameField.setText(String.join(System.lineSeparator(), sc.getDtoNameList()));
		svcNameField.setText(String.join(System.lineSeparator(), sc.getSvcNameList()));
		svcMethodField.setText(String.join(System.lineSeparator(), sc.getSvcMethodList()));
		bizNameField.setText(String.join(System.lineSeparator(), sc.getBizNameList()));
		bizMethodField.setText(String.join(System.lineSeparator(), sc.getBizMethodList()));
		daoNameField.setText(String.join(System.lineSeparator(), sc.getDaoNameList()));
		daoMethodField.setText(String.join(System.lineSeparator(), sc.getDaoMethodList()));
		dbioNameField.setText(String.join(System.lineSeparator(), sc.getDbioNameList()));
		dbioMethodField.setText(String.join(System.lineSeparator(), sc.getDbioMethodList()));
		savePathConfig();
	}

	void loadTextFieldsToScreen(Screen sc) {
		sc.setOmmNameList(new ArrayList<String>(Arrays.asList(ommNameField.getText().split("\\r?\\n"))));
		sc.setSvcNameList(new ArrayList<String>(Arrays.asList(svcNameField.getText().split("\\r?\\n"))));
		sc.setSvcMethodList(new ArrayList<String>(Arrays.asList(svcMethodField.getText().split("\\r?\\n"))));
		sc.setBizNameList(new ArrayList<String>(Arrays.asList(bizNameField.getText().split("\\r?\\n"))));
		sc.setBizMethodList(new ArrayList<String>(Arrays.asList(bizMethodField.getText().split("\\r?\\n"))));
		sc.setDaoNameList(new ArrayList<String>(Arrays.asList(daoNameField.getText().split("\\r?\\n"))));
		sc.setDaoMethodList(new ArrayList<String>(Arrays.asList(daoMethodField.getText().split("\\r?\\n"))));
		sc.setDbioNameList(new ArrayList<String>(Arrays.asList(dbioNameField.getText().split("\\r?\\n"))));
		sc.setDbioMethodList(new ArrayList<String>(Arrays.asList(dbioMethodField.getText().split("\\r?\\n"))));
	}

	void clearTextFields() {
		ommNameField.setText("");
		dtoNameField.setText("");
		svcNameField.setText("");
		svcMethodField.setText("");
		bizNameField.setText("");
		bizMethodField.setText("");
		daoNameField.setText("");
		daoMethodField.setText("");
		dbioNameField.setText("");
		dbioMethodField.setText("");
	}

	private static void configuringHTMLChooser() {
		fileChooser.setTitle("Select HTML");
		fileChooser.setInitialDirectory(new File(currentPath));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Screen HTML File", "*.html"));
	}

	private static void configuringTargetChooser() {
		directoryChooser.setTitle("Select Folder to Save");
		directoryChooser.setInitialDirectory(new File(currentPath));
	}

	private void savePathConfig() {
		try (OutputStream output = new FileOutputStream(FileUtils.getFileFromResource("config.properties"))) {
			Properties prop = new Properties();
			prop.setProperty("init.htmlpath", htmlField.getText());
			prop.setProperty("init.targetpath", targetField.getText());
			prop.setProperty("init.ldppath", ldpField.getText());
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	private void loadPathConfig() {
		try (InputStream input = new FileInputStream(FileUtils.getFileFromResource("config.properties"))) {
			Properties prop = new Properties();
			prop.load(input);
			htmlField.setText(prop.getProperty("init.htmlpath"));
			targetField.setText(prop.getProperty("init.targetpath"));
			ldpField.setText(prop.getProperty("init.ldppath"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPathConfig();

	}
}
