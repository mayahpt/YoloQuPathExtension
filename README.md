# QuPath YOLO Tumor Segmentation Extension -
**This fork attemps to expand the original interface to improve user defined settings**
**For more information visit:**

[Project Web Page](https://metehan41.github.io/TSEG_Web_Page/)

Project Repositories:
* Model Training: [tseg](https://github.com/ae-aydin/tseg)
* Extension Helper Script: [tseg-qupath-inf](https://github.com/ae-aydin/tseg-qupath-inf)
* Extension: [qupath-extension-tseg](https://github.com/ae-aydin/qupath-extension-tseg)

***

Enables tumor segmentation with YOLO-seg models through a Python inference script, managing input/output by sending ROI and receiving predictions in GeoJSON format for integration with QuPath.

<p align="center">
  <img src=interface.png width=450>
</p>

### Installation
1. Install the extension.
   * For installation, check https://qupath.readthedocs.io/en/latest/docs/intro/extensions.html.
2. Download the inference script from [*tseg-qupath-inf*](https://github.com/ae-aydin/tseg-qupath-inf) (either by cloning the repository or downloading the ZIP archive).
    * `Python > 3.8`
    * https://www.python.org/downloads/
3. Run *setup.bat* (for Windows) or *setup.sh* (for Linux and macOS) to prepare the inference folder.
    * This will create virtual environment and some folders.
4. Obtain a YOLO-seg model `(.pt, single class - 0: Tumor)` for segmentation.
   * Our current model: [*Drive Link*](https://drive.google.com/file/d/1LjO4FIN06ZCgs-9Zrwyu5m5ZLDDCekSf/view?usp=sharing). It segments stained tumor areas most effectively.
5. Place the model inside the `tseg-qupath-inf/models` directory and rename it to `model.pt` if required.

6. In QuPath, select a ROI using the rectangle tool and ensure it is selected.

7. Open the `Extensions > TSEG > Segment` window within QuPath and select the `tseg-qupath-inf` directory using the directory chooser.
    * For tips check `Extensions > TSEG > Help`
8. Change inference arguments if needed and click the **Segment Selected Region** button to initiate the segmentation process.
