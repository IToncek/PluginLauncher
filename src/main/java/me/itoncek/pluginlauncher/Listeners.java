package me.itoncek.pluginlauncher;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Listeners implements Listener {
    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e){
        if(PluginLauncher.inventoryStringHashMap.contains(e.getView().title().toString())){
            e.setCancelled(true);
        }
    }
}
