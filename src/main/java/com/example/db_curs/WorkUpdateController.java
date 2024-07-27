package com.example.db_curs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class WorkUpdateController {
    private static final ArrayList<String> updateArray = new ArrayList<>();
    @FXML
    private ChoiceBox<String> masterChoiceBox;
    @FXML
    private ChoiceBox<String> carChoiceBox;
    @FXML
    private ChoiceBox<String> serviceChoiceBox;
    @FXML
    private DatePicker dateWorkPicker;
    @FXML
    private Button closeButton;
    @FXML
    private Button submitButton;
    @FXML
    private AnchorPane anchor;
    private static ResultSet[] resultSetArray;
    private static final Stage stage = new Stage();
    static{
        stage.initModality(Modality.APPLICATION_MODAL);
    }
    public static ArrayList<String> display(String location, ResultSet[] rsa)
    {
        resultSetArray = rsa;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApplication.class.getResource(location));
        try {
            loader.load();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        Parent root = loader.getRoot();
        stage.setScene(new Scene(root));
        updateArray.clear();
        //stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
        stage.showAndWait();
        return updateArray;
    }

    @FXML
    void initialize()
    {
        try {
            // I don't like this, but it works, sooooo...
            while (resultSetArray[0].next()) {
                carChoiceBox.getItems().add(resultSetArray[0].getString(2));
            }
            while (resultSetArray[1].next()) {
                serviceChoiceBox.getItems().add(resultSetArray[1].getString(2));
            }
            while (resultSetArray[2].next()) {
                masterChoiceBox.getItems().add(resultSetArray[2].getString(2));
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        closeButton.setOnAction(e-> stage.close());

        // I don't like this either, but IDK
        submitButton.setOnAction(e-> {
            try {
                if (dateWorkPicker.getValue() != null) {
                    updateArray.add(dateWorkPicker.getValue().toString());
                } else {
                    updateArray.add(null);
                }
                if (carChoiceBox.getValue() != null) {
                    while (resultSetArray[0].previous())
                    {
                        if (Objects.equals(carChoiceBox.getValue(),resultSetArray[0].getString(2)))
                        {
                            updateArray.add(String.valueOf(resultSetArray[0].getInt(1)));
                        }
                    }
                } else {
                    updateArray.add(null);
                }
                if (serviceChoiceBox.getValue() != null) {
                    while (resultSetArray[1].previous())
                    {
                        if (Objects.equals(serviceChoiceBox.getValue(),resultSetArray[1].getString(2)))
                        {
                            updateArray.add(String.valueOf(resultSetArray[1].getInt(1)));
                        }
                    }
                } else {
                    updateArray.add(null);
                }
                if (masterChoiceBox.getValue() != null) {
                    while (resultSetArray[2].previous())
                    {
                        if (Objects.equals(masterChoiceBox.getValue(),resultSetArray[2].getString(2)))
                        {
                            updateArray.add(String.valueOf(resultSetArray[2].getInt(1)));
                        }
                    }
                } else {
                    updateArray.add(null);
                }
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }
                stage.close();
            }
        );
    }
}
