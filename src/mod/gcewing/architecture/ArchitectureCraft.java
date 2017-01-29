//------------------------------------------------------
//
//   ArchitectureCraft - Main
//
//------------------------------------------------------

package gcewing.architecture;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(
	modid = Info.modID,
	name = Info.modName,
	version = Info.versionNumber,
	acceptableRemoteVersions = Info.versionBounds,
	acceptedMinecraftVersions = Info.acceptedMinecraftVersions
)

public class ArchitectureCraft extends BaseMod<ArchitectureCraftClient> {

	public static ArchitectureCraft mod;
	public static BaseDataChannel channel;
	
	//
	//   Blocks and Items
	//
	
	public static SawbenchBlock blockSawbench;
	public static Block blockShape;

	public static Item itemSawblade;
	public static Item itemLargePulley;
	public static Item itemChisel;
	public static Item itemHammer;
	public static CladdingItem itemCladding;
	
	public ArchitectureCraft() {
		super();
		mod = this;
		channel = new BaseDataChannel(modID);
		//debugCreativeTabs = true;
	}
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	ArchitectureCraftClient initClient() {
		return new ArchitectureCraftClient(this);
	}

	protected void registerBlocks() {
		blockSawbench = newBlock("sawbench", SawbenchBlock.class);
		blockSawbench.setHardness(2.0F);
		blockShape = newBlock("shape", ShapeBlock.class, ShapeItem.class);
	}
	
	protected void registerTileEntities() {
		GameRegistry.registerTileEntity(SawbenchTE.class, "gcewing.sawbench");
		GameRegistry.registerTileEntity(ShapeTE.class, "gcewing.shape");
	}
	
	protected void registerItems() {
		itemSawblade = newItem("sawblade");
		itemLargePulley = newItem("largepulley");
		itemChisel = newItem("chisel", ChiselItem.class);
		itemHammer = newItem("hammer", HammerItem.class);
		itemCladding = newItem("cladding", CladdingItem.class);
	}

	protected void registerRecipes() {
	    ItemStack orangeDye = new ItemStack(Items.DYE, 1, EnumDyeColor.ORANGE.getDyeDamage());
		newRecipe(blockSawbench, 1,
			"I*I",
			"/0/",
			"/_/",
			'I', Items.IRON_INGOT, '*', itemSawblade, '/', Items.STICK, 
			'_', Blocks.WOODEN_PRESSURE_PLATE, '0', itemLargePulley);
		newRecipe(itemSawblade, 1,
			" I ",
			"I/I",
			" I ",
			'I', Items.IRON_INGOT, '/', Items.STICK);
		newRecipe(itemLargePulley, 1,
			" W ",
			"W/W",
			" W ",
			'W', Blocks.PLANKS, '/', Items.STICK);
		newRecipe(itemChisel, 1,
			"I ",
			"ds",
			'I', Items.IRON_INGOT, 's', Items.STICK, 'd', orangeDye);
		newRecipe(itemHammer, 1,
			"II ",
			"dsI",
			"ds ",
			'I', Items.IRON_INGOT, 's', Items.STICK, 'd', orangeDye);
}
	
	//--------------- GUIs ----------------------------------------------------------

	public final static int guiSawbench = 1;
	
	@Override
	protected void registerContainers() {
		addContainer(guiSawbench, SawbenchContainer.class);
	}
	
	public void openGuiSawbench(World world, BlockPos pos, EntityPlayer player) {
		openGui(player, guiSawbench, world, pos);
	}

}
