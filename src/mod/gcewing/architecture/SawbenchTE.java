//------------------------------------------------------------------------------
//
//   ArchitectureCraft - Sawbench Tile Entity
//
//------------------------------------------------------------------------------

package gcewing.architecture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import static gcewing.architecture.Shape.AnticylinderLargeQuarter;
import static gcewing.architecture.Shape.ArchD1;
import static gcewing.architecture.Shape.ArchD2;
import static gcewing.architecture.Shape.ArchD3A;
import static gcewing.architecture.Shape.ArchD3B;
import static gcewing.architecture.Shape.ArchD3C;
import static gcewing.architecture.Shape.ArchD4A;
import static gcewing.architecture.Shape.ArchD4B;
import static gcewing.architecture.Shape.ArchD4C;
import static gcewing.architecture.Shape.Architrave;
import static gcewing.architecture.Shape.ArchitraveCorner;
import static gcewing.architecture.Shape.BalustradeFancy;
import static gcewing.architecture.Shape.BalustradeFancyCorner;
import static gcewing.architecture.Shape.BalustradeFancyNewel;
import static gcewing.architecture.Shape.BalustradeFancyWithNewel;
import static gcewing.architecture.Shape.BalustradePlain;
import static gcewing.architecture.Shape.BalustradePlainEnd;
import static gcewing.architecture.Shape.BalustradePlainInnerCorner;
import static gcewing.architecture.Shape.BalustradePlainOuterCorner;
import static gcewing.architecture.Shape.BalustradePlainWithNewel;
import static gcewing.architecture.Shape.BanisterFancy;
import static gcewing.architecture.Shape.BanisterFancyBottom;
import static gcewing.architecture.Shape.BanisterFancyEnd;
import static gcewing.architecture.Shape.BanisterFancyNewelTall;
import static gcewing.architecture.Shape.BanisterFancyTop;
import static gcewing.architecture.Shape.BanisterPlain;
import static gcewing.architecture.Shape.BanisterPlainBottom;
import static gcewing.architecture.Shape.BanisterPlainEnd;
import static gcewing.architecture.Shape.BanisterPlainInnerCorner;
import static gcewing.architecture.Shape.BanisterPlainTop;
import static gcewing.architecture.Shape.BevelledInnerCorner;
import static gcewing.architecture.Shape.BevelledOuterCorner;
import static gcewing.architecture.Shape.CladdingSheet;
import static gcewing.architecture.Shape.CorinthianCapital;
import static gcewing.architecture.Shape.CorniceBottom;
import static gcewing.architecture.Shape.CorniceEndLH;
import static gcewing.architecture.Shape.CorniceEndRH;
import static gcewing.architecture.Shape.CorniceLH;
import static gcewing.architecture.Shape.CorniceRH;
import static gcewing.architecture.Shape.CorniceRidge;
import static gcewing.architecture.Shape.CorniceValley;
import static gcewing.architecture.Shape.Cylinder;
import static gcewing.architecture.Shape.CylinderHalf;
import static gcewing.architecture.Shape.CylinderLargeQuarter;
import static gcewing.architecture.Shape.CylinderQuarter;
import static gcewing.architecture.Shape.DoricCapital;
import static gcewing.architecture.Shape.DoricMetope;
import static gcewing.architecture.Shape.DoricTriglyph;
import static gcewing.architecture.Shape.DoricTriglyphCorner;
import static gcewing.architecture.Shape.IonicCapital;
import static gcewing.architecture.Shape.Pillar;
import static gcewing.architecture.Shape.PillarBase;
import static gcewing.architecture.Shape.Pole;
import static gcewing.architecture.Shape.Post;
import static gcewing.architecture.Shape.RoofInnerCorner;
import static gcewing.architecture.Shape.RoofOuterCorner;
import static gcewing.architecture.Shape.RoofOverhang;
import static gcewing.architecture.Shape.RoofOverhangGableEndLH;
import static gcewing.architecture.Shape.RoofOverhangGableEndRH;
import static gcewing.architecture.Shape.RoofOverhangGableLH;
import static gcewing.architecture.Shape.RoofOverhangGableRH;
import static gcewing.architecture.Shape.RoofOverhangInnerCorner;
import static gcewing.architecture.Shape.RoofOverhangOuterCorner;
import static gcewing.architecture.Shape.RoofOverhangRidge;
import static gcewing.architecture.Shape.RoofOverhangValley;
import static gcewing.architecture.Shape.RoofRidge;
import static gcewing.architecture.Shape.RoofSmartRidge;
import static gcewing.architecture.Shape.RoofSmartValley;
import static gcewing.architecture.Shape.RoofTile;
import static gcewing.architecture.Shape.RoofValley;
import static gcewing.architecture.Shape.Slab;
import static gcewing.architecture.Shape.SphereEighth;
import static gcewing.architecture.Shape.SphereEighthLarge;
import static gcewing.architecture.Shape.SphereEighthLargeRev;
import static gcewing.architecture.Shape.SphereFull;
import static gcewing.architecture.Shape.SphereHalf;
import static gcewing.architecture.Shape.SphereQuarter;
import static gcewing.architecture.Shape.Stairs;
import static gcewing.architecture.Shape.StairsInnerCorner;
import static gcewing.architecture.Shape.StairsOuterCorner;
import static gcewing.architecture.Shape.WindowCorner;
import static gcewing.architecture.Shape.WindowFrame;
import static gcewing.architecture.Shape.WindowMullion;

