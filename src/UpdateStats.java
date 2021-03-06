import au.com.bytecode.opencsv.CSVParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import java.sql.*;

public class UpdateStats {
    public static void main(String[] args) {
        final GameLog log = new GameLog();
        final LogFileReader reader = new LogFileReader(args[0]);

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
            Properties properties = new Properties();
            properties.put("user", args[3]);
            properties.put("password", args[4]);
            properties.put("ssl", "true");
            properties.put("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
            postgres = DriverManager.getConnection("jdbc:postgresql://" + args[1] + "/" + args[2], properties);

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
                            "UPDATE players SET (playtime, kills, deaths) = " +
                            "(playtime + ? * interval '1 S', kills + ?, deaths + ?) " +
                            "WHERE name=? RETURNING id");
                    stm.setInt(1, player.getPlayTime());
                    stm.setInt(2, player.kills);
                    stm.setInt(3, player.deaths);
                    stm.setString(4, player.name);
                    rs = stm.executeQuery();
                    rs.next();
                    player.id = rs.getInt(1);

                    //# Insert for every player in the round
                    stm = postgres.prepareStatement("INSERT INTO roundplayers " +
                            "(round_id, player_id, playtime, kills, deaths) VALUES " +
                            "(?, ?, ? * interval '1 S', ?, ?)");
                    stm.setInt(1, roundid);
                    stm.setInt(2, player.id);
                    stm.setInt(3, player.getPlayTime());
                    stm.setInt(4, player.kills);
                    stm.setInt(5, player.deaths);
                    stm.executeUpdate();
                }

                // Populate the death table
                stm = postgres.prepareStatement("INSERT INTO deaths " +
                        "(round_id, dead_id, killer_id, weapon, killfeed, headshot) VALUES (?, ?, ?, ?, ?, ?)");
                for (Death death : round.getDeaths()) {
                    stm.setInt(1, roundid);
                    stm.setInt(2, death.dead.id);
                    if (death.killer == null) {
                        stm.setNull(3, Types.INTEGER);
                    } else {
                        stm.setInt(3, death.killer.id);
                    }
                    stm.setString(4, death.weaponname);
                    stm.setString(5, death.killfeed);
                    stm.setBoolean(6, death.headshot);
                    stm.addBatch();
                }
                stm.executeBatch();
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


