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
    String label, P1, P2, subnum, WTRLoc;
    ArrayList<question> questions;
    double WTR;
    double Consistency;
    double PerfectCon;
    double WTRError;
    int numChoices;
    int numIncChoices;
    double maxConsistency;
    double SPrange;
    double SPrankRange;
    int numSwitches;
    boolean scaleValid;
    boolean choicesComplete;
    
    ArrayList<Integer> choices;
    ArrayList<Double> switchpoints;
    ArrayList<Double> ratios;
    ArrayList<Integer> SortedIndexes;
    ArrayList<Integer> ConsOfSP;
    ArrayList<Double> MaxConsSPs;
    ArrayList<Integer>MaxConsSPRanks;
    
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
        scaleValid=true;
        choicesComplete=true;
        checkScale();
        if(scaleValid){computeRnS();}
    } 
    public final void checkScale(){
        if(P1.isEmpty()){
            P1 = "P1unlabeled";
            scaleValid = false;
        }
        if(P2.isEmpty()){
            P2 = "P2unlabeled";
            scaleValid = false;
        }
        if(label.isEmpty()){
            label = "ScaleUnlabeled";
            scaleValid = false;
        }
        if(numChoices==0){
            numChoices = -1;
            scaleValid = false;
        }
        if(numChoices!=questions.size()){
            numChoices=-1;
            scaleValid=false;
        }
        for(question q:questions){
            if(q.questionValid){
            } else {
                scaleValid=false;
            }
        }
    }
          
       
    public final void computeRnS(){ //compute ratios and switchpoints from questions
        for(question q:questions){
            ratios.add(q.ratio);
        }
        //this code is to put the questions in order of descending ratio, and keep track of the new ordering for reordering the choices
        ArrayList<Double>tempRatios = new ArrayList();
        ArrayList<Integer>tempIndexes = new ArrayList();
        SortedIndexes = new ArrayList();
        for(int i=0; i<ratios.size(); i++){
            tempIndexes.add(i);
        }
        while(!ratios.isEmpty()){
            int index = ratios.indexOf(Collections.max(ratios));
            //System.out.println("Highest Ratio=" + ratios.get(index) + " at question index " + index);
            tempRatios.add(ratios.get(index));
            SortedIndexes.add(tempIndexes.get(index));
            ratios.remove(index);
            tempIndexes.remove(index);
        }
        ratios=tempRatios;

      
        //calculate switchpoints from ratios (where 1st and last sp equal the 1st and last ratio, and the middle ratios are averages.
        switchpoints = new ArrayList();
        switchpoints.add(ratios.get(0));
        //System.out.print("Switchpoints are: "+ switchpoints.get(switchpoints.size()-1)+ ", ");
        for(int i=0; i<(ratios.size()-1); i++){
            switchpoints.add((ratios.get(i)+ratios.get(i+1))/2);
            //System.out.print(switchpoints.get(switchpoints.size()-1)+ ", ");
        }
        switchpoints.add(ratios.get(ratios.size()-1));
        //System.out.println(switchpoints.get(switchpoints.size()-1));
        }
    
    public void computeWTRv2(){  //compute WTR and consistency metrics
        ConsOfSP = new ArrayList();
        MaxConsSPs = new ArrayList();
        MaxConsSPRanks = new ArrayList();
        
        //reorder choices to match sorted ratio order 
        ArrayList<Integer> tempChoices = new ArrayList();
        while(!SortedIndexes.isEmpty()){
            tempChoices.add(choices.get(SortedIndexes.get(0)));
            //System.out.println("Index of " + SortedIndexes.get(0) + " was choice of " + choices.get(SortedIndexes.get(0)));
            SortedIndexes.remove(0);
        }
        choices = tempChoices;
        
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
                //this makes the perfect choice for the lowest detectable WTR 1 (self)
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
            /*System.out.print("Perfect for "+switchpoints.get(i)+" would be: ");
            for(int s:perfectChoices){
                System.out.print(s+", ");
            }
            System.out.println(" and the consistency with that is: "+(double)((double)localCC/(double)ratios.size()));
            */
        }
        
        //find highest consistency switchpoints and calculate WTR
        int MaxC = Collections.max(ConsOfSP);
        maxConsistency = (double)MaxC/(double)choices.size();
        double TempWTR=0;
        for(int i=0; i<ConsOfSP.size(); i++){
            if(ConsOfSP.get(i)==MaxC){
                MaxConsSPs.add(switchpoints.get(i));
                MaxConsSPRanks.add(i);
                TempWTR+=switchpoints.get(i);
            }
        }
        SPrange=(double)Collections.max(MaxConsSPs)-(double)Collections.min(MaxConsSPs);
        SPrankRange=Collections.max(MaxConsSPRanks)-Collections.min(MaxConsSPRanks);
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
                        //System.out.println("i: " + i + ", choice: " + choices.get(i) + ", ratio: " + ratios.get(i)+ " = CONS");
                    } else {
                        numIncChoices++;
                        WTRError+=Math.abs(ratios.get(i)-WTR);
                        //System.out.println("i: " + i + ", choice: " + choices.get(i) + ", ratio: " + ratios.get(i)+ " = INC");
                    }
                } else {
                    if(ratios.get(i)<=WTR){
                        //System.out.println("i: " + i + ", choice: " + choices.get(i) + ", ratio: " + ratios.get(i)+ " = CONS");
                    } else {
                        numIncChoices++;
                        WTRError+=Math.abs(ratios.get(i)-WTR);
                        //System.out.println("i: " + i + ", choice: " + choices.get(i) + ", ratio: " + ratios.get(i)+ " = INC");
                    }
                }
            }
        }
        //System.out.println("numChoices: " + numChoices + ", numIncChoices: " + numIncChoices);
        Consistency=(double)(numChoices-numIncChoices)/(double)numChoices;
        //This code is checking to see if choices are homogenous (all 1s or 2s) and setting WTRLoc as External or Internal
        boolean allEqual = choices.stream().distinct().limit(2).count() <= 1;
        if(allEqual){
            WTRLoc="External";
        } else {
            WTRLoc="Internal";
        }
        numSwitches=0;
        for(int i=0; i<choices.size()-1; i++){
            if(Objects.equals(choices.get(i), choices.get(i+1))){    
            } else {
                numSwitches++;
            }
        }
        System.out.println("Computed WTR="+WTR+", Consistency="+Consistency+", WTRerror="+WTRError +", numSwitches="+numSwitches);
    }
}
