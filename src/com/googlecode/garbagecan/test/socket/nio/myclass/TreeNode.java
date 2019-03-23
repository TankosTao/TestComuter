
package com.googlecode.garbagecan.test.socket.nio.myclass;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TreeNode
{

public TreeNode()
{


// 创建根节点
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("MyShare");
    DefaultMutableTreeNode gdNode;


File[] files=new File("C:\\Users\\TankOStao\\Desktop\\MyShare").listFiles();
for(int i=0;i<files.length;i++)
{
    if (!files[i].isDirectory())
    {
        gdNode = new DefaultMutableTreeNode(files[i].getName().toString());
rootNode.add(gdNode);
    }
}

}
    public String[] getfile()
    {
        File file =new File("C:\\Users\\TankOStao\\Desktop\\MyShare");
        File[] files=file.listFiles();
        String[] str=new String[files.length];
        int j=0;
        for (int i=0;i<files.length;i++)
        {
            if (!files[i].isDirectory())
            {
                str[j]=files[i].getName().toString();
                j++;
            }
        }
        return str;
    }



        public static String[] trim(String character, String symbol){
            //
   return character.split(symbol);
        }


}
