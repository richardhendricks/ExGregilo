package co.q64.exgregilo.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

@Singleton
public class AdvancedSieveItemRender extends AbstractSieveItemRender {
	private @Inject AdvancedSieveRender asr;

	@Override
	public ResourceLocation getTexture() {
		return asr.getTexture();
	}

	@Override
	public IIcon getMeshTexture() {
		return asr.getMeshTexture();
	}
}
