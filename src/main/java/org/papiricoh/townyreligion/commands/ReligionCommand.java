package org.papiricoh.townyreligion.commands;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.papiricoh.townyreligion.TownyReligion;
import org.papiricoh.townyreligion.object.Religion;
import org.papiricoh.townyreligion.object.god.God;
import org.papiricoh.townyreligion.parser.ReligionParser;

import java.util.ArrayList;
import java.util.List;

public class ReligionCommand implements CommandExecutor, TabCompleter {
    private TownyReligion townyReligion;

    public ReligionCommand(TownyReligion townyReligion) {
        this.townyReligion = townyReligion;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is only player command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Use /tr help to display available commands.");
            return true;
        }


        if ("gods".equalsIgnoreCase(args[0])) {
            for (God g: TownyReligion.gods) {
                player.sendMessage("God: " + ChatColor.DARK_RED + g.getName() + ChatColor.WHITE + " Effect: " + ChatColor.AQUA + g.getPotion_effect().getType().toString());
            }
            return true;
        }

        if ("changeGod".equalsIgnoreCase(args[0])) {
            Religion religion = TownyReligion.getReligion(player);

            if(religion == null) {
                player.sendMessage("You don't have a religion.");
                return true;
            }
            boolean isMayor = TownyUniverse.getInstance().getResident(player.getUniqueId()).isMayor();
            if(!isMayor) {
                player.sendMessage("You are not the mayor.");
                return true;
            }

            try {
                Town town = TownyUniverse.getInstance().getResident(player.getUniqueId()).getTown();
                if(!religion.isFoundingTown(town)) {
                    player.sendMessage("Your town isn't the founding town of the religion.");
                    return true;
                }

                String godName = args[1];
                God god = getGod(godName);
                if(god == null) {
                    player.sendMessage("God: " + godName + " doesn't exist");
                    return true;
                }
                religion.setGod(god);
                player.sendMessage("Changed God to: " + godName + " in " + religion.getName());
                return true;
            } catch (NotRegisteredException e) {
                player.sendMessage("Internal error: Town does not exists.");
                return true;
            }

        }

        if ("setAltar".equalsIgnoreCase(args[0])) {
            Religion religion = TownyReligion.getReligion(player);

            if(religion == null) {
                player.sendMessage("You don't have a religion.");
                return true;
            }
            boolean isMayor = TownyUniverse.getInstance().getResident(player.getUniqueId()).isMayor();
            if(!isMayor) {
                player.sendMessage("You are not the mayor.");
                return true;
            }

            try {
                Town town = TownyUniverse.getInstance().getResident(player.getUniqueId()).getTown();
                if(!religion.isFoundingTown(town)) {
                    player.sendMessage("Your town isn't the founding town of the religion.");
                    return true;
                }

                Block targetBlock = player.getTargetBlock(null, 30); // The second parameter is the maximum distance to check.
                if (targetBlock != null) {
                    if(targetBlock.getType().equals(Material.CHEST)) {
                        Block blockBelow = targetBlock.getRelative(BlockFace.DOWN);
                        if(!blockBelow.getType().equals(religion.getMain_god().getBlock())) {
                            player.sendMessage("Block below needs to be " + religion.getMain_god().getBlock().toString());
                            return true;
                        }
                        religion.setAltar(targetBlock);
                        player.sendMessage("Altar block set for " + religion.getName());
                        return true;

                    }else {
                        player.sendMessage("Block needs to be a chest");
                        return true;
                    }

                } else {
                    player.sendMessage("No block in sight!");
                    return true;
                }

            } catch (NotRegisteredException e) {
                player.sendMessage("Internal error: Town does not exists.");
                return true;
            }
        }

        if ("info".equalsIgnoreCase(args[0])) {
            Religion religion = TownyReligion.getReligion(player);

            if(religion == null) {
                player.sendMessage("You don't have a religion.");
                return true;
            }

            player.sendMessage("Religion: " + ChatColor.GREEN + religion.getName() + ChatColor.WHITE + " God: " + ChatColor.GOLD + religion.getMain_god().getName() + ChatColor.WHITE + " Number of towns: " + ChatColor.DARK_RED + (religion.getTowns().size() + 1));
            return true;
        }

