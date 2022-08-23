package net.crazy.badges.core;

import com.google.inject.Singleton;
import net.crazy.badges.core.badges.Badge;
import net.crazy.badges.core.badges.BadgeUtil;
import net.crazy.badges.core.events.PlayerRenderEvent;
import net.crazy.badges.core.tags.BadgeTag;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
@AddonListener
public class Badges extends LabyAddon<AddonConfiguration> {

  public final ExecutorService executor = Executors.newFixedThreadPool(6);
  public final HashMap<UUID, Badge> badges = new HashMap<>();
  public final HashMap<UUID, ArrayList<Badge>> playerCache = new HashMap<>();

  @Override
  protected void enable() {
    this.registerSettingCategory();

    BadgeUtil badgeUtil = new BadgeUtil();
    badgeUtil.updateBadges();

    this.registerListener(PlayerRenderEvent.class);

    labyAPI().tagRegistry().register("badge", PositionType.ABOVE_NAME, BadgeTag.create(
        configuration().size()
    ));

    this.logger().info("[Badges] Addon enabled.");
  }

  @Override
  protected Class<AddonConfiguration> configurationClass() {
    return AddonConfiguration.class;
  }
}
