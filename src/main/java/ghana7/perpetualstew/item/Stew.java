package ghana7.perpetualstew.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class Stew extends Item {
    public Stew() {
        super(new Item.Properties());
    }

    public int getHunger(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if(nbt.contains("perpetualstew_hunger")) {
            return nbt.getInt("perpetualstew_hunger");
        }
        return 0;
    }

    public float getSaturation(ItemStack stack) {
        return getHunger(stack) * getSaturationMod(stack);
    }
    public float getSaturationMod(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if(nbt.contains("perpetualstew_saturationMod")) {
            return nbt.getFloat("perpetualstew_saturationMod");
        }
        return 0;
    }

    public String getNourishmentDescriptor(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if(nbt.contains("perpetualstew_saturationMod")) {
            float perpetualstew_saturationMod = nbt.getFloat("perpetualstew_saturationMod");
            if (perpetualstew_saturationMod == 2.4f) {
                return "Supernatural";
            } else if (perpetualstew_saturationMod == 1.6f) {
                return "Good";
            } else if (perpetualstew_saturationMod == 1.2f) {
                return "Normal";
            } else if (perpetualstew_saturationMod == 0.6f) {
                return "Low";
            } else if (perpetualstew_saturationMod == 0.2f) {
                return "Poor";
            }
        }
        return "None";
    }

    public void setHunger(ItemStack stack, int hunger) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("perpetualstew_hunger", hunger);
    }

    public void setSaturationMod(ItemStack stack, float saturation) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putFloat("perpetualstew_saturationMod", saturation);
    }
    @Override
    public boolean isFood() {
        return true;
    }

    @Nullable
    @Override
    public Food getFood() {
        return (new Food.Builder()).hunger(0).saturation(0).build();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity)
        {
            PlayerEntity entityplayer = (PlayerEntity)entityLiving;
            entityplayer.getFoodStats().addStats(getHunger(stack), getSaturation(stack));
            worldIn.playSound((PlayerEntity)null, entityplayer.getPosX(), entityplayer.getPosY(), entityplayer.getPosZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            //this.onFoodEaten(stack, worldIn, entityplayer);

            if (entityplayer instanceof ServerPlayerEntity)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)entityplayer, stack);
            }
        }
        ItemStack copy = stack.copy();
        copy.shrink(1);
        return copy;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if(nbt.contains("perpetualstew_hunger")) {
            tooltip.add(new StringTextComponent("Hunger: " + nbt.getInt("perpetualstew_hunger")).setStyle(Style.EMPTY.setColor(Color.fromHex("#00FFFF"))));
        }
        if(nbt.contains("perpetualstew_saturationMod")) {
            tooltip.add(new StringTextComponent("Nourishment: " + getNourishmentDescriptor(stack)).setStyle(Style.EMPTY.setColor(Color.fromHex("#00FFFF"))));
        }
    }
}
