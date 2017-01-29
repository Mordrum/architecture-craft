//------------------------------------------------------
//
//   ArchitectureCraft - Client Proxy
//
//------------------------------------------------------

package gcewing.architecture;

//import cpw.mods.fml.client.registry.RenderingRegistry;

public class ArchitectureCraftClient extends BaseModClient<ArchitectureCraft> {

	public static ShapeRenderDispatch shapeRenderDispatch = new ShapeRenderDispatch();

	public ArchitectureCraftClient(ArchitectureCraft mod) {
		super(mod);
		//debugModelRegistration = true;
		RenderWindow.init(this);
	}
	
	@Override
	void registerScreens() {
		addScreen(ArchitectureCraft.guiSawbench, SawbenchGui.class);
	}

	@Override
	protected void registerBlockRenderers() {
		addBlockRenderer(base.blockShape, shapeRenderDispatch);
	}
	
	@Override
	protected void registerItemRenderers() {
		addItemRenderer(base.itemCladding, new CladdingRenderer());
	}
	
}
