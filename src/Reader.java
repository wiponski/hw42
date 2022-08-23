

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Reader {



    private ArrayList users;
    private String  user;


    public Reader() {

    }

    @Override
    public String toString() {
        return "Reader{" +
                "users=" + users +
                '}';
    }

    public void handle(Socket socket) {
        System.out.printf("Подключен клиент: %s%n", socket);
        String username = getName();

       // addUsers(username);

        // логика обработки
        try (Scanner reader = getReader(socket);
             PrintWriter writer = getWriter(socket);
             socket;){
            sendResponse("Привет : "+ username, writer);

            while (true){
                String message = reader.nextLine().strip();

                System.out.printf("%s_%s : %s%n ",username,socket.getPort(), message);

                if(isEmptyMsg(message) || isQuitMsg(message)) {
                    System.out.println("Клиент закрыл соединение! ");
                    sendResponse("необходимо пересоедениться "+socket,writer);
                    break;
                }
                if(message.contains("name")) {                                                          //переименование
                    String newUsername = message.substring(4,message.length());
                    sendResponse("Вы сменили имя на!  "+newUsername,writer);
                    System.out.printf(" Пользователь %s теперь известен как %s %n",username,newUsername);
                    username=newUsername;
                }

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

    private static String getName(){
        Random rnd = new Random();
        Integer i = rnd.nextInt(5)+1;
        switch (i) {
            case 1:
                return "Aibek";
            case 2:
                return "Bolot";
            case 3:
                return "Alexandr";
            case 4:
                return "Vera";
            case 5:
                return "Bill";
            default:
                return "Gulchapcha";
        }
    }


}
