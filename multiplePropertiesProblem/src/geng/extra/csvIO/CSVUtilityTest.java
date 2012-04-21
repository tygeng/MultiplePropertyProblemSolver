package geng.extra.csvIO;

import java.util.Arrays;

import junit.framework.TestCase;

import java.io.File;

public class CSVUtilityTest extends TestCase {
    public void setUp() {
    }

    public void testCSVto2DArray() {
        String[][] array = CSVUtility.to2DArray(new File ("res/test.csv"));
        for(int i = 0; i<array.length;i++) {
            System.out.println(Arrays.toString(array[i]));
        }
        //System.out.println(Arrays.toString("d".split(" ")));
    }
}
