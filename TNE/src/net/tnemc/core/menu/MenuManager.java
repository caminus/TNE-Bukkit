package net.tnemc.core.menu;

import net.tnemc.core.menu.impl.AmountSelectionMenu;
import net.tnemc.core.menu.impl.CurrencySelectionMenu;
import net.tnemc.core.menu.impl.DisplayMenu;
import net.tnemc.core.menu.impl.MainMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/5/2017.
 */
public class MenuManager {
  public Map<String, Menu> menus = new HashMap<>();
  public Map<UUID, ViewerData> data = new HashMap<>();

  private static ItemStack border;

  public MenuManager() {
    border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
    ItemMeta setMeta = border.getItemMeta();
    setMeta.setDisplayName(ChatColor.WHITE + "'member borders?");
    border.setItemMeta(setMeta);

    menus.put("main", new MainMenu());
    menus.put("display", new DisplayMenu());
    menus.put("cur_selection_give", new CurrencySelectionMenu("cur_selection_give", "give"));
    menus.put("cur_selection_pay", new CurrencySelectionMenu("cur_selection_pay", "pay"));
    menus.put("cur_selection_set", new CurrencySelectionMenu("cur_selection_set", "set"));
    menus.put("cur_selection_take", new CurrencySelectionMenu("cur_selection_take", "take"));
    menus.put("give", new AmountSelectionMenu("give"));
    menus.put("pay", new AmountSelectionMenu("pay"));
    menus.put("set", new AmountSelectionMenu("set"));
    menus.put("take", new AmountSelectionMenu("take"));
  }

  public void open(String menu, Player player) {
    if(menus.containsKey(menu)) {
      Inventory inventory = menus.get(menu).buildInventory(player);
      InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
      if(player.getOpenInventory().getTopInventory().getSize() == inventory.getSize() && holder instanceof MenuHolder) {
        player.getOpenInventory().getTopInventory().setContents(inventory.getContents());
        ((MenuHolder)holder).setMenu(menu);
      } else {
        player.openInventory(menus.get(menu).buildInventory(player));
      }
    }
  }

  public void removeData(UUID id) {
    data.remove(id);
  }

  public Object getViewerData(UUID viewer, String identifier) {
    if(data.containsKey(viewer)) {
      return data.get(viewer).getValue(identifier);
    }
    return null;
  }

  public void setViewerData(UUID viewer, String identifier, Object value) {
    if(!data.containsKey(viewer)) {
      data.put(viewer, new ViewerData(viewer));
    }
    data.get(viewer).setValue(identifier, value);
  }

  public static ItemStack getBorder() {
    return border;
  }
}