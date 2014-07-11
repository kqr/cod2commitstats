
public class Death {
    public Player dead, killer;
    String weaponname;
    boolean headshot;
    public Death(Player dead, Player killer, String weaponname, String bodypart) {
        this.dead = dead;
        this.killer = killer;
        this.weaponname = weaponname;
        this.headshot = bodypart.equals("head");
    }
}
