package interfaz.mobimedline_sistema.service;

import java.security.SecureRandom;

public class OtpService {

    public static String generarCodigo6Digitos() {
        SecureRandom random = new SecureRandom();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }
}