package tests;

import java.io.IOException;

import org.junit.Test;
import org.snmp4j.PDU;

import com.fasterxml.jackson.core.JsonProcessingException;

import rest.SNMPClient;
import rest.SNMPRespondent;

public class SNMPClientGetNextTest {
	
	@Test
	public void testgetNextTest(){
		PDU pdu=null;
			SNMPRespondent resp=new SNMPRespondent();
			String str=resp.getTable(".1.3.6.1.2.1.4.22", "default");


	}
}
