//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base for 1.10 - Generic Tile Entity
//
//------------------------------------------------------------------------------------------------

package gcewing.architecture;

import gcewing.architecture.BaseMod.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class BaseTileEntity extends TileEntity
    implements BaseMod.ITileEntity
{

    public byte side, turn;
    public Ticket chunkTicket;

    public int getX() {return pos.getX();}
    public int getY() {return pos.getY();}
    public int getZ() {return pos.getZ();}

    public void setSide(int side) {
        this.side = (byte)side;
    }
    
    public void setTurn(int turn) {
        this.turn = (byte)turn;
    }
    
    public Trans3 localToGlobalRotation() {
        return localToGlobalTransformation(Vector3.zero);
    }

    public Trans3 localToGlobalTransformation() {
        return localToGlobalTransformation(Vector3.blockCenter(pos));
    }

//  public Trans3 localToGlobalTransformation(double x, double y, double z) {
//      return localToGlobalTransformation(new Vector3(x + 0.5, y + 0.5, z + 0.5);
//  }
    
    public Trans3 localToGlobalTransformation(Vector3 origin) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof IBlock)
            return ((IBlock)block).localToGlobalTransformation(world, pos, state, origin);
        else {
            System.out.printf("BaseTileEntity.localToGlobalTransformation: Wrong block type at %s\n", pos);
            return new Trans3(origin);
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        if (syncWithClient())
            writeToNBT(nbt);
        return nbt;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
         //System.out.printf("BaseTileEntity.getDescriptionPacket for %s\n", this);
         if (syncWithClient()) {
             NBTTagCompound nbt = new NBTTagCompound();
             writeToNBT(nbt);
            return new SPacketUpdateTileEntity(pos, 0, nbt);
         }
         else
             return null;
     }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        markBlockForUpdate();
    }
    
    boolean syncWithClient() {
        return true;
    }
    
    public void markBlockForUpdate() {
        BaseBlockUtils.markBlockForUpdate(world, pos);
    }
    
    public void playSoundEffect(SoundEvent name, float volume, float pitch) {
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, name, SoundCategory.BLOCKS, volume, pitch);
    }
    
    @Override
    public void onAddedToWorld() {
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        side = nbt.getByte("side");
        turn = nbt.getByte("turn");
        readContentsFromNBT(nbt);
    }
    
    public void readFromItemStack(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null)
            readFromItemStackNBT(nbt);
    }
    
    public void readFromItemStackNBT(NBTTagCompound nbt) {
        readContentsFromNBT(nbt);
    }
    
    public void readContentsFromNBT(NBTTagCompound nbt) {
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (side != 0)
            nbt.setByte("side", side);
        if (turn != 0)
            nbt.setByte("turn", turn);
        writeContentsToNBT(nbt);
        return nbt;
    }

    public void writeToItemStackNBT(NBTTagCompound nbt) {
        writeContentsToNBT(nbt);
    }
    
    public void writeContentsToNBT(NBTTagCompound nbt) {
    }
    
    public void markChanged() {
        markDirty();
        markBlockForUpdate();
    }

    @Override
    public void invalidate() {
        releaseChunkTicket();
        super.invalidate();
    }
    
    public void releaseChunkTicket() {
        if (chunkTicket != null) {
            ForgeChunkManager.releaseTicket(chunkTicket);
            chunkTicket = null;
        }
    }
 
    public static ItemStack blockStackWithTileEntity(Block block, int size, BaseTileEntity te) {
        return blockStackWithTileEntity(block, size, 0, te);
    }

    public static ItemStack blockStackWithTileEntity(Block block, int size, int meta, BaseTileEntity te) {
        ItemStack stack = new ItemStack(block, size, meta);
        if (te != null) {
            NBTTagCompound tag = new NBTTagCompound();
            te.writeToItemStackNBT(tag);
            stack.setTagCompound(tag);
        }
        return stack;
    }
    
    public ItemStack newItemStack(int size) {
        return blockStackWithTileEntity(getBlockType(), size, this);
    }

}