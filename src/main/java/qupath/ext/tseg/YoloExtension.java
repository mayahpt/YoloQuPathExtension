package qupath.ext.tseg;

import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qupath.ext.tseg.ui.ExtensionInterface;
import qupath.ext.tseg.ui.HelpInterface;
import qupath.fx.dialogs.Dialogs;
import qupath.fx.prefs.controlsfx.PropertyItemBuilder;
import qupath.lib.common.Version;
import qupath.lib.gui.QuPathGUI;
import qupath.lib.gui.extensions.GitHubProject;
import qupath.lib.gui.extensions.QuPathExtension;
import qupath.lib.gui.prefs.PathPrefs;

import java.io.IOException;
import java.util.Objects;

public class YoloExtension implements QuPathExtension, GitHubProject {
	
	private static final Logger logger = LoggerFactory.getLogger(YoloExtension.class);

	private static final String EXTENSION_NAME = "YOLO Tumor Segmentation";

	private static final String EXTENSION_DESCRIPTION = "Tumor Segmentation with YOLO-seg Models - HU AIN Final Project";

	private static final Version EXTENSION_QUPATH_VERSION = Version.parse("v0.5.0");

	private static final GitHubRepo EXTENSION_REPOSITORY = GitHubRepo.create(
			EXTENSION_NAME, "ae-aydin", "qupath-extension-tseg");

	private boolean isInstalled = false;

	public static final StringProperty defaultPathProperty = PathPrefs.createPersistentPreference(
			"defaultPath", null);

	public static final IntegerProperty tileSizeProperty = PathPrefs.createPersistentPreference(
			"tileSize", 512);

	public static final DoubleProperty downsampleProperty = PathPrefs.createPersistentPreference(
			"downsample", 1.0);

	public static final DoubleProperty overlapProperty = PathPrefs.createPersistentPreference(
			"overlap", 0.25);

	public static final StringProperty imageExtProperty = PathPrefs.createPersistentPreference(
			"imgExtension", "png");

	private Stage segmentStage;

    @Override
	public void installExtension(QuPathGUI qupath) {
		if (isInstalled) {
			logger.debug("{} is already installed", getName());
			return;
		}
		isInstalled = true;
		addMenuItem(qupath);
		addPreferencesToPane(qupath);
	}

	// Create a preference.
	private <T> void addPreference(QuPathGUI qupath, Property<T> pref, final Class<? extends T> cls, String name){
		var propertyItem = new PropertyItemBuilder<>(pref, cls)
				.name(name)
				.category(EXTENSION_NAME)
				.description(name)
				.build();
		qupath.getPreferencePane()
				.getPropertySheet()
				.getItems()
				.add(propertyItem);
	}

	// Store preferences within QuPath.
	private void addPreferencesToPane(QuPathGUI qupath) {
		addPreference(qupath, defaultPathProperty, String.class, "Python script directory");
		addPreference(qupath, downsampleProperty, Double.class, "Downsample rate");
		addPreference(qupath, imageExtProperty, String.class, "Tile image extension");
		addPreference(qupath, tileSizeProperty, Integer.class, "Tile size");
		addPreference(qupath, overlapProperty, Double.class, "Tile overlap ratio ");
	}

	// Add menu item to extension menu.
	private void addMenuItem(QuPathGUI qupath) {
		var menu = qupath.getMenu("Extensions>" + "TSEG", true);
		MenuItem helpItem = new MenuItem("Help");
		MenuItem segmentItem = new MenuItem("Segment");
		helpItem.setOnAction(e -> HelpInterface.showHelp());
		segmentItem.setOnAction(e -> createSegmentationWindow());
		menu.getItems().add(helpItem);
		menu.getItems().add(segmentItem);
	}

	// Create main extension window.
	private void createSegmentationWindow() {
		if (segmentStage == null) {
			try {
				segmentStage = new Stage();
				Image mainIcon = new Image(Objects.requireNonNull(YoloExtension.class.getResourceAsStream("images/icon.png")));
				segmentStage.getIcons().add(mainIcon);
				Scene segmentScene = new Scene(ExtensionInterface.createInstance());
				segmentStage.setScene(segmentScene);
				segmentStage.setResizable(false);
				segmentStage.setTitle("YOLO Tumor Segmentation");
			} catch (IOException e) {
				Dialogs.showErrorMessage("Extension Error", "GUI loading failed");
				logger.error("Unable to load extension interface FXML", e);
			}
		}
		segmentStage.show();
	}

	@Override
	public String getName() {
		return EXTENSION_NAME;
	}

	@Override
	public String getDescription() {
		return EXTENSION_DESCRIPTION;
	}
	
	@Override
	public Version getQuPathVersion() {
		return EXTENSION_QUPATH_VERSION;
	}

	@Override
	public GitHubRepo getRepository() {
		return EXTENSION_REPOSITORY;
	}

}
