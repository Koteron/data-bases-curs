package com.example.db_curs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import java.sql.*;

public class TableViewerController {
    private static final int TABLE_AMOUNT = 3;
    private static final String DB_CONFIG_PATH ="./src/main/resources/com/example/db_curs/config.txt";
    private static final String OUTPUT_FILE_PATH = "./src/main/resources/com/example/db_curs/output.txt";
    @FXML
    private TableView<Row> table;
    private static final String[] tableNames = {"cars", "services", "masters", "works"};
    private static final String[][] tableColumnDBNames = {{"num", "color", "mark", "is_foreign"},
            {"name", "cost_our", "cost_foreign"},
            {"name"},
            {"date_work", "car_id", "service_id", "master_id"}};
    private int currentTableIndex;
    @FXML
    private MenuItem sumMenuItem;
    @FXML
    private MenuItem fiveBestMenuItem;
    @FXML
    private MenuItem journalMenuItem;
    @FXML
    private MenuItem carsMenuItem;
    @FXML
    private MenuItem servicesMenuItem;
    @FXML
    private MenuItem mastersMenuItem;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    private Connection SQLConnection;

    private void updateTable()
    {
        switch (currentTableIndex) {
            case 0 -> loadCars();
            case 1 -> loadServices();
            case 2 -> loadMasters();
            case 3 -> loadJournal();
        }
    }
    public void loadJournal()
    {
        TableColumn<Row, String> id = new TableColumn<>("id");
        TableColumn<Row, String> date_work = new TableColumn<>("Дата");
        TableColumn<Row, String> master_id = new TableColumn<>("Номер автомобиля");
        TableColumn<Row, String> car_id = new TableColumn<>("Услуга");
        TableColumn<Row, String> service_id = new TableColumn<>("Мастер");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_work.setCellValueFactory(new PropertyValueFactory<>("column1"));
        master_id.setCellValueFactory(new PropertyValueFactory<>("column2"));
        car_id.setCellValueFactory(new PropertyValueFactory<>("column3"));
        service_id.setCellValueFactory(new PropertyValueFactory<>("column4"));
        table.getColumns().clear();
        table.getColumns().addAll(id, date_work, master_id, car_id, service_id);
        ResultSet rs;
        try {
            PreparedStatement st = SQLConnection.prepareStatement(
                    """
                            SELECT works.id, date_work, cars.num, services.name, masters.name FROM works\s
                                                        LEFT JOIN cars ON works.car_id = cars.id\s
                                                        LEFT JOIN services ON works.service_id = services.id\s
                                                        LEFT JOIN masters ON works.master_id = masters.id\s
                                                        ORDER BY works.id""");
            rs = st.executeQuery();
            ObservableList<Row> data = FXCollections.observableArrayList();
            while (rs.next())
            {
                data.add(new Row(rs.getInt(1),
                        Objects.requireNonNullElse(rs.getDate(2), "-").toString(),
                        Objects.requireNonNullElse(rs.getString(3), "-"),
                        Objects.requireNonNullElse(rs.getString(4), "-"),
                        Objects.requireNonNullElse(rs.getString(5), "-")));
            }
            table.setItems(data);
            currentTableIndex = 3;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadCars()
    {
        TableColumn<Row, String> id = new TableColumn<>("id");
        TableColumn<Row, String> num = new TableColumn<>("Номер автомобиля");
        TableColumn<Row, String> color = new TableColumn<>("Цвет");
        TableColumn<Row, String> mark = new TableColumn<>("Марка");
        TableColumn<Row, String> isForeign = new TableColumn<>("Инностранный");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        num.setCellValueFactory(new PropertyValueFactory<>("column1"));
        color.setCellValueFactory(new PropertyValueFactory<>("column2"));
        mark.setCellValueFactory(new PropertyValueFactory<>("column3"));
        isForeign.setCellValueFactory(new PropertyValueFactory<>("column4"));
        table.getColumns().clear();
        table.getColumns().addAll(id, num, color, mark, isForeign);
        ResultSet rs;
        try {
            PreparedStatement st = SQLConnection.prepareStatement("SELECT * FROM cars ORDER BY id");
            rs = st.executeQuery();
            ObservableList<Row> data = FXCollections.observableArrayList();
            while (rs.next())
            {
                String isForeignStr;
                if (rs.getBoolean(5))
                {
                    isForeignStr = "Да";
                }
                else {
                    isForeignStr = "Нет";
                }
                data.add(new Row(rs.getInt(1),
                        Objects.requireNonNullElse(rs.getString(2), "-"),
                        Objects.requireNonNullElse(rs.getString(3), "-"),
                        Objects.requireNonNullElse(rs.getString(4), "-"),
                        isForeignStr));
            }
            table.setItems(data);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        currentTableIndex = 0;
    }

    public void loadMasters()
    {
        TableColumn<Row, String> id = new TableColumn<>("id");
        TableColumn<Row, String> name = new TableColumn<>("Мастер");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("column1"));
        table.getColumns().clear();
        table.getColumns().addAll(id, name);
        ResultSet rs;
        try {
            PreparedStatement st = SQLConnection.prepareStatement("SELECT * FROM masters ORDER BY id");
            rs = st.executeQuery();
            ObservableList<Row> data = FXCollections.observableArrayList();
            while (rs.next())
            {
                data.add(new Row(rs.getInt(1),
                        Objects.requireNonNullElse(rs.getString(2), "-"), null, null, null));
            }
            table.setItems(data);
            currentTableIndex = 2;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadServices()
    {
        TableColumn<Row, String> id = new TableColumn<>("id");
        TableColumn<Row, String> name = new TableColumn<>("Название");
        TableColumn<Row, String> costOur = new TableColumn<>("Стоимость отеч.");
        TableColumn<Row, String> costForeign = new TableColumn<>("Стоимость иностр.");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("column1"));
        costOur.setCellValueFactory(new PropertyValueFactory<>("column2"));
        costForeign.setCellValueFactory(new PropertyValueFactory<>("column3"));
        table.getColumns().clear();
        table.getColumns().addAll(id, name, costOur, costForeign);
        ResultSet rs;
        try {
            PreparedStatement st = SQLConnection.prepareStatement("SELECT * FROM services ORDER BY id");
            rs = st.executeQuery();
            ObservableList<Row> data = FXCollections.observableArrayList();
            while (rs.next())
            {
                data.add(new Row(rs.getInt(1),
                        Objects.requireNonNullElse(rs.getString(2), "-"),
                        String.valueOf(rs.getDouble(3)),
                        String.valueOf(rs.getDouble(4)),
                        null));
            }
            table.setItems(data);
            currentTableIndex = 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private ArrayList<String> getUpdateData()
    {
        ArrayList<String> updateData = new ArrayList<>();
        switch (currentTableIndex) {
            case 0 -> updateData = DataPopUpController.display("car-update-screen.fxml");
            case 1 -> updateData = DataPopUpController.display("service-update-screen.fxml");
            case 2 -> updateData = DataPopUpController.display("master-update-screen.fxml");
            case 3 -> {
                ResultSet[] rs = new ResultSet[TABLE_AMOUNT];
                try {
                    for (int i = 0; i < TABLE_AMOUNT; ++i) {
                        PreparedStatement st = SQLConnection.prepareStatement("SELECT * FROM " + tableNames[i],
                                ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);
                        rs[i] = st.executeQuery();
                    }
                    updateData = WorkUpdateController.display("work-update-screen.fxml", rs);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return updateData;
    }

    void writeToOutputFile(String output)
    {
        File file = new File(OUTPUT_FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() != 0)
            {
                writer.write("\n");
            }
            writer.write(output);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean hasNoNullElements(List<?> list)
    {
        for (var str : list)
        {
            if (str == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isNumber(String str)
    {
        try
        {
            Integer.parseInt(str);
        }
        catch(Exception exc)
        {
            try {
                if (str.contains("D") || str.contains("d"))
                {
                    throw new NumberFormatException("D or d found");
                }
                Double.parseDouble(str);
            }
            catch(Exception ex)
            {
                return false;
            }
        }
        return true;
    }

    private boolean checkUpdateData(ArrayList<String> updateData)
    {
        if (updateData.isEmpty())
        {
            return true;
        }
        switch (currentTableIndex)
        {
            case 0 -> {
                return updateData.get(3) == null || (Objects.equals(updateData.get(3), "true") ||
                        Objects.equals(updateData.get(3), "false"));
            }
            case 1 -> {
                for (int i = 1; i <= 2; ++i)
                {
                    if (updateData.get(i) != null && !isNumber(updateData.get(i)))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkUserRights()
    {
        if (AuthorisationController.fullControl)
        {
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("В доступе отказано");
        alert.setHeaderText("Вы не имеете достаточного уровня доступа");
        alert.setContentText("Вашему аккаунту разрешено только формировать отчёты и просматривать таблицы," +
                " но не изменять их");
        alert.showAndWait();
        return false;
    }

    private boolean checkTriggers(ArrayList<String> updateData, Integer id) throws SQLException {
        String checkQuery = "";
        if (updateData.size() != 0 &&
                !(currentTableIndex == 0 && updateData.get(0) == null) &&
                !(currentTableIndex == 3 && updateData.get(0) == null && updateData.get(3) == null)) {
            switch (currentTableIndex) {
                case 0 -> {
                    if (id != null)
                    {
                        checkQuery = "SELECT * FROM car_num_exists_bool_id('" + updateData.get(0) + "', " + id + ")";
                    }
                    else
                    {
                        checkQuery = "SELECT * FROM car_num_exists_bool('" + updateData.get(0) + "')";
                    }
                }
                case 2 -> checkQuery = id == null ? "SELECT * FROM is_there_10_masters()" : "";
                case 3 -> {
                    if (updateData.get(0) != null && updateData.get(3) != null)
                    {
                        checkQuery = "SELECT * FROM is_there_more_than_1_work(" +
                                updateData.get(3)  + ", '" +
                                updateData.get(0) + "')";
                    }
                    else {
                        int master_id = -1;
                        String date = "";
                        if (updateData.get(3) == null) {
                            PreparedStatement st = SQLConnection.prepareStatement("SELECT master_id FROM works" +
                                    " WHERE id = " + id);
                            ResultSet rs = st.executeQuery();
                            rs.next();
                            master_id = rs.getInt(1);
                        }
                        else
                        {
                            master_id = Integer.parseInt(updateData.get(3));
                        }
                        if (updateData.get(0) == null) {
                            PreparedStatement st = SQLConnection.prepareStatement("SELECT date_work FROM works" +
                                    " WHERE id = " + id);
                            ResultSet rs = st.executeQuery();
                            rs.next();
                            date = rs.getDate(1).toString();
                        }
                        else
                        {
                            date = updateData.get(0);
                        }
                        checkQuery = "SELECT * FROM is_there_more_than_1_work(" + master_id + ", '" + date + "')";
                    }
                }
            }
        }
        if (!checkQuery.equals("")) {
            PreparedStatement st = SQLConnection.prepareStatement(checkQuery);
            System.out.println(checkQuery);
            ResultSet rs = st.executeQuery();
            rs.next();
            boolean passed = !rs.getBoolean(1);
            System.out.println(passed);
            return passed;
        }
        return true;
    }

    @FXML
    void initialize()
    {
        String url;
        String user;
        String password;

        try (BufferedReader reader = new BufferedReader(new FileReader(DB_CONFIG_PATH))) {
            url = reader.readLine();
            user = reader.readLine();
            password = reader.readLine();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return;
        }

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        try {
            SQLConnection = DriverManager.getConnection(url, props);
            System.out.println("Connection established!");
            loadJournal();
        }
        catch(SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось подключиться к базе данных!");
            alert.setContentText("Пожалуйста, проверьте настройки подключения!");
            alert.showAndWait();
            System.exit(1);
        }
        journalMenuItem.setOnAction(e-> loadJournal());
        carsMenuItem.setOnAction(e-> loadCars());
        servicesMenuItem.setOnAction(e-> loadServices());
        mastersMenuItem.setOnAction(e-> loadMasters());

        deleteButton.setOnAction(e->{
            if (checkUserRights()) {
                var select = table.getSelectionModel().getSelectedItem();
                if (select != null) {
                    try {
                        String query = "DELETE FROM " + tableNames[currentTableIndex] + " WHERE id = " + select.getId();
                        System.out.println(query);
                        PreparedStatement st = SQLConnection.prepareStatement(query);
                        st.executeUpdate();
                        updateTable();
                    } catch (SQLException ex) {
                        if (ex.getMessage().contains("is still referenced from table"))
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Невозможно удалить запись");
                            alert.setHeaderText("Данная запись всё еще упоминается в некоторых записях журнала");
                            alert.setContentText("Каскадное удаление работает только для автомобилей");
                            alert.showAndWait();
                        }
                        else
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        editButton.setOnAction(e -> {
            if (checkUserRights() && table.getSelectionModel().getSelectedItem() != null) {
                StringBuilder query = new StringBuilder("UPDATE " + tableNames[currentTableIndex] + " SET ");
                ArrayList<String> updateData = getUpdateData();
                if (!checkUpdateData(updateData)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Неверные значения в текстовых полях!");
                    alert.setContentText("Возможно вы ввели букву в текстовое поле для чисел");
                    alert.showAndWait();
                } else {
                    try {
                        if (checkTriggers(updateData, table.getSelectionModel().getSelectedItem().getId())) {
                            for (var element : updateData) {
                                if (element != null) {
                                    int lastNonNull = 0;
                                    for (int i = 0; i < updateData.size(); ++i) {
                                        if (updateData.get(i) != null) {
                                            lastNonNull = i;
                                        }
                                    }
                                    for (int i = 0; i < updateData.size(); ++i) {
                                        String str = updateData.get(i);
                                        if (str != null) {
                                            query.append(tableColumnDBNames[currentTableIndex][i]).append(" = ");
                                            if (isNumber(str))
                                            {
                                                query.append(str);
                                            }
                                            else
                                            {
                                                query.append("'").append(str).append("'");
                                            }
                                            if (i != lastNonNull) {
                                                query.append(", ");
                                            }
                                        }
                                    }
                                    query.append(" WHERE id = ").append(table.getSelectionModel().getSelectedItem().getId());
                                    System.out.println(query);
                                    PreparedStatement st = SQLConnection.prepareStatement(query.toString());
                                    System.out.println(st.executeUpdate());
                                    updateTable();
                                    break;
                                }
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            switch (currentTableIndex) {
                                case 0 -> {
                                    alert.setHeaderText("Автомобиль с таким номером уже существует!");
                                    //alert.setContentText("Пожалуйста, введите другой номер");
                                }
                                case 2 -> {
                                    alert.setHeaderText("Слишком много мастеров!");
                                    alert.setContentText("Количество мастеров не должно первышать 10");
                                }
                                case 3 -> {
                                    alert.setHeaderText("Слишком много работ для мастера!");
                                    alert.setContentText("У данного мастера уже есть более одной работы в заданный день. " +
                                            "Пожалуйста, выберете другой день");
                                }
                            }
                            alert.showAndWait();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        addButton.setOnAction(e -> {
            if (checkUserRights()) {
                ArrayList<String> updateData = getUpdateData();
                if (!updateData.isEmpty()) {
                    if (!checkUpdateData(updateData)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Неверные значения в текстовых полях!");
                        alert.setContentText("Возможно вы ввели букву в текстовое поле для чисел");
                        alert.showAndWait();
                    } else {
                        try {
                            if (checkTriggers(updateData, null)) {
                                StringBuilder query = new StringBuilder("INSERT INTO " + tableNames[currentTableIndex] + "(");
                                for (int i = 0; i < tableColumnDBNames[currentTableIndex].length; ++i) {
                                    query.append(tableColumnDBNames[currentTableIndex][i]);
                                    if (i != tableColumnDBNames[currentTableIndex].length - 1) {
                                        query.append(", ");
                                    }
                                }
                                query.append(") VALUES (");
                                for (int i = 0; i < updateData.size(); ++i) {
                                    String data = updateData.get(i);
                                    if (data == null) {
                                        query.append("null");
                                    } else {
                                        try {
                                            Integer.parseInt(data);
                                            query.append(data);
                                        } catch (Exception ex) {
                                            query.append("'").append(data).append("'");
                                        }
                                    }
                                    if (i != updateData.size() - 1) {
                                        query.append(", ");
                                    }
                                }
                                query.append(")");
                                PreparedStatement st = SQLConnection.prepareStatement(query.toString());
                                System.out.println(query);
                                st.executeUpdate();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Ошибка");
                                switch (currentTableIndex) {
                                    case 0 -> {
                                        alert.setHeaderText("Автомобиль с таким номером уже существует!");
                                        //alert.setContentText("Пожалуйста, введите другой номер");
                                    }
                                    case 2 -> {
                                        alert.setHeaderText("Слишком много мастеров!");
                                        alert.setContentText("Количество мастеров не должно первышать 10");
                                    }
                                    case 3 -> {
                                        alert.setHeaderText("Слишком много работ для мастера!");
                                        alert.setContentText("У данного мастера уже есть более одной работы в заданный день. " +
                                                "Пожалуйста, выберете другой день");
                                    }
                                }

                                alert.showAndWait();
                            }
                        } catch (SQLException exc) {
                            exc.printStackTrace();
                        }
                        updateTable();
                    }
                }
            }
        });

        fiveBestMenuItem.setOnAction(e -> {
            ArrayList<String> yearAndMonth = FiveBestController.display();
            if (!yearAndMonth.isEmpty() && hasNoNullElements(yearAndMonth)) {
                short monthNumber = 0;
                while (monthNumber < FiveBestController.months.length) {
                    if (Objects.equals(FiveBestController.months[monthNumber], yearAndMonth.get(1))) {
                        break;
                    }
                    ++monthNumber;
                }
                StringBuilder date = new StringBuilder("'" + yearAndMonth.get(0) + "-");
                if (monthNumber < 9) {
                    date.append(0);
                }
                date.append((monthNumber + 1)).append("-01'");
                String query = "SELECT * FROM find_five_best_masters(" + date + ")";
                System.out.println(query);
                try {
                    PreparedStatement st = SQLConnection.prepareStatement(query);
                    ResultSet rs = st.executeQuery();
                    writeToOutputFile("Отчёт за " + FiveBestController.months[monthNumber] + ", " + yearAndMonth.get(0) +
                            "\nМастер     Число обслуженных автомобилей");
                    while (rs.next()) {
                        writeToOutputFile(rs.getString(1) + "     " + rs.getString(2));
                    }
                    writeToOutputFile("\n");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        sumMenuItem.setOnAction(e -> {
            ArrayList<String> startAndEndDates = DataPopUpController.display("sum-on-interval.fxml");
            if (!startAndEndDates.isEmpty() && hasNoNullElements(startAndEndDates)) {
                String query = "SELECT * FROM find_sum_on_interval('" + startAndEndDates.get(0) + "', '" +
                        startAndEndDates.get(1) + "')";
                System.out.println(query);
                writeToOutputFile("Общая стоимость обслуживания отечественных и импортных автомобилей от '"
                        + startAndEndDates.get(0) + "' до '" + startAndEndDates.get(1) + "':");
                try {
                    PreparedStatement st = SQLConnection.prepareStatement(query);
                    ResultSet rs = st.executeQuery();
                    rs.next();
                    writeToOutputFile(rs.getString(1));
                    writeToOutputFile("\n");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
