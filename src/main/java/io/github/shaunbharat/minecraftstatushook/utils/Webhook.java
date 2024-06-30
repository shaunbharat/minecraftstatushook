package io.github.shaunbharat.minecraftstatushook.utils;

import com.google.gson.Gson;
import io.github.shaunbharat.minecraftstatushook.MinecraftStatusHook;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static io.github.shaunbharat.minecraftstatushook.utils.Color.rgb;

public class Webhook {
    private final MinecraftStatusHook plugin;

    public Webhook(MinecraftStatusHook plugin) {
        this.plugin = plugin;
    }

    public String getDiscordWebhook() {
        return plugin.getConfig().getString("discord-webhook");
    }

    public void setDiscordWebhook(String discordWebhook) {
        plugin.getConfig().set("discord-webhook", discordWebhook);
        plugin.saveConfig();
    }

    public void sendEmbed(String title, String message, java.awt.Color color) {
        try {
            Embed embed = new Embed(title, message, rgb(color));
            DiscordMessage discordMessage = new DiscordMessage(Collections.singletonList(embed));

            String json = new Gson().toJson(discordMessage);

            URL webhookURL = new URL(getDiscordWebhook());
            HttpURLConnection connection = (HttpURLConnection) webhookURL.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            // Post data
            connection.setDoOutput(true);
            OutputStream stream = connection.getOutputStream();
            stream.write(json.getBytes(StandardCharsets.UTF_8)); // required, otherwise Discord returns 400
            stream.flush();
            stream.close();

            // Read the response streams, then close it. For some reason, the webhook is never sent to if we don't read the response.
            // todo: figure out why this is, but there likely isn't a solution and we might just have to keep these lines here.
            InputStream responseStream = connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream();
            responseStream.close();

            connection.disconnect();

        } catch (Exception e) {
            plugin.getLogger().warning(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                plugin.getLogger().warning(ste.toString());
            }
        }
    }
}

class Embed {
    String title;
    String description;
    int color;

    Embed(String title, String description, int color) {
        this.title = title;
        this.description = description;
        this.color = color;
    }
}

class DiscordMessage {
    List<Embed> embeds;

    DiscordMessage(List<Embed> embeds) {
        this.embeds = embeds;
    }
}
