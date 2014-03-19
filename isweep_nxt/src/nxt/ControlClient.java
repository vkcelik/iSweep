package nxt;
import java.io.DataInputStream;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class ControlClient {
	public static void main(String[] args) throws Exception {
		String connected = "Connected";
		String waiting = "Waiting...";
		String closing = "Closing...";

		while (true)
		{
			LCD.drawString(waiting,0,0);
			LCD.refresh();

			BTConnection btc = Bluetooth.waitForConnection();

			LCD.clear();
			LCD.drawString(connected,0,0);
			LCD.refresh();	

			DataInputStream dis = btc.openDataInputStream();


			char c = dis.readChar();
			LCD.drawInt(c,0,1);
			LCD.refresh();

			switch (c) {
			case 'F':
				Motor.A.forward();
				Motor.B.forward();
				Thread.sleep(1000);
				Motor.A.stop(true);
				Motor.B.stop(true);
				break;

			default:
				break;
			}

			dis.close();
			Thread.sleep(100); // wait for data to drain
			LCD.clear();
			LCD.drawString(closing,0,0);
			LCD.refresh();
			btc.close();
			LCD.clear();
		}
	}

}
