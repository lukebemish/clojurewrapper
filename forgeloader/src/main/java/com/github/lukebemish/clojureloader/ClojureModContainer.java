package com.github.lukebemish.clojureloader;

import net.minecraftforge.eventbus.EventBusErrorMessage;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Optional;

import static net.minecraftforge.fml.loading.LogMarkers.LOADING;

public class ClojureModContainer extends ModContainer {

    private static final Logger LOGGER = LogManager.getLogger(ClojureModContainer.class);
    private final ModFileScanData scanResults;
    private final String clojure;
    private Object modInstance;
    public final IEventBus eventBus;
    public final Class<?> baseClass;

    public ClojureModContainer(final IModInfo info, final String clojure, final ModFileScanData scanData, final ModuleLayer gameLayer) {
        super(info);
        this.scanResults = scanData;
        this.clojure = clojure;
        LOGGER.debug(Logging.LOADING,"Creating Clojure container for {} on classloader {} with layer {}", clojure, this.getClass().getClassLoader(), gameLayer);
        this.activityMap.put(ModLoadingStage.CONSTRUCT, this::constructMod);
        this.eventBus = BusBuilder.builder().setExceptionHandler(this::onEventFailed).setTrackPhases(false).markerType(IModBusEvent.class).build();
        this.configHandler = Optional.of(event -> eventBus.post(event.self()));
        final ClojureModLoadingContext contextExtension = new ClojureModLoadingContext(this);
        this.contextExtension = () -> contextExtension;
        try {
            Module layer = gameLayer.findModule(ClojureLoader.API_MODID).orElseThrow();
            this.baseClass = Class.forName(layer, "com.github.lukebemish.clojurewrapper.loader.ClojureModWrapper");
            LOGGER.trace(LogMarkers.LOADING, "Loaded clojure runtime wrapper {}", layer.getClassLoader());
        } catch (NoSuchElementException ex) {
            LOGGER.info("ModIDs present... {}",scanData.getIModInfoData().stream().flatMap(x->x.getMods().stream()).map(IModInfo::getModId).toList());
            LOGGER.info("Mod info present... {}",scanData.getIModInfoData().stream().map(IModFileInfo::moduleName).toList());
            RuntimeException e = new RuntimeException("ClojureWrapper API doesn't exist!", ex);
            LOGGER.fatal(LogMarkers.LOADING, "ClojureWrapper API is not present, failing terribly...", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error(LogMarkers.LOADING, "Failed to load clojure runtime wrapper!", e);
            throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e);
        }
    }

    private void onEventFailed(IEventBus eventBus, Event event, IEventListener[] listeners, int busId, Throwable throwable) {
        LOGGER.error(new EventBusErrorMessage(event, busId, listeners, throwable));
    }

    private void constructMod() {
        try {
            LOGGER.trace(LogMarkers.LOADING, "Loading mod instance {} at {}",getModId(),clojure);
            this.modInstance = baseClass.getDeclaredConstructor(String.class).newInstance(clojure);
            LOGGER.trace(LogMarkers.LOADING, "Loaded mod instance {} at {}",getModId(),clojure);
        } catch (Exception e) {
            LOGGER.error(LogMarkers.LOADING, "Error running mod instance. ModID: {}, Clojure: {}", getModId(), clojure, e);
            throw new ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", e, clojure);
        }
    }

    @Override
    public boolean matches(Object mod) {
        return mod == this.modInstance;
    }

    @Override
    public Object getMod() {
        return this.modInstance;
    }

    public IEventBus getEventBus()
    {
        return this.eventBus;
    }

    @Override
    protected <T extends Event & IModBusEvent> void acceptEvent(final T e) {
        try {
            LOGGER.trace(LOADING, "Firing event for modid {} : {}", this.getModId(), e);
            this.eventBus.post(e);
            LOGGER.trace(LOADING, "Fired event for modid {} : {}", this.getModId(), e);
        } catch (Throwable t) {
            LOGGER.error(LOADING,"Caught exception during event {} dispatch for modid {}", e, this.getModId(), t);
            throw new ModLoadingException(modInfo, modLoadingStage, "fml.modloading.errorduringevent", t);
        }
    }
}
