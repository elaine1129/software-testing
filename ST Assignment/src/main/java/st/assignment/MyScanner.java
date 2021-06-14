package st.assignment;

import st.assignment.lib.ScannerWrapper;

public class MyScanner {
    private ScannerWrapper in;

    public MyScanner (){
        this.in = new ScannerWrapper();
    }

    public MyScanner setScannerWrapper(ScannerWrapper sw){
        this.in = sw;
        return this;
    }

    public int nextItemID(){
        int input;
        input = nextInt();

        if(input <= 0 || input > 20)
            throw new IllegalArgumentException("Invalid Item ID. Enter again: ");

        return input;
    }


    // throw IAE if quantity if equal or less than zero
    public int nextQuantity(){
        int input;
        input = nextInt();

        if(input <= 0)
            throw new IllegalArgumentException("Quantity cannot be less than or equal to 0. Enter again: ");

        return input;
    }

    public int nextInt(){
        String input;
        input = in.nextLine();

        try {
            return Integer.parseInt(input);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Not a number. Enter again: ");
        }
    }

    public String nextLine() {
        return in.nextLine();
    }

    public String next() {
        String input = in.nextLine();
        String[] aWord = input.split(" ");
        return aWord[0];
    }

}
