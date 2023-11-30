package de.hrw.dsalab.distsys.chat.main;

import de.hrw.dsalab.distsys.chat.Interfaces.InputListener;
import de.hrw.dsalab.distsys.chat.Interfaces.NetworkListener;
import de.hrw.dsalab.distsys.chat.RMI.RMIKeyboardListener;
import de.hrw.dsalab.distsys.chat.RMI.RMINetworkListener;
import de.hrw.dsalab.distsys.chat.Socket.ClientSocketListener;
import de.hrw.dsalab.distsys.chat.Socket.KeyboardListener;
import de.hrw.dsalab.distsys.chat.UDP.Broadcast.BroadcastKeyboardListener;
import de.hrw.dsalab.distsys.chat.UDP.Broadcast.BroadcastNetworkListener;
import de.hrw.dsalab.distsys.chat.UDP.Socket.UDPKeyboardListener;
import de.hrw.dsalab.distsys.chat.UDP.Socket.UDPSocketListener;
import de.hrw.dsalab.distsys.chat.XML.XMLReader;
import de.hrw.dsalab.distsys.chat.main.Client;
import de.hrw.dsalab.distsys.chat.UDP.Multicast.MulticastKeyboardListener;
import de.hrw.dsalab.distsys.chat.UDP.Multicast.MulticastNetworkListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chat extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Client client;
	private String nick;

	public Chat() {
		JPanel mainPanel;
		
		setTitle("Chat Tool v0.1");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		nick = retrieveNickName();
		mainPanel = setupChatView();
		getContentPane().add(mainPanel);
		getContentPane().getParent().invalidate();
		getContentPane().validate();
	}

	private JPanel setupChatView() {
		JPanel panel = new JPanel();
		JPanel southPanel = new JPanel();
		JTextArea textArea = new JTextArea();
		final JTextField textField = new JTextField();
		JButton sendButton = new JButton("Send");
				
		textField.setColumns(60);
		
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				client.getInListener().inputReceived(textField.getText());
				textField.setText("");
			}
			
		});

		
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setEditable(false);
		
		southPanel.setLayout(new FlowLayout());
		southPanel.add(textField);
		southPanel.add(sendButton);
		
		panel.setLayout(new BorderLayout());
		panel.add(textArea, BorderLayout.CENTER);
		panel.add(southPanel, BorderLayout.SOUTH);

		XMLReader xmlReader = new XMLReader("C:/Users/Neufe/Desktop/Felix/Projects/Uni/Distributed Systems/DistributedSystemsWI22-23/Chat/src/de/hrw/dsalab/distsys/chat/XML/config.xml");
		String fontName = xmlReader.getFontName();
		int fontSize = xmlReader.getFontSize();
		Font newFont = new Font(fontName, Font.PLAIN, fontSize);

		textField.setFont(newFont);
		sendButton.setFont(newFont);
		textArea.setFont(newFont);

		String theme = xmlReader.getTheme();
		if(theme.equalsIgnoreCase("dark")){
			textArea.setBackground(Color.DARK_GRAY);
			textArea.setForeground(Color.WHITE);
			southPanel.setBackground(Color.DARK_GRAY);
			textField.setBackground(Color.BLACK);
			textField.setForeground(Color.WHITE);
			sendButton.setBackground(Color.BLACK);
			sendButton.setForeground(Color.WHITE);
		}else{
			textArea.setBackground(Color.LIGHT_GRAY);
			textArea.setForeground(Color.BLACK);
			southPanel.setBackground(Color.LIGHT_GRAY);
			textField.setBackground(Color.WHITE);
			textField.setForeground(Color.BLACK);
			sendButton.setBackground(Color.WHITE);
			sendButton.setForeground(Color.BLACK);
		}

		if(xmlReader.getShortcuts().get("Enter").equalsIgnoreCase("send_message")){
			textField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.getInListener().inputReceived(textField.getText());
					textField.setText("");
				}
			});
		}

		int port = xmlReader.getPort();
		String connection = xmlReader.getConnection();
		// this is just an example, please modify for your listeners accordingly...

		//Setting default values
		InputListener kListener = new KeyboardListener(textArea, nick, port);
		NetworkListener sListener = new ClientSocketListener(textArea, nick, port);

		if(connection.equalsIgnoreCase("Socket")) {
			kListener = new KeyboardListener(textArea, nick, port);
			sListener = new ClientSocketListener(textArea, nick, port);
		}

		if(connection.equalsIgnoreCase("UDP")) {
			kListener = new UDPKeyboardListener(textArea, nick, port);
			UDPKeyboardListener temp = (UDPKeyboardListener) kListener;
			sListener = new UDPSocketListener(textArea,nick, temp.getSocket());
		}

		if(connection.equalsIgnoreCase("Broadcast")) {
			kListener = new BroadcastKeyboardListener(textArea,nick, port);
			BroadcastKeyboardListener temp = (BroadcastKeyboardListener) kListener;
			sListener = new BroadcastNetworkListener(textArea, nick,temp.getSocket());
		}

		if(connection.equalsIgnoreCase("Multicast")) {
			kListener = new MulticastKeyboardListener(textArea,nick, port);
			MulticastKeyboardListener temp = (MulticastKeyboardListener) kListener;
			sListener = new MulticastNetworkListener(textArea, nick, temp.getSocket());
		}

		if(connection.equalsIgnoreCase("RMI")) {
			try{
				kListener = new RMIKeyboardListener(textArea, nick);
				sListener = new RMINetworkListener(textArea, nick);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		this.client = new Client(kListener, sListener);

		return panel;
	}

	private String retrieveNickName() {
		return (String)JOptionPane.showInputDialog(this, "Enter your nickname please:", "Enter nickname", JOptionPane.QUESTION_MESSAGE);
	}

	public Client getClient() {
		return client;
	}

	public static void main(String[] args) {
		Chat chat = new Chat();

		//Creates a new Thread responsible for receiving messages
		Thread receiveThread = new Thread(() -> {
			while(true) {
				chat.getClient().getNetListener().messageReceived("");
			}
		});

		receiveThread.start();
	}

}
