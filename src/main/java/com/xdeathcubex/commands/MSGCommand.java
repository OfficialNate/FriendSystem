package com.xdeathcubex.commands;

import com.xdeathcubex.main.FriendSystem;
import com.xdeathcubex.mysql.MySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MSGCommand extends Command {

    public MSGCommand() {
        super("msg");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) cs;

        if(args.length < 2){
            p.sendMessage(new TextComponent(FriendSystem.prefix + "§cVerwendung: /msg <Freund> <Nachricht>"));
        } else {
            if(args[0].equals(p.getName())){
                p.sendMessage(new TextComponent(FriendSystem.prefix + "§cDu hast echt nichts besseres zu tun, oder xD?"));
            } else {
                ProxiedPlayer p1 = ProxyServer.getInstance().getPlayer(args[0]);
                if(p1 != null){
                    if(MySQL.is("Friends", p1.getUniqueId().toString(), p.getUniqueId().toString())){
                        if(MySQL.getProperties(p1.getUniqueId().toString(), "msgs")) {
                            String nachricht = "";
                            for (int i = 1; i < args.length; i++) {
                                nachricht += args[i] + " §e";
                            }
                            p.sendMessage(new TextComponent("§bich §7-> §b" + p1.getDisplayName() + " §8» §e" + nachricht));
                            p1.sendMessage(new TextComponent("§b" + p.getDisplayName() + " §7-> §bmir §8» §e" + nachricht));
                            FriendSystem.msg.put(p1, p);
                        } else {
                            p.sendMessage(new TextComponent(FriendSystem.prefix +"§c" + p1.getDisplayName() + "§c erlaubt keine privaten Nachrichten."));
                        }
                    } else {
                        p.sendMessage(new TextComponent(FriendSystem.prefix + "§cDu bist mit diesem Spieler nicht befreundet!"));
                    }
                } else {
                    p.sendMessage(new TextComponent(FriendSystem.prefix + "§cDer Spieler §6" + args[0] + " §cist momentan nicht online!"));
                }
            }
        }
    }
}