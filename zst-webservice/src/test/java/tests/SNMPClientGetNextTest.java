package tests;

import java.io.IOException;

import org.junit.Test;
import org.snmp4j.PDU;

import com.fasterxml.jackson.core.JsonProcessingException;

import rest.SNMPClient;

public class SNMPClientGetNextTest {
	
	@Test
	public void testgetNextTest(){
		SNMPClient client=new SNMPClient("default");
		PDU pdu=null;
		try {
			pdu=client.getTable(".1.3.6.1.2.1.4.22");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str;
		try {
			str = client.convertVariableBindingsToJSON(pdu);
			System.out.println(str);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
