package com.example.db_curs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class FiveBestController {
    @FXML
    public final static String[] months = {"Январь","Февраль", "Март", "Апрель", "Май",
            "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    public final static int START_YEAR = 2010;

    @FXML
    private Button closeButton;

    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<Integer> yearChoiceBox;

    @FXML
    private Button submitButton;

    private static final ArrayList<String> yearAndMonth = new ArrayList<>();

    private static final Stage stage = new Stage();
    static{
        stage.initModality(Modality.APPLICATION_MODAL);
    }
    public static ArrayList<String> display()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApplication.class.getResource("five-best-screen.fxml"));
        try {
            loader.load();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        Parent root = loader.getRoot();
        stage.setScene(new Scene(root));
        yearAndMonth.clear();
        //stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
        stage.showAndWait();
        return yearAndMonth;
    }

    @FXML
    void initialize()
    {
        for (String month : months)
        {
            monthChoiceBox.getItems().add(month);
        }

        int currentYear = LocalDate.now().getYear();
        for (int year = START_YEAR; year <= currentYear; ++year)
        {
            yearChoiceBox.getItems().add(year);
        }

        yearChoiceBox.setMaxHeight(500);
        closeButton.setOnAction(e-> stage.close());

        submitButton.setOnAction(e-> {
            if (monthChoiceBox.getValue() == null || yearChoiceBox.getValue() == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Одно из полей пусто!");
                alert.setContentText("Пожалуйста, введите и год и месяц!");
                alert.showAndWait();
            }
            else
            {
                yearAndMonth.add(yearChoiceBox.getValue().toString());
                yearAndMonth.add(monthChoiceBox.getValue());
                stage.close();
            }
        });
    }
}