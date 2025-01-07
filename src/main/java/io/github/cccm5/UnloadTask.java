package io.github.cccm5;

import com.degitise.minevid.dtlTraders.guis.items.TradableGUIItem;
import net.countercraft.movecraft.craft.PlayerCraft;

import net.countercraft.movecraft.craft.type.CraftType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class UnloadTask extends CargoTask {
    public UnloadTask(PlayerCraft craft, TradableGUIItem item) {
        super(craft, item);
    }

    public void execute() {
        List<Inventory> invs = Utils.getInventories(craft, item.getMainItem(), Material.CHEST, Material.TRAPPED_CHEST);
        if (!CargoMain.isIsPre1_13()) {
            invs.addAll(Utils.getInventories(craft, item.getMainItem(), Material.BARREL));
        }
        Inventory inv = invs.get(0);
        int count = 0;
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null && inv.getItem(i).isSimilar(item.getMainItem())) {
                count += inv.getItem(i).getAmount();
                inv.setItem(i, null);
            }
        }
        originalPilot.sendMessage(CargoMain.SUCCESS_TAG + "Unloaded " + count + " worth $"
                + String.format("%.2f", count * item.getTradePrice()) + " took a tax of "
                + String.format("%.2f", CargoMain.getUnloadTax() * count * item.getTradePrice()));
        CargoMain.getEconomy().depositPlayer(originalPilot,
                count * item.getTradePrice() * (1 - CargoMain.getUnloadTax()));

        if (invs.size() <= 1) {
            this.cancel();
            CargoMain.getQue().remove(originalPilot);
            originalPilot.sendMessage(CargoMain.SUCCESS_TAG + "All cargo unloaded");
            Bukkit.broadcastMessage(ChatColor.GOLD + craft.getPilot().getName() + " has unloaded cargo worth $" + String.format("%.2f", count * item.getTradePrice()) + " from a " + craft.getType().getStringProperty(CraftType.NAME) + " at X: " + craft.getPilot().getLocation().getBlockX() + ", Y: " + craft.getPilot().getLocation().getBlockY() + ", Z: " + craft.getPilot().getLocation().getBlockZ() + ".");
            craft.getPilot().playSound(craft.getPilot().getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1, 1);
            return;
        }
        new ProcessingTask(originalPilot, item, invs.size()).runTaskTimer(CargoMain.getInstance(), 0, 20);
    }
}
