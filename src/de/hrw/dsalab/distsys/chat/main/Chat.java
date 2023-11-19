package de.hrw.dsalab.distsys.chat.main;

import de.hrw.dsalab.distsys.chat.main.Client;
import de.hrw.dsalab.distsys.chat.UDP.Multicast.MulticastKeyboardListener;
import de.hrw.dsalab.distsys.chat.UDP.Multicast.MulticastNetworkListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		// this is just an example, please modify for your listeners accordingly...
		//UDPKeyboardListener kListener = new UDPKeyboardListener(textArea, nick);
		//UDPSocketListener  sListener = new UDPSocketListener(textArea,nick,kListener.getSocket());

		//BroadcastKeyboardListener kListener = new BroadcastKeyboardListener(textArea,nick);
		//BroadcastNetworkListener sListener = new BroadcastNetworkListener(textArea, nick,kListener.getSocket());

		MulticastKeyboardListener kListener = new MulticastKeyboardListener(textArea,nick);
		MulticastNetworkListener sListener = new MulticastNetworkListener(textArea, nick,kListener.getSocket());
		this.client = new Client(kListener,sListener);

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
			chat.getClient().getNetListener().messageReceived("");
		});

		receiveThread.start();
	}

}
