package flabs.mods.magitech.api;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.minecraft.item.ItemStack;

public class MagiRecipes {
	private static List<Map.Entry<ItemStack, Integer>> recipes = new Vector<Map.Entry<ItemStack, Integer>>();
	/**
	 * Get's the Magi for the Item, or 0 if the Item is not registered
	 * @param item - ignores Stacksize
	 * @return
	 */
	public static int getMagiForItem(ItemStack item) {
		if (item == null) {
			return 0;
		}

		for (Map.Entry<ItemStack, Integer> entry : recipes) {
			if (entry.getKey().isItemEqual(item)) {

				return entry.getValue();
			}
		}
		return 0;
	}
	/**
	 * Get's the Magi for the Item, or 0 if the Item is not registered
	 * <li> multiplied by the stacksize of the itemstack
	 * @return
	 */
	public static int getMagiForItemStack(ItemStack item) {
		return getMagiForItem(item)*item.stackSize;
	}
	/**
	 * Set's the Magi you get from throwing the item into the Crucible
	 * 
	 * @param magi
	 *            <li>Iron = 8 Magi
	 *            <li>Gold = 64 Magi
	 *            <li>Diamond = 256 Magi
	 *            <li>a Crucible holds 1024 Magi max, thus limiting the maximum amount.
	 * 
	 */
	public static void setMagi(ItemStack item, int magi) {
		recipes.add(new AbstractMap.SimpleEntry<ItemStack, Integer>(item, magi));
	}

	public static void init() {

	}
}
