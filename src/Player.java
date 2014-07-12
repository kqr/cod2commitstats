
public class Player {
    public String name;
    private int joinTime = -1;
    private int timePlayed = 0;
    public int id = -1;
    public int kills;
    public int deaths;

    public Player(String name) {
        this.name = name;
    }

    public void join(int joinTime) {
        this.joinTime = joinTime;
    }

    public void quit(int quitTime) {
        timePlayed += quitTime - joinTime;
        joinTime = -1;
    }

    public double efficacy() {
        return kills*kills*kills / ((double) timePlayed);
    }

    public boolean online() {
        return joinTime > -1;
    }

    public int getPlayTime() {
        return timePlayed;
    }
}
