package net.crazy.badges.core;

import net.crazy.badges.core.activities.BadgeActivity;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.AddonActivityWidget.AddonActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.MethodOrder;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
public class AddonConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> showOwn = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> compactBadges = new ConfigProperty<>(false);

  @SliderSetting(min = 5, max = 20)
  private final ConfigProperty<Integer> size = new ConfigProperty<>(10);

  @AddonActivitySetting
  @MethodOrder(after = "size")
  public Activity badgesOverview() {
    return new BadgeActivity();
  }

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public int size() {
    return this.size.get();
  }

  public boolean compact() {
    return this.compactBadges.get();
  }

  public boolean showOwn() {
    return this.showOwn.get();
  }
}
