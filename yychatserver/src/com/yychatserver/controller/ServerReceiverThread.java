package com.yychatserver.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

import com.yychat.model.Message;

public class ServerReceiverThread extends Thread{//���븴��
	Socket s;
	
	ObjectInputStream ois;
	ObjectOutputStream oos;
	Message mess;
	String sender;
	
	public ServerReceiverThread(Socket s){//s���뷢�������Ӧ�ķ�����Socket����
		this.s=s;
		//System.out.println(s);
	}

	public void run() {
		while (true) {
			try {
				ois = new ObjectInputStream(s.getInputStream());
				mess=(Message)ois.readObject();//����������Ϣ������
				sender=mess.getSender();
				System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵��"+mess.getContent());
				
				System.out.println("�ҵ�"+mess.getMessageType());
				
				if(mess.getMessageType().equals(Message.message_AddFriend)){
					String addFriendName=mess.getContent();
					System.out.println("��Ҫ�����º��ѵ����֣�"+addFriendName);
					if(!YychatDbUtil.seekUser(addFriendName)){
						mess.setMessageType(Message.message_AddFriendFailure_NoUser);
						
					}else{
						String relationType="1";
						if(YychatDbUtil.seekRelation(sender,addFriendName,relationType)){
							mess.setMessageType(Message.message_AddFriendFailure_AlreadyFriend);
							
						}else{
							//�����Ǻ��ѿ������ӣ�
							int count=YychatDbUtil.addRelation(sender,addFriendName,relationType);
							if(count!=0){
								mess.setMessageType(Message.message_AddFriendSuccess);
								String allFriendName=YychatDbUtil.getFriendString(sender);
								mess.setContent(allFriendName);
							}
						}
					}
					sendMessage(s, mess);
				}		
				if(mess.getMessageType().equals(Message.message_Common)){
					Socket s1=(Socket)StartServer.hmSocket.get(mess.getReceiver());//�õ�������������Ӧ�ķ�����Socket����
					sendMessage(s1,mess);
				}
				
				//��2�������������յ�������������ߺ�����Ϣ(���ͣ�message_OnlineFriend)
				if(mess.getMessageType().equals(Message.message_RequestOnlineFriend)){
					//�õ�ȫ�����ߺ��ѵ�����
					Set friendSet=StartServer.hmSocket.keySet();//������,���ߺ��Ѽ���
					Iterator it=friendSet.iterator();//����������
					String friendName;
					String friendString=" ";
					while(it.hasNext()){//�жϻ���û����һ��Ԫ��
						friendName=(String)it.next();//ȡ����һ��Ԫ��
						if(!friendName.equals(mess.getSender()))
							friendString=friendString+friendName+" ";//Ϊʲô�ÿո�
					}
					System.out.println("ȫ�����ѵ����֣�"+friendString);
					
					//����ȫ�����ѵ����ֵ��ͻ���
					mess.setContent(friendString);
					mess.setMessageType(Message.message_OnlineFriend);
					mess.setSender("Server");
					mess.setReceiver(sender);					
					sendMessage(s, mess);
				}
				
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}

	public void sendMessage(Socket s,Message mess) throws IOException {
		oos=new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(mess);//ת��������Ϣ
	}
}