public class SawbenchTE extends BaseTileInventory {

	final public static int materialSlot = 0;
	final public static int resultSlot = 1;
	
	final public static int[] materialSideSlots = {materialSlot};
	final public static int[] resultSideSlots = {resultSlot};
	
	public static boolean allowAutomation = false;
	
	public static ShapePage[] pages = {
		new ShapePage("Roofing",
			RoofTile, RoofOuterCorner, RoofInnerCorner,
			RoofRidge, RoofSmartRidge, RoofValley,
			RoofSmartValley, RoofOverhang, RoofOverhangOuterCorner,
			RoofOverhangInnerCorner, RoofOverhangGableLH, RoofOverhangGableRH,
			RoofOverhangGableEndLH, RoofOverhangGableEndRH, RoofOverhangRidge,
			RoofOverhangValley, BevelledOuterCorner, BevelledInnerCorner),
		new ShapePage("Rounded",
			Cylinder, CylinderHalf, CylinderQuarter, CylinderLargeQuarter, AnticylinderLargeQuarter,
			Pillar, Post, Pole, SphereFull, SphereHalf,
			SphereQuarter, SphereEighth, SphereEighthLarge, SphereEighthLargeRev),
		new ShapePage("Classical",
			PillarBase, Pillar, DoricCapital, DoricTriglyph, DoricTriglyphCorner, DoricMetope,
			IonicCapital, CorinthianCapital, Architrave, ArchitraveCorner, CorniceLH, CorniceRH,
			CorniceEndLH, CorniceEndRH, CorniceRidge, CorniceValley, CorniceBottom),
		new ShapePage("Window",
			WindowFrame, WindowCorner, WindowMullion),
		new ShapePage("Arches",
			ArchD1, ArchD2, ArchD3A, ArchD3B, ArchD3C, ArchD4A, ArchD4B, ArchD4C),
		new ShapePage("Railings",
			BalustradePlain, BalustradePlainOuterCorner, BalustradePlainInnerCorner,
			BalustradePlainWithNewel, BalustradePlainEnd,
			BanisterPlainTop, BanisterPlain, BanisterPlainBottom, BanisterPlainEnd, BanisterPlainInnerCorner,
			BalustradeFancy, BalustradeFancyCorner, BalustradeFancyWithNewel, BalustradeFancyNewel,
			BanisterFancyTop, BanisterFancy, BanisterFancyBottom, BanisterFancyEnd, BanisterFancyNewelTall),
		new ShapePage("Other",
			CladdingSheet, Slab, Stairs, StairsOuterCorner, StairsInnerCorner),
	};
	
	public IInventory inventory = new InventoryBasic("Items", false, 2);
	public int selectedPage = 0;
	public int[] selectedSlots = new int[pages.length];
	public boolean pendingMaterialUsage = false; // Material for the stack in the result slot
	                                      // has not yet been removed from the material slot
	                                      
	public Shape getSelectedShape() {
		if (selectedPage >= 0 && selectedPage < pages.length) {
			int slot = selectedSlots[selectedPage];
			if (slot >= 0 && slot < pages[selectedPage].size())
				return pages[selectedPage].get(slot);
		}
		return null;
	}
	
	@Override
	protected IInventory getInventory() {
		return inventory;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack stack) {
		super.setInventorySlotContents(i, stack);
		updateResultSlot();
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		//System.out.printf("SawbenchTE.decrStackSize: %d by %d on %s\n", slot, amount, world);
		if (slot == resultSlot)
		    usePendingMaterial();
		ItemStack result = super.decrStackSize(slot, amount);
		updateResultSlot();
		return result;
	}
	
	public ItemStack usePendingMaterial() {
		//System.out.printf("SawbenchTE.usePendingMaterial: pmu = %s on %s\n", pendingMaterialUsage, world);
        ItemStack origMaterialStack = getStackInSlot(materialSlot);
		if (pendingMaterialUsage) {
			pendingMaterialUsage = false;
            inventory.decrStackSize(materialSlot, materialMultiple());
		}
		return origMaterialStack;
	}
	
