module interfaz.mobimedline_sistema {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires jbcrypt;
    requires java.net.http;
    
    opens interfaz.mobimedline_sistema.model to javafx.base;

    opens interfaz.mobimedline_sistema to javafx.fxml;
    opens interfaz.mobimedline_sistema.controllers to javafx.fxml;
    
    exports interfaz.mobimedline_sistema;
}
