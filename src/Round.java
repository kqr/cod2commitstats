import java.util.HashSet;
import java.util.Set;

public class Round {
    private Set<String> players = new HashSet<>();
    private String mapname;
    private int starttime;
    private int endtime;
    public String winner;

    public Round(String map, int started) {
        mapname = map;
        starttime = started;
    }

    public void join(String player) {
        players.add(player);
    }

    public void end(int ended) {
        endtime = ended;
    }

    public String summary() {
        return getDuration()/60 + " minutes played on " + mapname + " where " + winner + " won!";
    }

    public int getDuration() {
        return endtime - starttime;
    }

    public Set<String> getPlayers() {
        return players;
    }
}
