package com.googlecode.garbagecan.test.socket.nio.server;

import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Myframe extends JFrame {
    static 	private String filepa="C:\\Users\\TankOStao\\Desktop\\MyShare";
    JTextField textField;
    JLabel label;
    JButton   but_1;
    JPanel jPanel;
  JTextArea textArea;
    JTree tree;

    private void Setlooks()
    {
 textField =new JTextField("HOST ADRESS:");
       but_1=new JButton("123");
     label=new JLabel("HOST ADRESS:");
     textArea=new JTextArea("USB-file_trans\n" +
             "\n" +
             "1.server for computer ： \n" +
             "Computer： （1）Thread1：receive and send message（/file）（2）Thread2：receive file\n" +
             "2.client for phone：\n" +
             "main function： （1）send request of  file F. Server will check for F.exsit() if F.exsit() send file to client\n" +
             "(2)send file to (Server will check if the file F .exsit(),not exist Server will receive the file.)");
      jPanel=new JPanel();
        setLayout(null);
        textField.setBounds(0,0,800,50);
        label.setBounds(0,0,800,50);
        textArea.setBounds(0,50,600,150);
tree=new JTree(getTree(filepa));
       label.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel.setBounds(0,200,600,600);
        tree.setBounds(0,0,600,600);
      //  jPanel.setBackground(Color.BLACK);
        jPanel.add(tree);
        setBounds(200,200,800,800);
        but_1.setBounds(600,200,100,30);

        but_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
     //   add(textField);
       add(label);
        add(but_1);
        add(jPanel);
        add(textArea);
        setVisible(true);

    }
    public  Myframe()
    {
      Setlooks();


    //function:
        InetAddress a= null;
        try {
            a = InetAddress.getLocalHost();
         label.setText("HOST ADRESS:"+a.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String string="";
        Thread t1 = getThread("Send-Server",string);
        Thread t2 = getThread("Receive-Server",string);

        t1.start();
        t2.start();


        try {
            t1.join();
            t2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private static Thread getThread(String name,String string)
    {
        return new Thread(name){
            @Override
            public void run() {
                if(name.equals("Send-Server"))
                {
                    ServerSocket serverSocket=new ServerSocket(1991,string);}
                else{
                    ClientSocket clientSocket=new ClientSocket(2666,string);
                }

            };

        };}
        private    DefaultMutableTreeNode  getTree(String str)
        {
            // 创建根节点
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("MyShare");
            DefaultMutableTreeNode gdNode;


            File[] files=new File(str).listFiles();
            for(int i=0;i<files.length;i++)
            {
                if (!files[i].isDirectory())
                {
                    gdNode = new DefaultMutableTreeNode(files[i].getName().toString());
                    rootNode.add(gdNode);
                }
            }
            return rootNode;
        }
}
