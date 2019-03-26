package com.googlecode.garbagecan.test.socket.nio.server;


import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Myframe extends JFrame {

    static 	private String filepa="C:\\Users\\TankOStao\\Desktop\\MyShare";
    JTextField textField;
    JTextArea textArea1;
    JTextArea textArea2;

    JLabel label;
    JButton   but_1;
    JPanel jPanel;
  JTextArea textArea;

    JPanel jPanel1;

    private JSplitPane sp;


    private void Setlooks(String yet)
    {
 textField =new JTextField("HOST ADRESS:");
       but_1=new JButton("Refresh");
     label=new JLabel("HOST ADRESS:");
     jPanel1=new JPanel();
     textArea=new JTextArea("USB-file_trans\n" +"Connect computer and phone with usb\nOperate on phone. Input the file name,computer will\n send Select the file,computer will receive");
     textArea1=new JTextArea();
        textArea2=new JTextArea("");

      jPanel=new JPanel();
        setLayout(null);
    //    this.setBackground(Color.WHITE);
        this.setBackground(Color.white);
        textField.setBounds(0,0,400,50);
        label.setBounds(0,0,400,50);
        textArea.setBounds(0,50,300,100);
        textArea1.setBounds(10,10,200,250);
        textArea2.setBounds(10,10,180,200);
     //   textArea3.setBounds(10,160,180,50);
        but_1.setBounds(300,50,100,100);
jPanel.setBorder(BorderFactory.createLoweredBevelBorder());
jPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
jPanel1.setBounds(200,150,200,250);
jPanel1.add(textArea2);

jPanel1.setBackground(Color.white);
       label.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel.setBackground(Color.white);
        jPanel1.setLayout(null);
jPanel.setLayout(null);
jPanel.add(textArea1);
      jPanel.setBounds(0,150,200,250);



       add(jPanel1);

        setBounds(300,200,400,400);



        but_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
getFileName(new File(yet),"");
            }

        });
     //   add(textField);
       add(label);
        add(but_1);
        add(jPanel);
        add(textArea);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        getFileName(new File(yet),"");

    }
    public  Myframe(String yet)
    {


        Setlooks(yet);
    //function:
        InetAddress a= null;
        try {
            a = InetAddress.getLocalHost();
         label.setText("HOST ADRESS:"+a.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Thread thread1=getThread("传送服务",yet,textArea2);
        Thread thread2=getThread("接收服务",yet,textArea2);
        thread1.start();
        thread2.start();
 }
    private static Thread getThread(String name,String yet,JTextArea textArea)
    {
        return new Thread(name){
            @Override
            public void run() {
                if(name.equals("传送服务")){
                    ServerSocket serverSocket=new ServerSocket(1991,textArea);}
                else{
                    ClientSocket clientSocket=new ClientSocket(2666,yet,textArea);
                }

            };

        };}

   private void getFileName(File file, String c){
        /**
         * 如果是文件夹,打印名称(带上制表符)
         */
        textArea1.setText("");
        if(file.isDirectory()){
            System.out.println(c + file.getName());
            textArea1.append("-------------------------------------------------"+"\n");
            textArea1.append("Diretory of yours:"+"\n"+file.getName().toString()+"   :\n");
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
                textArea1.append("Diretory of yours:"+f.getName().toString()+"\n");
                System.out.print("Diretory of yours:");
                getFileName(f, temp);
            } else {
                textArea1.append(temp + f.getName()+"\n");
              System.out.println(temp + f.getName());
            }
        }
    }
}
