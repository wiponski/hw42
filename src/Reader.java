import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Reader {
    public Reader() {

    }

    public void handle(Socket socket) {
        System.out.printf("Подключен клиент: %s%n", socket);
        // логика обработки
        try (Scanner reader = getReader(socket);
             PrintWriter writer = getWriter(socket);
             socket){
            sendResponse("Привет "+ socket, writer);
            while (true){
                String message = reader.nextLine().strip();
                System.out.printf("Got : %s%n ", message);
                if(isEmptyMsg(message) || isQuitMsg(message)) {
                    break;
                }
                sendResponse(message,writer);
            }
        }catch (NoSuchElementException e){
            System.out.println("Клиент закрыл соединение! ");
        }catch (IOException ex){
            ex.printStackTrace();
            System.out.printf("Клиент отключен: %s%n", socket);
        }
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream stream = socket.getOutputStream();
        return new PrintWriter(stream);
    }
    private Scanner getReader(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        InputStreamReader input = new InputStreamReader(stream,"UTF-8");
        return new Scanner(input);
    }
    private boolean isQuitMsg(String message){
        return "bye".equalsIgnoreCase(message);
    }
    private boolean isEmptyMsg(String message){
        return  message == null || message.isBlank();
    }

    private void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }

}
