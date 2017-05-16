package pl.lukaszgo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import pl.lukaszgo.action.ProjectFileWriteAction;
import pl.lukaszgo.action.UpdaterAction;

public class Controller {

    @FXML
    private ChoiceBox projectSelectionBox;

    @FXML
    private CheckBox isBeta;

    @FXML
    private CheckBox updaterCheckbox;

    private List<String> projectNames;

    private List<String> commands;

    private String currentSelectedProject;

    @FXML
    private void handleStartButton(ActionEvent event) {
        prepareAndExecuteProjectFileWriteAction();

        if(!updaterCheckbox.isSelected()) {
            Controller.showSchemaSuccessChangeInfo();
        }

        if(updaterCheckbox.isSelected()) {
            prepareUpdaterCommand();
            prepareAndExecuteUpdaterAction();
        }
    }

    @FXML
    private void deleteCurrentSelectedProject(ActionEvent event) {
        File file = new File(Settings.PROJECT_CHANGER_PATH + "/" + projectSelectionBox.getValue());

        if(Settings.DEBUG) {
            System.out.println("File to delete path: " + file.getAbsolutePath());
        }

        if(file.exists()) {
            file.delete();
            System.out.println("File deleted.");
            initialize();
        }
    }

    @FXML
    private void initialize() {
        projectNames = new ArrayList<>();
        getCurrentSelectedProject();
        setCurrentSelectedProjectInSelectionBox();
        getAllProjectFiles();
        populateSelectBox();
    }

    private void getCurrentSelectedProject() {
        List<String> list;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(Settings.DB_SCHEMAS_FILE_PATH))) {
            list = br.lines().collect(Collectors.toList());
            currentSelectedProject = list.get(0);
            currentSelectedProject = currentSelectedProject.replace("db.user=", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentSelectedProjectInSelectionBox() {
        projectSelectionBox.setValue(currentSelectedProject);
    }

    private void getAllProjectFiles() {
        File folder = new File(Settings.PROJECT_CHANGER_PATH);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++) {
            projectNames.add(listOfFiles[i].getName());
        }
    }

    private void populateSelectBox() {
        projectSelectionBox.setItems(FXCollections.observableArrayList(projectNames));
    }

    private void prepareUpdaterCommand() {
        commands = new ArrayList<>();

        commands.add("cmd.exe");
        commands.add("/C");
        commands.add("start");
        commands.add("java");
        commands.add("-jar");
        commands.add("omsUpdater.jar");

        if (Settings.DEBUG) {
            System.out.println("Debug commands: " + commands.toString());
        }

    }

    private void prepareAndExecuteUpdaterAction() {
        UpdaterAction updaterAction = new UpdaterAction(commands, isBeta.isSelected());
        updaterAction.start();
    }

    private void prepareAndExecuteProjectFileWriteAction() {
        ProjectFileWriteAction projectFileWriteAction = new ProjectFileWriteAction(projectSelectionBox.getValue().toString());
        projectFileWriteAction.execute();
    }

    public static void showSchemaSuccessChangeInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces!");
        alert.setContentText("Zmiana danych schematu przebiegła pomyślnie!");
        alert.show();
    }

}
