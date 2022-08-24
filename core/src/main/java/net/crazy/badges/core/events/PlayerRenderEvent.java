package net.crazy.badges.core.events;

import net.crazy.badges.core.Badges;
import net.crazy.badges.core.badges.Badge;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.model.entity.player.PlayerModelRenderEvent;
import net.labymod.api.inject.LabyGuice;
import java.util.ArrayList;
import java.util.LinkedList;
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

  private LinkedList<Badge> getUserBadges(UUID uuid) {
    LinkedList<Badge> playerBadges = new LinkedList<>();

    for (UUID badgeUUID : addon.badges.keySet()) {
      Badge badge = addon.badges.get(badgeUUID);

      if (badge.players().contains(uuid))
        playerBadges.add(badge);
    }

    return playerBadges;
  }

  private boolean hasBadge(LinkedList<Badge> badges, int id) {
    for (Badge badge : badges)
      if (badge.getId() == id)
        return true;

    return false;
  }

  private LinkedList<Badge> removeBadge(LinkedList<Badge> list, int id) {
    for (Badge badge : list) {
      if (badge.getId() == id)
        list.remove(badge);
    }
    return list;
  }
}
