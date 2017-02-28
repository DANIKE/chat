import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.io.IOException;

public class ChatClient extends Frame {
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private boolean bConnected = false;
	
	TextField tfTxt = new TextField();
	TextArea taContent = new TextArea();
	Thread tRecv = new Thread(new RecvThread());

	public static void main(String[] args) {
		new ChatClient().launchFrame();

	}

	public void launchFrame() {
		setLocation(400, 300);
		setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);
		add(taContent, BorderLayout.NORTH);
		pack();
		addWindowListener(new MyWindowMonitor());
		/*addWindowListener(new WindowAdapter() {
			
		})*/
		tfTxt.addActionListener(new TFListener());
		setVisible(true);
		connect();
		tRecv.start();
			
		
	}
	
	class MyWindowMonitor extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			unconnect();
			setVisible(false);
			System.exit(0);
		}
	}
	
	public void connect() {
		try {
			s = new Socket("127.0.0.1", 8888);
			
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
System.out.println("connected!");
			bConnected = true;
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void unconnect() {
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class TFListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String str = tfTxt.getText().trim();
			//taContent.setText(str);
			tfTxt.setText("");
			try {
				
				dos.writeUTF(str);
				dos.flush();
				//dos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	private class RecvThread implements Runnable {

		@Override
		public void run() {
			try {
				while(bConnected) {
					String str = dis.readUTF();
					//System.out.println(str);
					taContent.setText(taContent.getText() + str + '\n');
				} 
			} catch(SocketException e) {
				System.out.println("ÍË³öÁË£¬ bye!");
			} catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	

}
