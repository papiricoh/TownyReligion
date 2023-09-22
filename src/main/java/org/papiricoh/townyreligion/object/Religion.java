package org.papiricoh.townyreligion.object;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
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
    private Date time_established;
    private ArrayList<Town> towns;
    private Religion father_religion;
    private God main_god;
    private Chest altar;
    private boolean active_boost = false;
    private SacredBook sacredBook;


    public Religion(@NotNull String string_uuid, @NotNull String name, @NotNull Town foundingTown, @NotNull Date timeEstablished, ArrayList<Town> towns, Religion father_religion, @NotNull God mainGod, Block altar, SacredBook sacredBook) {
        this.uuid = UUID.fromString(string_uuid);
        this.name = name;
        this.founding_town = foundingTown;
        this.time_established = timeEstablished;
        this.towns = towns;
        this.father_religion = father_religion;
        this.main_god = mainGod;
        this.altar = this.configAltarBlock(altar);
        this.sacredBook = sacredBook;
    }


    public Religion(@NotNull String name, @NotNull Town founding_town, Religion father_religion, @NotNull God god) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.founding_town = founding_town;
        this.time_established = new Date();
        this.father_religion = father_religion;
        this.main_god = god;
        this.altar = null;
        this.sacredBook = null;
    }


    private Chest configAltarBlock(Block altar) {
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

    public SacredBook getSacredBook() {
        return sacredBook;
    }

    public void setSacredBook(SacredBook sacredBook) {
        this.sacredBook = sacredBook;
    }

    public void createSacredBook(String title, String content) {
        this.sacredBook = new SacredBook(title, content, this.main_god.getName());
    }

    public Religion getFather_religion() {
        return father_religion;
    }

    public String getName() {
        return name;
    }

    public Date getTime_established() {
        return time_established;
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