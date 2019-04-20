package com.qqchatserver.controller;

import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.Iterator;






import javax.swing.JOptionPane;






import com.yychat.model.Message;

public class ServerReceiverThread extends Thread{
	 
	Socket s;
	ObjectInputStream ois;
	 ObjectOutputStream oos;
	 Message mess;
	 
	 String sender;
     public ServerReceiverThread(Socket s){//
    	 this.s=s;
     }
     public void run(){
    	 
    	 while(true){
		try {
			//������Ϣ
			ois = new ObjectInputStream(s.getInputStream());
			mess=(Message)ois.readObject();
			sender=mess.getSender();
			System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵"+mess.getContent());
			
			if(mess.getMessageType().equals(Message.message_Common)){
			Socket s1=(Socket)StertServer.hmSocket.get(mess.getReceiver());
			sendMessage(s1,mess);
			}
			
			//��2�������������ܵ�������������ߺ�����Ϣ(���ͣ�mess_OnlineFriend)
			if(mess.getMessageType().equals(Message.message_RequestOnlineFriend)){
				Set friendSet=StertServer.hmSocket.keySet();//�����ϣ����ߺ��Ѽ���
				
				Iterator it=friendSet.iterator();//������
				String friendName;
				String friendString=" ";
				while(it.hasNext()){//�жϻ���û����һ��Ԫ��
					friendName=(String)it.next();//ȡ����һ��Ԫ��
					if(!friendName.equals(mess.getSender()))
					   friendString=friendName+" "+friendString;
					
				}
				System.out.println("ȫ�����ѵ�����"+friendString);
				
				
				
				mess.setContent(friendString);
				mess.setMessageType(Message.message_OnlineFriend);
				mess.setSender("Server");
				mess.setReceiver(sender);
				
				sendMessage(s,mess);
				
			}
		} catch (IOException e) {
		e.printStackTrace();
			 
		}
		catch ( ClassNotFoundException e){
			e.printStackTrace();
		}
    	 
     }
     }
	private void sendMessage(Socket s,Message mess) throws IOException {
		oos=new ObjectOutputStream(s.getOutputStream());//ת��������Ϣ
		 oos.writeObject(mess);
	}
}
	