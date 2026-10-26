package interfaz.mobimedline_sistema.controllers;

import interfaz.mobimedline_sistema.App;
import interfaz.mobimedline_sistema.CamposIncompletosException;
import interfaz.mobimedline_sistema.ContrasenasNoCoincidenException;
import interfaz.mobimedline_sistema.NombreConNumerosException;
import interfaz.mobimedline_sistema.controllers.VerificacionUsuario;
import interfaz.mobimedline_sistema.service.DialogService; // <--- Importamos nuestro servicio centralizado
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controlador para la pantalla de Registro de Mobimedline
 * @author engel
 */
public class RegistroController implements Initializable {

    @FXML
    private Button btnSolicitarRegistro;

    @FXML
    private Hyperlink linkVolverLogin;

    @FXML
    private TextField txtApellidos;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtNombre;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtTelefono;

    @FXML
    private void irLogin(ActionEvent event) {
        try {
            App.setRoot("IniciarSesionView");
        } catch (IOException e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void accionSolicitarRegistro(ActionEvent event) {
        try {
            validarFormulario();
            String usuarioGenerado = obtenerUsuario();
            procesarAperturaVentanaModal(usuarioGenerado);

        } catch (CamposIncompletosException e) {
            DialogService.mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos", e.getMessage(), txtNombre);

        } catch (ContrasenasNoCoincidenException e) {
            DialogService.mostrarAlerta(Alert.AlertType.ERROR, "Error de Contraseña", e.getMessage(), txtPassword);
            txtPassword.clear();
            txtConfirmPassword.clear();

        } catch (NombreConNumerosException e) {
            DialogService.mostrarAlerta(Alert.AlertType.WARNING, "Formato Inválido", e.getMessage(), txtNombre);

        } catch (IOException e) {
            DialogService.mostrarAlerta(Alert.AlertType.ERROR, "Error de Sistema", "No se pudo cargar la ventana emergente de resumen: " + e.getMessage(), txtNombre);
        }
    }

    private void procesarAperturaVentanaModal(String usuarioGenerado) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/mobimedline_sistema/VerificacionView.fxml"));
        Parent root = loader.load();

        VerificacionUsuario secundaryCtrl = loader.getController();
        secundaryCtrl.recibirDatos(
            txtNombre.getText().trim(),
            txtApellidos.getText().trim(),
            txtCorreo.getText().trim(),
            txtTelefono.getText().trim(),
            usuarioGenerado,
            txtPassword.getText()
        );

        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        limpiarCampos();
    }

    private void validarFormulario() throws CamposIncompletosException, ContrasenasNoCoincidenException, NombreConNumerosException {
        if (txtNombre.getText().trim().isEmpty() || 
            txtApellidos.getText().trim().isEmpty() || 
            txtCorreo.getText().trim().isEmpty() || 
            txtTelefono.getText().trim().isEmpty() || 
            txtPassword.getText().isEmpty() || 
            txtConfirmPassword.getText().isEmpty()) {
            throw new CamposIncompletosException("Por favor, llena todos los campos obligatorios para continuar.");
        }

        if (txtNombre.getText().matches(".*\\d.*") || txtApellidos.getText().matches(".*\\d.*")) {
            throw new NombreConNumerosException("Los campos de Nombre y Apellidos no pueden contener números.");
        }

        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            throw new ContrasenasNoCoincidenException("Las contraseñas no coinciden. Por favor, verifícalas.");
        }
    }

    private String obtenerUsuario() {
        String nom = txtNombre.getText().trim().toUpperCase();
        String ape = txtApellidos.getText().trim().toUpperCase();

        if (nom.isEmpty() || ape.length() < 2) return "USER";

        char p1 = ape.charAt(0);
        char p2 = 'X';
        String interna = ape.substring(1);
        for (char c : interna.toCharArray()) {
            if ("AEIOU".indexOf(c) != -1) {
                p2 = c;
                break;
            }
        }

        char p3 = ape.contains(" ") && ape.indexOf(" ") + 1 < ape.length() 
                ? ape.charAt(ape.indexOf(" ") + 1) : 'X';
        char p4 = nom.charAt(0);

        return "" + p1 + p2 + p3 + p4;
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellidos.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Al estar vinculados con la etiqueta @FXML en el archivo FXML mediante onAction,
        // ya no es obligatorio instanciar los setOnAction aquí, manteniendo el método limpio.
    }
}
