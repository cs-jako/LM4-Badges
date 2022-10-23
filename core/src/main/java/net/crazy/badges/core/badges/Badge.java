package net.crazy.badges.core.badges;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.crazy.badges.core.Badges;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.inject.LabyGuice;
import net.labymod.api.util.io.web.URLResolver;
import net.labymod.api.util.io.web.WebResponse;
import net.labymod.api.util.io.web.exception.WebRequestException;
import java.util.ArrayList;
import java.util.UUID;

public class Badge {
  private final Badges addon;

  private final int id;
  private final UUID uuid;
  private final String name;
  private final String description;

  private final ArrayList<UUID> players = new ArrayList<>();

  private final String playersUrl;
  private final String iconUrl;

  public Badge(int id, UUID uuid, String name, String description) {
    this.addon = LabyGuice.getInstance(Badges.class);
    this.id = id;
    this.uuid = uuid;
    this.name = name;
    this.description = description;

    this.playersUrl = String.format("https://laby.net/api/badge/%s", this.id);
    this.iconUrl = String.format("https://laby.net/texture/badge-small/%s.png", this.uuid.toString());

    updatePlayers();

  }

  public void updatePlayers() {
    players.clear();

    addon.executor.execute(() -> {
      URLResolver.readJson(playersUrl, true, new WebResponse<JsonElement>() {
        @Override
        public void success(JsonElement result) {
          JsonArray response = result.getAsJsonArray();
          for (int i = 0; i < response.size(); i++) {
            UUID player = UUID.fromString(response.get(i).getAsString());
            players.add(player);
          }
        }

        @Override
        public void failed(WebRequestException exception) {
          addon.logger().error(exception.getMessage());
        }
      });
    });
  }

  public ArrayList<UUID> players() {
    return this.players;
  }

  public Icon icon() {
    return Icon.url(this.iconUrl);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
