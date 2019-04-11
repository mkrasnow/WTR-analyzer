/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wtr.analyzer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/**
 *
 * @author Andy
 */
public class WTRAnalyzer {
    
    JFrame theFrame;
    JTextField directoryField, outputField;
    JTextArea directoryerror, outputinfo;
    JPanel wholePanel, topPanel, midPanel, bottomPanel, emptyPanel1,emptyPanel2;
    Rectangle wr = new Rectangle(800,400);
    Dimension topPan = new Dimension(800,200);
    Dimension midPan = new Dimension(800,200);
    Dimension bottomPan = new Dimension(800,200);
    Dimension wholeApp = new Dimension(800,400);
    Dimension labelDimension = new Dimension(800,40);
    Dimension spacer = new Dimension(800,50);
    String direc, fil;
    
    Font theFont = new Font("Arial", 0, 16);
    
    
    public static void main(String[] args) {
        
        WTRAnalyzer analyzer = new WTRAnalyzer();
        analyzer.buildGUI();
        
    }
    
    public void buildGUI(){
       
       theFrame = new JFrame("WTR Analyzer");
       theFrame.setResizable(false);
       theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
       JLabel directoryLabel = new JLabel("Choose directory or file to analyze");
       directoryLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
       
       directoryLabel.setFont(theFont);
       JLabel outputLabel = new JLabel("Choose directory to save to. Give file a name if deired.");
       outputLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
       outputLabel.setFont(theFont);
       JLabel goLabel = new JLabel("Click to start analysis");
       goLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
       goLabel.setFont(theFont);
   
       JButton startAnalysis = new JButton("START");
       startAnalysis.setFont(theFont);
       JButton getDirectory = new JButton("Choose");
       getDirectory.setFont(theFont);
       JButton getOutput = new JButton("Choose");
       getOutput.setFont(theFont);
       
       directoryField = new JTextField(20);
       directoryField.setFont(theFont);
       directoryField.setText("");
       directoryField.setMaximumSize(labelDimension);
       outputField = new JTextField(20);
       outputField.setFont(theFont);
       outputField.setText("");
       outputField.setMaximumSize(labelDimension);
       directoryerror = new JTextArea(2,20);
       directoryerror.setFont(theFont);
       directoryerror.setText("Errors are shown here");
       directoryerror.setMaximumSize(labelDimension);
       outputinfo = new JTextArea(2,15);
       outputinfo.setFont(theFont);
       outputinfo.setWrapStyleWord(true);
       outputinfo.setLineWrap(true);
       outputinfo.setText("Completion information displayed here");
       outputinfo.setMaximumSize(labelDimension);
       
       startAnalysis.addActionListener(new StartAnalysisButtonListener());
       getDirectory.addActionListener(new GetDirectoryButtonListener());
       getOutput.addActionListener(new GetOutputButtonListener());
       
       topPanel = new JPanel();
       topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.Y_AXIS));
       topPanel.add(directoryLabel);
       topPanel.add(directoryField);
       topPanel.add(directoryerror);
       topPanel.add(getDirectory);
       
       midPanel = new JPanel();
       midPanel.setLayout(new BoxLayout(midPanel,BoxLayout.Y_AXIS));
       midPanel.add(outputLabel);
       midPanel.add(outputField);
       midPanel.add(outputinfo);
       midPanel.add(getOutput);
       
       bottomPanel = new JPanel();
       bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.Y_AXIS));
       bottomPanel.add(startAnalysis);
       
       emptyPanel1 = new JPanel();
       emptyPanel1.setMinimumSize(spacer);
       
       emptyPanel2 = new JPanel();
       emptyPanel2.setMinimumSize(spacer);
       
       wholePanel = new JPanel();
       wholePanel.setLayout(new BoxLayout(wholePanel,BoxLayout.Y_AXIS));
       wholePanel.add(topPanel);
       wholePanel.add(emptyPanel1);
       wholePanel.add(midPanel);
       wholePanel.add(emptyPanel2);
       wholePanel.add(bottomPanel);
       
       theFrame.setLayout(new BorderLayout());

       theFrame.getContentPane().add(wholePanel);
       
       theFrame.setBounds(wr);
       theFrame.setPreferredSize(wholeApp);
       theFrame.setVisible(true);
       theFrame.validate();
    }
    
    class GetDirectoryButtonListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event){
            JFileChooser directory = new JFileChooser();
            directory.setFileSelectionMode(2);
            directory.setDialogTitle("Select the directory or single file to analyze");
            directory.showSaveDialog(theFrame);
            directoryField.setText(directory.getSelectedFile().getPath());
            theFrame.repaint();
        }
    }
    
    class GetOutputButtonListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event){
            outputField.setText("");
            JFileChooser directory = new JFileChooser();
            directory.setFileSelectionMode(2);
            directory.setDialogTitle("Select the directory to save to and enter a name.");
            directory.showSaveDialog(theFrame);
            outputField.setText(directory.getSelectedFile().getPath());
            theFrame.repaint();
        }
    }
    
    class StartAnalysisButtonListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event){
            
            direc = directoryField.getText();
            fil = outputField.getText();
            Go analysis = new Go();
            analysis.getGoing();
        
        }
    }

