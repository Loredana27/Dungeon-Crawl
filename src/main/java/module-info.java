module com.codecool.dungeoncrawl {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.media;
    requires java.desktop;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.naming;


    opens com.codecool.dungeoncrawl to javafx.fxml;
    exports com.codecool.dungeoncrawl;
}