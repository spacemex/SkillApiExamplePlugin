package com.github.spacemex.skills;

import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class ServerUtil {

    private final Skills plugin;
    private final Logger logger;


    public ServerUtil(Skills plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    public Logger getLogger() {
        return logger;
    }

    public boolean isPluginInstalled(String pluginName){
        return plugin.getServer().getPluginManager().getPlugin(pluginName) != null;
    }

    public void logError(String message){
        logger.severe(message);
    }
    public void logInfo(String message){
        logger.info(message);
    }
    public void logWarning(String message){
        logger.warning(message);
    }

    public void disablePlugin(Plugin plugin){
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    public Skills getPlugin(){
        return plugin;
    }
    public Server getServer(){
        return plugin.getServer();
    }
    // Generic method to register event listeners
    public <T extends Listener> void registerEvent(Class<T> eventClass) {
        try {
            // Use reflection to create an instance of the Listener class
            T eventInstance = eventClass.getConstructor(ServerUtil.class).newInstance(this);
            // Register the event instance
            getServer().getPluginManager().registerEvents(eventInstance, plugin);
        } catch (Exception e) {
            // Handle possible exceptions, e.g., no such method, instantiation failure
            e.printStackTrace();
            logError("Failed to register event class: " + eventClass.getName());
        }
    }
}
