import au.com.bytecode.opencsv.CSVParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class UpdateStats {
    public static void main(String[] args) {
        final GameLog log = new GameLog();
        final LogFileReader reader = new LogFileReader("20140709.log");

        String[] line;
        while ((line = reader.getNext()) != null) {
            try {
                log.addLine(line);
            } catch (ParseError e) {
                e.printStackTrace();
            }
        }

        Connection postgres = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            postgres = DriverManager.getConnection(
                    "jdbc:postgresql://10.0.0.101/cod2stats",
                    "cod2stats", "cod2stats");

            for (Round round : log.getAllRounds()) {
                //# Insert round and get the round ID out
                stm = postgres.prepareStatement("INSERT INTO rounds (map) VALUES (?) RETURNING id");
                stm.setString(1, round.getMapname());
                rs = stm.executeQuery();
                rs.next();
                int roundid = rs.getInt(1);

                for (Player player : round.getPlayers()) {
                    // Insert new players
                    stm = postgres.prepareStatement(
                            "INSERT INTO players (name) SELECT (?) " +
                            "WHERE NOT EXISTS (SELECT 1 FROM players WHERE name = ?)");
                    stm.setString(1, player.name);
                    stm.setString(2, player.name);
                    stm.executeUpdate();

                    // update playtime for all players and get their ID
                    stm = postgres.prepareStatement(
                            "UPDATE players SET playtime=playtime + ? * interval '1 S' " +
                            "WHERE name=? RETURNING id");
                    stm.setInt(1, player.getPlayTime());
                    stm.setString(2, player.name);
                    rs = stm.executeQuery();
                    rs.next();
                    player.id = rs.getInt(1);

                    //# Insert for every player in the round
                    stm = postgres.prepareStatement("INSERT INTO roundplayers (round_id, player_id, playtime) " +
                            "VALUES (?, ?, ? * interval '1 S')");
                    stm.setInt(1, roundid);
                    stm.setInt(2, player.id);
                    stm.setInt(3, player.getPlayTime());
                }

                // Populate the death table
                for (Death death : round.getDeaths()) {
                    stm = postgres.prepareStatement("INSERT INTO deaths (round_id, dead_id, killer_id, weapon) " +
                            "VALUES (?, ?, ?, ?)");
                    stm.setInt(1, roundid);
                    stm.setInt(2, death.dead.id);
                    stm.setInt(3, death.killer.id);
                    stm.setString(4, death.weaponname);
                    stm.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stm != null) stm.close();
                if (postgres != null) postgres.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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


