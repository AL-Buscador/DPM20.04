import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        final int port = 8989;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server started");
            System.out.println("OЖИДАНИЕ ПОДКЛЮЧЕНИЙ");

            while (true)
                try (
                        Socket socket = server.accept();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    while (true) {
                        String request = reader.readLine();
                        if (request.equals("exit")) {
                            break;
                        }
                        String response = engine.search(request).toString();
                        writer.write(response);
                        writer.newLine();
                        writer.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}