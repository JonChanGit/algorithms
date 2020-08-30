package cn.com.jonpad.io.nio.channel;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通道基本使用测试
 * @author Jon Chan
 * @date 2020/8/30 15:32
 */
public class FileChannelTest {

  /**
   * 文件输出测试
   * @throws Exception
   */
  @Test
  public void outputTest() throws Exception{
    String str = "字符串" + UUID.randomUUID();

    FileOutputStream fileOutputStream = new FileOutputStream("./test.txt");

    // 获取文件通道 此时的通道实际类型是 FIleChannelImpl , FileOutputStream 持有 FileChannel 引用
    FileChannel channel = fileOutputStream.getChannel();
    // 创建文件缓冲区
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    // 将 str 放入缓冲区
    byteBuffer.put(str.getBytes());// 一个汉字3个字节
    // 反转缓冲区,改为读模式
    byteBuffer.flip();
    // 将缓冲区数据读取后写入通道
    channel.write(byteBuffer);
    // 关闭流
    fileOutputStream.close();

    System.out.println("写入文件内容: " + str);
  }


  /**
   * 文件读取测试
   * @throws Exception
   */
  @Test
  public void inputTest() throws Exception{
    FileInputStream fileInputStream = new FileInputStream("./test.txt");
    FileChannel channel = fileInputStream.getChannel();
    // 创建缓冲区
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    channel.read(byteBuffer);
    // 反转缓冲区,改为读模式 可以不反转 , byteBuffer.array() 直接拿到Buffer的数组
    // byteBuffer.flip();
    System.out.println("读取文件内容: " + new String(byteBuffer.array()));
    fileInputStream.close();

  }


  /**
   * 双向读写Buffer测试
   * @throws Exception
   */
  @Test
  public void inputAndOutputTest() throws Exception{
    FileInputStream fileInputStream = new FileInputStream("./test.jpg");
    FileChannel inputFileChannel = fileInputStream.getChannel();

    FileOutputStream fileOutputStream = new FileOutputStream("./testOutput.jpg");
    FileChannel outputFileChannel = fileOutputStream.getChannel();

    // 创建缓冲区
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    int read;
    // input读取到Buffer中
    while ((read = inputFileChannel.read(byteBuffer)) > -1){
      // 将Buffer数据写入Output中
      byteBuffer.flip();
      outputFileChannel.write(byteBuffer);

      // 读取后需要重置Buffer,或者写入时会导致死循环
      byteBuffer.clear();
      System.out.println(read);
      TimeUnit.SECONDS.sleep(1);
    }

    fileInputStream.close();
    fileOutputStream.close();
  }


  /**
   * 使用Channel直接复制文件
   * @throws Exception
   */
  @Test
  public void inputAndOutputTest2() throws Exception{

    FileInputStream fileInputStream = new FileInputStream("./test.jpg");
    FileChannel inputFileChannel = fileInputStream.getChannel();

    FileOutputStream fileOutputStream = new FileOutputStream("./testOutput.jpg");
    FileChannel outputFileChannel = fileOutputStream.getChannel();

    outputFileChannel.transferFrom(inputFileChannel, 0, inputFileChannel.size());

    // 回收资源
    outputFileChannel.close();
    inputFileChannel.close();
    fileInputStream.close();
    fileInputStream.close();

  }


}
