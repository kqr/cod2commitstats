
public class Death {
    public Player dead, killer;
    String weaponname, killfeed;
    boolean headshot;
    public Death(Player dead, Player killer, String weaponname, String killfeed, String bodypart) {
        this.dead = dead;
        this.killer = killer;
        this.weaponname = weaponname;
        this.killfeed = killfeed;
        this.headshot = bodypart.equals("head");
    }
}
