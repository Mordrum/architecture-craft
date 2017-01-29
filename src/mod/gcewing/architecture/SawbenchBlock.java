//------------------------------------------------------------------------------
//
//   ArchitectureCraft - Sawbench Block
//
//------------------------------------------------------------------------------

package gcewing.architecture;

import gcewing.architecture.BaseMod.ModelSpec;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SawbenchBlock extends BaseBlock<SawbenchTE> {

	static String model = "block/sawbench.smeg";
	static String[] textures = {"sawbench-wood", "sawbench-metal"};
	static ModelSpec modelSpec = new ModelSpec(model, textures);

	public SawbenchBlock() {
		super(Material.WOOD, SawbenchTE.class);
		this.setCreativeTab(CreativeTabs.TOOLS);
		//renderID = -1;
	}
	
	@Override
	public IOrientationHandler getOrientationHandler() {
		return BaseOrientation.orient4WaysByState;
	}
	
	@Override
	public String[] getTextureNames() {
		return textures;
	}
	
	@Override
	public ModelSpec getModelSpec(IBlockState state) {
		return modelSpec;
	}
	
//	@Override
//	public String getModelNameForState(IBlockState state) {
//		return model;
//	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		//System.out.printf("SawbenchBlock.onBlockActivated\n");
		if (!player.isSneaking()) {
			if (!world.isRemote) {
				//System.out.printf("SawbenchBlock.onBlockActivated: opening gui\n");
				ArchitectureCraft.mod.openGuiSawbench(world, pos, player);
			}
			return true;
		}
		else
			return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new SawbenchTE();
	}

//	@Override
//	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int data) {
//		Utils.dumpInventoryIntoWorld(world, x, y, z);
//		super.onBlockDestroyedByPlayer(world, x, y, z, data);
//	}
	
}
