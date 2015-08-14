/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 onbjerg
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author onbjerg
 */
package io.github.onbjerg.itembuilder;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This is a chainable builder for @{link ItemStack}s.
 *
 * @author Bitterbjerg
 * @version 1.0
 */
public class ItemBuilder implements Listener {

    private final ItemStack i;
    private ItemBuilder parent = null;
    private static Plugin plugin;
    private static boolean listener;
    private final static HashMap<String, List<PotionEffect>> effects = new HashMap<>();

    /**
     * Set the plugin the item builder should use to register it's internal listener
     * @param plugin The plugin to use
     */
    public static void setPlugin(Plugin plugin) {
        ItemBuilder.plugin = plugin;
    }

    /**
     * Create a new item builder with a given {@link ItemBuilder} parent and {@link Material}.
     * @param parent The parent of this item builder
     * @param material The material to start the builder from
     */
    private ItemBuilder(final ItemBuilder parent, final Material material) {
        this.parent = parent;
        i = new ItemStack(material);
    }

    /**
     * Create a new item builder with the given {@link Material}.
     * @param material The material to start the builder from
     */
    public ItemBuilder(final Material material) {
        i = new ItemStack(material);
    }

    /**
     * Create a new item builder with the given {@link ItemStack}.
     * @param is The item stack to start the builder from
     */
    public ItemBuilder(final ItemStack is) {
        i = is;
    }

    /**
     * Changes the amount of the {@link ItemStack}.
     * @param amount The new amount
     * @return The builder for chaining
     */
    public ItemBuilder amount(final int amount) {
        i.setAmount(amount);

        return this;
    }

    /**
     * Set the name of the {@link ItemStack}
     * @param name The name of the item stack
     * @return The builder for chaining
     */
    public ItemBuilder name(final String name) {
        final ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        i.setItemMeta(meta);

        return this;
    }

    /**
     * Add a line of lore to the {@link ItemStack}
     * @param line The line of lore to add
     * @return The builder for chaining
     */
    public ItemBuilder lore(final String line) {
        final ItemMeta meta = i.getItemMeta();

        List<String> lore = meta.getLore();
        if(lore == null) lore = new ArrayList<>();
        lore.add(line);

        meta.setLore(lore);
        i.setItemMeta(meta);

        return this;
    }

    /**
     * Set the durability of the {@link ItemStack}
     * @param durability The new durability of the item stack
     * @return The builder for chaining
     */
    public ItemBuilder durability(final short durability) {
        i.setDurability(durability);

        return this;
    }

    /**
     * Set the data for the {@link ItemStack}
     * @param data The byte of data
     * @return The builder for chaining
     */
    @SuppressWarnings("deprecated")
    public ItemBuilder data(final int data) {
        i.setData(new MaterialData(i.getType(), (byte) data));

        return this;
    }

    /**
     * Add an enchantment to the {@link ItemStack}
     * @param enchantment The enchantment to add
     * @param level The level of the enchantment
     * @return The builder for chaining
     */
    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        i.addUnsafeEnchantment(enchantment, level);

