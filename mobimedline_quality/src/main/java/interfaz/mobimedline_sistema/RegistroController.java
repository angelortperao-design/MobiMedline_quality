package interfaz.mobimedline_sistema;

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
    private void IrLogin(ActionEvent event) {
        try {
            App.setRoot("LoginView");
        } catch (IOException e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void accionSolicitarRegistro(ActionEvent event) {
        try {
            // 1. Validar que los campos no estén vacíos, formato correcto y contraseñas coincidentes
            validarFormulario();

            // 2. Generar el identificador interno (base 4 letras) para control
            String usuarioGenerado = obtenerUsuario();

            // 3. Abrir la ventana emergente modal de recapitulación
            procesarAperturaVentanaModal(usuarioGenerado);

        } catch (CamposIncompletosException e) {
            mostrarAlerta("Campos Incompletos", e.getMessage(), Alert.AlertType.WARNING);

        } catch (ContrasenasNoCoincidenException e) {
            mostrarAlerta("Error de Contraseña", e.getMessage(), Alert.AlertType.ERROR);
            txtPassword.clear();
            txtConfirmPassword.clear();

        } catch (NombreConNumerosException e) {
            mostrarAlerta("Formato Inválido", e.getMessage(), Alert.AlertType.WARNING);

        } catch (IOException e) {
            mostrarAlerta("Error de Sistema", "No se pudo cargar la ventana emergente de resumen: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void procesarAperturaVentanaModal(String usuarioGenerado) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VerificacionView.fxml"));
        Parent root = loader.load();

        // Transferencia de datos al controlador secundario
        VerificacionUsuario secundaryCtrl = loader.getController();
        secundaryCtrl.recibirDatos(
            txtNombre.getText().trim(),
            txtApellidos.getText().trim(),
            txtCorreo.getText().trim(),
            txtTelefono.getText().trim(),
            usuarioGenerado,
            txtPassword.getText()
        );

        // Configuración de la escena transparente / emergente
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        // Limpieza de campos tras cerrar la ventana modal
        limpiarCampos();
    }

    private void validarFormulario() throws CamposIncompletosException, ContrasenasNoCoincidenException, NombreConNumerosException {
        // Validación de campos vacíos
        if (txtNombre.getText().trim().isEmpty() || 
            txtApellidos.getText().trim().isEmpty() || 
            txtCorreo.getText().trim().isEmpty() || 
            txtTelefono.getText().trim().isEmpty() || 
            txtPassword.getText().isEmpty() || 
            txtConfirmPassword.getText().isEmpty()) {

            throw new CamposIncompletosException("Por favor, llena todos los campos obligatorios para continuar.");
        }

        // Validación de formato sin números en Nombre y Apellidos
        if (txtNombre.getText().matches(".*\\d.*") || txtApellidos.getText().matches(".*\\d.*")) {
            throw new NombreConNumerosException("Los campos de Nombre y Apellidos no pueden contener números.");
        }

        // Validación de coincidencia de contraseñas
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

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (btnSolicitarRegistro != null) {
            btnSolicitarRegistro.setOnAction(this::accionSolicitarRegistro);
        }
        if (linkVolverLogin != null) {
            linkVolverLogin.setOnAction(this::IrLogin);
        }
    }
}
