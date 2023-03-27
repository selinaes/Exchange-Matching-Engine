package org.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    private ServerSocket serverSocket;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 5, TimeUnit.MILLISECONDS, workQueue);

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        
    }

    public void run(){
        while (!Thread.currentThread().isInterrupted()) {
        final Socket client_socket = acceptOrNull();
            if (client_socket == null) {
                continue;
            }
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // do something with client_socket
                        System.out.println("Client accepted");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }  
    }

    /**
     * This is a helper method to accept a socket from the ServerSocket
     * or return null if it timesout.
     *
     * @return the socket accepted from the ServerSocket
     */
    public Socket acceptOrNull() {
        try {
            return serverSocket.accept();
        } catch (IOException ioe) {
            return null;
        }
    }
}
