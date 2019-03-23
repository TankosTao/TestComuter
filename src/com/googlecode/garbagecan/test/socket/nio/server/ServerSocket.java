package com.googlecode.garbagecan.test.socket.nio.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


import static com.googlecode.garbagecan.test.socket.nio.server.MyServer.check;
import static com.googlecode.garbagecan.test.socket.nio.server.MyServer.trim;

public class ServerSocket
{
    static 	private String filepa="C:\\Users\\TankOStao\\Desktop\\MyShare";
    static private String filepath="C:\\Users\\TankOStao\\Desktop\\MyShare\\";
    Selector selector = null;
    ServerSocketChannel serverSocketChannel = null;
    private final static Logger logger;
    boolean quit1=true;
    boolean quit2=true;

    public void close()
    {

        this.quit1=false;
        this.quit2=false;
    }

    static {
        logger = Logger.getLogger(MyServer.class.getName());
    }

    ServerSocket(int port,String string)
    {
        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;

        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (selector.select() > 0&&quit1) {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                System.out.println("---------------Send_Server----------Readytofly——————————————————————");
                while (it.hasNext()) {

                    SelectionKey readyKey = it.next();
                    it.remove();

                    SocketChannel socketChannel = null;
                    try {
                        socketChannel = ((ServerSocketChannel) readyKey.channel()).accept();
                        string = receiveData(socketChannel);
                        logger.log(Level.INFO, string);

                        if (check(string,";")) {
                            String [] str=trim(string,";");
                            System.out.println("process1");

                            if (str[0].equals("1"))
                            {
                                System.out.println("1");
                                if (!string.isEmpty()) {
                                    File f = new File(filepath+str[1]);
                                    if (f.exists() && f.isFile())
                                    {
                                        sendData(socketChannel, str[1]);

                                    } else {

                                        logger.info("wrong info");
                                    }
                                }
                            }
                            if (str[0].equals("2"))  //2==send
                            {
                                System.out.println("2");
                                File f = new File(filepath+str[1]);
                                if (f.exists()) {

                                    sendData(socketChannel,"");
                                }
                                else {
                                System.out.println("I am here with str[1]  "+str[1]);
                                    string=str[1];
                                    sendData(socketChannel,str[1]);

                                    //  receiveFile(socketChannel,new File(filepath+str[1]));

                                }
                                //receiveFile();
                            }
                            if (str[0].equals("3"))  //2==send
                            {
                                System.out.println("3");
                                File f = new File(filepath+str[1]);
                                sendFile(socketChannel,new File(filepath+str[1]));
                                //receiveFile();
                            }
                            if (str[0].equals("4"))  //2==send
                            {
                                File f = new File(filepath+str[1]);
                                if (f.exists()) {

                                    sendData(socketChannel,"");
                                }
                                else {
                                    sendData(socketChannel,str[1]);
                                }
                                //receiveFile();
                            }
                        }else
                        { System.out.println("Here i AM in receive");

                            sendFile(socketChannel,new File(filepath+string));

                        }
                    }catch(Exception ex){
                        logger.log(Level.SEVERE, "1", ex);
                    } finally {
                        try {
                            socketChannel.close();
                        } catch(Exception ex) {
                            logger.log(Level.SEVERE, "2", ex);
                        }
                    }
                }
            }
        } catch (ClosedChannelException ex) {
       logger.log(Level.SEVERE, "3", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "4", ex);
        } finally {
            try {
                selector.close();
            } catch(Exception ex) {
                logger.log(Level.SEVERE, "5", ex);
            }
            try {
                serverSocketChannel.close();
            } catch(Exception ex) {
                logger.log(Level.SEVERE, "6", ex);
            }
        }
    }


    private static String receiveData(SocketChannel socketChannel) throws IOException {
        String string = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            byte[] bytes;
            int size = 0;
            while ((size = socketChannel.read(buffer)) >= 0) {
                buffer.flip();
                bytes = new byte[size];
                buffer.get(bytes);
                baos.write(bytes);
                buffer.clear();
            }
            bytes = baos.toByteArray();
            string = new String(bytes);
        }catch(Exception ex){
            logger.log(Level.SEVERE, "7", ex);
        }finally {
            try {
                baos.close();
            } catch(Exception ex) {
                logger.log(Level.SEVERE, "8", ex);
            }
        }
        return string;
    }

    private static void sendData(SocketChannel socketChannel, String string) throws IOException {
        byte[] bytes = string.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        socketChannel.write(buffer);
        socketChannel.socket().shutdownOutput();
    }

    private static void receiveFile(SocketChannel socketChannel, File file) throws IOException {
        FileOutputStream fos = null;
        FileChannel channel = null;

        try {
            fos = new FileOutputStream(file);
            channel = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

            int size = 0;
            while ((size = socketChannel.read(buffer)) != -1) {
                buffer.flip();
                if (size > 0) {
                    buffer.limit(size);
                    channel.write(buffer);
                    buffer.clear();
                }
            }
        }catch(Exception ex){
            logger.log(Level.SEVERE, "9", ex);
        } finally {
            try {
                channel.close();
            } catch(Exception ex) {
                logger.log(Level.SEVERE, "10", ex);
            }
            try {
                fos.close();
            } catch(Exception ex) {
                logger.log(Level.SEVERE, "11", ex);
            }
        }
    }

    private static void sendFile(SocketChannel socketChannel, File file) throws IOException {
        FileInputStream fis = null;
        FileChannel channel = null;
        try {
            fis = new FileInputStream(file);
            channel = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            int size = 0;

            while ((size = channel.read(buffer)) != -1) {
                System.out.println(size);
                buffer.rewind();
                buffer.limit(size);
                socketChannel.write(buffer);
                buffer.clear();
            }
            socketChannel.socket().shutdownOutput();
            socketChannel.socket().shutdownOutput();
        }catch(Exception ex){
            logger.log(Level.SEVERE, "12", ex);
        } finally {
            try {
                channel.close();
            } catch(Exception ex) {
                logger.log(Level.SEVERE, "13", ex);
            }
            try {
                fis.close();
            } catch(Exception ex) {
                logger.log(Level.SEVERE, "14", ex);
            }
        }
    }
}
