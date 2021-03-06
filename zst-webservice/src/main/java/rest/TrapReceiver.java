package rest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.tools.console.SnmpRequest;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

public class TrapReceiver implements CommandResponder, Runnable{
	private BlockingQueue<PDU> traps;
	private TransportIpAddress address;
	private ThreadPool threadPool;
	private AbstractTransportMapping transport;
	
	public TrapReceiver(TransportIpAddress address,BlockingQueue<PDU>traps){
		this.traps=traps;
		this.address=address;
		
	}
	public synchronized void listen() throws IOException
	  {
	    if (address instanceof TcpAddress)
	    {
	      transport = new DefaultTcpTransportMapping((TcpAddress) address);
	    }
	    else
	    {
	      transport = new DefaultUdpTransportMapping((UdpAddress) address);
	    }

	    threadPool = ThreadPool.create("DispatcherPool", 10);
	    MessageDispatcher mtDispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
	    // add message processing models
	    mtDispatcher.addMessageProcessingModel(new MPv1());
	    mtDispatcher.addMessageProcessingModel(new MPv2c());

	    // add all security protocols
	    SecurityProtocols.getInstance().addDefaultProtocols();
	    SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

	    //Create Target
	    CommunityTarget target = new CommunityTarget();
	    target.setCommunity( new OctetString("public"));
	    
	    Snmp snmp = new Snmp(mtDispatcher, transport);
	    snmp.addCommandResponder(this);
	    
	    transport.listen();
	    System.out.println("Listening on " + address);

	    try
	    {
	      this.wait();
	    }
	    catch (InterruptedException ex)
	    {
	      Thread.currentThread().interrupt();
	    }
	  }
	
	 public void stop(){
		 threadPool.stop();
		 try {
			transport.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	 public synchronized void processPdu(CommandResponderEvent cmdRespEvent)
	  {
	    System.out.println("Received PDU...");
	    PDU pdu = cmdRespEvent.getPDU();
	    if (pdu != null)
	    {

	      System.out.println("Trap Type = " + pdu.getType());
	      System.out.println("Variable Bindings = " + pdu.getVariableBindings());
	      int pduType = pdu.getType();
          traps.add(cmdRespEvent.getPDU());
          System.out.println("added");
	      if ((pduType != PDU.TRAP) && (pduType != PDU.V1TRAP) && (pduType != PDU.REPORT)
	      && (pduType != PDU.RESPONSE))
	      {
	        pdu.setErrorIndex(0);
	        pdu.setErrorStatus(0);
	        pdu.setType(PDU.RESPONSE);
	        StatusInformation statusInformation = new StatusInformation();
	        StateReference ref = cmdRespEvent.getStateReference();
	        try
	        {
	          cmdRespEvent.getMessageDispatcher().returnResponsePdu(cmdRespEvent.getMessageProcessingModel(),
	          cmdRespEvent.getSecurityModel(), cmdRespEvent.getSecurityName(), cmdRespEvent.getSecurityLevel(),
	          pdu, cmdRespEvent.getMaxSizeResponsePDU(), ref, statusInformation);
	        }
	        catch (MessageException ex)
	        {
	          System.err.println("Error while sending response: " + ex.getMessage());
	          LogFactory.getLogger(SnmpRequest.class).error(ex);
	        }
	      }
	    }
	  }

	@Override
	public void run() {
		try {
			listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	}