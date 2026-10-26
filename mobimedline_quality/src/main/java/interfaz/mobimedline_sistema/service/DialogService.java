package interfaz.mobimedline_sistema.service;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;

public class DialogService {

    /**
     * Muestra una alerta estándar vinculada a la ventana actual para evitar que se oculte al fondo.
     * 
     * @param tipo El tipo de alerta (INFORMATION, ERROR, WARNING, etc.)
     * @param titulo El título de la ventana de alerta.
     * @param mensaje El contenido o texto del mensaje.
     * @param nodoReferencia Cualquier componente gráfico (Node) de la ventana actual para obtener el Stage.
     */
    public static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje, Node nodoReferencia) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Vincula la alerta al escenario principal para asegurar el enfoque correcto
        if (nodoReferencia != null && nodoReferencia.getScene() != null) {
            Stage stageActual = (Stage) nodoReferencia.getScene().getWindow();
            alert.initOwner(stageActual);
        }

        alert.showAndWait();
    }
    
    /**
     * Muestra una ventana de confirmación reutilizable vinculada al nodo de referencia.
     */
    public static boolean mostrarConfirmacion(String titulo, String mensaje, Node nodoReferencia) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        if (nodoReferencia != null && nodoReferencia.getScene() != null) {
            Stage stageActual = (Stage) nodoReferencia.getScene().getWindow();
            alert.initOwner(stageActual);
        }

        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}