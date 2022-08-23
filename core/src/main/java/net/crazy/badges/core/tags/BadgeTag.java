package net.crazy.badges.core.tags;

import net.crazy.badges.core.Badges;
import net.crazy.badges.core.badges.Badge;
import net.labymod.api.client.entity.LivingEntity;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.inject.LabyGuice;
import java.util.ArrayList;

public class BadgeTag extends IconTag {
  private final Badges addon;

  private BadgeTag(int size) {
    super(size);
    this.addon = LabyGuice.getInstance(Badges.class);
  }

  public static BadgeTag create(int size) {
    return new BadgeTag(size);
  }

  @Override
  public void render(Stack stack, LivingEntity livingEntity) {
    if (!(livingEntity instanceof Player))
      return;

    Player player = (Player) livingEntity;

    if (!addon.playerCache.containsKey(player.getUniqueId()))
      return;

    ArrayList<Badge> badges = addon.playerCache.get(player.getUniqueId());
    int amount = badges.size();
    int size = addon.configuration().size();

    float renderWidth = getWidth(player) - (float) (amount * size - (amount - 1) * 5) / 2 - (amount * 5);
    float renderHeight = getHeight(player) - size;

    for (Badge badge : badges) {
      Icon icon = badge.icon();

      icon.render(stack, renderWidth, renderHeight, size, size, false, getColor(player));
      renderWidth += 15;
    }
  }

  @Override
  public boolean isVisible(LivingEntity livingEntity) {
    if (!addon.configuration().enabled().get())
      return false;

    if (!(livingEntity instanceof Player))
      return false;

    Player player = (Player) livingEntity;

    if (player.isInvisible())
      return false;

    if (player.isCrouching())
      return false;

    ClientPlayer clientPlayer = labyAPI.minecraft().clientPlayer();
    double distance = clientPlayer.getDistanceSquared(livingEntity);

    if (distance > (double) (64.0F * 64.0F))
      return false;

    if (player.getUniqueId().equals(clientPlayer.getUniqueId()) &&
    !addon.configuration().showOwn())
      return false;

    return true;
  }
}
