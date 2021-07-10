package co.q64.exgregilo.link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.com.google.inject.Injector;
import co.q64.exgregilo.api.link.LinkBase;
import co.q64.exgregilo.api.link.LinkManager;
import co.q64.exgregilo.api.link.MalformedLinkException;
import co.q64.exgregilo.api.link.ModLink;
import co.q64.exgregilo.api.util.Logger;
import cpw.mods.fml.common.Loader;

@Singleton
public class SimpleLinkManager implements LinkManager {
	private @Inject Logger logger;
	private @Inject Injector injector;
	private @Inject Set<Class<? extends LinkBase>> links;

	private Map<Class<? extends LinkBase>, ModLink> pendingLinks = new HashMap<Class<? extends LinkBase>, ModLink>();
	private List<LinkBase> enabledLinks = new ArrayList<LinkBase>();
	private boolean enabled;

	@Inject
	public void init() {
		for (Class<? extends LinkBase> link : links) {
			registerLink(link);
		}
	}

	private void registerLink(Class<? extends LinkBase> linkClass) {
		if (enabled) {
			throw new IllegalStateException("Links need to be registered in PreLoad");
		}
		ModLink link = getModLink(linkClass);
		if (link == null) {
			throw new MalformedLinkException("The main link class must contain the ModLink annotation");
		}
		pendingLinks.put(linkClass, link);
	}

	@Override
	public boolean isEnabled(Class<? extends LinkBase> linkClass) {
		for (LinkBase link : enabledLinks) {
			if (link.getClass().equals(linkClass)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public <T> T getLink(Class<T> linkClass) {
		for (LinkBase link : enabledLinks) {
			if (link.getClass().isAssignableFrom(linkClass)) {
				return linkClass.cast(link);
			}
		}
		return null;
	}

	public void enableLinks() {
		for (LinkBase link : enabledLinks) {
			link.loadLink();
		}
		for (LinkBase link : enabledLinks) {
			link.postloadLink();
		}
	}

	public void loadLinks() {
		if (enabled) {
			throw new IllegalStateException("Links have already been enabled!");
		}
		for (Entry<Class<? extends LinkBase>, ModLink> e : pendingLinks.entrySet()) {
			boolean loaded = Loader.isModLoaded(e.getValue().modId());
			logger.info("ExGregilo " + (loaded ? "linked to " : "did not link to ") + e.getValue().modName());
			if (loaded) {
				enabledLinks.add(injector.getInstance(e.getKey()));
			}
		}
		pendingLinks.clear();
		for (int i = 0; i < 2; i++) {
			for (LinkBase link : enabledLinks) {
				switch (i) {
				case 0:
					link.initLink();
					break;
				case 1:
					link.preloadLink();
					break;
				}
			}
		}
		enabled = true;
	}

	@Override
	public ModLink getModLink(Class<? extends LinkBase> linkClass) {
		return linkClass.getAnnotation(ModLink.class);
	}
}
