package ghana7.perpetualstew;

import ghana7.perpetualstew.block.StewTub;
import ghana7.perpetualstew.item.Stew;
import ghana7.perpetualstew.item.TastingSpoon;
import ghana7.perpetualstew.rendering.StewTubTileEntityRenderer;
import ghana7.perpetualstew.tile.StewTubTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PerpetualStewMod.MODID)
public class PerpetualStewMod
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "perpetualstew";

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

    public static final RegistryObject<Block> STEW_TUB = BLOCKS.register("stew_tub", () ->
            new StewTub()
    );

    public static final RegistryObject<Item> STEW_TUB_BI = ITEMS.register("stew_tub", () ->
            new BlockItem(STEW_TUB.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS))
    );

    public static final RegistryObject<Item> STEW = ITEMS.register("stew", () ->
            new Stew()
    );

    public static final RegistryObject<Item> TASTING_SPOON = ITEMS.register("tasting_spoon", () ->
            new TastingSpoon()
    );

    public static final RegistryObject<TileEntityType<StewTubTileEntity>> STEW_TUB_TE = TILE_ENTITY_TYPES.register(
            "stew_tub", () -> TileEntityType.Builder.create(StewTubTileEntity::new, STEW_TUB.get()).build(null)
    );
    public PerpetualStewMod() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(STEW_TUB_TE.get(), StewTubTileEntityRenderer::new);
        RenderTypeLookup.setRenderLayer(STEW_TUB.get(), RenderType.getCutout());
    }
}
