package dungeonforge.events;

import dungeonforge.core.Player;

public class DamageMeter implements  GameEventListener {

    private EventBus bus;
    private final Player player;
    private double damageThreshold;

    public DamageMeter(EventBus bus, Player player, double damageThreshold) {
        this.player = player;
        this.damageThreshold = damageThreshold;
        this.bus = bus;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType().equals(EventType.DAMAGE_TAKEN)) {
            if (player.getHp() < damageThreshold) {
                bus.message("~~~ DANGER " + player.getName() + " ROBINSON!!!! ~~~\n" +
                        "    your Hp is below the threshold -- " + damageThreshold);
            }
        }
    }
}
