import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class Round {
    private String mapname;
    private Map<String, Player> players = new HashMap<>();
    private ArrayList<Death> deaths = new ArrayList<>();
    int starttime;
    public boolean active = false;

    public Round(String map, int time) {
        mapname = map;
        starttime = time;
    }

    public void join(String name, int time) {
        if (players.containsKey(name)) {
            players.get(name).join(time);
        } else {
            Player p = new Player(name);
            players.put(name, p);
            p.join(time);
        }
    }

    public void quit(String name, int time) {
        players.get(name).quit(time);
    }

    public void death(String dead, String killer, String weaponname) {
        deaths.add(new Death(players.get(dead), players.get(killer), weaponname));
        active = true;
    }

    public void end(int time) {
        for (Player p : players.values()) {
            if (p.online()) {
                p.quit(time);
            }
        }
    }

    public String getMapname() {
        return mapname;
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public ArrayList<Death> getDeaths() {
        return deaths;
    }
}
