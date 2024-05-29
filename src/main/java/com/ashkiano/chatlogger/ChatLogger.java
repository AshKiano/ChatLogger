package com.ashkiano.chatlogger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatLogger extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("ChatLogger has been enabled");
        Metrics metrics = new Metrics(this, 22036);
        this.getLogger().info("Thank you for using the ChatLogger plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChatLogger has been disabled");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getPlayer().getName() + ": " + event.getMessage();
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> logChat(message, timeStamp));
    }

    private void logChat(String message, String timeStamp) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String logMessage = timeStamp + " " + message;
        File logFile = new File(getDataFolder(), "chatlog-" + date + ".txt");

        try {
            if (!logFile.exists()) {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(logMessage);
                writer.newLine();
            }
        } catch (IOException e) {
            getLogger().severe("Could not write to chat log file: " + e.getMessage());
        }
    }
}
