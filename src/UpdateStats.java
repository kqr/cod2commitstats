import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;


public class UpdateStats {
    public static void main(String[] args) {
        GameLog log = new GameLog();
        WrappedCSVReader reader = new WrappedCSVReader("20140709.log");

        String[] line;
        while ((line = reader.getNext()) != null) {
            try {
                log.add(line);
            } catch (ParseError e) {
                // Commented to make it shut up
                //System.out.println(e);
            }
        }

        for (String p : log.getPlayers()) {
            System.out.println(p);
        }

    }
}


class WrappedCSVReader {
    CSVReader reader;
    public WrappedCSVReader(String fn) {
        try {
            reader = new CSVReader(new FileReader("20140709.log"), ';');
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public String[] getNext() {
        String[] line = new String[0];

        while (true) {
            try {
                line = reader.readNext();
                return line;
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}


