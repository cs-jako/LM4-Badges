package net.crazy.badges.core.badges;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.crazy.badges.core.Badges;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Response;
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

  public Badge(Badges addon, int id, UUID uuid, String name, String description) {
    this.addon = addon;
    this.id = id;
    this.uuid = uuid;
    this.name = name;
    this.description = description;

    this.playersUrl = String.format("https://laby.net/api/badge/%s", this.id);
    this.iconUrl = String.format(
        "https://laby.net/texture/badge-small/%s.png",
        this.uuid.toString()
    );

    this.updatePlayers();

  }

  public void updatePlayers() {
    this.players.clear();
    Request.ofGson(JsonElement.class)
        .url(this.playersUrl)
        .async(true)
        .execute(this::handleResponse);
  }

  public ArrayList<UUID> players() {
    return this.players;
  }

  public Icon icon() {
    return Icon.url(this.iconUrl).resolution(50, 50);
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  private void handleResponse(Response<JsonElement> response) {
    try {
      if (response.hasException()) {
        throw response.exception();
      }

      if (response.isEmpty()) {
        throw new IllegalStateException("Invalid badge response");
      }

      JsonElement element = response.get();
      if (!element.isJsonArray()) {
        throw new IllegalStateException("Invalid badge response");
      }

      JsonArray entries = element.getAsJsonArray();
      for (int index = 0; index < entries.size(); index++) {
        UUID player = UUID.fromString(entries.get(index).getAsString());
        this.players.add(player);
      }

    } catch (Exception exception) {
      this.addon.pushNotification(
          "Badges - Error",
          "There was an error while fetching the Players of Badge: " + Badge.this.id
      );
      this.addon.logger().error(exception.getMessage());
    }
  }
}
