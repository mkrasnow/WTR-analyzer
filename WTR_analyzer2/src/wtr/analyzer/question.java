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
    double choose1stake4player1, choose1stake4player2, choose2stake4player1, choose2stake4player2, ratio;
    boolean questionValid;
    
    //this is for valid questions
    public question(int qn, double c1sp1, double c1sp2, double c2sp1, double c2sp2){
        num=qn;
        choose1stake4player1=c1sp1;
        choose1stake4player2=c1sp2;
        choose2stake4player1=c2sp1;
        choose2stake4player2=c2sp2;
        ratio = (c1sp1-c2sp1)/(c2sp2-c1sp2);
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
