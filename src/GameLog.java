
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class GameLog {

    private ArrayDeque<Round> rounds = new ArrayDeque<>();

    public void add(final String[] line) throws ParseError {
        final String meta[] = line[0].split(" +", 3);
        final String timestamp = meta[0];
        final String msgtype = meta[1];
        final Round currentRound = rounds.peekFirst();

        switch (msgtype) {
            case "InitGame:":
                String map = getMetaVar("mapname", meta[2]);
                int starttime = parseTimestamp(timestamp);
                rounds.push(new Round(map, starttime));
                break;
            case "ShutdownGame:":
                if (currentRound.winner != null) {
                    int endtime = parseTimestamp(timestamp);
                    currentRound.end(endtime);
                } else {
                    // If there's no winner, nobody played. discard the round
                    rounds.pop();
                }
                break;
            case "W":
                currentRound.winner = line[3];
                break;
            case "J":
                String player = line[3];
                currentRound.join(player);
                break;
            case "Q":
                break;
            case "K":
                break;
            case "D":
                break;
            default:
                throw new ParseError("Unrecognised type " + msgtype);
        }
    }

    public Set<String> getAllPlayers() {
        Set<String> players = new HashSet<>();

        for (Round round : rounds) {
            players.addAll(round.getPlayers());
        }

        return players;
    }

    public ArrayDeque<Round> getAllRounds() {
        return rounds;
    }

    private static int parseTimestamp(String timestamp) {
        Scanner timeScanner = new Scanner(timestamp);
        timeScanner.useDelimiter(":");
        int minutes = timeScanner.nextInt();
        int seconds = timeScanner.nextInt();
        return minutes*60+seconds;
    }

    private static String getMetaVar(String var, String metavars) {
        Matcher m = Pattern.compile(".*\\\\" + var + "\\\\(.+?)(\\\\.*|$)").matcher(metavars);
        m.matches();
        return m.group(1);
    }

}


