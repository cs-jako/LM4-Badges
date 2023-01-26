package net.crazy.badges.core.tags;

import java.util.LinkedList;
import net.crazy.badges.core.Badges;
import net.crazy.badges.core.badges.Badge;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;

public class BadgeTag extends IconTag {

  private final Badges addon;

  private BadgeTag(Badges addon, int size) {
    super(size);
    this.addon = addon;
  }

  public static BadgeTag create(Badges addon, int size) {
    return new BadgeTag(addon, size);
  }

  @Override
  public void render(Stack stack, Entity livingEntity) {
    if (!(livingEntity instanceof Player)) {
      return;
    }

    Player player = (Player) livingEntity;

    if (!addon.playerCache.containsKey(player.getUniqueId())) {
      return;
    }

    LinkedList<Badge> badges;
    if (addon.configuration().compact()) {
      badges = addon.playerCompactCache.get(player.getUniqueId());
    } else {
      badges = addon.playerCache.get(player.getUniqueId());
    }

    if (badges == null) {
      return;
    }

    int amount = badges.size();
    int size = addon.configuration().size();

    float renderWidth =
        getWidth() - (float) (amount * size - (amount - 1) * 5) / 2 - (amount * 5);
    float renderHeight = getHeight() - size;

    for (Badge badge : badges) {
      Icon icon = badge.icon();

      icon.render(stack, renderWidth, renderHeight, size, size, false, getColor());
      renderWidth += 15;
    }
  }

  @Override
  public boolean isVisible() {
    if (!addon.configuration().enabled().get()) {
      return false;
    }

    Entity livingEntity = this.entity;
    if (entity == null)
      return false;

    if (!(livingEntity instanceof Player)) {
      return false;
    }

    Player player = (Player) livingEntity;

    if (!addon.playerCache.containsKey(player.getUniqueId()) ||
        addon.playerCache.get(player.getUniqueId()).size() == 0) {
      return false;
    }

    if (player.isInvisible()) {
      return false;
    }

    if (player.isCrouching()) {
      return false;
    }

    ClientPlayer clientPlayer = labyAPI.minecraft().clientPlayer();
    double distance = clientPlayer.getDistanceSquared(livingEntity);

    if (distance > (double) (64.0F * 64.0F)) {
      return false;
    }

    if (player.getUniqueId().equals(clientPlayer.getUniqueId()) &&
        !addon.configuration().showOwn()) {
      return false;
    }

    return true;
  }
}
