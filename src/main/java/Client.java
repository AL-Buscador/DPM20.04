import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("127.0.0.1", 8989);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Соединение с сервером установлено.");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите запрос:");
            String line = scanner.nextLine();
            writer.write(line);
            writer.newLine();
            writer.flush();
            String response = reader.readLine();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}