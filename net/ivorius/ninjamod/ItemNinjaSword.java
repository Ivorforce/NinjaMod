package net.ivorius.ninjamod;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemNinjaSword extends ItemSword
{
	public Icon invisibleIcon;

	public int invisibilityDelay = 160;
	public int teleportDelay = 40;
	public int aoeDelay = 80;

	public ItemNinjaSword(int par1, EnumToolMaterial par2EnumToolMaterial)
	{
		super(par1, par2EnumToolMaterial);
	}

	@Override
	public ItemStack onItemRightClick( ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer )
	{
		if (par3EntityPlayer.isSneaking())
		{
			if (!this.hasDelay(par1ItemStack, "invisibleDelay"))
			{
				this.setDelay(par1ItemStack, "invisibleDelay", invisibilityDelay);

				spawnSmoke(par3EntityPlayer, par2World, 100);
				if (!par2World.isRemote)
				{
					par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.invisibility.id, 100));
				}

				par1ItemStack.damageItem(5, par3EntityPlayer);

				if (!par2World.isRemote)
					par2World.playSoundAtEntity(par3EntityPlayer, "ninjamod:poof", 1.0f, 1.0f);
			}
		}
		else
		{
			if (!this.hasDelay(par1ItemStack, "teleportDelay"))
			{
				this.setDelay(par1ItemStack, "teleportDelay", teleportDelay);

				float distance = 5.0f;
				Vec3 look = par3EntityPlayer.getLookVec();

				spawnSmoke(par3EntityPlayer, par2World, 100);
				par3EntityPlayer.moveEntity(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
				spawnSmoke(par3EntityPlayer, par2World, 50);

				par1ItemStack.damageItem(3, par3EntityPlayer);

				if (!par2World.isRemote)
					par2World.playSoundAtEntity(par3EntityPlayer, "ninjamod:poof", 1.0f, 1.0f);
			}
		}

		return par1ItemStack;
	}

	private void spawnSmoke( Entity entity, World world, int number )
	{
		for (int i = 0; i < number; i++)
		{
			float xPlus = 0.0f;
			float yPlus = 0.0f;
			float zPlus = 0.0f;

			do
			{
				xPlus += (world.rand.nextFloat() - 0.5f) * entity.width * 1.5f;
				yPlus += (world.rand.nextFloat() - 0.5f) * entity.height;
				zPlus += (world.rand.nextFloat() - 0.5f) * entity.width * 1.5f;
			} while (world.rand.nextFloat() < 0.8f);

			world.spawnParticle(world.rand.nextFloat() < 0.3f ? "largesmoke" : "smoke", entity.posX + xPlus, entity.posY + yPlus, entity.posZ + zPlus, 0.0f, 0.0f, 0.0f);
		}
	}

	@Override
	public void onUpdate( ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5 )
	{
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);

		if (par1ItemStack.getTagCompound() != null && par3Entity instanceof EntityLivingBase)
		{
			NBTTagCompound compound = par1ItemStack.getTagCompound();

			this.decreaseDelay(par1ItemStack, "teleportDelay");
			this.decreaseDelay(par1ItemStack, "invisibleDelay");
			this.decreaseDelay(par1ItemStack, "aoeDelay");
			this.decreaseDelay(par1ItemStack, "aoeAttack");
			
			if (this.getDelay(par1ItemStack, "aoeAttack") == 1)
			{
				float range = 4.0f;
				AxisAlignedBB hitbox = par3Entity.boundingBox.expand(range, range * 0.3f, range);
				List hitEntities = par2World.getEntitiesWithinAABBExcludingEntity(par3Entity, hitbox);

				for (Object object : hitEntities)
				{
					Entity entity = (Entity) object;

					if (!par2World.isRemote)
						entity.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)par3Entity), 3.0f);
				}

				par1ItemStack.damageItem(4, (EntityLivingBase)par3Entity);

				int number = 150;
				for (int i = 0; i < number; i++)
				{
					float xPlus = 0.0f;
					float yPlus = 0.0f;
					float zPlus = 0.0f;

					do
					{
						xPlus += (par2World.rand.nextFloat() - 0.5f) * range;
						yPlus += (par2World.rand.nextFloat() - 0.5f) * range * 0.3f;
						zPlus += (par2World.rand.nextFloat() - 0.5f) * range;
					} while (par2World.rand.nextFloat() < 0.8f);

					par2World.spawnParticle(par2World.rand.nextFloat() < 0.3f ? "largesmoke" : "smoke", par3Entity.posX + xPlus, par3Entity.posY + yPlus, par3Entity.posZ + zPlus, 0.0f, 0.0f, 0.0f);
				}				
			}
		}

		if (par3Entity != null && par3Entity.isInvisible() && par3Entity instanceof EntityLivingBase)
		{
			par3Entity.getDataWatcher().updateObject(7, new Integer(0));
		}
	}

	public boolean hasDelay( ItemStack par1ItemStack, String delayName )
	{
		return this.getDelay(par1ItemStack, delayName) > 0;
	}

	public int getDelay( ItemStack par1ItemStack, String delayName )
	{
		NBTTagCompound compound = par1ItemStack.getTagCompound();

		if (compound != null && compound.hasKey(delayName))
		{
			int delay = compound.getInteger(delayName);

			return delay;
		}

		return 0;
	}

	public void setDelay( ItemStack par1ItemStack, String delayName, int number )
	{
		par1ItemStack.setTagInfo(delayName, new NBTTagInt(delayName, number));
	}

	public void decreaseDelay( ItemStack par1ItemStack, String delayName )
	{
		NBTTagCompound compound = par1ItemStack.getTagCompound();

		if (compound != null && compound.hasKey(delayName))
		{
			int delay = compound.getInteger(delayName);
			if (delay > 0)
				compound.setInteger(delayName, delay - 1);
		}
	}

	@Override
	public boolean hitEntity( ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase )
	{
		if (par3EntityLivingBase.isSneaking())
		{
			if (!this.hasDelay(par1ItemStack, "aoeDelay"))
			{
				World world = par2EntityLivingBase.worldObj;

				this.setDelay(par1ItemStack, "aoeDelay", aoeDelay);
				this.setDelay(par1ItemStack, "aoeAttack", 15);

				if (!world.isRemote)
					world.playSoundAtEntity(par3EntityLivingBase, "ninjamod:swordAoE", 1.0f, 1.0f);
			}
		}

		return super.hitEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
	}

	@Override
	public void addInformation( ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4 )
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);

		par3List.add(EnumChatFormatting.GRAY + String.format("Teleport Delay: %.1f seconds", teleportDelay / 20.0f));
		par3List.add(EnumChatFormatting.GRAY + String.format("Invisibility Delay: %.1f seconds", teleportDelay / 20.0f));
		par3List.add(EnumChatFormatting.GRAY + String.format("AoE Swing Delay: %.1f seconds", aoeDelay / 20.0f));
	}

	@Override
	public void registerIcons( IconRegister par1IconRegister )
	{
		super.registerIcons(par1IconRegister);

		invisibleIcon = par1IconRegister.registerIcon("ninjamod:ninjaSword_invisible");
	}

	@Override
	public Icon getIcon( ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining )
	{
		if (player.isInvisible())
			return invisibleIcon;

		return super.getIcon(stack, renderPass, player, usingItem, useRemaining);
	}
}
