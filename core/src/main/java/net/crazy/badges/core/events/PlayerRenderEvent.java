package net.crazy.badges.core.events;

import java.util.LinkedList;
import java.util.UUID;
import net.crazy.badges.core.Badges;
import net.crazy.badges.core.badges.Badge;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.model.entity.player.PlayerModelRenderEvent;

public class PlayerRenderEvent {

  private final Badges addon;

  public PlayerRenderEvent(Badges addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onPlayerRender(PlayerModelRenderEvent event) {
    Player player = event.player();
    UUID uuid = player.getUniqueId();

    if (!addon.playerCache.containsKey(uuid)) {
      addon.playerCache.put(uuid, getUserBadges(uuid));
      addon.playerCompactCache.put(uuid, getCompactBadges(uuid));
    }
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

  private LinkedList<Badge> getCompactBadges(UUID uuid) {
    LinkedList<Badge> playerBadges = getUserBadges(uuid);

    if (hasBadge(playerBadges, 10))
      playerBadges = removeBadge(playerBadges, 9);

    if (hasBadge(playerBadges, 11))
      playerBadges = removeBadge(playerBadges, 10);

    if (hasBadge(playerBadges, 13))
      playerBadges = removeBadge(playerBadges, 11);

    return playerBadges;
  }

  private boolean hasBadge(LinkedList<Badge> badges, int id) {
    for (Badge badge : badges)
      if (badge.getId() == id)
        return true;

    return false;
  }

  private LinkedList<Badge> removeBadge(LinkedList<Badge> list, int id) {
    list.removeIf(badge -> badge.getId() == id);
    return list;
  }
}
