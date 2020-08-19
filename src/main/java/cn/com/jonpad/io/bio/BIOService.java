package cn.com.jonpad.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 传统BIO服务器模型
 * @author Jon Chan
 * @date 2020/8/19 16:42
 */
public class BIOService {
  public static void main(String[] args) throws IOException {
    ExecutorService executorService = Executors.newCachedThreadPool();

    ServerSocket serverSocket = new ServerSocket(6666);
    while (true){
      // accept 会阻塞
      final Socket socket = serverSocket.accept();

      System.out.println("新增一个连接");

      executorService.execute(() -> {
        handler(socket);
      });

    }
  }


  public static void handler(Socket socket){
    byte[] bytes = new byte[1024];
    InputStream inputStream = null;
    try {
      inputStream = socket.getInputStream();

      int s = 0;
      // read会阻塞
      while ((s = inputStream.read(bytes)) != -1){
        System.out.println(Thread.currentThread().getName() + ">>>>"+ new String(bytes, 0 ,s));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }finally {
      if(inputStream != null){
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
