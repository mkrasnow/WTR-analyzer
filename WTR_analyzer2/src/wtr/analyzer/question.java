/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wtr.analyzer;

import java.lang.reflect.Field;

/**
 *
 * @author Max
 */
public class question {
    int num;
    double pick1pay1, pick1pay2, pick2pay1, pick2pay2, ratio;
    boolean questionValid;
    
    //this is for valid questions
    public question(int qn, double p1p1, double p1p2, double p2p1, double p2p2){
        num=qn;
        pick1pay1=p1p1;
        pick1pay2=p1p2;
        pick2pay1=p2p1;
        pick2pay2=p2p2;
        ratio = (p1p1-p2p1)/(p2p2-p2p1);
        questionValid=true;
        System.out.println("Qnum: " + num + ", ratio: "+ ratio);
    }
    
    //this is for invalid questions
    public question(){
        questionValid=false;
    }
    
    public boolean checkNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null)
                return true;
        return false;            
    }
}
