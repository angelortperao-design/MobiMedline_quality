package interfaz.mobimedline_sistema.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;

public class ViewManager{

    public static void cargarVistaCentro(AnchorPane contenedorCentro, String nombreArchivo) {
        try {
            String ruta = "/interfaz/mobimedline_sistema/" + nombreArchivo + ".fxml";
            URL url = ViewManager.class.getResource(ruta);

            if (url == null) {
                System.err.println("No se encontró el archivo en: " + ruta);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Node vista = loader.load();

            AnchorPane.setTopAnchor(vista, 0.0);
            AnchorPane.setBottomAnchor(vista, 0.0);
            AnchorPane.setLeftAnchor(vista, 0.0);
            AnchorPane.setRightAnchor(vista, 0.0);

            contenedorCentro.getChildren().setAll(vista);

        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + e.getMessage());
            e.printStackTrace();
        }
    }
}