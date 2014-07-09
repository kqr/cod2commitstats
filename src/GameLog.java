
import java.util.Set;
import java.util.HashSet;

public class GameLog {

    private Set<String> players;

    public GameLog() {
        players = new HashSet<>();
    }

    public void add(String[] line) throws ParseError {
         char msgtype = line[0].charAt(line[0].length() - 1);

         switch (msgtype) {
             case 'J':
                 players.add(line[3]);
                 break;
             default:
                 throw new ParseError("Unrecognised type " + msgtype);
         }
    }

    public Set<String> getPlayers() {
        return players;
    }

}


