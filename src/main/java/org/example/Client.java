package org.example;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.lang.Thread.sleep;


public class Client {
  private Socket socket;
  private PrintWriter output;
  private BufferedReader input;
  private InputStream inputStream;
  private DataInputStream dataInputStream;

  public Client(Socket client_socket) {
    this.socket = client_socket;
    this.setInputOutput();
  }

  public void setInputOutput() {
    try {
      this.output = new PrintWriter(socket.getOutputStream(), true);
      this.inputStream = socket.getInputStream();
      this.dataInputStream = new DataInputStream(new BufferedInputStream(this.inputStream));
//      this.input = new BufferedReader(new InputStreamReader(inputStream));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public String recvLine() throws IOException {
    StringBuilder res = new StringBuilder();
    boolean finished = false;
    while (!finished) {
      int c = this.dataInputStream.read();
      System.out.println("c is" + c);
      if (c == '\n') {
        finished = true;
      } else if (Character.isDigit(c)) {
        res.append((char) c);
      } else {
        throw new IOException("xml length wrong format");
      }
    }
    return res.toString();
  }

  public String recvBytes(int numBytes) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[numBytes];
    buffer = dataInputStream.readNBytes(numBytes);
//    System.out.println("Received bytes: " + Arrays.toString(buffer));
    return new String(buffer, StandardCharsets.UTF_8);

  }

  public void send(String string) {
    this.output.write(string);
    this.output.flush();
  }


}