        if ("leave".equalsIgnoreCase(args[0])) {
            Religion religion = TownyReligion.getReligion(player);

            if(religion == null) {
                player.sendMessage("You don't have a religion.");
                return true;
            }
            boolean isMayor = TownyUniverse.getInstance().getResident(player.getUniqueId()).isMayor();
            if (isMayor) {
                try {
                    Town town = TownyUniverse.getInstance().getResident(player.getUniqueId()).getTown();
                    boolean religionIsNotEmpty = religion.removeTown(town);
                    if (!religionIsNotEmpty) {
                        TownyReligion.religions.remove(religion);
                        ReligionParser.deleteReligionFile(religion.getUuid() + ".txt", townyReligion);
                    }
                    player.sendMessage("Leaved religion " + religion.getName() + ".");
                    return true;
                } catch (NotRegisteredException e) {
                    player.sendMessage("Internal error: Town does not exists.");
                    return true;
                }
            }else {
                player.sendMessage("You are not the mayor.");
                return true;
            }

        }

        if ("adopt".equalsIgnoreCase(args[0])) {
            if (args.length < 2) {
                player.sendMessage("Please, select religion to adopt.");
                return true;
            }

            String religionName = args[1];
            if(TownyReligion.getReligion(player) != null) {
                player.sendMessage("You have already a religion.");
                return true;
            }
            boolean isMayor = TownyUniverse.getInstance().getResident(player.getUniqueId()).isMayor();
            if (isMayor) {
                try {
                    Religion religion = null;
                    for (Religion r: TownyReligion.religions) {
                        if(r.getName().equals(religionName)) {
                            religion = r;
                        }
                    }
                    if (religion == null) {
                        player.sendMessage("The religion does not exists.");
                        return true;
                    }
                    religion.addTown(TownyUniverse.getInstance().getResident(player.getUniqueId()).getTown());
                    player.sendMessage("Religion " + religionName + " adopted!");
                    return true;
                } catch (NotRegisteredException e) {
                    player.sendMessage("You don't have a town.");
                    return true;
                }
            }else {
                player.sendMessage("You are not the mayor.");
                return true;
            }

        }

        if ("create".equalsIgnoreCase(args[0])) {
            if (args.length < 2) {
                player.sendMessage("Please, set god and religion name.");
                return true;
            }
            String godName = args[1];
            God god = getGod(godName);
            if(god == null) {
                player.sendMessage("This god doesn't exists.");
                return true;
            }

            String religionName = args[2];
            if(TownyReligion.getReligion(player) != null) {
                player.sendMessage("You don't have permissions or you have already a religion.");
                return true;
            }
            boolean isMayor = TownyUniverse.getInstance().getResident(player.getUniqueId()).isMayor();
            if (isMayor) {
                try {
                    Religion religion = new Religion(religionName, TownyUniverse.getInstance().getResident(player.getUniqueId()).getTown(), god);
                    TownyReligion.religions.add(religion);
                    player.sendMessage("Religion " + religionName + " created!");
                    return true;
                } catch (NotRegisteredException e) {
                    player.sendMessage("You don't have a town.");
                    return true;
                }
            }else {
                player.sendMessage("You are not the mayor.");
                return true;
            }

        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("create");
            suggestions.add("adopt");
            suggestions.add("leave");
            suggestions.add("info");
            suggestions.add("setAltar");
            suggestions.add("changeGod");
            suggestions.add("gods");

        } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            for (God g : TownyReligion.gods) {
                suggestions.add(g.getName());
            }
        }else if (args.length == 2 && args[0].equalsIgnoreCase("changeGod")) {
            for (God g : TownyReligion.gods) {
                suggestions.add(g.getName());
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("adopt")) {
            for (Religion r : TownyReligion.religions) {
                suggestions.add(r.getName());
            }
        }

        return suggestions;
    }

    private God getGod(String godName) {
        for (God g : TownyReligion.gods) {
            if(g.getName().equals(godName)) {
                return g;
            }
        }
        return null;
    }

}
