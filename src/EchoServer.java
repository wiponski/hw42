import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    private  final  int port;
    private  final ExecutorService pool = Executors.newCachedThreadPool();
    private  EchoServer(int port){
        this.port = port;
    }
    public static EchoServer bindToPort(int port){
        return  new EchoServer(port);
    }

    public void run(){
        try (var server = new ServerSocket(port)){

            while(!server.isClosed()){
                Reader reader = new Reader();
                Socket clientSocket = server.accept();
                pool.submit(() -> reader.handle(clientSocket));
            }
        }catch (IOException e){
            System.out.printf("��������� ����� ���� %s ����� %n. ",port);
            e.printStackTrace();
        }
    }


}
