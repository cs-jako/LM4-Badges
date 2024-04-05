package net.crazy.badges.core.badges;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.stream.Collectors;

import net.crazy.badges.core.Badges;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Response;

public class BadgeUtil {

  private static final String BADGES_ENDPOINT = "https://laby.net/api/badges";
  private final Badges addon;
  private final Request<JsonElement> request;

  public BadgeUtil(Badges addon) {
    this.addon = addon;

    this.request = Request.ofGson(JsonElement.class)
        .url(BADGES_ENDPOINT)
        .async(true);
  }

  public void updateBadges() {
    this.request.execute(this::handleResponse);
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

      for (int i = 0; i < entries.size(); i++) {
        JsonObject object = entries.get(i).getAsJsonObject();

        int id = object.get("id").getAsInt();
        if (id == 1 || id == 3) {
          continue;
        }

        UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        String name = object.get("name").getAsString();
        String description = object.get("description").getAsString();

        Badge badge = new Badge(this.addon, id, uuid, name, description);
        this.addon.badges.put(uuid, badge);
      }

      this.addon.badges = this.sortBadgesByID(this.addon.badges);

    } catch (Exception exception) {
      this.addon.pushNotification("Badges - Error", "There was an error while loading all badges!");
      this.addon.logger().error(exception.getMessage());
    }
  }

  private LinkedHashMap<UUID, Badge> sortBadgesByID(LinkedHashMap<UUID, Badge> badges) {
    return badges.entrySet().stream()
        .sorted(Comparator.comparing(entry -> entry.getValue().getId()))
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
  }
}
