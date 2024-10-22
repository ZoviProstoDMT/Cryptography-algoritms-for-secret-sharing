package org.openjfx;

import SecretShareLogic.Key;
import SecretShareLogic.VerifiableSecretSharing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.openjfx.animations.Shake;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class LagrangeController {

    @FXML
    private Button minimizeBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private Button testKeyBtn;

    @FXML
    private Button backBtn;

    @FXML
    private ListView<String> keyList;

    @FXML
    private Button generateSecretBtn;

    @FXML
    private ImageView checkBtn;

    @FXML
    private Text textInfo;

    @FXML
    private ListView<String> keyList2;

    @FXML
    private TextField inputX;

    @FXML
    private Text yRes;

    @FXML
    private TextArea polynomRes;

    @FXML
    void initialize() {
        App.setBaseWindowActionsActions(closeBtn, minimizeBtn, backBtn);
        keyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        textInfo.setText("необходимо выбрать " + VerifiableSecretSharing.getK() + " ключей");
        buildData();
        checkBtn.setOnMouseClicked(event -> showPolynomResult());
        testKeyBtn.setOnAction(actionEvent -> {
            try {
                App.setRoot("testKeyPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        generateSecretBtn.setOnAction(actionEvent -> {
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

    public void showPolynomResult() {
        MultipleSelectionModel<String> keysSelectionModel = keyList.getSelectionModel();
        ArrayList<Key> keys = new ArrayList<>();
        if (inputX.getText().equals(""))
            new Shake(inputX);
        else {
            try {
                for (int i = 0; i < VerifiableSecretSharing.getK(); i++) {
                    String[] str = keysSelectionModel.getSelectedItems().get(i).trim().split("\\D+");
                    long x = Long.parseLong(str[1]);
                    BigInteger y = new BigInteger(str[2]);
                    keys.add(new Key(x, y));
                }
            } catch (Exception e) {
                new Shake(keyList);
            }
            if (keys.size() < VerifiableSecretSharing.getK()) {
                new Shake(keyList);
            } else {
                ObservableList<String> tableList = FXCollections.observableArrayList();
                for (Key key : keys) {
                    tableList.add(key.toString());
                }
                keyList2.setItems(tableList);

                polynomRes.setText("f(x) = " + VerifiableSecretSharing.polynom.toString()
                        + "    (mod " + VerifiableSecretSharing.p + ")");
                polynomRes.editableProperty().setValue(false);
                String[] str = inputX.getText().trim().split("\\D+");
                yRes.setText("Для Х = " + str[0] + ", Y = " +
                        VerifiableSecretSharing.lagrangeFunc(Long.parseLong(str[0]), keys));
                keysSelectionModel.clearSelection();
            }
        }
    }
}
