package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.command.CommandManager;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.utils.AMPlayer;
import com.lordralex.antimulti.utils.PlayerList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

/**
 *
 * @version 1.0
 * @author icelord871
 * @since 1.2
 */
public class LoginSystem extends CommandManager implements Listener {

    PlayerList<AMPlayer> players = new PlayerList<AMPlayer>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        String cmdName = cmd.getName().toLowerCase();
        if (!(sender instanceof Player)) {
            return true;
        }
        if (cmdName.equals("login")) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "The correct usage is /login <pw>");
                return true;
            }
            AMPlayer user = findPlayer(sender.getName());
            if (user == null || user.isLoggedIn()) {
                sender.sendMessage(ChatColor.RED + "You do not have a reason to log in");
                return true;
            }
            if (user.logIn(args[0])) {
                sender.sendMessage(ChatColor.GREEN + "You have logged in");
                players.remove(user);
            } else {
                sender.sendMessage(ChatColor.RED + "Incorrect password");
            }
            return true;
        }
        if (cmdName.equals("register")) {
            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED + "The correct usage is /register <pw> <pw>");
                return true;
            }
            if (!args[0].equals(args[1])) {
                sender.sendMessage(ChatColor.RED + "Your passwords do not match");
                return true;
            }
            AMPlayer user = findPlayer(sender.getName());
            if (user == null) {
                user = new AMPlayer((Player) sender);
            }
            if (user.changePW("None", args[0])) {
                sender.sendMessage(ChatColor.GREEN + "You have changed your password to " + ChatColor.BLUE + args[0]);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Could not change your password");
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "login,register";
    }

    @Override
    public String getHelp() {
        return "/login <password>, /register <password> <password>";
    }

    public boolean requireLogin(Player player, AMPlayer user) {
        if (Configuration.fakeOnline()) {
            return true;
        }
        if (Configuration.useLoginProtection()) {
            if (Configuration.requirePlayerLogin()) {
                return true;
            }
            if (Configuration.requireAdminLogin() && checkPerm(player, "antimulti.admin")) {
                return true;
            }
        }
        if (user.registered()) {
            return true;
        }
        return false;
    }

    private AMPlayer findPlayer(String name) {
        return players.get(name);
    }

    private AMPlayer findPlayer(Player player) {
        return players.get(player.getName());
    }

    @Override
    public void reload() {
        for (int i = 0; i < players.size(); i++) {
            AMPlayer player = players.get(i);
            if (player.isLoggedIn()) {
                players.remove(i);
                i--;
            }
        }
    }

    @Override
    public void disable() {
        players.clear();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event) {
        int maxMoveDistance = Configuration.getMoveBuffer();
        AMPlayer amplayer = findPlayer(event.getPlayer());
        if (amplayer == null || amplayer.isLoggedIn()) {
            players.remove(amplayer);
            return;
        }
        Location login = amplayer.getLoginLocation();
        Location newLocation = event.getTo();
        double distance = login.distanceSquared(newLocation);
        if (distance > Math.pow(maxMoveDistance, 2)) {
            event.getPlayer().teleport(login);
            if (amplayer.registered()) {
                event.getPlayer().sendMessage(ChatColor.RED + "Please login with /login <pw>");
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "Please register with /register <pw> <pw>");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }
        AMPlayer amplayer = new AMPlayer(event.getPlayer());
        if (Configuration.fakeOnline()) {
            if (!amplayer.registered()) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "You are not registered");
                return;
            }
        } else if (!requireLogin(event.getPlayer(), amplayer)) {
            amplayer.logIn();
        } else {
            players.add(amplayer);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerKickEvent event) {
        AMPlayer amplayer = findPlayer(event.getPlayer());
        if (amplayer == null || amplayer.isLoggedIn()) {
            players.remove(amplayer);
            return;
        }
        if (!amplayer.isLoggedIn()) {
            event.getPlayer().teleport(amplayer.getLoginLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        AMPlayer amplayer = findPlayer(event.getPlayer());
        if (amplayer == null || amplayer.isLoggedIn()) {
            players.remove(amplayer);
            return;
        }
        if (!amplayer.isLoggedIn()) {
            event.getPlayer().teleport(amplayer.getLoginLocation());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (player != null) {
                players.remove(player);
            }
            return;
        }
        Player person = event.getPlayer();
        player.setLoginLocation(person.getLocation());
        if (player.registered()) {
            person.sendMessage("Please use /login <pw>");
        } else if (requireLogin(person, player)) {
            person.sendMessage("Please use /register <pw> <pw>");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInvOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            AMPlayer player = findPlayer((Player) event.getPlayer());
            if (player == null || player.isLoggedIn()) {
                if (players.contains(player)) {
                    int index = players.find(player);
                    players.remove(index);
                }
            } else {
                event.setCancelled(true);
                ((Player) event.getPlayer()).sendMessage(ChatColor.RED + "Please login or register first");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBedEnter(PlayerBedEnterEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBucketFill(PlayerBucketFillEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
                return;
            }
        }
        String cmd = event.getMessage();
        if (!cmd.startsWith("/login") && !cmd.startsWith("/register")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDrop(PlayerDropItemEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerFish(PlayerFishEvent event) {
        AMPlayer player = findPlayer(event.getPlayer());
        if (player == null || player.isLoggedIn()) {
            if (players.contains(player)) {
                int index = players.find(player);
                players.remove(index);
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please login or register first");
        }
    }
}