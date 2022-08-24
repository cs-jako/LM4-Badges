package net.crazy.badges.core.badges;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.UUID;
import net.crazy.badges.core.Badges;
import net.labymod.api.inject.LabyGuice;
import net.labymod.api.util.io.web.URLResolver;
import net.labymod.api.util.io.web.WebResponse;
import net.labymod.api.util.io.web.exception.WebRequestException;

public class BadgeUtil {

  private final Badges addon;

  public BadgeUtil() {
    this.addon = LabyGuice.getInstance(Badges.class);
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

              Badge badge = new Badge(id, uuid, name, description);
              addon.logger().info("Added: " + name + " (" + id + ")");
              addon.badges.put(uuid, badge);
            }
          }

          @Override
          public void failed(WebRequestException exception) {
            addon.logger().error(exception.getMessage());
          }
        }));
  }
}
