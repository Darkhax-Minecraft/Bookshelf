package net.darkhax.bookshelf.supporters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.mojang.authlib.GameProfile;

import net.darkhax.bookshelf.Bookshelf;
import net.darkhax.bookshelf.util.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = Bookshelf.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SupporterHandler {

    private static final String LOCATION = "https://darkhax.net/assets/supporters.json";
    private static final Map<UUID, Supporter> SUPPORTERS = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().create();
    private static final List<Consumer<Supporter>> listeners = new ArrayList<>();

    @Nullable
    public static Supporter getSupporterData (Player player) {

        return player != null ? getSupporterData(player.getGameProfile()) : null;
    }

    @Nullable
    public static Supporter getSupporterData (GameProfile profile) {

        return profile != null ? getSupporterData(profile.getId()) : null;
    }

    @Nullable
    public static Supporter getSupporterData (UUID id) {

        return SUPPORTERS.get(id);
    }

    @SubscribeEvent
    public static void init (FMLCommonSetupEvent event) {

        new Thread(SupporterHandler::load).start();
    }

    public static void addListener (Consumer<Supporter> listener) {

        if (listener != null) {

            listeners.add(listener);
            Bookshelf.LOG.debug("Added supporter listener {} from mod {}.", listener.toString(), ModUtils.getActiveMod());
        }

        else {

            Bookshelf.LOG.error("Attempt to register null support listener?", new Throwable());
        }
    }

    private static void load () {

        if (SUPPORTERS.isEmpty()) {

            final long startTime = System.currentTimeMillis();

            try (final Reader reader = getHttpReader()) {

                final SupporterData data = GSON.fromJson(reader, SupporterData.class);

                Arrays.stream(data.playerList).forEach(sup -> {

                    SUPPORTERS.put(sup.id, sup);
                    listeners.forEach(listener -> listener.accept(sup));
                });
            }

            catch (final IOException e) {

                Bookshelf.LOG.warn("Unnable to read supporter data. This is not likely an issue.", e);
            }

            Bookshelf.LOG.debug("Loaded {} supporters. Took {}ms.", SUPPORTERS.size(), System.currentTimeMillis() - startTime);
        }

        else {

            final String userIds = SUPPORTERS.keySet().stream().map(Object::toString).collect(Collectors.joining(", "));
            Bookshelf.LOG.warn("Supporter registry already has {} entries. Aborting population request. {}", SUPPORTERS.size(), userIds);
        }
    }

    private static Reader getHttpReader () throws IOException {

        final HttpURLConnection connection = (HttpURLConnection) new URL(LOCATION).openConnection();

        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:68.0) Gecko/20100101 Firefox/68.0");
        connection.setRequestProperty("Accept-Encoding", "gzip");

        if ("gzip".equals(connection.getContentEncoding())) {

            return new InputStreamReader(new GZIPInputStream(connection.getInputStream()));
        }

        else {

            return new InputStreamReader(connection.getInputStream());
        }
    }

    class SupporterData {

        @Expose
        Supporter[] playerList;
    }

    public static class Supporter {

        @Expose
        UUID id;

        @Expose
        String name;

        @Expose
        String title;

        @Expose
        Map<String, JsonObject> data;

        public UUID getId () {

            return this.id;
        }

        public String getName () {

            return this.name;
        }

        public String getTitle () {

            return this.title;
        }

        @Nullable
        public JsonObject getData (String id) {

            return this.data.get(id);
        }
    }
}