package mames1.net.mamesosu.Object;

import io.github.cdimascio.dotenv.Dotenv;
import mames1.net.mamesosu.Event.OpenSupport;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot {

    final long GUILD_ID;
    final long CATEGORY_ID;
    final String TOKEN;

    JDA jda;

    public Bot() {
        Dotenv dotenv = Dotenv.configure()
                .load();
        GUILD_ID = Long.parseLong(dotenv.get("GID"));
        CATEGORY_ID = Long.parseLong(dotenv.get("CID"));
        TOKEN = dotenv.get("TOKEN");
    }

    public void start() {
        jda = JDABuilder.createDefault(this.TOKEN)
                .setRawEventsEnabled(true)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS
                ).enableCache(
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.ROLE_TAGS,
                        CacheFlag.EMOJI
                )
                .disableCache(
                        CacheFlag.VOICE_STATE,
                        CacheFlag.STICKER,
                        CacheFlag.SCHEDULED_EVENTS
                ).setActivity(
                        Activity.playing("Support <3"))
                .addEventListeners(
                        new OpenSupport()
                )
                .build();
    }

    public JDA getJda() {
        return jda;
    }

    public long getGUILD_ID() {
        return GUILD_ID;
    }

    public long getCATEGORY_ID() {
        return CATEGORY_ID;
    }
}
