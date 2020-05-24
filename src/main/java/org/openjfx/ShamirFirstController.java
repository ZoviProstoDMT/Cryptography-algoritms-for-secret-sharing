package org.openjfx;

import SecretShareLogic.VerifiableSecretSharing;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.openjfx.animations.Shake;

import java.io.IOException;

public class ShamirFirstController {

    @FXML
    private Button closeBtn;


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
    private TextArea mainTextArea;

    @FXML
    private ImageView depositBtn;

    @FXML
    void initialize() {

        mainTextArea.editableProperty().setValue(false);

        generateButton.setOnAction(actionEvent -> {
            if (!secretField.getText().equals("") && !pField.getText().equals("") &&
                    !nField.getText().equals("") && !kField.getText().equals("")) {
                VerifiableSecretSharing.setP(Integer.parseInt(pField.getText()));
                VerifiableSecretSharing.g = VerifiableSecretSharing.getPRoot().get(0);
                VerifiableSecretSharing.setSecret(Integer.parseInt(secretField.getText()));
                VerifiableSecretSharing.setN(Integer.parseInt(nField.getText()));
                VerifiableSecretSharing.setK(Integer.parseInt(kField.getText()));
                VerifiableSecretSharing.startMethod();
                try {
                    App.setRoot("shamirSecondPage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                shakeAnimation(secretField);
                shakeAnimation(pField);
                shakeAnimation(nField);
                shakeAnimation(kField);
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

        depositBtn.setOnMouseClicked(event -> {
            mainTextArea.setText("\n\nDeveloped by Dmitriy Kozikov. © 2020\n-\nGitHub: github.com/ZoviProstoDMT\n");
        });
    }

    public void shakeAnimation(TextField textField) {
        if (textField.getText().equals("")) {
            new Shake(textField);
            textField.setStyle("-fx-border-color: red");
        }
        else
            textField.setStyle("-fx-border-color: linear-gradient(to top, rgba(220, 90, 90, 0.5), rgba(26, 72, 172, 0.5));");
    }
}

