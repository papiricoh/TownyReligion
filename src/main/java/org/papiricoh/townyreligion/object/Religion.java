package org.papiricoh.townyreligion.object;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.papiricoh.townyreligion.object.book.SacredBook;
import org.papiricoh.townyreligion.object.god.God;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Religion {
    private UUID uuid;
    private String name;
    private Town founding_town;
    private ArrayList<Town> towns;
    private God main_god;
    private Chest altar;
    private boolean active_boost = false;


    public Religion(@NotNull UUID uuid, @NotNull String name, @NotNull Town foundingTown, ArrayList<Town> towns, @NotNull God mainGod, Block altar) {
        this.uuid = uuid;
        this.name = name;
        this.founding_town = foundingTown;
        this.towns = towns;
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


    public Chest configAltarBlock(Block altar) {
        Block block = new Location(altar.getWorld(), altar.getX(), altar.getY(), altar.getZ()).getBlock();

        if(block.getType().equals(Material.CHEST)) {
            Chest chest = (Chest) block;
            return chest;
        }
        return null;
    }

    public void setGod(God god) {
        this.main_god = god;
    }

    public boolean offerToGod() {
        if(this.altar == null || this.main_god == null) {
            this.active_boost = false;
            return false;
        }
        Inventory altar_inventory = this.altar.getBlockInventory();
        if(altar_inventory.contains(this.main_god.getMaterial()) || altar_inventory.getItem(altar_inventory.first(main_god.getMaterial())).getAmount() >= 4) {
            altar_inventory.remove(new ItemStack(main_god.getMaterial(), 4));
            this.active_boost = true;
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

    public void removeTown(Town town) {
        if(town.equals(this.founding_town)) {
            if(this.towns.size() > 0) {
                Town newFoundingTown = this.towns.get(0);
                this.founding_town = newFoundingTown;
                this.towns.remove(newFoundingTown);
            }else {
                this.founding_town = null;//TODO DELETE RELIGION
            }
        }else {
            this.towns.remove(town);
        }
    }

    public God getMain_god() {
        return this.main_god;
    }

    public String getName() {
        return name;
    }


    public Chest getAltar() {
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
}