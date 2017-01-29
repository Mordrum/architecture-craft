//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base for 1.8 - Generic Item
//
//------------------------------------------------------------------------------------------------

package gcewing.architecture;

import gcewing.architecture.BaseMod.IItem;
import gcewing.architecture.BaseMod.ModelSpec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaseItem extends Item implements IItem {

    public String[] getTextureNames() {
        return null;
    }

    public ModelSpec getModelSpec(ItemStack stack) {
        return null;
    }
    
    public int getNumSubtypes() {
        return 1;
    }
    
    @Override
    public boolean getHasSubtypes() {
        return getNumSubtypes() > 1;
    }
    
}
