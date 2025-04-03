// Updated ExtensionInterface.java based on original + new UI field integration

package qupath.ext.tseg.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import qupath.ext.tseg.YoloExtension;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

// Class for managing main interface using FXML.
public class ExtensionInterface extends GridPane {

    private static final ResourceBundle resources = ResourceBundle.getBundle("qupath.ext.tseg.ui.strings");

    @FXML private TextField pyScriptDirField;
    @FXML private Slider confSlider;
    @FXML private Slider iouSlider;
    @FXML private Text infoText;
    @FXML private Button runButton;
    @FXML private TextArea scriptOutput;

    // New user-editable parameters
    @FXML private TextField tileSizeField;
    @FXML private TextField downsampleField;
    @FXML private Slider overlapSlider;
    @FXML private TextField imageExtField;

    public static ExtensionInterface createInstance() throws IOException {
        return new ExtensionInterface();
    }

    // Constructor
    private ExtensionInterface() throws IOException {
        var url = ExtensionInterface.class.getResource("interface.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
        loadSavedDirectoryPath();
        initializeValues();
    }

    private void initializeValues() {
        tileSizeField.setText(String.valueOf(YoloExtension.tileSizeProperty.get()));
        downsampleField.setText(String.valueOf(YoloExtension.downsampleProperty.get()));
        overlapSlider.setValue(YoloExtension.overlapProperty.get());
        imageExtField.setText(YoloExtension.imageExtProperty.get());
        confSlider.setValue(0.5); // Optional default
        iouSlider.setValue(0.5);  // Optional default
    }

    @FXML
    private void selectScriptDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(resources.getString("ext.dir_chooser"));
        File selectedDir = directoryChooser.showDialog(null);
        if (selectedDir != null) {
            String selectedPath = selectedDir.getAbsolutePath();
            pyScriptDirField.setText(selectedPath);
            YoloExtension.defaultPathProperty.set(selectedPath);
        }
    }

    private void loadSavedDirectoryPath() {
        String savedPath = YoloExtension.defaultPathProperty.getValue();
        if (savedPath != null) {
            pyScriptDirField.setText(savedPath);
        }
    }

    @FXML
    private void runScript() {
        runButton.setDisable(true);
        scriptOutput.clear();
        scriptOutput.appendText(resources.getString("run.running") + "\n");

        // Save user input
        try {
            YoloExtension.tileSizeProperty.set(Integer.parseInt(tileSizeField.getText()));
            YoloExtension.downsampleProperty.set(Double.parseDouble(downsampleField.getText()));
            YoloExtension.overlapProperty.set(overlapSlider.getValue());
            YoloExtension.imageExtProperty.set(imageExtField.getText());
        } catch (NumberFormatException e) {
            scriptOutput.appendText("Invalid input in one of the fields.\n");
            runButton.setDisable(false);
            return;
        }

        String pyScriptDirPath = pyScriptDirField.getText();
        PathConfig pathConfig = new PathConfig(pyScriptDirPath);
        double confidence = confSlider.getValue();
        double iou = iouSlider.getValue();

        QPImage QPImage = new QPImage();
        if (QPImage.getROI() == null) {
            scriptOutput.appendText(resources.getString("run.no_roi") + "\n");
            runButton.setDisable(false);
            return;
        }

        ScriptManager scriptManager = new ScriptManager(pathConfig, QPImage, confidence, iou, scriptOutput);
        scriptManager.setOnSucceeded(event -> {
            scriptOutput.appendText(resources.getString("run.done") + "\n");
            runButton.setDisable(false);
        });

        scriptManager.setOnFailed(event -> {
            Throwable exception = scriptManager.getException();
            scriptOutput.appendText("ERROR:" + exception.getMessage() + "\n");
            scriptOutput.appendText(resources.getString("run.fail") + "\n");
            runButton.setDisable(false);
        });

        Thread thread = new Thread(scriptManager);
        thread.start();
    }
} 