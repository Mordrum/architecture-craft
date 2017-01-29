//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base for 1.10 - Generic Block with optional Tile Entity
//
//------------------------------------------------------------------------------------------------

package gcewing.architecture;

import gcewing.architecture.BaseMod.ModelSpec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class BaseBlock<TE extends TileEntity>
    extends BlockContainer implements BaseMod.IBlock
{

    public static boolean debugState = false;

    // --------------------------- Orientation -------------------------------

    public interface IOrientationHandler {
    
        void defineProperties(BaseBlock block);
        IBlockState onBlockPlaced(Block block, World world, BlockPos pos, EnumFacing side, 
            float hitX, float hitY, float hitZ, IBlockState baseState, EntityLivingBase placer);
        //Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state);
        Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin);
    }
    
    public static class Orient1Way implements IOrientationHandler {
    
        public void defineProperties(BaseBlock block) {
        }
        
        public IBlockState onBlockPlaced(Block block, World world, BlockPos pos, EnumFacing side, 
            float hitX, float hitY, float hitZ, IBlockState baseState, EntityLivingBase placer)
        {
            return baseState;
        }
        
        public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin) {
            return new Trans3(origin);
        }
    
    }
    
    public static IOrientationHandler orient1Way = new Orient1Way();

    // --------------------------- Members -------------------------------

    protected MapColor mapColor;
    protected IProperty[] properties;
    protected Object[][] propertyValues;
    protected int numProperties; // Do not explicitly initialise
    protected EnumBlockRenderType renderID = EnumBlockRenderType.MODEL;
    protected Class<? extends TileEntity> tileEntityClass = null;
    protected IOrientationHandler orientationHandler = orient1Way;
    protected String[] textureNames;
    protected ModelSpec modelSpec;

    // --------------------------- Constructors -------------------------------
    
    public BaseBlock(Material material) {
        this(material, null, null, null);
    }

    public BaseBlock(Material material, IOrientationHandler orient) {
        this(material, orient, null, null);
    }

    public BaseBlock(Material material, Class<TE> teClass) {
        this(material, null, teClass, null);
    }

    public BaseBlock(Material material, IOrientationHandler orient, Class<TE> teClass) {
        this(material, orient, teClass, null);
    }

    public BaseBlock(Material material, Class<TE> teClass, String teID) {
        this(material, null, teClass, teID);
    }

    public BaseBlock(Material material, IOrientationHandler orient, Class<TE> teClass, String teID) {
        super(material);
        if (orient == null)
            orient = orient1Way;
        this.orientationHandler = orient;
        tileEntityClass = teClass;
//        if (teClass != null) {
//            if (teID == null)
//                teID = teClass.getName();
//            try {
//                GameRegistry.registerTileEntity(teClass, teID);
//            }
//            catch (IllegalArgumentException e) {
//                // Ignore redundant registration
//            }
//        }
    }

    // --------------------------- States -------------------------------
    
    public IOrientationHandler getOrientationHandler() {
        return orient1Way;
    }
    
    protected void defineProperties() {
        properties = new IProperty[4];
        propertyValues = new Object[4][];
        getOrientationHandler().defineProperties(this);
    }
    
    protected void addProperty(IProperty property) {
        if (debugState)
            System.out.printf("BaseBlock.addProperty: %s to %s\n", property, getClass().getName());
        if (numProperties < 4) {
            int i = numProperties++;
            properties[i] = property;
            Object[] values = BaseUtils.arrayOf(property.getAllowedValues());
            propertyValues[i] = values;
        }
        else
            throw new IllegalStateException("Block " + getClass().getName() +
                " has too many properties");
        if (debugState)
            System.out.printf("BaseBlock.addProperty: %s now has %s properties\n",
                getClass().getName(), numProperties);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        if (debugState)
            System.out.printf("BaseBlock.createBlockState: Defining properties\n");
        defineProperties();
        if (debugState)
            dumpProperties();
        checkProperties();
        IProperty[] props = Arrays.copyOf(properties, numProperties);
        if (debugState)
            System.out.printf("BaseBlock.createBlockState: Creating BlockState with %s properties\n", props.length);
        return new BlockStateContainer(this, props);
    }
    
    private void dumpProperties() {
        System.out.printf("BaseBlock: Properties of %s:\n", getClass().getName());
        for (int i = 0; i < numProperties; i++) {
            System.out.printf("%s: %s\n", i, properties[i]);
            Object[] values = propertyValues[i];
            for (int j = 0; j < values.length; j++)
                System.out.printf("   %s: %s\n", j, values[j]);
        }
    }

    protected void checkProperties() {
        int n = 1;
        for (int i = 0; i < numProperties; i++)
            n *= propertyValues[i].length;
        if (n > 16)
            throw new IllegalStateException(String.format(
                "Block %s has %s combinations of property values (16 allowed)",
                getClass().getName(), n));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        for (int i = numProperties - 1; i >= 0; i--) {
            Object value = state.getValue(properties[i]);
            Object[] values = propertyValues[i];
            int k = values.length - 1;
            while (k > 0 && !values[k].equals(value))
                --k;
            if (debugState)
                System.out.printf("BaseBlock.getMetaFromState: property %s value %s --> %s of %s\n",
                    i, value, k, values.length);
            meta = meta * values.length + k;
        }
        if (debugState)
            System.out.printf("BaseBlock.getMetaFromState: %s --> %s\n", state, meta);
        return meta & 15; // To be on the safe side
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getDefaultState();
        int m = meta;
        for (int i = numProperties - 1; i >= 0; i--) {
            Object[] values = propertyValues[i];
            int n = values.length;
            int k = m % n;
            m /= n;
            state = state.withProperty(properties[i], (Comparable)values[k]);
        }
        if (debugState)
            System.out.printf("BaseBlock.getStateFromMeta: %s --> %s\n", meta, state);
        return state;
    }

