package flabs.mods.magitech.item;

import flabs.mods.magitech.Magitech;
import flabs.mods.magitech.Version;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Wand extends Item {

	public Wand(int par1) {
		super(par1);
		this.setContainerItem(this);
		this.setMaxDamage(128);
	}

    @Override
    public void registerIcons(IconRegister ir) {
        this.itemIcon=ir.registerIcon(Version.unlocalizedWand);
    }
	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack) {
		ItemStack ret = itemStack.copy();
		if (ret.stackSize <= 0) {
			ret.stackSize = 1;
		}
		Magitech.log(ret);
		ret.setItemDamage(ret.getItemDamage() + 1);
		if (ret.getItemDamage() >= ret.getMaxDamage()) {
			return null;
		}
		return ret;
	}
}
