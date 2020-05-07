package org.openjfx;

import java.net.URL;
import java.util.ResourceBundle;

import SecretShareLogic.SecretSharing;
import SecretShareLogic.ShamirsSecretSharing;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ShamirSecondController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button generateButton;

    @FXML
    private Label polynomField;

    @FXML
    private TextField polynomField2;

    @FXML
    private Text polynomField3;

    ShamirsSecretSharing shamirsSecretSharing = new ShamirsSecretSharing();

    @FXML
    void initialize() {
         shamirsSecretSharing.start();
//        polynomField.setText(SecretSharing.getPolynom().toString());
//        polynomField2.setText(SecretSharing.getPolynom().toString());
//        polynomField3.setText(SecretSharing.getPolynom().toString());
    }
}