        return this;
    }

    /**
     * Add an enchantment to the {@link ItemStack} with a level of 1.
     * @param enchantment The enchantment to add
     * @return The builder for chaining
     */
    public ItemBuilder enchantment(final Enchantment enchantment) {
        return enchantment(enchantment, 1);
    }

    /**
     * Set the material of the {@link ItemStack}
     * @param material The new material
     * @return The builder for chaining
     */
    public ItemBuilder type(final Material material) {
        i.setType(material);

        return this;
    }

    /**
     * Clear all enchantments from the {@link ItemStack}
     * @return The builder for chaining
     */
    public ItemBuilder clearEnchantments() {
        for(final Enchantment e : i.getEnchantments().keySet()) {
            i.removeEnchantment(e);
        }

        return this;
    }

    /**
     * Set the color of the {@link ItemStack} if it is a type of leather armor.
     * @param color The new color of the armor
     * @return The builder for chaining
     */
    public ItemBuilder color(Color color) {
        if(i.getType() == Material.LEATHER_BOOTS ||
                i.getType() == Material.LEATHER_CHESTPLATE ||
                i.getType() == Material.LEATHER_LEGGINGS ||
                i.getType() == Material.LEATHER_BOOTS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
            meta.setColor(color);
            i.setItemMeta(meta);

            return this;
        }

        throw new IllegalArgumentException("Color is only applicable for leather armor!");
    }

    /**
     * Set the color of the {@link ItemStack} if it is wool.
     * @param color The new color of the wool
     * @return The builder for chaining
     */
    public ItemBuilder color(DyeColor color) {
        if(i.getType() == Material.WOOL) {
            data(color.getData());

            return this;
        }

        throw new IllegalArgumentException("Dye color is only applicable for wool!");
    }

    /**
     * Add an effect to the {@link ItemStack} upon consumption. Requires that you register the plugin
     * the item builder should use using {@link #setPlugin(Plugin)}.
     * @param type The type of the effect
     * @param duration The duration of the effect
     * @param amplifier The amplifier of the effect
     * @param ambient Whether or not the particles of the effect should be visible
     * @return The builder for chaining
     */
    public ItemBuilder effect(PotionEffectType type, int duration, int amplifier, boolean ambient) {
        return effect(new PotionEffect(type, duration, amplifier, ambient));
    }

    /**
     * Add an effect to the {@link ItemStack} upon consumption. Requires that you register the plugin
     * the item builder should use using {@link #setPlugin(Plugin)}.
     * @param type The type of the effect
     * @param duration The duration of the effect. Set it to -1 if it should be endless.
     * @param amplifier The amplifier of the effect
     * @return The builder for chaining
     */
    public ItemBuilder effect(PotionEffectType type, int duration, int amplifier) {
        return effect(new PotionEffect(type, (duration == -1) ? 1000000 : duration, amplifier));
    }

    /**
     * Add an effect to the {@link ItemStack} upon consumption. Requires that you register the plugin
     * the item builder should use using {@link #setPlugin(Plugin)}.
     * @param type The type of the effect
     * @param duration The duration of the effect. Set it to -1 if it should be endless.
     * @return The builder for chaining
     */
    public ItemBuilder effect(PotionEffectType type, int duration) {
        return effect(new PotionEffect(type, (duration == -1) ? 1000000 : duration, 1));
    }

    /**
     * Add an effect to the {@link ItemStack} upon consumption. Requires that you register the plugin
     * the item builder should use using {@link #setPlugin(Plugin)}.
     * @param effect The potion effect to add
     * @return The builder for chaining
     */
    public ItemBuilder effect(PotionEffect effect) {
        if(plugin == null) {
            throw new IllegalArgumentException("You can not use potion effects without setting the item builder's plugin");
        }
        if(!listener) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
            listener = true;
        }

        String name = i.getItemMeta().getDisplayName();
        if(!effects.containsKey(name)) {
            effects.put(name, new ArrayList<PotionEffect>());
        }
        effects.get(name).add(effect);

        return this;
    }

    /**
     * Get a new item builder with this one as a parent.
     * @return The new item builder with this one as a parent.
     */
    public ItemBuilder and(final Material material) {
        return new ItemBuilder(this, material);
    }

    /**
     * Get the item builder's parent.
     * @return The parent of this item builder
     */
    public ItemBuilder parent() {
        return parent;
    }

    /**
     * Get the built {@link ItemStack}
     * @return The item stack that was built.
     */
    public ItemStack build() {
        return i;
    }

    /**
     * Get all of the built {@link ItemStack}s. Iterates through all item builders this builder has to
     * retrieve a list of {@link ItemStack}s.
     * @return The item stacks that were built.
     */
    public List<ItemStack> get() {
        final List<ItemStack> items = new ArrayList<>();

        ItemBuilder current = this;
        do {
            items.add(current.build());
            current = current.parent();
        } while(current != null);

        return items;
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if(event.getItem().hasItemMeta()) {
            String name = event.getItem().getItemMeta().getDisplayName();

            if(!effects.containsKey(name)) return;

            for (PotionEffect potionEffect : effects.get(name)) {
                event.getPlayer().addPotionEffect(potionEffect);
            }
        }
    }

}
