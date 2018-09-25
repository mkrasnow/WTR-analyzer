/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wtr.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 *
 * @author Max
 */
public class scale {
    String label, P1, P2, subnum;
    ArrayList<question> questions;
    double WTR;
    double Consistency;
    double PerfectCon;
    double WTRError;
    int numChoices;
    int numIncChoices;
    
    ArrayList<Integer> choices;
    ArrayList<Double> switchpoints;
    ArrayList<Double> ratios;
    ArrayList<Integer> ConsOfSP;
    ArrayList<Double> MaxConsSPs;
    
    public scale(){
        choices = new ArrayList();
        switchpoints = new ArrayList();
        ratios = new ArrayList();
        questions=new ArrayList();
    }
    public scale(String p1, String p2, String lab, int nc, ArrayList<question> qs){
        P1=p1;
        P2=p2;
        label=lab;
        numChoices=nc;
        questions=qs;
        choices = new ArrayList();
        switchpoints = new ArrayList();
        ratios = new ArrayList();
        computeRnS();
        System.out.println(label + ", ratios size: " + ratios.size());
        
    }
    public scale(String num, String p1, String p2, String lab, int nc, ArrayList<question> qs, ArrayList<Integer> ch){
        subnum=num;
        P1=p1;
        P2=p2;
        label=lab;
        questions=qs;
        choices = ch;
        numChoices=nc;
        choices = new ArrayList();
        switchpoints = new ArrayList();
        ratios = new ArrayList();
        computeRnS();
        computeWTRv2();
        System.out.println(subnum + ":" + label + ", switchpoints size: " + switchpoints.size());
    }
    
    public boolean checkScale(){
        if((P1 != null) && (P2 != null) && (label != null) && /*(numChoices != null) &&*/ (questions != null) && (choices != null) && (switchpoints != null) && (ratios != null)){
            try{
                for (question q : questions)
                    if (q.checkNull())
                        return false;
            }
            catch(IllegalAccessException e)
            {
                System.out.println(e);
            }
        }
        
        return true;
    }
          
       
    public void computeRnS(){ //compute ratios and switchpoints from questions
        for(question q:questions){
            ratios.add(q.ratio);
        }
        //calculate switchpoitns from ratios (where 1st and last sp equal the 1st and last ratio, and the middle ratios are averages.
        switchpoints = new ArrayList();
        switchpoints.add(ratios.get(0));
        System.out.print("Switchpoints are: "+ switchpoints.get(switchpoints.size()-1)+ ", ");
        for(int i=0; i<(ratios.size()-1); i++){
            switchpoints.add((ratios.get(i)+ratios.get(i+1))/2);
            System.out.print(switchpoints.get(switchpoints.size()-1)+ ", ");
        }
        switchpoints.add(ratios.get(ratios.size()-1));
        System.out.println(switchpoints.get(switchpoints.size()-1));
        }
    
    public void computeWTRv2(){  //compute WTR and consistency metrics
        ConsOfSP = new ArrayList();
        MaxConsSPs = new ArrayList();
        
        //calculate consistency of all possible switchpoints
        for(int i=0; i<switchpoints.size(); i++){
            //create perfect choices for that switchpoint
            ArrayList<Integer>perfectChoices = new ArrayList();
            for(int j=0; j<ratios.size(); j++){
                if(ratios.get(j)>switchpoints.get(i)){
                    perfectChoices.add(1);
                } else {
                    perfectChoices.add(2);
                }
                if(Objects.equals(ratios.get(j), switchpoints.get(i))&&j==ratios.size()-1){
                    perfectChoices.remove(j);
                    perfectChoices.add(1);
                }
            }
            //test choices against perfect
            //int localConsistencyCount
            int localCC=0;
            for(int j=0; j<choices.size(); j++){
                if(Objects.equals(choices.get(j), perfectChoices.get(j))){
                    localCC++;
                }
            }
            ConsOfSP.add(localCC);
            System.out.print("Perfect for "+switchpoints.get(i)+" would be: ");
            for(int s:perfectChoices){
                System.out.print(s+", ");
            }
            System.out.println(" and the consistency with that is: "+(double)((double)localCC/(double)ratios.size()));
        }
        
        //find highest consistency switchpoints and calculate WTR
        int MaxC = Collections.max(ConsOfSP);
        double TempWTR=0;
        for(int i=0; i<ConsOfSP.size(); i++){
            if(ConsOfSP.get(i)==MaxC){
                MaxConsSPs.add(switchpoints.get(i));
                TempWTR+=switchpoints.get(i);
            }
        }
        WTR=(double)TempWTR/(double)MaxConsSPs.size();
        //Consistency=(double)MaxC/(double)ratios.size();
        
        //calculate WTRError
        /*if(Consistency>.9999999){
            WTRError=0;
        } else */{

            numIncChoices=0;
            for(int i=0; i<ratios.size(); i++){
                if(choices.get(i)==1){
                    if(ratios.get(i)>=WTR){
                        System.out.println("i: " + i + ", choice: " + choices.get(i) + ", ratio: " + ratios.get(i)+ " = CONS");
                    } else {
                        numIncChoices++;
                        WTRError+=Math.abs(ratios.get(i)-WTR);
                        System.out.println("i: " + i + ", choice: " + choices.get(i) + ", ratio: " + ratios.get(i)+ " = INC");
                    }
                } else {
                    if(ratios.get(i)<=WTR){
                        System.out.println("i: " + i + ", choice: " + choices.get(i) + ", ratio: " + ratios.get(i)+ " = CONS");
                    } else {
                        numIncChoices++;
                        WTRError+=Math.abs(ratios.get(i)-WTR);
                        System.out.println("i: " + i + ", choice: " + choices.get(i) + ", ratio: " + ratios.get(i)+ " = INC");
                    }
                }
            }
        }
        System.out.println("numChoices: " + numChoices + ", numIncChoices: " + numIncChoices);
        Consistency=(double)(numChoices-numIncChoices)/(double)numChoices;
        System.out.println("Computed WTR="+WTR+", Consistency="+Consistency+", WTRerror="+WTRError);
    }
}
