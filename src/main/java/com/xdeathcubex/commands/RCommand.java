package com.xdeathcubex.commands;

import com.xdeathcubex.main.FriendSystem;
import com.xdeathcubex.mysql.MySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RCommand extends Command {

    public RCommand() {
        super("r");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) cs;
        ProxiedPlayer p1 = FriendSystem.msg.get(p);

        if (args.length < 1) {
            p.sendMessage(new TextComponent(FriendSystem.prefix + "§cVerwendung: /r <Nachricht>"));
        } else {
            if (MySQL.is("Friends", p.getUniqueId().toString(), p1.getUniqueId().toString())) {
                if(MySQL.getProperties(p1.getUniqueId().toString(), "msgs")) {
                    String nachricht = "";
                    for (String arg : args) {
                        nachricht += arg + " §e";
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
        }
    }


}
