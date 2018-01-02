package tests;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.Test;
import org.snmp4j.PDU;
import org.snmp4j.smi.TcpAddress;

import rest.TrapReceiver;

public class SNMPReceiverTest {
	
	@Test
	public void testReceivingTest(){
		BlockingQueue<PDU> traps=new LinkedBlockingDeque<PDU>();                        
		TrapReceiver receiver= new TrapReceiver();
		try {
			receiver.listen(new TcpAddress(InetAddress.getLocalHost(),162), traps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert(traps.size()==0);
	}

}
