package org.papiricoh.townyreligion.object;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.papiricoh.townyreligion.object.book.SacredBook;
import org.papiricoh.townyreligion.object.god.God;

import java.util.ArrayList;
import java.util.Date;

public class Religion {
    private String name;
    private Town founding_town;
    private Date time_established;
    private ArrayList<Town> towns;
    private Religion father_religion;
    private God main_god;
    private Chest altar;
    private boolean active_boost = false;

    private SacredBook sacredBook;


    public Religion(String name, Town foundingTown, Date timeEstablished, ArrayList<Town> towns, Religion father_religion, God mainGod, Block altar, SacredBook sacredBook) {
        this.name = name;
        this.founding_town = foundingTown;
        this.time_established = timeEstablished;
        this.towns = towns;
        this.father_religion = father_religion;
        this.main_god = mainGod;
        this.altar = this.configAltarBlock(altar);
        this.sacredBook = sacredBook;
    }


    public Religion(String name, Town founding_town, Religion father_religion, God god) {
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

    public boolean addTown(Town town) {
        if(!containsTown(town)) {
            this.towns.add(town);
            return true;
        }
        return false;
    }

    public void removeTown(Town town) {
        this.towns.remove(town);
    }

    public SacredBook getSacredBook() {
        return sacredBook;
    }

    public void setSacredBook(SacredBook sacredBook) {
        this.sacredBook = sacredBook;
    }

    public void createSacredBook() {

    }
}