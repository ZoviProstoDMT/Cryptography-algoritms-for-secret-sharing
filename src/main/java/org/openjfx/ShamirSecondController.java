package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import SecretShareLogic.SecretSharing;
import SecretShareLogic.ShamirsSecretSharing;
import SecretShareLogic.VerifiableSecretSharing;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ShamirSecondController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView closeBtn;

    @FXML
    private Button generateButton;

    @FXML
    private Button backBtn;

    @FXML
    private Label polynomField;

    @FXML
    private ImageView backBtnIcon;

    @FXML
    private ListView<?> keyList;

    VerifiableSecretSharing verifiableSecretSharing = new VerifiableSecretSharing();

    @FXML
    void initialize() {
        verifiableSecretSharing.startMethod();
        polynomField.setText(polynomField.getText() + VerifiableSecretSharing.polynom.toString());
    }
}
