package net.ivorius.ninjamod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemNinjaArmor extends ItemArmor
{
	public ItemNinjaArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4)
	{
		super(par1, par2EnumArmorMaterial, par3, par4);
	}

	@Override
	public String getArmorTexture( ItemStack stack, Entity entity, int slot, String type )
	{
		if (entity.isInvisible())
		{
			return "ninjamod:models/ninjaArmor_invisible.png";
		}

		return "ninjamod:models/ninjaArmor_" + (slot == 2 ? 2 : 1) + ".png";
	}
}