//  @Override
//  public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
//      return new BaseBlockState(state, world, pos);
//  }

    // -------------------------- Subtypes ------------------------------
    
    public int getNumSubtypes() {
        return 1;
    }
    
    // -------------------------- Harvesting ----------------------------
    
    protected ThreadLocal<TileEntity> harvestingTileEntity = new ThreadLocal();
    
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        harvestingTileEntity.set(te);
        super.harvestBlock(world, player, pos, state, te, stack);
        harvestingTileEntity.set(null);
    }

	@Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntity te = world.getTileEntity(pos);
        if (te == null)
            te = harvestingTileEntity.get();
        return getDropsFromTileEntity(world, pos, state, te, fortune);
    }
    
	protected List<ItemStack> getDropsFromTileEntity(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity te, int fortune) {
        return super.getDrops(world, pos, state, fortune);
    }	    
    
    // -------------------------- Rendering -----------------------------

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return renderID;
    }
    
    public String getQualifiedRendererClassName() {
        String name = getRendererClassName();
        if (name != null)
            name = getClass().getPackage().getName() + "." + name;
        return name;
    }
    
    protected String getRendererClassName() {
        return null;
    }
        
    public void setModelAndTextures(String modelName, String... textureNames) {
        this.textureNames = textureNames;
        this.modelSpec = new ModelSpec(modelName, textureNames);
    }
    
    public void setModelAndTextures(String modelName, Vector3 origin, String... textureNames) {
        this.textureNames = textureNames;
        this.modelSpec = new ModelSpec(modelName, origin, textureNames);
    }
    
    public String[] getTextureNames() {
        return textureNames;
    }
    
    public ModelSpec getModelSpec(IBlockState state) {
        return modelSpec;
    }
    
    public Trans3 localToGlobalRotation(IBlockAccess world, BlockPos pos) {
        return localToGlobalRotation(world, pos, world.getBlockState(pos));
    }
    
    public Trans3 localToGlobalRotation(IBlockAccess world, BlockPos pos, IBlockState state) {
        return localToGlobalTransformation(world, pos, state, Vector3.zero);
    }
    
    public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos) {
        return localToGlobalTransformation(world, pos, world.getBlockState(pos));
    }
    
    public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state) {
        return localToGlobalTransformation(world, pos, state, Vector3.blockCenter(pos));
    }
    
    public Trans3 localToGlobalTransformation(IBlockAccess world, BlockPos pos, IBlockState state, Vector3 origin) {
        IOrientationHandler oh = getOrientationHandler();
        return oh.localToGlobalTransformation(world, pos, state, origin);
    }
    
    // -------------------------- Tile Entity -----------------------------
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return tileEntityClass != null;
    }
    
    public TE getTileEntity(IBlockAccess world, BlockPos pos) {
        if (hasTileEntity())
            return (TE)world.getTileEntity(pos);
        else
            return null;
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (tileEntityClass != null) {
            try {
                return tileEntityClass.newInstance();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else
            return null;
    }
    
    // -------------------------------------------------------------------

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side,
        float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState state = getOrientationHandler().onBlockPlaced(this, world, pos, side,
            hitX, hitY, hitZ, getStateFromMeta(meta), placer);
        return state;
    }



    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        if (hasTileEntity(state)) {
            TileEntity te = getTileEntity(world, pos);
            if (te instanceof BaseMod.ITileEntity)
                ((BaseMod.ITileEntity)te).onAddedToWorld();
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (hasTileEntity(state)) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof IInventory)
                InventoryHelper.dropInventoryItems(world, pos, (IInventory)te);
        }
    }

    @Override
    public boolean addLandingEffects(IBlockState state, WorldServer world, BlockPos pos,
        IBlockState iblockstate, EntityLivingBase entity, int numParticles)
    {
        IBlockState particleState = getParticleState(world, pos);
        world.spawnParticle(EnumParticleTypes.BLOCK_DUST, entity.posX, entity.posY, entity.posZ,
            numParticles, 0, 0, 0, 0.15, new int[]{Block.getStateId(particleState)});
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(IBlockState blockState, World world, RayTraceResult target, ParticleManager pm) {
        BlockPos pos = target.getBlockPos();
        IBlockState state = getParticleState(world, pos);
        ParticleDigging fx;
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        AxisAlignedBB boundingBox = blockState.getBoundingBox(world, pos);
        float f = 0.1F;
        double d0 = i + RANDOM.nextDouble() * (boundingBox.maxX - boundingBox.minX - (f * 2.0F)) + f + boundingBox.minX;
        double d1 = j + RANDOM.nextDouble() * (boundingBox.maxY - boundingBox.minY - (f * 2.0F)) + f + boundingBox.minY;
        double d2 = k + RANDOM.nextDouble() * (boundingBox.maxZ - boundingBox.minZ - (f * 2.0F)) + f + boundingBox.minZ;
        switch (target.sideHit) {
            case DOWN: d1 = j + boundingBox.minY - f; break;
            case UP: d1 = j + boundingBox.maxY + f; break;
            case NORTH: d2 = k + boundingBox.minZ - f; break;
            case SOUTH: d2 = k + boundingBox.maxZ + f; break;
            case WEST: d0 = i + boundingBox.minX - f; break;
            case EAST: d0 = i + boundingBox.maxX + f; break;
        }
        fx = new DiggingFX(world, d0, d1, d2, 0, 0, 0, state);
        pm.addEffect(fx.setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager pm) {
        IBlockState state = getParticleState(world, pos);
        ParticleDigging fx;
        byte b0 = 4;
        for (int i = 0; i < b0; ++i) {
            for (int j = 0; j < b0; ++j) {
                for (int k = 0; k < b0; ++k) {
                    double d0 = pos.getX() + (i + 0.5D) / b0;
                    double d1 = pos.getY() + (j + 0.5D) / b0;
                    double d2 = pos.getZ() + (k + 0.5D) / b0;
                    fx = new DiggingFX(world, d0, d1, d2,
                        d0 - pos.getX() - 0.5D, d1 - pos.getY() - 0.5D, d2 - pos.getZ() - 0.5D,
                        state);
                    pm.addEffect(fx.setBlockPos(pos));
                }
            }
        }
        return true;
    }
    
    public IBlockState getParticleState(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return getActualState(state, world, pos);
    }
    
    // Workaround for ParticleDigging having protected constructor
    
    @SideOnly(Side.CLIENT)
    public static class DiggingFX extends ParticleDigging {
    
        public DiggingFX(World world, double x1, double y1, double z1, double x2, double y2, double z2, IBlockState state) {
            super(world, x1, y1, z1, x2, y2, z2, state);
        }
    
    }

}
