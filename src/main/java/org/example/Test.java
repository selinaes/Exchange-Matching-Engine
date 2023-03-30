package org.example;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Test {
//  private Socket clientSocket;
//  private PrintWriter out;
//  private BufferedReader in;

  public static void main(String[] args) {
    try {
      Socket socket = new Socket("localhost", 12345);
      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
      Scanner scanner = new Scanner(new File("src/main/java/org/example/input.txt"));
//      InputStream in = new BufferedInputStream(new FileInputStream("src/main/java/org/example/input.txt"));
//      FileInputStream in = new FileInputStream("src/main/java/org/example/input.txt"));
//      String line = "";
//      while (in.available() > 0) {
////        try {
//////          line = input.readLine();
//////          out.writeUTF(line);
////        }
////        catch (IOException i) {
////          System.out.println(i);
////        }
//      }

//      File inputFile = new File("src/main/java/org/example/input.txt");
//      InputStream in = new FileInputStream(inputFile);

//      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//      OutputStream outputStream = socket.getOutputStream();
//      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
//      String line;
//      while ((line = reader.readLine()) != null) {
//        writer.write(line);
//        writer.newLine();
//      }
//      writer.flush();
//
//
//      byte[] bytes = new byte[16 * 1024];
//      OutputStream out = socket.getOutputStream();
//      int count;
//      while ((count = in.read(bytes)) > 0) {
//        out.write(bytes, 0, count);
//      }
      out.close();
//      in.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
