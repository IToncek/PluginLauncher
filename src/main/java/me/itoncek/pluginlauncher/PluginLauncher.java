package me.itoncek.pluginlauncher;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluginLauncher extends JavaPlugin {

    @Override
    public void onEnable() {
        new AutoUpdater(this,"String-Url to check version","String-URL to download new version", getConfig().getBoolean("autodownload"), getConfig().getBoolean("autorestart"),getConfig().getBoolean("broadcast"));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

