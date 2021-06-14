package st.assignment;

import st.assignment.lib.Writalbe;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CSVUtil {

    /**
     * Read data from a csv with delimiter ","
     * @param path
     * @return a multidimension String array
     */
    public String[][] readCSV(String path){
        List<String> dataRead = new ArrayList<>();

        File fileToRead = new File(path);
        Scanner fin = null;

        try {
            fin = new Scanner(fileToRead);
        } catch(FileNotFoundException e) {
            throw new IllegalArgumentException("File " + path + " does not exist. ");
        }

        String line = null;
        while (fin.hasNextLine())
        {
            line = fin.nextLine();
            dataRead.add(line);
        }
        fin.close();

        if(dataRead.isEmpty())
            throw new IllegalArgumentException("Empty data in files");


        return dataRead.stream()
                .map(e-> Arrays.stream(e.split(",")).toArray(String[]::new))
                .toArray(String[][]::new);
    }

    public void writeWritableToCSV(List<? extends Writalbe> dataList, String path){
        writeCSV(dataList.stream().map(Writalbe::toFile).collect(Collectors.toList()), path);
    }

    public void writeCSV(List<String> dataList, String path){
        File destination = new File(path);
        PrintWriter pw = null;
        Scanner fin = null;

        try {
            //read header if header is header exist
            fin = new Scanner(destination);
            String header = null;
            if(fin.hasNextLine()){
                header = fin.nextLine();
            }
            fin.close();

            pw = new PrintWriter(destination);
            if (header != null) {
                pw.println(header);
            }
            for (String singleLine : dataList) {
                pw.println(singleLine);
            }
            pw.close();
        } catch(FileNotFoundException e) {
            //create a new file
            createNewFile(path);
            //write first time
            writeCSV(dataList, path);
            //write second time to create a header for the file (rewrite first row as header)
            writeCSV(dataList, path);
        }
    }

    private void createNewFile(String path){
        File destination = new File(path);
        try {
            PrintWriter pw = new PrintWriter(destination);
            pw.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Problem creating new file " + path);
        }
    }

    public void appendCSV(List<String> data, String path){
        File destination = new File(path);
        try {

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(destination, true)));

            for (String row: data) {
                pw.println(row);
            }
            pw.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("File " + path + " does not exist. ");
        }
    }

}
