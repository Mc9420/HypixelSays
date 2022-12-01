package me.misoryan.hypixelsays.util;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @Author Misoryan
 * @Date 2022/11/30 20:11
 */
public class ItemUtil {

    public static ItemStack changeNbt(ItemStack is,String key, String value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }
        extra.setString(key, value);
        tag.set("extra", extra);

        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static String getItemStringData(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return null;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return null;
        }

        if (!extra.hasKey(key)) {
            return null;
        }

        return extra.getString(key);
    }
}
