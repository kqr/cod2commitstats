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
                log.addLine(line);
            } catch (ParseError e) {
                //Commented to make it shut up
                e.printStackTrace();
            }
        }

        for (Round round : log.getAllRounds()) {
            //PLAYERS (playername, playtime)
            for (Player player : round.getPlayers()) {
                // Insert new players
                // update playtime for all players
            }

            //ROUNDS (roundid, mapname, timestamp)
            //# Insert once and get the ID out
            //# Timestamp is actual date+timestamp (calculated from file name and log TS)

            //DEATHS (roundid, deadname, killername, weaponname)
            //# Insert for every death in the round
            //
            //ROUNDPLAYERS (roundid, playername, playtime)
            //# Insert for every player in the round
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
            while (true) {
                String line = file.readLine();
                if (line != null && !line.matches("^[0-9]+:[0-9][0-9] -+$")) {
                    return parser.parseLine(line.trim());
                } else if (line == null) {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}


