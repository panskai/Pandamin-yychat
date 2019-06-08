package com.yychatclient.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



import com.yychat.model.Message;
import com.yychatclient.controller.ClientConnect;

public class FriendChat1 extends JFrame implements ActionListener{//���̳�

	//Center����
	JScrollPane jsp;
	JTextArea jta;
	
	//South����
	JPanel jp;
	JTextField jtf;
	JButton jb;
	JButton jb1;
	JButton jb2;
	String sender;
	String receiver;
	
	public FriendChat1(String sender,String receiver){
		this.sender=sender;
		this.receiver=receiver;
		
		jta = new JTextArea();//�ı�����
		jta.setEditable(false);
		jta.setForeground(Color.red);
		jsp = new JScrollPane(jta);
		this.add(jsp,"Center");
		
		jp=new JPanel();
		jtf=new JTextField(15);
		jtf.setForeground(Color.red);
		jb=new JButton("����");
		jb.addActionListener(this);
		jb1=new JButton("����");
		jb1.addActionListener(this);
		jb2=new JButton("̰����");
		jb2.addActionListener(this);
		jp.add(jtf);
		jp.add(jb);
		jp.add(jb1);
		jp.add(jb2);
		this.add(jp,"South");
		
		this.setSize(450,240);
		this.setTitle(sender+"���ں�"+receiver+"����");
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);//������ʾ����
		this.setVisible(true);		
	}
	
	public static void main(String[] args) {
		FriendChat1 friendChat=new FriendChat1("1","2");

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {//�¼���������
		if(arg0.getSource()==jb){
			jta.append(jtf.getText()+"\r\n");
			
			//�����������������Ϣ
			Message mess=new Message();
			mess.setSender(sender);
			mess.setReceiver(receiver);
			mess.setContent(jtf.getText());
			mess.setMessageType(Message.message_Common);
			ObjectOutputStream oos;
			try {
				Socket s=(Socket)ClientConnect.hmSocket.get(sender);
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(mess);
				
				//�ܲ�����������������˷�������������Ϣ��
			/*	ObjectInputStream ois = new ObjectInputStream(ClientConnect.s.getInputStream());
				mess=(Message)ois.readObject();//����������Ϣ
				String showMessage=mess.getSender()+"��"+mess.getReceiver()+"˵��"+mess.getContent();
				System.out.println(showMessage);
				jta.append(showMessage+"\r\n");*/
				
			} catch (IOException  e) {				
				e.printStackTrace();
			}			
		}
		if(arg0.getSource()==jb1){
			new CaptureScreen();
			}
		if (arg0.getSource() == jb2) {
			GreedSnake gs=new GreedSnake();
		}
	}
	
	public void appendJta(String showMessage){
		jta.append(showMessage+"\r\n");		
	}
}