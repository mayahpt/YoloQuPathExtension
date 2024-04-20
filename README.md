# QuPath YOLO Tumor Segmentation Extension

**HU AIN Final Project**

Enables tumor segmentation with YOLO-seg models through a Python inference script, managing input/output by sending ROI data and receiving predictions in GeoJSON format for integration with QuPath.

### Installation
1. Download the inference script from [*tseg-qupath-inf*](https://github.com/ae-aydin/tseg-qupath-inf) (either by cloning the repository or downloading the ZIP archive).
    * `Python > 3.6`
2. Run *setup.bat* (for Windows) or *setup.sh* (for Unix-based systems) to prepare the inference folder.
    * This will create virtual environment and some folders.
3. Obtain a YOLO-seg model (.pt, single class - 0: Tumor) for segmentation.

4. Place the model inside the `tseg-qupath-inf/models` directory and rename it to `model.pt`.

5. In QuPath, select a ROI using the rectangle tool and ensure it is selected.

6. Open the `Extensions > TSEG > Segment` window within QuPath and select the `tseg-qupath-inf` directory using the directory chooser.
    * For tips check `Extensions > TSEG > Help`
7.  Change inference arguments if needed and click the **Segment Selected Region** button to initiate the segmentation process.
