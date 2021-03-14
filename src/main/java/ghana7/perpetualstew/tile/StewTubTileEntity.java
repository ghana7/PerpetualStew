package ghana7.perpetualstew.tile;

import com.sun.org.apache.xpath.internal.operations.String;
import ghana7.perpetualstew.PerpetualStewMod;
import ghana7.perpetualstew.util.FoodInfo;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class StewTubTileEntity extends TileEntity implements ITickableTileEntity {
    private int timerMax = 1200; // 1 minute
    private int timer = timerMax;
    private int cookTime = 0;
    private int waterCapacity = 4000;
    private int currentWaterAmount = 0;
    private ItemStack[] lastTenItems;
    public int color;
    private int totalFoods;

    private int totalPoisonous;
    private int totalMeats;
    private int totalVeggies;
    private int totalSweets;
    private int totalMagic;

    public StewTubTileEntity() {
        super(PerpetualStewMod.STEW_TUB_TE.get());
        lastTenItems = new ItemStack[10];
        for(int i = 0; i < lastTenItems.length; i++) {
            lastTenItems[i] = ItemStack.EMPTY.copy();
        }
    }

    @Override
    public void tick() {
        if(world.isRemote) {
            return;
        }

        if(timer > 0) {
            if(currentWaterAmount >= 1000) {
                timer--;
            }
        }

        if(timer <= 0) {
            timer = timerMax;
            cookTime++;
        }
    }
    //region saveload
    @Override
    public void read(BlockState state, CompoundNBT tag) {
        lastTenItems = new ItemStack[10];
        for(int i = 0; i < lastTenItems.length; i++) {
            lastTenItems[i] = ItemStack.EMPTY.copy();
        }
        for(int i = 0; i < lastTenItems.length; i++) {
            lastTenItems[i] = ItemStack.read((CompoundNBT) tag.get("item" + i));
        }
        color = getAverageColor();
        timer = tag.getInt("timer");
        currentWaterAmount = tag.getInt("waterAmount");
        cookTime = tag.getInt("cookTime");

        totalFoods = tag.getInt("perpetualstew_totalFoods");
        totalPoisonous = tag.getInt("perpetualstew_totalPoisonous");
        totalMeats = tag.getInt("perpetualstew_totalMeats");
        totalVeggies = tag.getInt("perpetualstew_totalVeggies");
        totalSweets = tag.getInt("perpetualstew_totalSweets");
        totalMagic = tag.getInt("perpetualstew_totalMagic");
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        for(int i = 0; i < lastTenItems.length; i++) {
            tag.put("item" + i, lastTenItems[i].serializeNBT());
        }
        tag.putInt("timer", timer);
        tag.putInt("waterAmount", currentWaterAmount);
        tag.putInt("cookTime", cookTime);
        tag.putInt("perpetualstew_totalFoods", totalFoods);
        tag.putInt("perpetualstew_totalPoisonous", totalPoisonous);
        tag.putInt("perpetualstew_totalMeats", totalMeats);
        tag.putInt("perpetualstew_totalVeggies", totalVeggies);
        tag.putInt("perpetualstew_totalSweets", totalSweets);
        tag.putInt("perpetualstew_totalMagic", totalMagic);

        return super.write(tag);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        super.handleUpdateTag(state, nbt);
        read(state, nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);

        // the number here is generally ignored for non-vanilla TileEntities, 0 is safest
        return new SUpdateTileEntityPacket(this.getPos(), 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
    {
        this.read(getBlockState(), packet.getNbtCompound());
    }
    //endregion

    public boolean addWater(int amount) {
        if(currentWaterAmount <= waterCapacity) {
            currentWaterAmount += amount;
            if(currentWaterAmount > waterCapacity) {
                currentWaterAmount = waterCapacity;
            }
            markDirty();
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
            return true;
        }
        return false;
    }

    public boolean removeWater(int amount) {
        if(currentWaterAmount >= amount) {
            currentWaterAmount -= amount;
            markDirty();
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
            return true;
        }
        return false;
    }

    public int getWater() {
        return currentWaterAmount;
    }

    public ItemStack addFood(ItemStack food, PlayerEntity player) {
        for(int i = 0; i < lastTenItems.length - 1; i++) {
            lastTenItems[i] = lastTenItems[i + 1];
        }
        ItemStack temp = food.copy();
        temp.shrink(food.getCount() - 1);
        lastTenItems[lastTenItems.length - 1] = temp;

        //update shit
        color = getAverageColor();
        PerpetualStewMod.LOGGER.debug("set color to " + Integer.toHexString(color));
        updateNutritionStats(food, player);

        ItemStack cp = food.copy();
        cp.shrink(1);
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
        return cp;
    }

    private int getAverageColor() {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        for(int i = 0; i < lastTenItems.length; i++) {
            int color = FoodInfo.getFoodColor(lastTenItems[i].getItem());
            redSum += (color >> 16) & 255;
            greenSum += (color >> 8) & 255;
            blueSum += (color >> 0) & 255;
        }
        redSum /= lastTenItems.length;
        greenSum /= lastTenItems.length;
        blueSum /= lastTenItems.length;
        PerpetualStewMod.LOGGER.debug("rgb: " + redSum + " " + greenSum + " " + blueSum);
        PerpetualStewMod.LOGGER.debug("est color " + ((redSum << 16) + (greenSum << 8) + blueSum) + " - " + Integer.toHexString((redSum << 16) + (greenSum << 8) + blueSum));
        return (redSum << 16) + (greenSum << 8) + blueSum;
    }

    public int getHunger() {
        return 4 + (int)Math.floor(Math.log(totalFoods) / Math.log(2.0));
    }

    public float getSaturationMod() {
        int saturationTier;
        int totalNormalFoods = totalMeats + totalSweets + totalVeggies;
        float minAmount = (Math.min(Math.min(totalMeats, totalSweets), totalVeggies) * 1.0f) / totalNormalFoods;
        float maxAmount = (Math.max(Math.max(totalMeats, totalSweets), totalVeggies) * 1.0f) / totalNormalFoods;
        float fdd = maxAmount - minAmount;
        if(fdd < 0.1f) {
            saturationTier = 4;
        } else if(fdd < 0.3f) {
            saturationTier = 3;
        } else {
            saturationTier = 2;
        }

        if((totalMagic * 1.0f) / totalFoods > 0.1f && totalMagic > totalPoisonous) {
            saturationTier++;
        }

        if(totalPoisonous > totalMagic) {
            saturationTier = 1;
        }

        switch (saturationTier) {
            case 1:
                return 0.2f;
            case 2:
                return 0.6f;
            case 3:
                return 1.2f;
            case 4:
                return 1.6f;
            case 5:
                return 2.4f;
            default:
                return 0.0f;
        }
    }

    private void updateNutritionStats(ItemStack food, PlayerEntity player) {
        totalFoods++;
        switch (FoodInfo.getFoodType(food.getItem())) {
            case Meat:
                totalMeats++;
                break;
            case Magic:
                totalMagic++;
                break;
            case Poison:
                totalPoisonous++;
                break;
            case Sweets:
                totalSweets++;
                break;
            case Veggie:
                totalVeggies++;
                break;
        }
        player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.add_item",
                new TranslationTextComponent(food.getTranslationKey())),
        false);
        displayQualityInfo(player);

    }

    public void displayQualityInfo(PlayerEntity player) {
        int totalNormalFoods = totalMeats + totalSweets + totalVeggies;
        float minAmount = (Math.min(Math.min(totalMeats, totalSweets), totalVeggies) * 1.0f) / totalNormalFoods;
        float maxAmount = (Math.max(Math.max(totalMeats, totalSweets), totalVeggies) * 1.0f) / totalNormalFoods;
        float fdd = maxAmount - minAmount;


        if(totalPoisonous > totalMagic) {
            player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.poison_stew"),
                    false);
        } else {
            if(fdd < 0.1f) {
                player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.great_flavor"),
                        false);
            } else if(fdd < 0.3f) {
                if(totalMeats > totalSweets && totalMeats > totalVeggies) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.too_much_meat"),
                            false);
                } else if(totalSweets > totalMeats && totalSweets > totalVeggies) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.too_much_sweet"),
                            false);
                } else if(totalVeggies > totalMeats && totalVeggies > totalSweets) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.too_much_veggie"),
                            false);
                } else if(totalVeggies == totalMeats) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.needs_sweet"),
                            false);
                } else if(totalVeggies == totalSweets) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.needs_meat"),
                            false);
                } else if(totalSweets == totalMeats) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.needs_veggie"),
                            false);
                }
            } else {
                if(totalMeats > totalSweets && totalMeats > totalVeggies) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.way_too_much_meat"),
                            false);
                } else if(totalSweets > totalMeats && totalSweets > totalVeggies) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.way_too_much_sweet"),
                            false);
                } else if(totalVeggies > totalMeats && totalVeggies > totalSweets) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.way_too_much_veggie"),
                            false);
                }else if(totalVeggies == totalMeats) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.really_needs_sweet"),
                            false);
                } else if(totalVeggies == totalSweets) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.really_needs_meat"),
                            false);
                } else if(totalSweets == totalMeats) {
                    player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.really_needs_veggie"),
                            false);
                }
            }

            if((totalMagic * 1.0f) / totalFoods > 0.1f && totalMagic > totalPoisonous) {
                player.sendStatusMessage(new TranslationTextComponent("message.perpetualstew.magic_stew"),
                        false);
            }
        }
    }

}
