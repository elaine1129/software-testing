package st.assignment.lib;

import java.util.*;
import java.util.stream.Stream;

public class TablePrinter {
    public void printTable(List<? extends Printable> data, String[] header){

       //data {["1", "2"]}
        //Output
        //+----+--------+--------+-----------+----------+----------+----------+------------+----------+------+
        //|ID  |Name    |Password|PhoneNumber|UnitNumber|StreetName|PostalCode|Area        |District  |State |
        //+----+--------+--------+-----------+----------+----------+----------+------------+----------+------+
        //|A123|Alex Tan|pass123 |12342961   |No 1      |Jalan 4/5 |75000     |Kuala Linggi|Alor Gajah|Melaka|
        //+----+--------+--------+-----------+----------+----------+----------+------------+----------+------+

        //add header
        List<String[]> table = new ArrayList<>();
        table.add(header);

        //add data
        for (Printable cell : data) {
            String[] row = cell.toTable().split(",");
            table.add(row);
        }

        //convert to multidimension array
        String[][] tableArr = (String[][]) table.toArray(String[][]::new);


        //get max column width {0=1, 1=3}
        Map<Integer, Integer> columnLengths = new HashMap<>();
        Arrays.stream(tableArr)
                .forEach( a -> {
                    Stream.iterate(0, (i -> i < a.length), (i -> ++i)).forEach(i -> {
                        columnLengths.putIfAbsent(i, 0);
                        if (columnLengths.get(i) < a[i].length()) {
                            columnLengths.put(i, a[i].length());
                        }
                    });
                });

        // | %-2s | %-10s |
        final StringBuilder formatString = new StringBuilder("");
        columnLengths.forEach((key, value) -> formatString.append("| %-" + value + "s "));
        formatString.append("|\n");

        // add header and last row line : +--+----+--+
        String line = columnLengths.entrySet().stream().reduce("", (ln,b) -> {
            String tempLine = "+-";
            tempLine = tempLine + Stream.iterate(0, (i-> i < b.getValue()), (i -> ++i))
                    .reduce("", (ln1, b1) -> ln1 + "-", (a1, b1)-> a1 + b1);
                            tempLine = tempLine + "-";
                            return ln + tempLine;
                            }, (a, b) -> a+b);
        line = line + "+\n";

        //print table
        System.out.print(line);
        Arrays.stream(tableArr).limit(1).forEach(a -> System.out.printf(formatString.toString(),a));
        System.out.print(line);

        Stream.iterate(1, (i -> i < tableArr.length), (i -> ++i))
                .forEach(a-> System.out.printf(formatString.toString(),tableArr[a]));

        System.out.print(line);
    }

//    print single data
    public void printTable(Printable data, String[] header){
        printTable(Collections.singletonList(data), header);
    }




}
