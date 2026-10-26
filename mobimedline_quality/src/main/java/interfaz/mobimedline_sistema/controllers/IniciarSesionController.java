package interfaz.mobimedline_sistema.controllers;

import interfaz.mobimedline_sistema.App;
import interfaz.mobimedline_sistema.model.Usuarios;
import interfaz.mobimedline_sistema.service.DialogService;
import interfaz.mobimedline_sistema.service.UsuarioService;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;

public class IniciarSesionController {

    public static Usuarios usuarioActual; 

    @FXML
    private TextField tfnombreUs;

    @FXML
    private PasswordField passfiCon;
    
    @FXML
    private Hyperlink linkRegistrar;

    @FXML
    private void handleLogin() {
        String user = tfnombreUs.getText().trim();
        String pass = passfiCon.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            DialogService.mostrarAlerta(Alert.AlertType.WARNING, "No llenaste un campo", "Por favor, verifica tus datos.", tfnombreUs);
            return; 
        }

        // Delegamos la validación al servicio
        Usuarios usuarioEncontrado = UsuarioService.autenticar(user, pass);

        if (usuarioEncontrado != null) {
            usuarioEncontrado.setContraseña(null); 
            usuarioActual = usuarioEncontrado;

            try {
                if (usuarioActual.getPermisos()) { // true = Gerente
                    System.out.println("Iniciando sesión como Gerente: " + usuarioActual.getNombre());
                    App.setRoot("MenuGerente"); 
                } else { // false = Empleado
                    System.out.println("Iniciando sesión como Empleado: " + usuarioActual.getNombre());
                    App.setRoot("MenuVentas");
                }
            } catch (IOException e) {
                e.printStackTrace();
                DialogService.mostrarAlerta(Alert.AlertType.ERROR, "ERROR DE SISTEMA", "No se pudo cargar la ventana.", tfnombreUs);
            }
        } else {
            DialogService.mostrarAlerta(Alert.AlertType.ERROR, "Credencial incorrecta", "Usuario o contraseña no válidos.", tfnombreUs);
            passfiCon.clear();
        }
    }
    
    @FXML
    private void irRegistro(ActionEvent event) {
        try {
            App.setRoot("RegistroView");
        } catch (IOException e) {
            System.err.println("Error al cambiar de ventana: " + e.getMessage());
            e.printStackTrace();
        }
    }
}