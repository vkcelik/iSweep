package pc;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

public class ControlServer {

	public static void main(String[] args) throws Exception{
		NXTConnector conn = new NXTConnector();

		conn.addLogListener(new NXTCommLogListener(){

			public void logEvent(String message) {
				System.out.println("BTSend Log.listener: "+message);

			}

			public void logEvent(Throwable throwable) {
				System.out.println("BTSend Log.listener - stack trace: ");
				throwable.printStackTrace();

			}

		} 
				);
		// Connect to any NXT over Bluetooth
		boolean connected = conn.connectTo("btspp://gruppe 3");


		if (!connected) {
			System.err.println("Failed to connect to any NXT");
			System.exit(1);
		}

		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		try {
			dos.writeChar('F');
			dos.flush();
		} catch (IOException ioe) {
			System.out.println("IO Exception writing bytes:");
			System.out.println(ioe.getMessage());
		}

		try {
			dos.close();
			conn.close();
		} catch (IOException ioe) {
			System.out.println("IOException closing connection:");
			System.out.println(ioe.getMessage());
		}
	}
}
