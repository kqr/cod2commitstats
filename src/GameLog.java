
import java.util.ArrayDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class GameLog {
    private ArrayDeque<Round> rounds = new ArrayDeque<>();

    public ArrayDeque<Round> getAllRounds() {
        return rounds;
    }

    public void addLine(final String[] line) throws ParseError {
        final String meta[] = line[0].split(" +", 3);
        final int time = parseTimestamp(meta[0]);
        final String msgtype = meta[1];
        final Round currentRound = rounds.peekFirst();

        if (msgtype.matches("^-+$")) {
            return;
        }

        switch (msgtype) {
            case "InitGame:":
                String map = getMetaVar("mapname", meta[2]);
                rounds.push(new Round(map, time));
                break;
            case "ShutdownGame:":
                if (currentRound.active) {
                    currentRound.end(time);
                } else {
                    // If there was no kill during the round, it doesn't count. Discard it
                    rounds.pop();
                }
                break;
            case "J":
                currentRound.join(line[3], time);
                break;
            case "Q":
                currentRound.quit(line[3], time);
                break;
            case "K":
                currentRound.death(line[4], line[8], line[9]);
                break;
            // These are ignored but kept as cases because they're not unknown
            case "D": case "W": case "Weapon": case "say": case "ExitLevel:":
                break;
            default:
                throw new ParseError("Unrecognised log message type '" + msgtype + "'");
        }
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


