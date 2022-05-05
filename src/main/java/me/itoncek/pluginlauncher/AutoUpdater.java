package me.itoncek.pluginlauncher;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AutoUpdater {

    private final Plugin plugin;
    private final String urlVersionCheck;
    private final String urlDownload;
    private final String currentVersion;
    private final boolean autoDownload;
    private final boolean autoShutdown;
    private final boolean broadcast;

    public AutoUpdater(Plugin plugin, String UrlVersion, String UrlDownload) {
        this(plugin, UrlVersion, UrlDownload, true, true, true);
    }

    public AutoUpdater(Plugin plugin, String UrlVersion, String UrlDownload, boolean AutoDownload, boolean AutoShutdown, boolean Broadcast) {
        this.plugin = plugin;
        this.urlVersionCheck = UrlVersion;
        this.urlDownload = UrlDownload;
        this.currentVersion = plugin.getDescription().getVersion();
        this.autoDownload = AutoDownload;
        this.autoShutdown = AutoShutdown;
        this.broadcast = Broadcast;
        start();
    }

    private void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String v = null;
                try {
                    v = getText(urlVersionCheck);
                } catch (Exception e) {
                    plugin.getLogger().info("Failed to get Version!!!");
                    plugin.getLogger().info("---------- Stack Trace ----------");
                    e.printStackTrace();
                    plugin.getLogger().info("---------- Stack Trace ----------");
                }
                assert v != null;
                if (!v.equalsIgnoreCase(currentVersion)) {
                    plugin.getLogger().info("New update is Avaliable...");
                    if(autoDownload)
                        startDownload();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 200L);
    }

    private void startDownload() {
        try {
            URL download = new URL(this.urlDownload);
            BufferedInputStream in = null;
            FileOutputStream fout = null;
            try {
                plugin.getLogger().info("Trying to download from: " + download);
                in = new BufferedInputStream(download.openStream());
                fout = new FileOutputStream("plugins" + System.getProperty("file.separator") + plugin.getName() + ".jar");

                final byte[] data = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                }
            }catch (Exception e){
                plugin.getLogger().info("Failed to Download new Version!!!");
                plugin.getLogger().info("---------- Stack Trace ----------");
                e.printStackTrace();
                plugin.getLogger().info("---------- Stack Trace ----------");
                return;
            } finally {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
            }
            plugin.getLogger().info("Succesfully downloaded update: " + download.getFile());
            if(this.broadcast){
                String broadCastMessage = "§3[AutoUpdater] §bRestarting server for update!";
                for(World world : Bukkit.getWorlds()) {
                    world.sendMessage(Component.text(broadCastMessage));
                }
            }
            if(this.autoShutdown){
                Bukkit.getServer().spigot().restart();
            }
        } catch (IOException e) {
            plugin.getLogger().info("Failed to Download new Version!!!");
            plugin.getLogger().info("---------- Stack Trace ----------");
            e.printStackTrace();
            plugin.getLogger().info("---------- Stack Trace ----------");
        }
    }

    private String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}