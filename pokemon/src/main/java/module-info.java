module com.pokemon {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires transitive javafx.graphics;
    requires javafx.base;
    

    opens com.pokemon to javafx.fxml;
    exports com.pokemon;
}
