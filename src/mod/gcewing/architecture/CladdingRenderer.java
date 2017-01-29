//------------------------------------------------------------------------------
//
//   ArchitectureCraft - Cladding Item Renderer
//
//------------------------------------------------------------------------------

package gcewing.architecture;

import gcewing.architecture.BaseModClient.ICustomRenderer;
import gcewing.architecture.BaseModClient.IModel;
import gcewing.architecture.BaseModClient.IRenderTarget;
import gcewing.architecture.BaseModClient.ITexture;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CladdingRenderer implements ICustomRenderer {

	public void renderBlock(IBlockAccess world, BlockPos pos, IBlockState state,
		IRenderTarget target, BlockRenderLayer layer, Trans3 t) {}
		
	public void renderItemStack(ItemStack stack, IRenderTarget target, Trans3 t) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null) {
			String blockName = nbt.getString("block");
			int meta = stack.getMetadata();
			Block block = Block.getBlockFromName(blockName);
			if (block != null) {
				IBlockState state = block.getStateFromMeta(meta);
				if (state != null) {
					TextureAtlasSprite sprite = Utils.getSpriteForBlockState(state);
					if (sprite != null) {
						ITexture texture = BaseTexture.fromSprite(sprite);
						IModel model = ArchitectureCraft.mod.client.getModel("shape/cladding.smeg");
						model.render(t, target, texture);
					}
				}
			}
		}
	}

}

