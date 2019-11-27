/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package localappchat2.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author TV.Tinh
 */
public class FromNetworkPacket {
    
    private String address;
    private ByteArrayOutputStream baos;
//    private long timeStamp;
//    public String type = "TCP/UDP/Multicast";
    
    public FromNetworkPacket(String addr, byte[] data){
        this.address = addr;
        this.baos = new ByteArrayOutputStream();
        try {baos.write(data);}
        catch (IOException ex){System.err.println(ex.getMessage());}
    }
    
    public String getAddress(){
        return address;
    }
    public byte[] getData(){
        return baos.toByteArray();
    }
    
}
