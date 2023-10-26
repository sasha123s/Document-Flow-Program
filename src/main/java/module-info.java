module isedPackage {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens isedPackage to javafx.fxml;
    exports isedPackage;
}