	public void returnUnusedMaterial(ItemStack origMaterialStack) {
	    //if (!world.isRemote)
	    //    System.out.printf("SawbenchTE.returnUnusedMaterial: before: pmu = %s, material = %s, result = %s\n",
	    //        pendingMaterialUsage, getStackInSlot(materialSlot), getStackInSlot(resultSlot));
	    if (!pendingMaterialUsage) {
	        ItemStack materialStack = getStackInSlot(materialSlot);
	        ItemStack resultStack = getStackInSlot(resultSlot);
	        int m = materialMultiple();
	        int n = resultMultiple();
	        if (resultStack != null && resultStack.getCount() == n) {
	            if (materialStack != null)
	                materialStack.setCount(materialStack.getCount() + m);
	            else {
	                materialStack = origMaterialStack;
	                materialStack.setCount(m);
	            }
	            inventory.setInventorySlotContents(materialSlot, materialStack);
	            pendingMaterialUsage = true;
	        }
	    }
	    //if (!world.isRemote)
	    //    System.out.printf("SawbenchTE.returnUnusedMaterial: after: pmu = %s, material = %s, result = %s\n",
	    //        pendingMaterialUsage, getStackInSlot(materialSlot), getStackInSlot(resultSlot));
	}

	/**
	 * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
	 * block.
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		if (side == 1) // UP
			return materialSideSlots;
		else
			return resultSideSlots;
	}

	@Override
	public void readFromNBT(NBTTagCompound tc) {
		super.readFromNBT(tc);
		selectedPage = tc.getInteger("Page");
		int[] ss = tc.getIntArray("Slots");
		if (ss != null)
			for (int page = 0; page < pages.length; page++) {
				int slot = page < ss.length ? ss[page] : 0;
				selectedSlots[page] = slot >= 0 && slot < pages[page].size() ? slot : 0;
		}
		pendingMaterialUsage = tc.getBoolean("PMU");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tc) {
		super.writeToNBT(tc);
		tc.setInteger("Page", selectedPage);
		tc.setIntArray("Slots", selectedSlots);
		tc.setBoolean("PMU", pendingMaterialUsage);
		return tc;
	}
	
	public void setSelectedShape(int page, int slot) {
		if (page >= 0 && page < pages.length) {
			selectedPage = page;
			if (slot >= 0 && slot < pages[selectedPage].size()) {
				selectedSlots[selectedPage] = slot;
				markDirty();
				updateResultSlot();
				BaseMod.sendTileEntityUpdate(this);
			}
		}
	}

	void updateResultSlot() {
		//System.out.printf("SawbenchTE.updateResultSlot: pmu = %s on %s\n", pendingMaterialUsage, world);
		ItemStack oldResult = getStackInSlot(resultSlot);
		if (oldResult == null || pendingMaterialUsage) {
			ItemStack resultStack = makeResultStack();
			if (!ItemStack.areItemStacksEqual(resultStack, oldResult))
				inventory.setInventorySlotContents(resultSlot, resultStack);
			pendingMaterialUsage = resultStack != null;
			//System.out.printf("SawbenchTE.updateResultSlot: now pmu = %s on %s\n", pendingMaterialUsage, world);
		}
	}
	
	protected ItemStack makeResultStack() {
        Shape resultShape = getSelectedShape();
        if (resultShape != null) {
			ItemStack materialStack = getStackInSlot(materialSlot);
            if (materialStack != null && materialStack.getCount() >= resultShape.materialUsed) {
                Item materialItem = materialStack.getItem();
                if (materialItem instanceof ItemBlock) {
                    Block materialBlock = Block.getBlockFromItem(materialItem);
                    if (isAcceptableMaterial(materialBlock)) {
                        return resultShape.kind.newStack(resultShape, materialBlock,
                            materialStack.getItemDamage(), resultShape.itemsProduced);
                    }
                }
            }
        }
        return null;
    }
    
    protected boolean isAcceptableMaterial(Block block) {
        String name = Block.REGISTRY.getNameForObject(block).toString();
        System.out.printf("SawbenchTE.isAcceptableMaterial: %s %s\n", name, block.getUnlocalizedName());
        if (block == Blocks.GLASS || block == Blocks.STAINED_GLASS || block instanceof BlockSlab ||
            name.startsWith("chisel:glass"))
                return true;
        return block.getDefaultState().isFullCube() && !block.hasTileEntity();
    }

	int materialMultiple() {
		int factor = 1;
		ItemStack materialStack = getStackInSlot(materialSlot);
		if (materialStack != null) {
		    Block materialBlock = Block.getBlockFromItem(materialStack.getItem());
		    if (materialBlock instanceof BlockSlab)
		        factor = 2;
		}
		Shape shape = getSelectedShape();
		if (shape != null)
			return factor * shape.materialUsed;
		return 0;
	}
	
	int resultMultiple() {
		//return productMadeForShape[selectedShape];
		Shape shape = getSelectedShape();
		if (shape != null)
			return shape.itemsProduced;
		return 0;
	}
	
    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
        return slot == materialSlot;
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
        if (side == EnumFacing.DOWN)
            return allowAutomation && slot == resultSlot;
        else
            return slot == materialSlot;
    }

}

