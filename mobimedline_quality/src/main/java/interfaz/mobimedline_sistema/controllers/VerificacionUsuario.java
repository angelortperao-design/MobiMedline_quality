package interfaz.mobimedline_sistema.controllers;

import interfaz.mobimedline_sistema.AgendaUsuariosBase;
import interfaz.mobimedline_sistema.model.Usuarios;
import interfaz.mobimedline_sistema.service.DialogService;
import interfaz.mobimedline_sistema.service.OtpService;
import interfaz.mobimedline_sistema.service.TelegramService;
import interfaz.mobimedline_sistema.service.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class VerificacionUsuario {

    @FXML private AnchorPane apVentana;
    @FXML private Button btnCancelar;
    @FXML private Button btnGuardar;
    @FXML private Button btnReenviarCodigo;
    @FXML private Label lblMostrarCorreo;
    @FXML private Label lblMostrarNombre;
    @FXML private TextField txtCodigoTelegram;

    private String nomG, apellidosG, correoG, telefonoG, usuG, passG;
    private String codigoGeneradoOficial;

    // Coloca aquí tu chat_id numérico personal obtenido con @userinfobot
    private static final String CHAT_ID_ADMIN_TEMPORAL = "1870537281";

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

        generarYEnviarCodigoTelegram();
    }

    private void generarYEnviarCodigoTelegram() {
        // Generamos el código usando el servicio OTP
        this.codigoGeneradoOficial = OtpService.generarCodigo6Digitos();

        System.out.printf("[OTP] Código generado para el usuario '%s': %s%n", usuG, codigoGeneradoOficial);

        String mensaje = "🔔 *Nueva Solicitud de Registro*\n" +
                "👤 Usuario: " + usuG + "\n" +
                "📝 Nombre: " + nomG + " " + apellidosG + "\n" +
                "🔑 Código de Validación: `" + codigoGeneradoOficial + "`";

        notificarAGerentes(mensaje);
    }

    private void notificarAGerentes(String mensaje) {
        List<Usuarios> listaUsuarios = AgendaUsuariosBase.getUsuariosBase();
        boolean enviadoAlgunGerente = false;

        for (Usuarios u : listaUsuarios) {
            if (u.getPermisos()) {
                // En el futuro modularizarás esto por u.getChatIdTelegram()
                String chatIdGerente = CHAT_ID_ADMIN_TEMPORAL; 

                System.out.printf("[TELEGRAM] Notificando al gerente: %s%n", u.getNombre());
                boolean enviado = TelegramService.enviarMensaje(chatIdGerente, mensaje);
                if (enviado) {
                    enviadoAlgunGerente = true;
                }
            }
        }

        // Si no hay gerentes en la lista local aún, enviamos al chat de respaldo temporal
        if (!enviadoAlgunGerente) {
            System.out.println("[TELEGRAM] Usando chat de respaldo para la notificación...");
            TelegramService.enviarMensaje(CHAT_ID_ADMIN_TEMPORAL, mensaje);
        }
    }

    @FXML
    private void AccionConfirmarGuardado(ActionEvent event) {
        String codigoIngresado = txtCodigoTelegram.getText().trim();

        if (codigoIngresado.isEmpty()) {
            DialogService.mostrarAlerta(Alert.AlertType.WARNING, "Código Requerido", "Por favor, ingrese el código de 6 dígitos.", txtCodigoTelegram);
            return;
        }

        if (!codigoIngresado.equals(codigoGeneradoOficial)) {
            System.out.printf("[SEGURIDAD] Intento fallido: El usuario ingresó '%s' pero se esperaba '%s'.%n", codigoIngresado, codigoGeneradoOficial);
            DialogService.mostrarAlerta(Alert.AlertType.ERROR, "Código Incorrecto", "El código proporcionado no es válido.", txtCodigoTelegram);
            return;
        }

        System.out.printf("[SEGURIDAD] ¡Código correcto! Activando y registrando al usuario: %s%n", usuG);

        Usuarios nuevo = new Usuarios(usuG, nomG, apellidosG, "", passG);
        nuevo.setAprobado(true);
        UsuarioService.registrarUsuario(nuevo, AgendaUsuariosBase.getUsuariosBase());

        DialogService.mostrarAlerta(Alert.AlertType.INFORMATION, "Cuenta Activada", "El usuario " + usuG + " ha sido verificado y activado exitosamente.", txtCodigoTelegram);

        Stage stage = (Stage) apVentana.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void AccionReenviarCodigo(ActionEvent event) {
        System.out.println("[TELEGRAM] Solicitud de reenvío de código...");
        generarYEnviarCodigoTelegram();
        DialogService.mostrarAlerta(Alert.AlertType.INFORMATION, "Código Reenviado", "Se ha generado y enviado un nuevo código.", txtCodigoTelegram);
    }

    @FXML
    private void AccionCancelarGuardado(ActionEvent event) {
        System.out.println("[VENTANA] Operación cancelada por el usuario.");
        Stage stage = (Stage) apVentana.getScene().getWindow();
        stage.close();
    }
}