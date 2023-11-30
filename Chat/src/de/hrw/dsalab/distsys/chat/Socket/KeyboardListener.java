package de.hrw.dsalab.distsys.chat.Socket;

import de.hrw.dsalab.distsys.chat.Interfaces.InputListener;

import javax.swing.JTextArea;
import java.io.IOException;
import java.net.Socket;

public class KeyboardListener implements InputListener {

	private JTextArea textArea;
	private String nick;
	boolean connectionShutDown = false;
	Socket socket;
	
	public KeyboardListener(JTextArea textArea, String nick, int port) {
		this.textArea = textArea;
		this.nick = nick;

		try {
			socket = new Socket("localhost", port);
		} catch (IOException e) {
			connectionShutDown = true;
			System.err.println("Server offline try restarting");
		}
	}
	
	@Override
	public void inputReceived(String str) {
		if(connectionShutDown){
			return;
		}
		String message = "<" + nick + "> " + str + System.getProperty("line.separator");
		textArea.append(message);

		try{
			//sending the data to the Server
			socket.getOutputStream().write(message.getBytes());
			//Flushing the buffer
			socket.getOutputStream().flush();
		}catch(Exception ex){
			try{
				socket.close();
				connectionShutDown = true;
				System.out.println("No Server connection, closing socket");
			}catch (IOException ioe){
				System.err.println("Sending Socket could not be closed");
			}
		}
	}
}
