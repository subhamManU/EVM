/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evm;

import MFS100.FingerData;
import MFS100.MFS100;
import MFS100.MFS100Event;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
/**
 *
 * @author User
 */
public class FPMatcher implements MFS100Event{
    
    private byte[] ansi;
    private int currentRowNo, limit;
    private DatabaseHandler dbHandler;
    private MFS100 mfs100;
    private String username;

    public FPMatcher(byte[] ansi) {
        this.ansi = ansi;
        mfs100 = new MFS100(this);
        currentRowNo = 0;
        dbHandler = new  DatabaseHandler();
        limit = dbHandler.count();
    }
    
    public boolean match(){
        
        byte[] gansi;
        for(int i = 0; i <= limit; i++){
           gansi = getNextAnsi(i);
           
            
           if(gansi == null)
               return false;
           
           int val = mfs100.MatchANSI(ansi, gansi);
           if(val >= 14000){
               setUsername(Hex.encodeHexString(gansi));
               return true;
           }
        }
        
        return false;
    }
    
    private byte[] getNextAnsi(int i){
        String hex = "";
        try{
            hex = dbHandler.getANSI(i);
        }catch(Exception e){
            System.out.println("ERROR");
            return "xxx".getBytes();
        }
        return HexToByte(hex);
    }
    
    public boolean findMatch() throws Exception{
        String arr[], uname;
        int i = 0, j = 0 ;
        
        do{
            arr = dbHandler.getFingerANSI(j++);
            
            for( i = 0 ;i < 10; i++){
                int val = mfs100.MatchANSI(ansi, HexToByte(arr[i]));
                if(val >= 14000){
                    uname = dbHandler.getUsername(j-1);
                    setUsername(uname);
                    return true;
                }
            }
            
            
        }while(j < limit);
        return false;
    }
    
    private byte[] HexToByte(String hex){
        try {
            return Hex.decodeHex(hex.toCharArray());
        } catch (DecoderException ex) {
            Logger.getLogger(FPMatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } 

    @Override
    public void OnPreview(FingerData fd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void OnCaptureCompleted(boolean bln, int i, String string, FingerData fd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setUsername(String ansi) {
        username = ansi;
        
    }
    
    public String getUsername(){
        boolean bool = checkFlag(username);
        if(bool)
            return username;
        else
            return "NA";
    }

    private boolean checkFlag(String username) {
        int flag = dbHandler.getFlag(username);
        
        if(flag == 1){
            return false;
        }
        return true;
    }
    
    
    
}
