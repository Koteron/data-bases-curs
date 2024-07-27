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
import java.util.ArrayList;
import java.util.Objects;

public class DataPopUpController {
    private static final ArrayList<String> updateArray = new ArrayList<>();
    @FXML
    private Button closeButton;
    @FXML
    private Button submitButton;
    @FXML
    private AnchorPane anchor;
    private static final Stage stage = new Stage();
    static{
        stage.initModality(Modality.APPLICATION_MODAL);
    }
    public static ArrayList<String> display(String location)
    {
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
        stage.showAndWait();
        return updateArray;
    }

    @FXML
    void initialize()
    {
        closeButton.setOnAction(e-> stage.close());

        submitButton.setOnAction(e-> {
                var anchorElements = anchor.getChildren();
                boolean radioAdded = false;
                boolean radioExists = false;
                for (var element : anchorElements) {
                    if (element.getClass().equals(TextField.class)) {
                        String str = ((TextField) element).getText();
                        if (Objects.equals(str, "")) {
                            updateArray.add(null);
                        }
                        else {
                            updateArray.add(str);
                        }
                    } else if (!radioAdded && element.getClass().equals(RadioButton.class)) {
                        if (((RadioButton) element).isSelected()) {
                            if (Objects.equals((element).getId(), "isForeignButton"))
                            {
                                updateArray.add("true");
                            }
                            else if (Objects.equals((element).getId(), "isOurButton"))
                            {
                                updateArray.add("false");
                            }
                            radioAdded = true;
                        }
                        radioExists = true;
                    }
                    else if (element.getClass().equals(DatePicker.class))
                    {
                        if (((DatePicker)element).getValue() != null)
                        {
                            updateArray.add(((DatePicker)element).getValue().toString());
                        }
                        else
                        {
                            updateArray.add(null);
                        }
                    }
                }
                if (!radioAdded && radioExists)
                {
                    updateArray.add(null);
                }
                stage.close();
            }
        );
    }
}
