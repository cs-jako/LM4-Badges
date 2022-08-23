package net.crazy.badges.core.events;

import net.crazy.badges.core.Badges;
import net.crazy.badges.core.badges.Badge;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.model.entity.player.PlayerModelRenderEvent;
import net.labymod.api.inject.LabyGuice;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerRenderEvent {

  private final Badges addon;

  public PlayerRenderEvent() {
    this.addon = LabyGuice.getInstance(Badges.class);
  }

  @Subscribe
  public void onPlayerRender(PlayerModelRenderEvent event) {
    Player player = event.player();
    UUID uuid = player.getUniqueId();

    if (!addon.playerCache.containsKey(uuid))
      addon.playerCache.put(uuid, getUserBadges(uuid));
  }

  private ArrayList<Badge> getUserBadges(UUID uuid) {
    ArrayList<Badge> playerBadges = new ArrayList<>();

    addon.badges.forEach((badgeId, badge) -> {
      if (badge.players().contains(uuid)) {
        playerBadges.add(badge);
      }
    });
    return playerBadges;
  }
}
