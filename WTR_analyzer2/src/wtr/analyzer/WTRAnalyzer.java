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
import java.util.Collections;
import javax.swing.*;

/**
 *
 * @author Andy
 */
public class WTRAnalyzer {
    
    JFrame theFrame;
    JTextField directoryField, outputField, directoryerror, outputinfo;
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
       directoryField.setText("Set input files...");
       directoryField.setMaximumSize(labelDimension);
       outputField = new JTextField(20);
       outputField.setFont(theFont);
       outputField.setMaximumSize(labelDimension);
       directoryerror = new JTextField(20);
       directoryerror.setFont(theFont);
       directoryerror.setText("Errors are shown here");
       directoryerror.setMaximumSize(labelDimension);
       outputinfo = new JTextField(20);
       outputinfo.setFont(theFont);
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
                        System.out.println(thisLine[a]);
                        if (!thisLine[a].isEmpty()) {
                            numScales++;
                            System.out.println("Not empty!");
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
                        try{
                            for(int k=1; k<numChoices+1; k++){
                                System.out.println("Choice " + k);
                                String thisC, thisp1p1, thisp1p2, thisp2p1, thisp2p2;
                                thisC=header.get(4).get(k);
                                thisp1p1=header.get(5).get(k);
                                thisp1p2=header.get(6).get(k);
                                thisp2p1=header.get(7).get(k);
                                thisp2p2=header.get(8).get(k);
                                question thisQ = new question(Integer.parseInt(thisC), Double.parseDouble(thisp1p1), Double.parseDouble(thisp1p2), Double.parseDouble(thisp2p1), Double.parseDouble(thisp2p2));
                                qs.add(thisQ);
                            }
                            
                            //assemble scale and add to list of scales in datafile
                            System.out.println("Ready to make a scale");
                            scale thisScale = new scale(p1, p2, label, numChoices, qs);
                            System.out.println("Scale label: "+thisScale.label + ", P1: " + thisScale.P1+ ", P2: " + thisScale.P2+ ", Choices: " + thisScale.questions.size());
                            if(thisScale.checkScale()){
                                theScales.add(thisScale);
                            }else{
                                System.out.println("This scale was rejected");
                            }
                        }
                        catch(Exception e)
                        {
                            qs = null;
                            System.out.println(e);
                        }
                        
                        
                        
                        
                        System.out.println(theScales.size() + " correctly formatted scale(s) processed so far");
                        
                        //clean out that scale
                        for(int k=0; k<numChoices+1; k++){
                            System.out.println("Removing column " + k + " from header");
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
                    System.out.println("Removing header from data");
                    //delete header from data
                    lines.remove(10);
                    lines.remove(9);
                    lines.remove(8);
                    lines.remove(7);
                    lines.remove(6);
                    lines.remove(5);
                    lines.remove(4);
                    lines.remove(3);
                    lines.remove(2);
                    lines.remove(1);
                    lines.remove(0);
                    
                    
                    //process the data
                    System.out.println(lines.size()+" subjects' worth of data left in file");
                    for(String row:lines){
                        Subject thisSub = new Subject();
                        String[] thisRow = row.split(",");
                        String subnum = thisRow[0];
                        ArrayList<String> subData = new ArrayList();
                        subData.addAll(Arrays.asList(thisRow));
                        thisSub.SubNum=subData.get(0);
                        subData.remove(0);
                        System.out.print(subnum + "'s data string: ");
                        for (String s:subData){
                            System.out.print(s+", ");
                        } 
                        System.out.println("");
                        for(int k=0; k<theScales.size(); k++){
                            //System.out.println("This happened");
                            //scale tempScale = theScales.get(k);
                            
                            scale tempScale = new scale(); 
                            tempScale.P1 = theScales.get(k).P1;
                            tempScale.P2 = theScales.get(k).P2;
                            tempScale.label = theScales.get(k).label;
                            tempScale.numChoices = theScales.get(k).numChoices;
                            tempScale.questions.addAll(theScales.get(k).questions);
                            tempScale.ratios.addAll(theScales.get(k).ratios);
                            tempScale.switchpoints.addAll(theScales.get(k).switchpoints);
                            
                            //tempScale.choices.clear();
                            System.out.println(tempScale.WTR+ " should be null");
                            System.out.println("The scale starts out with "+tempScale.choices.size()+" choices (should be 0)");
                            for(int l=0; l<tempScale.numChoices; l++){
                                //System.out.println("Reading value of: " + subData.get(l));
                                tempScale.choices.add(Integer.parseInt(subData.get(l)));
                            }
                            System.out.println("Successfully added " +tempScale.choices.size() + " decisions");
                            for(int l=0; l<tempScale.numChoices; l++){
                                System.out.print("Scale "+ k+", removing value of: " + subData.get(0)+ " from subject data string.....     ");
                                subData.remove(0);
                                System.out.println(" success");
                            }
                            if(subData.isEmpty()){
                            } else {
                                if(subData.get(0).equals("")){
                                    subData.remove(0);
                                    System.out.println("Removed empty cell from data string");
                                }
                            }
                            
                            tempScale.computeWTRv2();
                            thisSub.scales.add(tempScale);
                            System.out.println("This subject now has "+thisSub.scales.size()+" scales processed");
                        }
                        subList.add(thisSub);
                        for(Subject s:subList){
                            System.out.print(s.SubNum);
                            for(scale t:s.scales){
                                System.out.print(" WTR="+t.WTR+" ");
                            }
                            System.out.println();
                        }
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
                    writer.write(",Scale Label,Player 1:Player 2,Scale Length,");
                    for (int j=0; j<thisScale.ratios.size(); j++){
                        writer.write("Ratio " + j+",");
                    }
                    for (int j=0; j<thisScale.switchpoints.size(); j++){
                        writer.write("SwitchPoint " + j+",");
                    }
                    writer.write("WTR,# Avgd SPs,Avgd SPs,# Inconsistent,Consistency,WTRerror");
                }
                writer.write("\n");
                // output each person's data
                System.out.println(subList.size()+" subjects to write!");
                for(int i=0; i<subList.size(); i++){
                    String subdata = "";
                    Subject sub = subList.get(i);
                    subdata = sub.SubNum;
                    System.out.println(sub.SubNum);
                    for(int j=0; j<sub.scales.size(); j++){
                        scale thisScale=sub.scales.get(j);
                        subdata+=","+thisScale.label+",";
                        subdata+=thisScale.P1+":"+sub.scales.get(j).P2+",";
                        subdata+=thisScale.ratios.size()+",";
                        for (int k=0; k<thisScale.ratios.size(); k++){
                            subdata+=thisScale.ratios.get(k)+",";
                        }
                        for (int k=0; k<thisScale.switchpoints.size(); k++){
                            subdata+=thisScale.switchpoints.get(k)+",";
                        }
                        subdata+=thisScale.WTR+",";
                        subdata+=thisScale.MaxConsSPs.size()+",";
                        String sps="";
                        for (int k=0; k<thisScale.MaxConsSPs.size(); k++){
                            sps+=thisScale.MaxConsSPs.get(k)+";";
                        }
                        subdata+=sps.substring(0, sps.length()-2)+",";
                        subdata+=thisScale.numIncChoices+",";
                        subdata+=thisScale.Consistency+",";
                        subdata+=thisScale.WTRError;
                        
                    }
                    System.out.println(subdata);
                    writer.write(subdata);
                    writer.write("\n");
                }
                
            } catch (IOException ex) {
            ex.printStackTrace();
            //ex.printStackTrace();
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