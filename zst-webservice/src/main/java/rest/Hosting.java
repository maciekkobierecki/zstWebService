package rest;

import org.snmp4j.smi.OID;

public class Hosting {

    private OID Id;
    private String name;
    private long websites;

    public Hosting(OID id, String name, long websites) {
        Id = id;
        this.name = name;
        this.websites = websites;
    }

    public OID getId(){
    	return Id;
    }
    public String getName(){
    	return name;
    }
    public long getWebsites(){
    	return websites;
    }
    
}
