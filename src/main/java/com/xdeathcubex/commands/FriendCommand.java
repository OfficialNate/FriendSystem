package com.xdeathcubex.commands;

import com.xdeathcubex.main.FriendSystem;
import com.xdeathcubex.mysql.MySQL;
import com.xdeathcubex.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend");
    }

    UUID uuid = null;
    ProxiedPlayer p1 = null;

    TextComponent notexist = new TextComponent(FriendSystem.prefix + "§cDieser Spieler existiert nicht!");

    @Override
    public void execute(CommandSender cs, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer)cs;
            if (args.length == 0) {
                player.sendMessage(new TextComponent("§e---------- §aFriendSystem §e----------\n"
                        + "§e/friend add <Name> §8- §7Fügt einen Freund hinzu\n"
                        + "§e/friend remove <Name> §8- §7Entfernen einen Freund\n"
                        + "§e/friend list §8- §7Zeigt alle Freunde\n"
                        + "§e/friend requests §8- §7 Zeigt offene Freundschaftsanfragen an\n"
                        + "§e/friend jump <Name> §8- §7Zum Server vom Freund verbinden\n"
                        + "§e/friend accept <Name> §8- §7Nimmt eine Freundschaftsanfrage an\n"
                        + "§e/friend deny <Name> §8- §7Lehnt eine Freundschaftsanfrage ab\n"
                        + "§e/msg <Name> <Nachricht> §8- §7Sendet einem Freund eine Nachricht\n"
                        + "§e/r <Nachricht> §8- §7Antwortet auf eine Nachricht\n"
                        + "§e/friend toggle §8- §7Anfragen deaktivieren\n"
                        + "§e/friend togglemsgs §8- §7private Nachrichten deaktivieren\n"
                        + "§e/friend togglejump §8- §7Nachspringen deaktivieren\n"
                        + "§e/friend togglenotifies §8- §7Join / Leave Nachrichten deaktivieren\n"
                        + "§e---------- §aFriendSystem §e----------"));


            } else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
                if (args[1].equalsIgnoreCase(player.getName())) {
                    player.sendMessage(new TextComponent(FriendSystem.prefix + "§cHast du sonst keine Freunde?"));
                } else {
                    uuid = UUIDFetcher.getUUID(args[1]);
                    if (uuid == null || MySQL.get("Friends", uuid.toString()) == null) {
                        player.sendMessage(notexist);
                    } else {
                        if (MySQL.is("Friends", uuid.toString(), player.getUniqueId().toString())) {
                            player.sendMessage(new TextComponent(FriendSystem.prefix + "§cDu bist mit diesem Spieler bereits befreundet!"));
                        } else {
                            if (MySQL.is("Requests", player.getUniqueId().toString(), uuid.toString())) {
                                player.sendMessage(new TextComponent(FriendSystem.prefix + "§cDu hast diesem Spieler bereits eine Freundschaftsanfrage geschickt!"));
                            } else {
                                if (MySQL.getProperties(uuid.toString(), "invites")) {
                                    MySQL.add(uuid.toString(), "Requests", player.getUniqueId().toString());
                                    player.sendMessage(new TextComponent(FriendSystem.prefix + "§7Anfrage wurde an §6" + args[1] + " §7geschickt!"));
                                    p1 = ProxyServer.getInstance().getPlayer(args[1]);
                                    if (p1 != null) {
                                        p1.sendMessage(new TextComponent(FriendSystem.prefix + "§6" + player.getDisplayName() + " §7hat dir eine Freundschaftsanfrage geschickt."));
                                        p1.sendMessage(new TextComponent(FriendSystem.prefix + "§e/friend accept §8- §7um die Anfrage §aanzunehmen"));
                                        p1.sendMessage(new TextComponent(FriendSystem.prefix + "§e/friend deny §8- §7um die Anfrage §cabzulehnen"));
                                    }
                                } else {
                                    player.sendMessage(new TextComponent(FriendSystem.prefix + "§6" + p1.getDisplayName() + " §cempfängt keine Freundschaftsanfragen!"));
                                }
                            }
                        }
                    }
                }


            } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                uuid = UUIDFetcher.getUUID(args[1]);
                if (uuid == null) {
                    player.sendMessage(notexist);
                } else {
                    if (!MySQL.is("Friends", uuid.toString(), player.getUniqueId().toString())) {
                        player.sendMessage(new TextComponent(FriendSystem.prefix + "§cDu bist mit diesem Spieler nicht befreundet!"));
                    } else {
                        MySQL.remove(uuid.toString(), "Friends", player.getUniqueId().toString());
                        MySQL.remove(player.getUniqueId().toString(), "Friends", uuid.toString());
                        player.sendMessage(new TextComponent(FriendSystem.prefix + "§7Du hast §6 " + args[1] + " §7von deiner Freundesliste entfernt!"));
                        p1 = ProxyServer.getInstance().getPlayer(args[1]);
                        if (p1 != null) {
                            p1.sendMessage(new TextComponent(FriendSystem.prefix + "§6" + player.getDisplayName() + " §7hat dich von seiner Freundesliste entfernt."));
                        }
                    }
                }


            } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                player.sendMessage(new TextComponent("§eFreunde: "));
                String freunde = MySQL.get("Friends", player.getUniqueId().toString());
                    String[] freunde1 = freunde.split(" ");
                    for (String friends : freunde1) {
                        if(!friends.equals("")) {
                            player.sendMessage(new TextComponent("§8- §7" + UUIDFetcher.getName(UUID.fromString(friends))));
                        }
                    }


            } else if (args.length == 1 && args[0].equalsIgnoreCase("requests")) {
                player.sendMessage(new TextComponent("§eAusstehende Freundschaftsanfragen: "));
                String anfragen = MySQL.get("Requests", player.getUniqueId().toString());
                String[] anfragen1 = anfragen.split(" ");
                for (String requests : anfragen1) {
                    if (!requests.equals("")) {
                        player.sendMessage(new TextComponent("§8- §7" + UUIDFetcher.getName(UUID.fromString(requests))));
                    }
                }
            } else if(args.length == 2 && args[0].equalsIgnoreCase("jump")) {
                uuid = UUIDFetcher.getUUID(args[1]);
                if(uuid == null){
                    player.sendMessage(notexist);
                } else {
                    if(MySQL.is("Friends", uuid.toString(), player.getUniqueId().toString())){
                        if(MySQL.getProperties(uuid.toString(), "jump")){
                            p1 = ProxyServer.getInstance().getPlayer(uuid);
                            player.connect(p1.getServer().getInfo());
                        } else {
                            player.sendMessage(new TextComponent(FriendSystem.prefix + "§6" +p1.getDisplayName() + " §cerlaubt kein Nachjoinen!"));
                        }
                    }
                }

            } else if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
                 uuid = UUIDFetcher.getUUID(args[1]);
                 if (uuid == null) {
                    player.sendMessage(notexist);
                } else {
                    if (MySQL.is("Friends", uuid.toString(), player.getUniqueId().toString())) {
                        player.sendMessage(new TextComponent(FriendSystem.prefix + "§cDu bist bereits mit diesem Spieler befreundet!"));
                    } else {
                        if (MySQL.is("Requests", uuid.toString(), player.getUniqueId().toString())) {
                            MySQL.remove(player.getUniqueId().toString(), "Requests", uuid.toString());
                            MySQL.add(uuid.toString(), "Friends", player.getUniqueId().toString());
                            MySQL.add(player.getUniqueId().toString(), "Friends", uuid.toString());
                            player.sendMessage(new TextComponent(FriendSystem.prefix + "§7Du bist nun mit §6" + args[1] + " §7befreundet!"));
                            p1 = ProxyServer.getInstance().getPlayer(args[1]);
                            if (p1 != null) {
                                p1.sendMessage(new TextComponent(FriendSystem.prefix + "§6" + player.getDisplayName() + " §7hat deine Freundschaftsanfrage angenommen!"));
                            }
                        } else {
                            player.sendMessage(new TextComponent(FriendSystem.prefix + "§cDu hast keine Freundschaftsanfrage von diesem Spieler bekommen!"));
                        }
                    }
                }


            } else if (args.length == 2 && args[0].equalsIgnoreCase("deny")) {
                uuid = UUIDFetcher.getUUID(args[1]);
                if (uuid == null) {
                    player.sendMessage(notexist);
                } else {
                    if (MySQL.is("Requests", uuid.toString(), player.getUniqueId().toString())) {
                        MySQL.remove(player.getUniqueId().toString(), "Requests", uuid.toString());
                        player.sendMessage(new TextComponent(FriendSystem.prefix + "§7Du hast die Freundschaftsanfrage von §6" + args[1] + " §7abgelehnt!"));
                        p1 = ProxyServer.getInstance().getPlayer(args[1]);
                        if (p1 != null) {
                            p1.sendMessage(new TextComponent(FriendSystem.prefix + "§6" + player.getDisplayName() + " §7hat deine Freundschaftsanfrage abgelehnt!"));
                        }
                    } else {
                        player.sendMessage(new TextComponent(FriendSystem.prefix + "§cDu hast keine Freundschaftsanfrage von diesem Spieler bekommen!"));
                    }
                }
            } else if(args.length == 1 && args[0].equalsIgnoreCase("toggle")){
                MySQL.changeProperties(player.getUniqueId().toString(), "invites");
                player.sendMessage(MySQL.getProperties(player.getUniqueId().toString(), "invites") ? FriendSystem.prefix+"Du kannst nun §awieder §7Freundschaftsanfragen erhalten.":
                                                                                                     FriendSystem.prefix+"Du erhälst nun §ckeine §7Freundschaftsanfragen mehr.");
            } else if(args.length == 1 && args[0].equalsIgnoreCase("togglemsgs")){
                MySQL.changeProperties(player.getUniqueId().toString(), "msgs");
                player.sendMessage(MySQL.getProperties(player.getUniqueId().toString(), "msgs") ? FriendSystem.prefix+"Du erhälst nun §awieder §7private Nachrichten." :
                                                                                                  FriendSystem.prefix+"Du erhälst nun §ckeine §7privaten Nachrichten.");
            } else if(args.length == 1 && args[0].equalsIgnoreCase("togglejump")){
                MySQL.changeProperties(player.getUniqueId().toString(), "jump");
                player.sendMessage(MySQL.getProperties(player.getUniqueId().toString(), "jump") ? FriendSystem.prefix+"Dir kann nun §awieder §7nachgesprungen werden." :
                                                                                                  FriendSystem.prefix+"Dir kann nun §cnicht mehr §7nachgesprungen werden.");
            } else if(args.length == 1 && args[0].equalsIgnoreCase("togglenotifies")){
                MySQL.changeProperties(player.getUniqueId().toString(), "notifies");
                player.sendMessage(MySQL.getProperties(player.getUniqueId().toString(), "notifies") ? FriendSystem.prefix+"Du erhälst §awieder §7Join/Leave Benachrichtungen." :
                                                                                                      FriendSystem.prefix+"Du erhälst nun §ckeine §7Join/Leave Benachrichtigungen.");
            }
    }
}