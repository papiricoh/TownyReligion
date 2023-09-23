package org.papiricoh.townyreligion.object.god;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class God {
    private String name;
    private String lore;
    private final PotionEffect potion_effect;
    private Material material;
    private Material block;

    public God(String name, PotionEffect potion_effect) {
        this.name = name;
        this.potion_effect = potion_effect;
    }

    public God(String name, PotionEffect potion_effect, Material material, Material block) {
        this.name = name;
        this.potion_effect = potion_effect;
        this.material = material;
        this.block = block;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getName() {
        return name;
    }


    public PotionEffect getPotion_effect_type() {
        return potion_effect;
    }

    public Material getMaterial() {
        return this.material;
    }

    public Material getBlock() {
        return block;
    }

    public void setBlock(Material block) {
        this.block = block;
    }
}