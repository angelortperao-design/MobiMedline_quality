package interfaz.mobimedline_sistema.controllers;

import interfaz.mobimedline_sistema.model.Usuarios;
import interfaz.mobimedline_sistema.service.DialogService;
import interfaz.mobimedline_sistema.service.UsuarioService;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controlador modular para la gestión del Control de Accesos y aprobaciones pendientes.
 * Implementa sincronización automática en segundo plano y separación de responsabilidades operativas.
 */
public class ControlAccesosController {
    
    private static final int INTERVALO_MONITOREO_SEGUNDOS = 3;
    private static final String ROL_DEFAULT_EMPLEADO = "Empleado";
    private static final String ROL_GERENTE = "Gerente";
    private static final String ESTADO_ACTIVO = "Aprobado";
    private static final String ESTADO_PENDIENTE = "Pendiente";

    @FXML private TableView<Usuarios> tblUsuarios;
    @FXML private TableColumn<Usuarios, String> colUsuario;
    @FXML private TableColumn<Usuarios, String> colNombre;
    @FXML private TableColumn<Usuarios, String> colCorreo;
    @FXML private TableColumn<Usuarios, String> colTelefono;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroEstado;
    @FXML private Label lblPendientes;

    @FXML private VBox panelAcciones;
    @FXML private Label lblTituloAcciones;
    @FXML private ComboBox<String> cmbRolAccion;
    @FXML private ComboBox<String> cmbValidacionAccion;
    @FXML private Button btnGuardarCambios;
    @FXML private Button btnRechazar;

    private final ObservableList<Usuarios> listaUsuarios = FXCollections.observableArrayList();
    private FilteredList<Usuarios> listaFiltrada;
    private final UsuarioService usuarioService = new UsuarioService();
    private Usuarios usuarioSeleccionado;
    private Timeline timelineMonitoreo;

    @FXML
    public void initialize() {
        configurarComponentesInterfaz();
        cargarDatosPendientes();
        iniciarMonitoreoExterno();
    }

    /**
     * Configura de forma modular los elementos visuales iniciales de la vista.
     */
    private void configurarComponentesInterfaz() {
        configurarColumnasTabla();
        configurarFiltrosBusqueda();
        configurarPanelAcciones();
    }

