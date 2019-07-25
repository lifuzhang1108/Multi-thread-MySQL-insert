package com.company;

import java.io.*;
import java.sql.*;
import java.util.*;

class Helper {
    public static void insert(String keyword,int times,Connection conn) {
        try
        {
            String query = " insert into words (keyword, times)"
                    + " values (?, ?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, keyword);
            preparedStmt.setInt (2, times);
            preparedStmt.execute();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
    public static Map<String, Integer> processFile(String fileName) {

        File file = new File(fileName);
        BufferedReader bufferedReader = null;
        try {bufferedReader = new BufferedReader(new FileReader(file));} catch(Exception e) {}
        String inputLine = null;
        Map<String, Integer> azMap = new HashMap();
        try {
            while ((inputLine = bufferedReader.readLine()) != null) {
                String[] words = inputLine.split("[ \n\t\r\".,;:!?(){}]");

                for (int counter = 0; counter < words.length; counter++) {
                    String key = words[counter].toLowerCase(); // remove .toLowerCase for Case Sensitive result.
                    if (key.length() > 0) {
                        if (azMap.get(key) == null) {
                            azMap.put(key, 1);
                        } else {
                            int value = azMap.get(key).intValue();
                            value++;
                            azMap.put(key, value);
                        }
                    }
                }
            }
        }
        catch (IOException error) {
            System.out.println("Invalid File");
        } finally {
            try{bufferedReader.close();}catch(Exception e) {}
        }
        return azMap;
    /*
        String article="";
        try ( FileInputStream input = new FileInputStream(fileName)){
            int data=input.read();
            while (data!=-1) {
                article = article + "" + (char) data;
                data=input.read();
            }

        }
        catch (Exception e) {}
        Map<String, Integer> azMap = new HashMap<>();
        String[] words = article.split("[ \n\t\r\".,;:!?(){}]");

        for (int counter = 0; counter < words.length; counter++) {
            String key = words[counter].toLowerCase(); // remove .toLowerCase for Case Sensitive result.
            if (key.length() > 0) {
                if (azMap.get(key) == null) {
                    azMap.put(key, 1);
                } else {
                    int value = azMap.get(key).intValue();
                    value++;
                    azMap.put(key, value);
                }
            }
        }
        return azMap;
*/
    }
}
class T1 extends Thread {
    public void run()  {
    }
    public static Map<String, Integer> show(){
       return Helper.processFile("splitFile_1.txt");
    }
}
class T2 extends Thread {
    public void run()  {
    }
    public static Map<String, Integer> show(){
        return Helper.processFile("splitFile_2.txt");
    }
}
class T3 extends Thread {
    public void run()  {
    }
    public static Map<String, Integer> show(){
        return Helper.processFile("splitFile_3.txt");
    }
}



public class Main {

    public static void main(String[] args) throws IOException {
        try {
            // create a mysql database connection
            String myDriver = "com.mysql.cj.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost/menagerie";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "Alex41134312");


            long start = System.nanoTime();
            //ReadOrigFile readOrigFile = new ReadOrigFile();
            //readOrigFile.loadFile("d:\\user\\01388081\\桌面\\test.txt");
            T1 obj1 = new T1();
            T2 obj2 = new T2();
            T3 obj3 = new T3();
            obj1.start();
            obj2.start();
            obj3.start();
            Map<String, Integer> m1 = obj1.show();
            Map<String, Integer> m2 = obj2.show();
            Map<String, Integer> m3 = obj3.show();

            for (String key : m2.keySet()) {
                if (m1.containsKey(key)) {
                    m1.put(key, m2.get(key) + m1.get(key));
                } else {
                    m1.put(key, m2.get(key));
                }
            }
            for (String key : m3.keySet()) {
                if (m1.containsKey(key)) {
                    m1.put(key, m3.get(key) + m1.get(key));
                } else {
                    m1.put(key, m3.get(key));
                }
            }

            Set<Map.Entry<String, Integer>> entrySet = m1.entrySet();

            /*System.out.println("Words" + "\t\t" + "# of Occurances");
            for (Map.Entry<String, Integer> entry : entrySet) {
                System.out.println(entry.getKey() + "\t\t" + entry.getValue());
            }*/
            List<String> myTopOccurrence = azFindMaxOccurance(m1, 16, conn);
            /*
            System.out.println("\nMaixmum Occurance of Word in file: ");
            for (String result : myTopOccurrence) {
                System.out.println("==> " + result);
            }*/

            FileWriter fw = new FileWriter("d:\\user\\01388081\\桌面\\out.txt");
            fw.write("\nJSON:{");
            for (String result : myTopOccurrence) {
                fw.write("\r\n " + result);
            }
            fw.write("\r\n}");
            fw.close();
            conn.close();

            long end = System.nanoTime();
            long duration = (end - start) / 1000000;
            System.out.println(duration + "ms");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }


    public static List<String> azFindMaxOccurance(Map<String, Integer> map, int n, Connection conn) {
        List<azComparable> l = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet())
            l.add(new azComparable(entry.getKey(), entry.getValue()));

        Collections.sort(l);
        List<String> list = new ArrayList<>();
        //for (azComparable w : l.subList(0, n))
        for (azComparable w : l) {
            list.add("\"" + w.wordFromFile + "\":" + w.numberOfOccurrence);
            Helper.insert(w.wordFromFile, w.numberOfOccurrence,conn);
        }
        return list;
    }
}

class azComparable implements Comparable<azComparable> {
    public String wordFromFile;
    public int numberOfOccurrence;

    public azComparable(String wordFromFile, int numberOfOccurrence) {
        super();
        this.wordFromFile = wordFromFile;
        this.numberOfOccurrence = numberOfOccurrence;
    }

    @Override
    public int compareTo(azComparable arg0) {
        int azCompare = Integer.compare(arg0.numberOfOccurrence, this.numberOfOccurrence);
        return azCompare != 0 ? azCompare : wordFromFile.compareTo(arg0.wordFromFile);
    }

    @Override
    public int hashCode() {
        final int uniqueNumber = 19;
        int azResult = 9;
        azResult = uniqueNumber * azResult + numberOfOccurrence;
        azResult = uniqueNumber * azResult + ((wordFromFile == null) ? 0 : wordFromFile.hashCode());
        return azResult;
    }

    @Override
    public boolean equals(Object azObj) {
        if (this == azObj)
            return true;
        if (azObj == null)
            return false;
        if (getClass() != azObj.getClass())
            return false;
        azComparable other = (azComparable) azObj;
        if (numberOfOccurrence != other.numberOfOccurrence)
            return false;
        if (wordFromFile == null) {
            if (other.wordFromFile != null)
                return false;
        } else if (!wordFromFile.equals(other.wordFromFile))
            return false;
        return true;
    }
}