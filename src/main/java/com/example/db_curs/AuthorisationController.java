package com.example.db_curs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorisationController {

    final static HashMap<String, String> authorizationMap = new HashMap<>();
    final static HashMap<String, Boolean> userAccessMap = new HashMap<>();
    static boolean fullControl = false;
    static {
        authorizationMap.putAll(Stream.of(new String[][] {
                { "0a041b9462caa4a31bac3567e0b6e6fd9100787db2ab433d96f6d178cabfce90",
                        "65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5" },
                { "6025d18fe48abd45168528f18a82e265dd98d421a7084aa09f61b341703901a3",
                        "1e08e2ebac699af4054e21432bc9a1df1e96011ab607c542199404573bfc48a1" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1])));
        userAccessMap.put("0a041b9462caa4a31bac3567e0b6e6fd9100787db2ab433d96f6d178cabfce90", true);
        userAccessMap.put("6025d18fe48abd45168528f18a82e265dd98d421a7084aa09f61b341703901a3", false);
    }
    public static String getHash(String str)
    {
        String result = null;
        try {
            MessageDigest strDigest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = strDigest.digest(
                    str.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            result = hexString.toString();
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    void initialize()
    {
        submitButton.setOnAction(e->{
            String login = loginField.getText();
            String password = passwordField.getText();
            if (Objects.equals(login, ""))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Имя пользователя не найдено!");
                alert.setContentText("Пожалуйста, введите имя пользователя");
                alert.showAndWait();
            }
            else if (Objects.equals(password, ""))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Пароль не найден");
                alert.setContentText("Пожалуйста, введите пароль");
                alert.showAndWait();
            }
            else
            {
                if (Objects.equals(authorizationMap.get(getHash(login)), getHash(password)))
                {
                    fullControl = userAccessMap.get(getHash(login));
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApplication.class.getResource("table-screen.fxml"));
                    try {
                        loader.load();
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    if (fullControl) {
                        stage.setTitle("Окно работы с базой данных (Полный доступ)");
                    }
                    else {
                        stage.setTitle("Окно работы с базой данных (Ограниченный доступ)");
                    }
                    stage.setScene(new Scene(root));
                    stage.show();
                    ((Node)(e.getSource())).getScene().getWindow().hide();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Неверные данные авторизации");
                    alert.setHeaderText("Пользователь не найден!");
                    alert.setContentText("Пользователя с таким именем и паролем не существует");
                    alert.showAndWait();
                }
            }

        });
    }
}