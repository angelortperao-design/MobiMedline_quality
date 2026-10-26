package interfaz.mobimedline_sistema.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TelegramService {

    private static final String TOKEN_BOT = "8892478841:AAHJJbZOkdiZiC29LFuSAIHm5R4O8YijzHY";

    public static boolean enviarMensaje(String chatId, String mensaje) {
        if (chatId == null || chatId.equals("TU_CHAT_ID_AQUÍ")) {
            System.out.println("[TELEGRAM] Advertencia: El chat ID no está configurado.");
            return false;
        }

        try {
            String urlString = "https://api.telegram.org/bot" + TOKEN_BOT + "/sendMessage";
            String payload = "{\"chat_id\":\"" + chatId + "\",\"text\":\"" + mensaje + "\",\"parse_mode\":\"Markdown\"}";

            System.out.printf("[TELEGRAM] Conectando con la API para el chat ID: %s...%n", chatId);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.printf("[TELEGRAM] Respuesta HTTP recibida - Código de estado: %d%n", response.statusCode());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.printf("[TELEGRAM] Error de conexión: %s%n", e.getMessage());
            return false;
        }
    }
}