package org.openjfx;

import java.io.IOException;

import SecretShareLogic.Key;
import SecretShareLogic.VerifiableSecretSharing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ShamirSecondController {

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView backBtnIcon1;

    @FXML
    private Button generateSecret;

    @FXML
    private TextArea polynomField;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView backBtnIcon;

    @FXML
    private LineChart<Long, Double> polynomXY;

    @FXML
    private Button testKeysBtn;

    @FXML
    private ListView<String> keyList;

    @FXML
    private Button lagrangeFuncBtn;

    @FXML
    private Button minimizeBtn;

    @FXML
    private ImageView backBtnIcon11;

    @FXML
    void initialize() {
        polynomField.editableProperty().setValue(false);
        polynomField.setText("f(x) = " + VerifiableSecretSharing.polynom.toString()
                + "    (mod " + VerifiableSecretSharing.p + ")");
        buildData();
        buildChart();

        generateSecret.setOnAction(actionEvent -> {
            try {
                App.setRoot("decryptSecretPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        lagrangeFuncBtn.setOnAction(actionEvent -> {
            try {
                App.setRoot("lagrangePage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        closeBtn.setOnAction(actionEvent -> {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });

        minimizeBtn.setOnAction(actionEvent -> {
            Stage stage = (Stage) minimizeBtn.getScene().getWindow();
            stage.setIconified(true);
        });

        backBtn.setOnAction(actionEvent -> {
            try {
                App.setRoot("shamirFirstPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        testKeysBtn.setOnAction(actionEvent -> {
            try {
                App.setRoot("testKeyPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void buildData() {
        ObservableList<String> tableList = FXCollections.observableArrayList();
        for (int i = 0; i < VerifiableSecretSharing.xyKey.size(); i++) {
            tableList.add(VerifiableSecretSharing.xyKey.get(i).toString());
        }
        keyList.setItems(tableList);
    }

    public void buildChart() {
        XYChart.Series<Long, Double> keysSeries = new XYChart.Series<>();
        polynomXY.legendVisibleProperty().setValue(false);
        for(Key p : VerifiableSecretSharing.xyKey)
            keysSeries.getData().add(new XYChart.Data<>(p.getX(),p.getY().doubleValue()));
        polynomXY.getData().addAll(keysSeries);
    }
}
