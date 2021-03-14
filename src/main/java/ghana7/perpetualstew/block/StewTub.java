package ghana7.perpetualstew.block;

import ghana7.perpetualstew.PerpetualStewMod;
import ghana7.perpetualstew.item.Stew;
import ghana7.perpetualstew.tile.StewTubTileEntity;
import ghana7.perpetualstew.util.FoodInfo;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class StewTub extends Block {
    public StewTub() {
        super(AbstractBlock.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5F).notSolid());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return PerpetualStewMod.STEW_TUB_TE.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        ItemStack itemstack = player.getHeldItem(handIn);
        if (itemstack.isEmpty()) {
            return ActionResultType.PASS;
        } else {
            StewTubTileEntity stewTubTileEntity = (StewTubTileEntity) worldIn.getTileEntity(pos);

            Item item = itemstack.getItem();
            if (item == Items.WATER_BUCKET) {
                if (stewTubTileEntity.getWater() < 4000 && !worldIn.isRemote) {
                    if (!player.isCreative()) {
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    }

                    stewTubTileEntity.addWater(1000);
                    worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.BUCKET) {
                if (stewTubTileEntity.getWater() >= 1000 && !worldIn.isRemote) {
                    if (!player.isCreative()) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, new ItemStack(Items.WATER_BUCKET));
                        } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                            player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    stewTubTileEntity.removeWater(1000);
                    worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if(item.isFood() && !(item instanceof Stew)) {
                if(!worldIn.isRemote() && stewTubTileEntity.getWater() > 0) {
                    ItemStack newStack = stewTubTileEntity.addFood(itemstack, player);
                    if(!player.isCreative()) {
                        player.setHeldItem(handIn, newStack);
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.BOWL) {
                if(!worldIn.isRemote() && stewTubTileEntity.getWater() > 0) {
                    ItemStack newStack = new ItemStack(PerpetualStewMod.STEW.get(), 1);
                    ((Stew)PerpetualStewMod.STEW.get()).setHunger(newStack, stewTubTileEntity.getHunger());
                    ((Stew)PerpetualStewMod.STEW.get()).setSaturationMod(newStack, stewTubTileEntity.getSaturationMod());
                    player.addItemStackToInventory(newStack);
                    if(!player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == PerpetualStewMod.TASTING_SPOON.get()) {
                if(!worldIn.isRemote() && stewTubTileEntity.getWater() > 0) {
                    stewTubTileEntity.displayQualityInfo(player);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else {
                return ActionResultType.PASS;
            }
        }
    }
}
