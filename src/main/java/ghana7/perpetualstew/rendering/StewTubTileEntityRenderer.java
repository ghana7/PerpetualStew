package ghana7.perpetualstew.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import ghana7.perpetualstew.PerpetualStewMod;
import ghana7.perpetualstew.tile.StewTubTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.common.extensions.IForgeFluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.IFluidHandler;

@OnlyIn(Dist.CLIENT)
public class StewTubTileEntityRenderer extends TileEntityRenderer<StewTubTileEntity> {
    public StewTubTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    private void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
        renderer.pos(stack.getLast().getMatrix(), x, y, z)
                .color(r, g, b, a)
                .tex(u, v)
                .lightmap(0, 240)
                .normal(1, 0, 0)
                .endVertex();
    }


    @Override
    public void render(StewTubTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(tileEntityIn.getWater() > 0) {



            Fluid renderFluid = Fluids.WATER;
            FluidAttributes attributes = renderFluid.getAttributes();
            ResourceLocation fluidStill = attributes.getStillTexture(); //maybe add a dummy stack
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluidStill);
            IVertexBuilder builder = bufferIn.getBuffer(RenderType.getTranslucent());

            //int color = renderFluid.getAttributes().getColor();
            int color = tileEntityIn.color;
            float a = 1.0F;
            float r = (color >> 16 & 0xFF) / 255.0F;
            float g = (color >> 8 & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;

            matrixStackIn.push();
            matrixStackIn.translate(0.125f, 0, 0.125f);
            matrixStackIn.translate(0, 0.0625f * 3f * (tileEntityIn.getWater() / 1000) - 0.8125f, 0);
            matrixStackIn.scale(0.75f, 1, 0.75f);

            add(builder, matrixStackIn, 0, 1, 1, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
            add(builder, matrixStackIn, 1, 1, 1, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
            add(builder, matrixStackIn, 1, 1, 0, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
            add(builder, matrixStackIn, 0, 1, 0, sprite.getMinU(), sprite.getMinV(), r, g, b, a);

            matrixStackIn.pop();
        }
    }
}
