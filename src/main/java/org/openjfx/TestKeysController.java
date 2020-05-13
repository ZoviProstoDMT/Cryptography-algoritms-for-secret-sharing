package org.openjfx;

import SecretShareLogic.Point;
import SecretShareLogic.VerifiableSecretSharing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestKeysController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView backBtnIcon1;

    @FXML
    private Button generateSecret;

    @FXML
    private Label polynomField;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView backBtnIcon;

    @FXML
    private LineChart<Double, Double> polynomXY;

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
        polynomField.setText(polynomField.getText() + VerifiableSecretSharing.polynom.toString());
        buildData();
        buildChart();

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
    }

    public void buildData() {
        ObservableList<String> tableList = FXCollections.observableArrayList();
        for (int i = 0; i < VerifiableSecretSharing.xyKey.size(); i++) {
            tableList.add("                "+VerifiableSecretSharing.xyKey.get(i).toString());
        }
        System.out.println(tableList);
        keyList.setItems(tableList);
    }

    public void buildChart() {
        XYChart.Series<Double, Double> keysSeries = new XYChart.Series<>();
        for(Point p : VerifiableSecretSharing.xyKey)
            keysSeries.getData().add(new XYChart.Data<>(p.getX(),p.getY()));
        polynomXY.getData().addAll(keysSeries);
    }
}
