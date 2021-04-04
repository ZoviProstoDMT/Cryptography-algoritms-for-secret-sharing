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
    private ListView<String> keyList;

    @FXML
    private Button lagrangeFuncBtn;

    @FXML
    private ImageView checkBtn;

    @FXML
    private Label secretRes;

    @FXML
    private Text textInfo;

    @FXML
    private ListView<String> keyList2;

    @FXML
    void initialize() {
        App.setBaseWindowActionsActions(closeBtn, minimizeBtn, backBtn);
        buildData();
        keyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        textInfo.setText("необходимо выбрать " + VerifiableSecretSharing.getK() + " ключей");
        checkBtn.setOnMouseClicked(event -> showSecretResult());
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
        ArrayList<Key> keys = new ArrayList<>();
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

            secretRes.setText(String.valueOf(VerifiableSecretSharing.lagrangeFunc(0, keys)));
            keysSelectionModel.clearSelection();
        }
    }
}
