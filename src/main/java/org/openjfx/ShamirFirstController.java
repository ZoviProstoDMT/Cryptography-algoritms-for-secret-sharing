package org.openjfx;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ShamirFirstController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField secretField;

    @FXML
    private TextField fField;

    @FXML
    private TextField nField;

    @FXML
    private TextField kField;

    @FXML
    private Button generateButton;

    @FXML
    private ImageView closeBtn;

    @FXML
    void initialize() {

        generateButton.setOnAction(actionEvent -> {
//            SecretSharing.setSecret(Integer.parseInt(secretField.getText()));
//            SecretSharing.setF(Integer.parseInt(fField.getText()));
//            SecretSharing.setN(Integer.parseInt(nField.getText()));
//            SecretSharing.setK(Integer.parseInt(kField.getText()));
            try {
                App.setRoot("shamirSecondPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
