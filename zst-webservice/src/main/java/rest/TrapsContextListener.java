package rest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.snmp4j.PDU;
import org.snmp4j.smi.TcpAddress;

public class TrapsContextListener implements ServletContextListener{
	public static String TRAPS="traps";
	private Thread listeningThread;
	private TrapReceiver receiver;
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		listeningThread.interrupt();
		receiver.stop();
		
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
			ServletContext context=contextEvent.getServletContext();
			BlockingQueue<PDU> traps=new LinkedBlockingDeque<PDU>();
			context.setAttribute(TRAPS, traps);
			try {
				receiver= new TrapReceiver(new TcpAddress(InetAddress.getLocalHost(),162), traps);
				listeningThread=new Thread(receiver);
				listeningThread.start();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}

