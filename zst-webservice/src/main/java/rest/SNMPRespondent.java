package rest;
import java.io.IOException;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.snmp4j.PDU;
import org.snmp4j.smi.VariableBinding;

@Path("/snmp")
public class SNMPRespondent {

	SNMPClient snmpClient;
	@GET
	@Path("getNext")
	public String getNext(
			@QueryParam("OID") String oid,
			@QueryParam("community") String community){
		
		PDU responsePDU;
		try {
			snmpClient=new SNMPClient(community);
			responsePDU = snmpClient.sendGetNext(oid);
			String response=snmpClient.convertVariableBindingsToJSON(responsePDU);
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "null";
	} 
	
	
	@GET
	@Path("get")
	public String get(
			@QueryParam("OID") String oid,
			@QueryParam("community") String community){
		
		PDU responsePDU;
		try {
			snmpClient=new SNMPClient(community);
			responsePDU = snmpClient.sendGet(oid);
			String response=snmpClient.convertVariableBindingsToJSON(responsePDU);
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "null";
	}
	
	@GET
	@Path("getBulk")
	public String getBulk(
			@QueryParam("OID") String oid,
			@QueryParam("community") String community){
		
		PDU responsePDU;
		try {
			snmpClient=new SNMPClient(community);
			responsePDU = snmpClient.sendGetBulk(oid,0,100);
			String response=snmpClient.convertVariableBindingsToJSON(responsePDU);
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "null";
	}
	
	@GET
	@Path("getTable")
	public String getTable(
			@QueryParam("OID") String oid,
			@QueryParam("community") String community){
		PDU responsePDU;
		try {
			snmpClient=new SNMPClient(community);
			responsePDU = snmpClient.getTable(oid);
			String response=snmpClient.convertVariableBindingsToJSON(responsePDU);
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "null";
		
	}
	
	
	
	
	@GET
	@Path("/test")
	public String get(){
		return "test";
	}
}
