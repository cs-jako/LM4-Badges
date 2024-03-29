package net.crazy.badges.core.badges;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.stream.Collectors;

import net.crazy.badges.core.Badges;
import net.labymod.api.util.io.web.URLResolver;
import net.labymod.api.util.io.web.WebResponse;
import net.labymod.api.util.io.web.exception.WebRequestException;

public class BadgeUtil {

  private final Badges addon;

  public BadgeUtil(Badges addon) {
    this.addon = addon;
  }

  public void updateBadges() {
    addon.executor.execute(() -> URLResolver.readJson("https://laby.net/api/badges",
        true, new WebResponse<JsonElement>() {
          @Override
          public void success(JsonElement result) {
            JsonArray response = result.getAsJsonArray();

            for (int i = 0; i < response.size(); i++) {
              JsonObject object = response.get(i).getAsJsonObject();

              int id = object.get("id").getAsInt();
              if (id == 1 || id == 3) {
                continue;
              }

              UUID uuid = UUID.fromString(object.get("uuid").getAsString());
              String name = object.get("name").getAsString();
              String description = object.get("description").getAsString();

              Badge badge = new Badge(addon, id, uuid, name, description);
              addon.badges.put(uuid, badge);
            }

            addon.badges = sortBadgesByID(addon.badges);
          }

          @Override
          public void failed(WebRequestException exception) {
            addon.pushNotification("Badges - Error", "There was an error while loading all badges!");
            addon.logger().error(exception.getMessage());
          }
        }));
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
