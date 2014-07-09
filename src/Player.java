
public class Player {
    public String name;
    private int joinTime = -1;
    private int timePlayed = 0;

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

    public boolean online() {
        return joinTime > -1;
    }

    public int getPlayTime() {
        return timePlayed;
    }
}
