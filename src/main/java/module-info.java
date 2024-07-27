module com.example.db_curs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.db_curs to javafx.fxml;
    exports com.example.db_curs;
}