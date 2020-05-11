package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import SecretShareLogic.Point;
import SecretShareLogic.SecretSharing;
import SecretShareLogic.ShamirsSecretSharing;
import SecretShareLogic.VerifiableSecretSharing;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ShamirSecondController {

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
    private Label polynomField;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView backBtnIcon;

    @FXML
    private ListView<String> keyList;

    VerifiableSecretSharing verifiableSecretSharing = new VerifiableSecretSharing();

    @FXML
    void initialize() {
        verifiableSecretSharing.startMethod();
        polynomField.setText(polynomField.getText() + VerifiableSecretSharing.polynom.toString());

        ObservableList<Point> keys = FXCollections.observableArrayList(VerifiableSecretSharing.xyKey);
        keyList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


    }
}
