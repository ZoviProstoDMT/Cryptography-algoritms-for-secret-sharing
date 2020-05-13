package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import SecretShareLogic.SecretSharing;
import SecretShareLogic.VerifiableSecretSharing;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ShamirFirstController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView backBtnIcon1;

    @FXML
    private Button generateButton;

    @FXML
    private TextField secretField;

    @FXML
    private TextField pField;

    @FXML
    private TextField kField;

    @FXML
    private TextField nField;

    @FXML
    private Button minimizeBtn;

    @FXML
    private ImageView backBtnIcon11;

    @FXML
    void initialize() {

        generateButton.setOnAction(actionEvent -> {
//            VerifiableSecretSharing.setSecret(Integer.parseInt(secretField.getText()));
//            VerifiableSecretSharing.setP(Integer.parseInt(pField.getText()));
//            VerifiableSecretSharing.setN(Integer.parseInt(nField.getText()));
//            VerifiableSecretSharing.setK(Integer.parseInt(kField.getText()));
            VerifiableSecretSharing.startMethod();
            try {
                App.setRoot("shamirSecondPage");
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

    }
}
