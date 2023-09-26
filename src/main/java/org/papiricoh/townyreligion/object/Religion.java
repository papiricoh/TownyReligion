package org.papiricoh.townyreligion.object;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.papiricoh.townyreligion.object.god.God;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Religion {
    private UUID uuid;
    private String name;
    private Town founding_town;
    private ArrayList<Town> towns;
    private God main_god;
    private Block altar;
    private boolean active_boost = false;


    public Religion(@NotNull UUID uuid, @NotNull String name, @NotNull UUID foundingTown, ArrayList<Town> towns, @NotNull God mainGod, Block altar) {
        this.uuid = uuid;
        this.name = name;
        this.founding_town = TownyUniverse.getInstance().getTown(foundingTown);
        if(towns == null) {
            this.towns = new ArrayList<>();
        }else {
            this.towns = towns;
        }
        this.main_god = mainGod;
        this.altar = this.configAltarBlock(altar);

    }


    public Religion(@NotNull String name, @NotNull Town founding_town, @NotNull God god) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.founding_town = founding_town;
        this.main_god = god;
        this.altar = null;
        this.towns = new ArrayList<>();
    }


    public Block configAltarBlock(Block block) {
        if (block == null) {
            return null;
        }

        if(block.getType().equals(Material.CHEST)) {
            return block;
        }
        return null;
    }

    public void setAltar(Block chest) {
        this.altar = chest;
    }

    public void setGod(God god) {
        this.main_god = god;
    }

    public boolean offerToGod() {
        if(this.altar == null || this.main_god == null) {
            this.active_boost = false;
            return false;
        }
        if(this.altar.getType() != Material.CHEST) {
            this.altar = null;
            this.active_boost = false;
            return false;
        }
        Inventory altar_inventory = ((Chest) this.altar.getState()).getBlockInventory();
        if(altar_inventory == null) {
            this.altar = null;
            this.active_boost = false;
            return false;
        }

        if(altar_inventory.contains(this.main_god.getMaterial()) && altar_inventory.getItem(altar_inventory.first(main_god.getMaterial())).getAmount() >= 4) {
            altar_inventory.removeItem(new ItemStack(main_god.getMaterial(), 2));
            this.active_boost = true;
            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            for (Player player : onlinePlayers) {
                try {
                    Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());

                    if (resident.hasTown() && this.containsTown(resident.getTown())) {
                        Town playerTown = resident.getTown();
                        TownBlock townBlock = TownyUniverse.getInstance().getTownBlock(WorldCoord.parseWorldCoord(player.getLocation()));
                        Town blockTown = townBlock.getTown();

                        if(playerTown.equals(blockTown)) {
                            player.addPotionEffect(this.main_god.getPotion_effect());
                        }
                    }
                } catch (NotRegisteredException e) {

                }
            }
            return true;
        }
        this.active_boost = false;
        return false;
    }

    public boolean containsTown(Town town) {
        return this.towns.contains(town) || this.founding_town.equals(town);
    }
    public Town getFoundingTown() {
        return founding_town;
    }

    public boolean addTown(Town town) {
        if(!containsTown(town)) {
            this.towns.add(town);
            return true;
        }
        return false;
    }

    public boolean removeTown(Town town) {
        if(town.equals(this.founding_town)) {
            if(this.towns.size() > 0) {
                Town newFoundingTown = this.towns.get(0);
                this.founding_town = newFoundingTown;
                this.towns.remove(newFoundingTown);
                return true;
            }else {
                this.founding_town = null;
                return false;//RETURNS FALSE TO DELETE RELIGION
            }
        }else {
            this.towns.remove(town);
            return true;
        }
    }

    public God getMain_god() {
        return this.main_god;
    }

    public String getName() {
        return name;
    }


    public Block getAltar() {
        return this.altar;
    }

    public boolean isActive_boost() {
        return active_boost;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ArrayList<Town> getTowns() {
        return this.towns;
    }
    public boolean isFoundingTown(Town town) {
        return this.founding_town.equals(town);
    }
}