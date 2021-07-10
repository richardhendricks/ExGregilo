package co.q64.exgregilo.container.blocks;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import co.q64.exgregilo.api.binders.ModDataBinders.AdvancedSieveBlockName;
import co.q64.exgregilo.api.link.LinkBase;
import co.q64.exgregilo.block.AdvancedSieve;
import co.q64.exgregilo.block.AdvancedSieveItemBlock;
import co.q64.exgregilo.container.GregiloBlockContainer;
import co.q64.exgregilo.link.gregtech.GregTech;

@Singleton
public class AdvancedSieveContainer extends GregiloBlockContainer {
	private @Inject Provider<AdvancedSieve> provider;
	private @Inject @AdvancedSieveBlockName String name;

	@Override
	public Provider<? extends Block> getBlockProvider() {
		return provider;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<? extends ItemBlock> getItemBlock() {
		return AdvancedSieveItemBlock.class;
	}

	@Override
	public List<Class<? extends LinkBase>> getRequired() {
		return require(GregTech.class);
	}
}