public class Go {
    
    File output;
    
    //constructor
    public Go(){
    }
    private boolean parseableInt(String s){
        try{
            Integer.parseInt(s);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
    private boolean parseableDub(String s){
        try{
            Double.parseDouble(s);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
        
    //methods
    public void getGoing(){
        System.out.println("Started!");
        //Name directory where data is stored
        File dir = new File(direc);
        //Name file data will be written to
        File tempfil = new File(fil);
        
        if (fil.equals("")){
            output = new File("yourData.csv");
            outputinfo.setText("File saved in same directory as applet and named \"yourdata\"");
        } else if (tempfil.isDirectory()){
            output = new File(fil + "\\yourData.csv");
            outputinfo.setText("File saved to chosen directory and named \"yourdata\"");
        } else if (fil.endsWith(".csv")){
            output = new File(fil);
            outputinfo.setText("File saved to "+fil);
        } else {
            output = new File(fil+ ".csv");
            outputinfo.setText("File saved to chosen directory");
        }
        
        //Create the readers and writers for all this
        //As user, only "reader" and "writer" need ever be referred to
        FileReader dataReader = null;
        BufferedReader reader = null;
        FileWriter dataWriter = null;
        BufferedWriter writer = null;
        
        
        //determine whetehr is a directory or single file
        FilenameFilter csvFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
		String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".csv")) {
                    return true;
		} else {
                    return false;
		}
		}
        };
               
        ArrayList<String> dirContents = new ArrayList();
        if(dir.isDirectory()){
            //create a list of strings of all the data files to analyze
            for(int x = 0; x<dir.list(csvFilter).length;x++){
                dirContents.add(direc+"\\"+dir.list(csvFilter)[x]);
            }
        } else{
            dirContents.add(direc);
        }
        
            //Create array to store subjects
            ArrayList<Subject> subList = new ArrayList();
            
            //For each separate data file, analyze
            for(int i = 0; i<dirContents.size(); i++){
                try {
                    
                    //create name of file to be analyzed
                    //file is relativized to application's location
                    String toRead = dirContents.get(i);
                    dataReader = new FileReader(toRead);
                    reader = new BufferedReader(dataReader);
                    System.out.println("Opened file!");
                    //create temporary variable to read in lines of data
                    String line = null;
                    ArrayList<String> lines = new ArrayList();
                    
                    //read in lines of data
                    while ((line=reader.readLine()) != null){
                        lines.add(line);
                    }
                    System.out.println(lines.size() + " total lines read from file");
                    
                    //process the header
                    //count the number of scales
                    int numScales = 0;
                    String[] thisLine=lines.get(0).split(",");
                    for(int a=0; a<thisLine.length; a++) {
                        //System.out.println(thisLine[a]);
                        if (!thisLine[a].isEmpty()) {
                            numScales++;
                            //System.out.println("Not empty!");
                        }
                    }
                    numScales/=2;
                    System.out.println(numScales+ " scales in this data file");
                    ArrayList<ArrayList<String>> header = new ArrayList();
                    for(int j=0; j<9; j++){
                        ArrayList<String> lineToAdd = new ArrayList();
                        thisLine=lines.get(j).split(",");
                        lineToAdd.addAll(Arrays.asList(thisLine));
                        header.add(lineToAdd);
                    }
                    System.out.println("Successfully read header with "+header.size() + " lines");
                    ArrayList<scale> theScales  = new ArrayList();
                    
                    ArrayList<ArrayList<String>> body = new ArrayList();
                    for(int j=11; j<lines.size(); j++){
                        ArrayList<String> lineToAdd = new ArrayList();
                        thisLine=lines.get(j).split(",");
                        lineToAdd.addAll(Arrays.asList(thisLine));
                        body.add(lineToAdd);
                    }

                    for(int j=0; j<numScales; j++){
                        System.out.println("Setting up scale " + j);
                        //get p1,p2,label, numchoices
                        String p1, p2, label; 
                        int numChoices;
                        p1=header.get(0).get(1);
                        p2=header.get(1).get(1);
                        label=header.get(2).get(1);
                        
                        //read each choice into a question & make list questions
                        numChoices=Integer.parseInt(header.get(3).get(1));
                        ArrayList<question> qs = new ArrayList();
                        System.out.println("Scale has " + numChoices+ " choices");
                        
                        for(int k=1; k<numChoices+1; k++){
                            System.out.println("Choice " + k);
                            String thisC, thisp1p1, thisp1p2, thisp2p1, thisp2p2;
                            thisC=header.get(4).get(k);
                            thisp1p1=header.get(5).get(k);
                            thisp1p2=header.get(6).get(k);
                            thisp2p1=header.get(7).get(k);
                            thisp2p2=header.get(8).get(k);
                            if(parseableDub(thisp1p1)&&parseableDub(thisp1p2)&&parseableDub(thisp2p1)&&parseableDub(thisp2p2)&&parseableInt(thisC)){//this checks if the numbers are parseable as ints and doubles
                                question thisQ = new question(Integer.parseInt(thisC), Double.parseDouble(thisp1p1), Double.parseDouble(thisp1p2), Double.parseDouble(thisp2p1), Double.parseDouble(thisp2p2));
                                qs.add(thisQ);
                            } else {
                                question thisQ = new question();
                                qs.add(thisQ);
                            }
  
                        }

                        //assemble scale and add to list of scales in datafile
                        System.out.println("Ready to make a scale");
                        scale thisScale = new scale(p1, p2, label, numChoices, qs);
                        System.out.println("Scale label: "+thisScale.label + ", P1: " + thisScale.P1+ ", P2: " + thisScale.P2+ ", Choices: " + thisScale.questions.size());

                        theScales.add(thisScale);

                        System.out.println(theScales.size() + " scale(s) processed so far");
                        
                        //clean out that scale
                        for(int k=0; k<numChoices+1; k++){
                            //System.out.println("Removing column " + k + " from header");
                            if(header.get(0).isEmpty()){} else {header.get(0).remove(0);}
                            if(header.get(1).isEmpty()){} else {header.get(1).remove(0);}
                            if(header.get(2).isEmpty()){} else {header.get(2).remove(0);}
                            if(header.get(3).isEmpty()){} else {header.get(3).remove(0);}
                            if(header.get(4).isEmpty()){} else {header.get(4).remove(0);}
                            if(header.get(5).isEmpty()){} else {header.get(5).remove(0);}
                            if(header.get(6).isEmpty()){} else {header.get(6).remove(0);}
                            if(header.get(7).isEmpty()){} else {header.get(7).remove(0);}
                            if(header.get(8).isEmpty()){} else {header.get(8).remove(0);}
                        }
                    }
                    
                    //process the data
                    System.out.println(body.size()+" subjects' worth of data left in file");
                    for(ArrayList<String> row:body){
                        Subject thisSub = new Subject();
                        String subnum = row.get(0);
                        thisSub.SubNum=row.get(0);
                        row.remove(0);
                        System.out.print(subnum + "'s data string: ");
                        for (String s:row){
                            System.out.print(s+", ");
                        } 
                        System.out.println("");
                        for(int k=0; k<theScales.size(); k++){
                            //System.out.println("This happened");
                            //scale tempScale = theScales.get(k);
                            
                            scale tempScale = new scale(theScales.get(k).P1,theScales.get(k).P2,theScales.get(k).label,theScales.get(k).numChoices,theScales.get(k).questions); 
                            tempScale.scaleValid=theScales.get(k).scaleValid;


                            System.out.println(tempScale.WTR+ " should be 0");
                            System.out.println("The scale starts out with "+tempScale.choices.size()+" choices (should be 0)");
                            for(int l=0; l<tempScale.numChoices; l++){
                                if(row.get(l).equals("1")||row.get(l).equals("2")){
                                    //System.out.println("Reading value of: " + subData.get(l));
                                    tempScale.choices.add(Integer.parseInt(row.get(l)));
                                } else {
                                    tempScale.choicesComplete=false;
                                }
                                
                            }
                            //System.out.println("Attempted to add " + tempScale.numChoices + " decisions");
                            for(int l=0; l<tempScale.numChoices; l++){
                                //System.out.print("Scale "+ k+", removing value of: " + row.get(0)+ " from subject data string.....     ");
                                row.remove(0);
                                //System.out.println(" success");
                            }
                            if(row.isEmpty()){
                            } else {
                                if(row.get(0).equals("")){
                                    row.remove(0);
                                    //System.out.println("Removed empty cell from data string");
                                }
                            }
                            if(tempScale.scaleValid&&tempScale.choicesComplete){
                                tempScale.computeWTRv2();
                            }  else{
                                tempScale.WTR=99999;
                                tempScale.Consistency=99999;
                                tempScale.PerfectCon=99999;
                                tempScale.WTRError=99999;
                                tempScale.numIncChoices=99999;
                                tempScale.maxConsistency=99999;
                                tempScale.SPrange=99999;
                                tempScale.SPrankRange=99999;
                                tempScale.numSwitches=99999;
                            }
                            thisSub.scales.add(tempScale);
                            System.out.println("This subject now has "+thisSub.scales.size()+" scales processed");
                        }
                        subList.add(thisSub);
                        /*for(Subject s:subList){
                            System.out.print(s.SubNum);
                            for(scale t:s.scales){
                                System.out.print(" WTR="+t.WTR+" ");
                            }
                            System.out.println();
                        }*/
                    }
                                      
                    
                            
                    //close the reader
                    reader.close();
                    System.out.println("Closed file!");
                    } catch (Exception ex) {
                    //ex.printStackTrace();
                    directoryerror.setText("Problem reading the files. Perhaps no files were selected");
                    theFrame.validate();
                }
            }
            //Write the data to file
            
            try {
                dataWriter = new FileWriter(output);
                writer = new BufferedWriter(dataWriter);
                
                //create a header row
                writer.write("Subject");
                for(int i=0; i<subList.get(0).scales.size(); i++){
                    scale thisScale = subList.get(0).scales.get(i);
                    if(thisScale.scaleValid){
                        writer.write(",Scale_Label,Player_1-Player_2,Scale_Length,");
                        for (int j=0; j<thisScale.ratios.size(); j++){
                            writer.write("Ratio_" + j+",");
                        }
                        for (int j=0; j<thisScale.switchpoints.size(); j++){
                            writer.write("SwitchPoint_" + j+",");
                        }
                        for (int j=0; j<thisScale.numChoices; j++){
                            writer.write("Choice_" + j+",");
                        }
                        writer.write("Num_Avgd_SPs,Max_SP_Consistency,Avgd_SPs,Avgd_SP_range,Avgd_SP_rankRange,Num_Inconsistent,numSwitches_"+thisScale.label+",Consistency_"+thisScale.label+",WTRerror_"+thisScale.label+",WTR_"+thisScale.label+",WTRLoc");
                    } else {
                        writer.write(",Scale Invalid - Check Datafile");
                    }
                }
                writer.write("\n");
                // output each person's data
                System.out.println(subList.size()+" subjects to write!");
                for(int i=0; i<subList.size(); i++){
                    String subdata = "";
                    Subject sub = subList.get(i);
                    subdata = sub.SubNum;
                    System.out.println(sub.SubNum + " has " + sub.scales.size() + " scales");
                    for(int j=0; j<sub.scales.size(); j++){
                        scale thisScale=sub.scales.get(j);
                        if(!thisScale.scaleValid){
                            subdata+=",Scale Invalid - Check Datafile";
                        } else {
                            subdata+=","+thisScale.label+",";
                            subdata+=thisScale.P1+":"+sub.scales.get(j).P2+",";
                            subdata+=thisScale.ratios.size()+",";
                            for (int k=0; k<thisScale.ratios.size(); k++){
                                subdata+=thisScale.ratios.get(k)+",";
                            }
                            for (int k=0; k<thisScale.switchpoints.size(); k++){
                                subdata+=thisScale.switchpoints.get(k)+",";
                            }
                            
                            if(!thisScale.choicesComplete){
                                for (int k=0;k<thisScale.numChoices+10;k++){
                                    subdata+="Error in Choices,";
                                }
                                subdata+="Error in Choices";
                            } else {
                                for (int k=0; k<thisScale.choices.size(); k++){
                                    subdata+=thisScale.choices.get(k)+",";
                                }
                                subdata+=thisScale.numSwitches+",";
                                subdata+=thisScale.MaxConsSPs.size()+",";
                                subdata+=thisScale.maxConsistency+",";
                                String sps="";
                                for (int k=0; k<thisScale.MaxConsSPs.size(); k++){
                                    sps+=thisScale.MaxConsSPs.get(k)+";";
                                }
                                subdata+=sps.substring(0, sps.length()-2)+",";
                                subdata+=thisScale.SPrange+",";
                                subdata+=thisScale.SPrankRange+",";
                                subdata+=thisScale.numIncChoices+",";
                                subdata+=thisScale.Consistency+",";
                                subdata+=thisScale.WTRError+",";
                                subdata+=thisScale.WTR+",";
                                subdata+=thisScale.WTRLoc;
                            }
                        }
                    }
                    System.out.println(subdata);
                    writer.write(subdata);
                    writer.write("\n");
                }
                
            } catch (IOException ex) {
                    ex.printStackTrace();
                    outputinfo.setText("problem saving the data");
                    theFrame.validate();
            }
            
            //Close the writer
            try {
                writer.close();
                theFrame.validate();
            } catch (Exception ex) {
                //ex.printStackTrace();
                outputinfo.setText("problem saving the data");
                theFrame.validate();
            }
        //} //else {
            //System.out.println("Not A Directory, Sorry!");
        //}
                    
        }
    }
}
