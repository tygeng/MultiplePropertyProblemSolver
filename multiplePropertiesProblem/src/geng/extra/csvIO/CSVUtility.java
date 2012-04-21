package geng.extra.csvIO;

import java.util.ArrayList;

import java.nio.charset.Charset;

import java.io.FileNotFoundException;

import java.io.File;

import java.util.Scanner;

public class CSVUtility {
    public static String[][] to2DArray(File source, String charsetName) {
        Scanner scanner;
        try {
            scanner = new Scanner(source, charsetName);
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

       ArrayList<String[]> list = new ArrayList<String[]>();
        while (scanner.hasNextLine()) {
            list.add(LineToCells(scanner.nextLine()));
        }
        scanner.close();
        String [][] result = new String[list.size()][];
        return list.toArray(result);

    }

    private static String[] LineToCells(String line) {
        // TODO Auto-generated method stub
        ArrayList<String> cells = new ArrayList<String>();
        int start = 0;
        int end = 0;
        while((end=line.indexOf(",",start))!=-1) {
            cells.add(line.substring(start,end));
            start=end+1;

        }
        cells.add(line.substring(start));
        String[] result = new String[cells.size()];
        cells.toArray(result);
        return result;
    }

    public static String[][] to2DArray(File source) {
        return to2DArray(source, Charset.defaultCharset().name());
    }

}
