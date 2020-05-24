package org.openjfx;

import SecretShareLogic.Point;
import SecretShareLogic.VerifiableSecretSharing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.openjfx.animations.Shake;

import java.io.IOException;
import java.util.ArrayList;

public class DecryptSecretController {

    @FXML
    private Button minimizeBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private Button testKeyBtn;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView backBtnIcon;

    @FXML
    private ListView<String> keyList;

    @FXML
    private Button lagrangeFuncBtn;

    @FXML
    private ImageView checkBtn;

    @FXML
    private AnchorPane secretResField;

    @FXML
    private Label secretRes;

    @FXML
    private Text textInfo;

    @FXML
    private ListView<String> keyList2;

    @FXML
    void initialize() {
        keyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        textInfo.setText("необходимо выбрать " + VerifiableSecretSharing.getK() +" ключей");
        buildData();

        checkBtn.setOnMouseClicked(event -> {
            showSecretResult();
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
                App.setRoot("shamirSecondPage");
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

        testKeyBtn.setOnAction(actionEvent -> {
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

    public void showSecretResult() {
        MultipleSelectionModel<String> keysSelectionModel = keyList.getSelectionModel();
        ArrayList<Point> points = new ArrayList<>();
        try {
            for (int i = 0; i < VerifiableSecretSharing.getK(); i++) {
                String[] str = keysSelectionModel.getSelectedItems().get(i).trim().split("\\D+");
                double x = Double.parseDouble(str[1]);
                double y = Double.parseDouble(str[3]);
                points.add(new Point(x, y));
            }
        } catch (Exception e) {
            new Shake(keyList);
        }
        if (points.size() < VerifiableSecretSharing.getK()) {
            new Shake(keyList);
        }
        else {
            ObservableList<String> tableList = FXCollections.observableArrayList();
            for (Point point : points) {
                tableList.add(point.toString());
            }
            keyList2.setItems(tableList);

            secretRes.setText(String.valueOf(VerifiableSecretSharing.lagrangeFunc(0, points)));
            keysSelectionModel.clearSelection();
        }
    }
}
