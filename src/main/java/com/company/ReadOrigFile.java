package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
public class ReadOrigFile {
    public void loadFile(String filename){
        int numLines =0 ;
        int numLinesBreak =0 ;
        int fileNumber = 1;
        File file = new File(filename);
        System.out.println(filename + " Path:" + file.getAbsolutePath());
        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(fileReader);
            String line = null;
            String headerLine = null;
            StringBuffer breakLine = new StringBuffer();
            String splitFileName = null;
            File splitFile = null;
            FileWriter fileWritter = null;
            PrintWriter bufferWritter = null;
            headerLine = bufReader.readLine();
            while( (line = bufReader.readLine()) != null ) {
                numLines++;
                numLinesBreak++;
                breakLine.append(line+"\n");
                if(numLinesBreak==1000){
                    splitFileName = "splitFile_"+fileNumber+".txt";
                    splitFile = new File(splitFileName);
                    if(splitFile.exists()){
                        splitFile.delete();
                        splitFile.createNewFile();
                    }
                    fileWritter = new FileWriter(splitFile);
                    bufferWritter = new PrintWriter(fileWritter);
                    breakLine.deleteCharAt(breakLine.length()-1);//remove last newline
                    bufferWritter.println(headerLine);
                    bufferWritter.println(breakLine.toString());
                    bufferWritter.close();
                    fileWritter.close();
                    fileNumber++;
                    numLinesBreak=0;
                    breakLine.setLength(0);
                }
//System.out.println(numLines+":Line:"+line);
            }
//for last iteration
            splitFileName = "splitFile_"+fileNumber+".txt";
            splitFile = new File(splitFileName);
            if(splitFile.exists()){
                splitFile.delete();
                splitFile.createNewFile();
            }
            fileWritter = new FileWriter(splitFile);
            bufferWritter = new PrintWriter(fileWritter);
            breakLine.deleteCharAt(breakLine.length()-1);//remove last newline
            bufferWritter.println(headerLine);
            bufferWritter.println(breakLine.toString());
            bufferWritter.close();
            fileWritter.close();
            fileNumber++;
            numLinesBreak=0;
        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
// TODO Auto-generated method stub
        ReadOrigFile readOrigFile = new ReadOrigFile();
        readOrigFile.loadFile("d:\\user\\01388081\\桌面\\test.txt");

    }
}

