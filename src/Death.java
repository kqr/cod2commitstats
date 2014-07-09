
public class Death {
    public Player dead, killer;
    String weaponname;
    public Death(Player dead, Player killer, String weaponname) {
        this.dead = dead;
        this.killer = killer;
        this.weaponname = weaponname;
    }
}
