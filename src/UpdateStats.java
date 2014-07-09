import au.com.bytecode.opencsv.CSVParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;


public class UpdateStats {
    public static void main(String[] args) {
        final GameLog log = new GameLog();
        final LogFileReader reader = new LogFileReader("20140709.log");

        String[] line;
        while ((line = reader.getNext()) != null) {
            try {
                log.add(line);
            } catch (ParseError e) {
                //Commented to make it shut up
                //System.out.println(e);
            }
        }

        System.out.print("All players: ");
        for (String p : log.getAllPlayers()) {
            System.out.print(p);
            System.out.print(" ");
        }
        System.out.println("");
        for (Round r : log.getAllRounds()) {
            System.out.println(r.summary());
        }

    }
}


class LogFileReader {
    BufferedReader file;
    final CSVParser parser = new CSVParser(';', '"', '&');

    public LogFileReader(final String fn) {
        try {
            file = new BufferedReader(new FileReader(fn));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String[] getNext() {
        try {
            String line = file.readLine();
            if (line != null) {
                return parser.parseLine(line.trim());
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}


