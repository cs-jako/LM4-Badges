package net.crazy.badges.core.activities;

import net.crazy.badges.core.Badges;
import net.crazy.badges.core.badges.Badge;
import net.labymod.api.client.gui.screen.LabyScreen;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import org.jetbrains.annotations.Nullable;
import java.util.LinkedList;

@AutoActivity
@Link("overview.lss")
public class BadgeActivity extends Activity {
  private final Badges addon;

  private LinkedList<BadgeWidget> badgeWidgets = new LinkedList<>();
  private TilesGridWidget<BadgeWidget> gridWidget;
  private ListSession<?> listSession = new ListSession<>();

  public BadgeActivity() {
    this.addon = Badges.addon;

    for (Badge badge : addon.badges.values())
      badgeWidgets.add(new BadgeWidget(badge.icon(), badge.getName(), badge.getDescription()));

    gridWidget = new TilesGridWidget<>();
    gridWidget.addId("gridWidget");
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    for (BadgeWidget badgeWidget : badgeWidgets)
      this.gridWidget.addTile(badgeWidget);


    ScrollWidget scrollWidget = new ScrollWidget(gridWidget, listSession);
    document().addChild(scrollWidget);
  }

  @Override
  public <T extends LabyScreen> @Nullable T renew() {
    return new BadgeActivity().generic();
  }
}
