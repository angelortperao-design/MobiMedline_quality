package interfaz.mobimedline_sistema;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class VerificacionUsuario {

    @FXML
    private AnchorPane apVentana;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnReenviarCodigo;

    @FXML
    private Label lblMostrarCorreo;

    @FXML
    private Label lblMostrarNombre;

    @FXML
    private TextField txtCodigoTelegram;

    private String nomG, apellidosG, correoG, telefonoG, usuG, passG;

    public void recibirDatos(String nombre, String apellidos, String correo, String telefono, String usuario, String pass) {
        this.nomG = nombre;
        this.apellidosG = apellidos;
        this.correoG = correo;
        this.telefonoG = telefono;
        this.usuG = usuario;
        this.passG = pass;

        if (lblMostrarNombre != null) {
            lblMostrarNombre.setText(nombre + " " + apellidos + " (ID: " + usuario + ")");
        }
        if (lblMostrarCorreo != null) {
            lblMostrarCorreo.setText(correo);
        }
    }

    @FXML
    private void AccionConfirmarGuardado(ActionEvent event) {
        String codigoIngresado = txtCodigoTelegram.getText().trim();

        if (codigoIngresado.isEmpty()) {
            Alert error = new Alert(Alert.AlertType.WARNING);
            error.setTitle("Código Requerido");
            error.setHeaderText(null);
            error.setContentText("Por favor, ingrese el código de 6 dígitos enviado a Telegram.");
            error.showAndWait();
            return;
        }

        // Lógica de registro en lista estática
        Usuarios nuevo = new Usuarios(usuG, nomG, apellidosG, "", passG);
        AgendaUsuariosBase.getUsuariosBase().add(nuevo);

        Alert exito = new Alert(Alert.AlertType.INFORMATION);
        exito.setTitle("Cuenta Activada");
        exito.setHeaderText(null);
        exito.setContentText("El usuario " + usuG + " ha sido verificado y activado exitosamente.");
        exito.showAndWait();

        Stage stage = (Stage) apVentana.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void AccionCancelarGuardado(ActionEvent event) {
        Stage stage = (Stage) apVentana.getScene().getWindow();
        stage.close();
    }
}
