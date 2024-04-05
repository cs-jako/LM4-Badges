package net.crazy.badges.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.crazy.badges.core.badges.Badge;
import net.crazy.badges.core.badges.BadgeUtil;
import net.crazy.badges.core.events.CacheEvents;
import net.crazy.badges.core.events.PlayerRenderEvent;
import net.crazy.badges.core.tags.BadgeTag;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;

@AddonMain
public class Badges extends LabyAddon<AddonConfiguration> {
  public static Badges addon;

  public LinkedHashMap<UUID, Badge> badges = new LinkedHashMap<>();
  public final HashMap<UUID, LinkedList<Badge>> playerCache = new HashMap<>();
  public BadgeUtil badgeUtil;

  @Override
  protected void enable() {
    this.registerSettingCategory();
    addon = this;

    badgeUtil = new BadgeUtil(this);
    badgeUtil.updateBadges();

    this.registerListener(new PlayerRenderEvent(this));
    this.registerListener(new CacheEvents(this));

    labyAPI().tagRegistry().register("badge", PositionType.ABOVE_NAME, BadgeTag.create(
        this,
        configuration().size()
    ));

    addon.configuration().getCompactBadges().addChangeListener((state) -> {
      if (state) {
        // Don't purge "cache" when enabling compact mode as this doesn't cause any issues
        return;
      }

      /**
       * Purge "Cache"
       * Meaning: Clear the playerCache as due to the #removeIf inside BadgeTag the badges list get's updated
       * This should not really be a performance bottleneck as it's just resets everything back to the beginning
       * and when a player is rendered, their correct badges are loaded from the normal badges cache
       */
      playerCache.clear();
    });

    this.logger().info("[Badges] Addon enabled.");
  }

  @Override
  protected Class<AddonConfiguration> configurationClass() {
    return AddonConfiguration.class;
  }

  public void pushNotification(String title, String text) {
    Notification.Builder builder = Notification.builder()
        .title(Component.text(title))
        .text(Component.text(text))
        .type(Type.SYSTEM);
    labyAPI().notificationController().push(builder.build());
  }
}
