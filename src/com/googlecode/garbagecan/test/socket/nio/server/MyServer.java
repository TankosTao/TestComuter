package com.googlecode.garbagecan.test.socket.nio.server;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer {

static 	private String filepa="C:\\Users\\TankOStao\\Desktop\\MyShare";
	static private String filepath="C:\\Users\\TankOStao\\Desktop\\MyShare\\";
	private final static Logger logger = Logger.getLogger(MyServer.class.getName());
   private static String filel;
	public static String[] trim(String character, String symbol){
		//
		return character.split(symbol);
	}
	public static boolean check(String str,String a)
	{

		int result1 = str.indexOf(a);
		if(result1 != -1){
			System.out.println("字符串str中包含子串:"+result1);
			return true;
		}
		else
			{
		return false;
		}
	}

	public static void main(String[] args)
	{
filecho();
	}

 public static void filecho(){
	 //初始化文件选择框
	 JFileChooser fDialog = new JFileChooser();
//设置文件选择框的标题
	 fDialog.setDialogTitle("select a file as your exchange file");
//弹出选择框
	 fDialog.setFileSelectionMode(1);
	 int returnVal = fDialog.showOpenDialog(null);
// 如果是选择了文件
	 if(JFileChooser.APPROVE_OPTION == returnVal){
//打印出文件的路径，你可以修改位 把路径值 写到 textField 中

		 filel=fDialog.getSelectedFile().toString();
		 String newPath;
		 newPath = filel.replaceAll("\\\\","\\\\\\\\");
		 System.out.println(newPath);
		 File file=new File(newPath);
		 if(file.isDirectory())
		 {
			getFileName(new File(newPath), "");
			filepath=newPath+"\\";

			 Myframe myframe=new Myframe(newPath);



		 }
		 else
		 {
			 JOptionPane.showMessageDialog(null, "Wrong", "This is not a directory", JOptionPane.ERROR_MESSAGE);
		 }


	 }
 }


	public static void  telld(String[] str)
    {
        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;

        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(1991));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


            while (selector.select() > 0) {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey readyKey = it.next();
                    it.remove();

                    SocketChannel socketChannel = null;
                    String string = "";
                    try {
                        socketChannel = ((ServerSocketChannel) readyKey.channel()).accept();
                        string = receiveData(socketChannel);
                        logger.log(Level.INFO, string);
                         str=trim(string,";");
                   break;
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
	public static void ClientRe()
	{
		Selector selector = null;
		ServerSocketChannel serverSocketChannel = null;

		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().setReuseAddress(true);
			serverSocketChannel.socket().bind(new InetSocketAddress(1991));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			while (selector.select() > 0) {
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey readyKey = it.next();
					it.remove();

					SocketChannel socketChannel = null;
					String string = "";
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
public static void ClientSend()
{
    Selector selector = null;
    ServerSocketChannel serverSocketChannel = null;

    try {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(1991));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (selector.select() > 0) {
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey readyKey = it.next();
                it.remove();

                SocketChannel socketChannel = null;
                String string = "";
                try {
                    socketChannel = ((ServerSocketChannel) readyKey.channel()).accept();
                    string = receiveData(socketChannel);
                    logger.log(Level.INFO, string);
                    String [] str=trim(string,";");
                    if (check(string,";")) {
                        if (str[0].equals("1")) {       //1==receive
                            if (!string.isEmpty()) {
                                File f = new File(filepath+str[1]);
                                if (f.exists() && f.isFile())
                                {

                                    sendData(socketChannel, str[1]);
                                } else {

                                    logger.info("file doesn't exist or is not a file");
                                }
                            }
                        }
                        if (str[0].equals("2"))  //2==send
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

                        receiveFile(socketChannel,new File(filepath+string));

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



		public static   void getFileName(File file, String c){
			/**
			 * 如果是文件夹,打印名称(带上制表符)
			 */
			if(file.isDirectory()){
				System.out.println(c + file.getName());
			}
			/**
			 * 获取所有子文件
			 */
			File[] files = file.listFiles();
			for(File f : files){
				/**
				 * 首先加一个制表符
				 */
				String temp = c + "\t";
				if(f.isDirectory()){
					/**
					 * 如果是文件夹,则进行递归
					 */
					getFileName(f, temp);
				} else {
					/**
					 * 如果不是文件夹,则直接打印
					 */
					System.out.println(temp + f.getName());
				}
			}
		}



}
