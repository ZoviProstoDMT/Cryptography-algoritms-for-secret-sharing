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
import java.math.BigInteger;

public class TestKeysController {

    @FXML
    private Button minimizeBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private Button generateSecret;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView backBtnIcon;

    @FXML
    private ListView<String> keyList;

    @FXML
    private Button lagrangeFuncBtn;

    @FXML
    private TextField keyInput;

    @FXML
    private AnchorPane choosenKeyField;

    @FXML
    private Label choosenKey;

    @FXML
    private Text positiveRes;

    @FXML
    private Text negativeRes;

    @FXML
    private ImageView checkBtn;

    @FXML
    void initialize() {
        buildData();

        checkBtn.setOnMouseClicked(event -> {
            showResult();
        });

        generateSecret.setOnAction(actionEvent -> {
            try {
                App.setRoot("decryptSecretPage");
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

        generateSecret.setOnAction(actionEvent -> {
            try {
                App.setRoot("decryptSecretPage");
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

    public void showResult() {
        MultipleSelectionModel<String> keysSelectionModel = keyList.getSelectionModel();
        if (keyInput.getText().equals("") && keysSelectionModel.getSelectedItems().isEmpty()) {
            new Shake(keyList);
            new Shake(keyInput);
        }
        else {
            if (keyInput.getText().equals("")) {
                String[] str = keysSelectionModel.getSelectedItems().toString().trim().split("\\D+");
                long x = Long.parseLong(str[1]);
                BigInteger y = new BigInteger(str[2]);
                testKeyResult(x, y);
            }
            else {
                String[] str = keyInput.getText().trim().split("\\D+");
                long x = Long.parseLong(str[0]);
                BigInteger y = new BigInteger(str[1]);
                testKeyResult(x, y);
            }
        }
        keyInput.setText("");
        keysSelectionModel.clearSelection();
    }

    public void testKeyResult(long x, BigInteger y) {
        Point point = new Point(x, y);
        choosenKey.setText(point.toString());
        if (VerifiableSecretSharing.testKey(point)) {
            choosenKeyField.setStyle("-fx-border-color: linear-gradient(to bottom, lawngreen, green)");
            negativeRes.setStyle("-fx-opacity: 0");
            positiveRes.setStyle("-fx-opacity: 0.5");
        } else {
            choosenKeyField.setStyle("-fx-border-color: linear-gradient(to bottom, red, darkred)");
            positiveRes.setStyle("-fx-opacity: 0");
            negativeRes.setStyle("-fx-opacity: 0.5");
        }
    }
}
