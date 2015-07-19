package com.xdeathcubex.events;

import com.xdeathcubex.main.FriendSystem;
import com.xdeathcubex.mysql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(ServerConnectEvent e) {
        String uuid = e.getPlayer().getUniqueId().toString();
        MySQL.setUser(uuid);
        MySQL.setupProperties(uuid);
        String[] friends = MySQL.get("Friends", uuid).split(" ");
        for(String freunde : friends){
            if(!freunde.equals("")){
                if(ProxyServer.getInstance().getPlayer(UUID.fromString(freunde)) != null) {
                    ProxiedPlayer p1 = ProxyServer.getInstance().getPlayer(UUID.fromString(freunde));
                    if(MySQL.getProperties(p1.getUniqueId().toString(), "notifies")) {
                        p1.sendMessage(new TextComponent(FriendSystem.prefix + e.getPlayer().getDisplayName() + " §7ist nun §aonline"));
                    }
                }
            }
        }
    }
}