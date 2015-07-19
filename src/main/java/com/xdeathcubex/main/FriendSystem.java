package com.xdeathcubex.main;

import com.xdeathcubex.commands.FriendCommand;
import com.xdeathcubex.commands.MSGCommand;
import com.xdeathcubex.commands.RCommand;
import com.xdeathcubex.events.JoinEvent;
import com.xdeathcubex.events.LeaveEvent;
import com.xdeathcubex.mysql.MySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.HashMap;

public class FriendSystem extends Plugin {

    public static HashMap<ProxiedPlayer, ProxiedPlayer> msg = new HashMap<>();
    public static String prefix = "§9Friends §8» §7";
    public static FriendSystem instance;

    public void onEnable(){
        instance = this;
        new MySQL("5.1.80.121", "root", "hase", "Lightning");
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerListener(this, new JoinEvent());
        pluginManager.registerListener(this, new LeaveEvent());
        pluginManager.registerCommand(this, new FriendCommand());
        pluginManager.registerCommand(this, new MSGCommand());
        pluginManager.registerCommand(this, new RCommand());
        System.out.println("Plugin has been activated.");
    }
}
