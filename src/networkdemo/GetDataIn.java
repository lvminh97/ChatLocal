/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

/**
 *
 * @author Windows 7
 */

public final class GetDataIn {
    private final String msg;
    
    public GetDataIn(String s){
        this.msg = s;
        print();
    }
    public void print(){
        if(msg!= null){
            System.out.println(this.msg);
        }
    }
}
