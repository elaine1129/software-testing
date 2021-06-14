package st.assignment.lib;

import java.util.Scanner;

public class ScannerWrapper {
    private Scanner in;

    public ScannerWrapper (){
        this.in = new Scanner(System.in);
    }

    public String nextLine() {
        return in.nextLine();
    }

}