    private void configurarColumnasTabla() {
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            usuarioSeleccionado = newVal;
            actualizarPanelInferior(newVal);
        });
    }

    private void configurarFiltrosBusqueda() {
        if (cmbFiltroEstado != null) {
            cmbFiltroEstado.setVisible(false);
        }

        listaFiltrada = new FilteredList<>(listaUsuarios, p -> true);

        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> aplicarFiltroTexto());
        }

        tblUsuarios.setItems(listaFiltrada);
    }

    private void configurarPanelAcciones() {
        cmbRolAccion.setItems(FXCollections.observableArrayList(ROL_DEFAULT_EMPLEADO, ROL_GERENTE));
        cmbValidacionAccion.setItems(FXCollections.observableArrayList(ESTADO_ACTIVO, ESTADO_PENDIENTE));
        cambiarEstadoHabilitacionPanel(false);
    }

    private void actualizarPanelInferior(Usuarios user) {
        if (user != null) {
            lblTituloAcciones.setText("Acciones para el Usuario Seleccionado (Usuario: " + user.getUsuario() + ")");
            cmbRolAccion.setValue(user.getRol() != null ? user.getRol() : ROL_DEFAULT_EMPLEADO);
            cmbValidacionAccion.setValue(user.isAprobado() ? ESTADO_ACTIVO : ESTADO_PENDIENTE);
            cambiarEstadoHabilitacionPanel(true);
        } else {
            lblTituloAcciones.setText("Acciones para el Usuario Seleccionado (Ninguno)");
            cambiarEstadoHabilitacionPanel(false);
        }
    }

    private void cambiarEstadoHabilitacionPanel(boolean habilitado) {
        cmbRolAccion.setDisable(!habilitado);
        cmbValidacionAccion.setDisable(!habilitado);
        btnGuardarCambios.setDisable(!habilitado);
        if (btnRechazar != null) {
            btnRechazar.setDisable(!habilitado);
        }
    }

    @FXML
    public void handleGuardarCambios() {
        if (usuarioSeleccionado == null) return;

        // 1. Leemos el rol seleccionado
        String nuevoRol = cmbRolAccion.getValue();

        // 2. Leemos la opción elegida en el ComboBox de Verificación (ej. "Aprobado" o tu texto correspondiente)
        String seleccionVerificacion = cmbValidacionAccion.getValue(); 
        boolean esAprobado = "Aprobado".equals(seleccionVerificacion); // Ajusta el texto según las opciones de tu ComboBox

        boolean confirmado = DialogService.mostrarConfirmacion(
            "Guardar Verificación",
            "¿Desea actualizar el estado de validación para el usuario " + usuarioSeleccionado.getUsuario() + "?",
            tblUsuarios
        );

        if (confirmado) {
            // 3. Mandamos tanto el rol como la decisión de verificación
            guardarValidacionUsuario(nuevoRol, esAprobado);
        }
    }

    private void guardarValidacionUsuario(String rol, boolean esAprobado) {
        // Ya no modificamos el objeto directamente aquí. 
        // Delegamos toda la responsabilidad y el cambio al UsuarioService:

        usuarioService.actualizarRol(usuarioSeleccionado.getUsuario(), rol);
        usuarioService.actualizarValidacion(usuarioSeleccionado.getUsuario(), esAprobado);

        // Refrescamos la tabla
        cargarDatosPendientes();

        DialogService.mostrarAlerta(
            Alert.AlertType.INFORMATION,
            "Éxito",
            "La verificación del usuario se ha procesado correctamente.",
            tblUsuarios
        );
    }

    @FXML
    public void handleRechazar() {
        if (usuarioSeleccionado == null) return;

        boolean confirmado = DialogService.mostrarConfirmacion(
            "Rechazar Registro", 
            "¿Está seguro de rechazar y eliminar el registro del usuario " + usuarioSeleccionado.getUsuario() + "?", 
            tblUsuarios
        );

        if (confirmado) {
            usuarioService.eliminarUsuario(usuarioSeleccionado.getUsuario());
            cargarDatosPendientes();
            DialogService.mostrarAlerta(
                Alert.AlertType.INFORMATION, 
                "Registro Rechazado", 
                "El usuario ha sido rechazado y eliminado del sistema.", 
                tblUsuarios
            );
        }
    }

    private void iniciarMonitoreoExterno() {
        timelineMonitoreo = new Timeline(new KeyFrame(Duration.seconds(INTERVALO_MONITOREO_SEGUNDOS), event -> {
            sincronizarConCambiosExternos();
        }));
        timelineMonitoreo.setCycleCount(Timeline.INDEFINITE);
        timelineMonitoreo.play();
    }

    private void sincronizarConCambiosExternos() {
        List<Usuarios> todos = UsuarioService.obtenerTodosLosUsuarios();
        long pendientesActuales = todos.stream().filter(u -> !u.isAprobado()).count();
        
        if (pendientesActuales != listaUsuarios.size()) {
            cargarDatosPendientes(); // Sincronización silenciosa ante cambios de Telegram u otras fuentes
        }
    }

    private void aplicarFiltroTexto() {
        String busqueda = txtBuscar.getText() != null ? txtBuscar.getText().toLowerCase().trim() : "";

        listaFiltrada.setPredicate(user -> {
            boolean coincideBusqueda = busqueda.isEmpty()
                    || (user.getNombreCompleto() != null && user.getNombreCompleto().toLowerCase().contains(busqueda))
                    || (user.getCorreo() != null && user.getCorreo().toLowerCase().contains(busqueda))
                    || (user.getUsuario() != null && user.getUsuario().toLowerCase().contains(busqueda));

            return coincideBusqueda && !user.isAprobado();
        });

        actualizarContadoresVista();
    }

    @FXML
    public void handleRefrescar() {
        cargarDatosPendientes();
    }

    private void cargarDatosPendientes() {
        listaUsuarios.clear();
        List<Usuarios> todos = UsuarioService.obtenerTodosLosUsuarios();
        for (Usuarios u : todos) {
            if (!u.isAprobado()) {
                listaUsuarios.add(u);
            }
        }
        tblUsuarios.getSelectionModel().clearSelection();
        aplicarFiltroTexto();
        actualizarContadoresVista();
    }

    private void actualizarContadoresVista() {
        long pendientes = listaUsuarios.stream().filter(u -> !u.isAprobado()).count();

        if (lblPendientes != null) {
            lblPendientes.setText("Pendientes: " + pendientes);
        }
    }
}