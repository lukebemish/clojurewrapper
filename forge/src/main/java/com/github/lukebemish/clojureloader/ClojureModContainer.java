package com.github.lukebemish.clojureloader;

import net.minecraftforge.eventbus.EventBus;
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
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ClojureModContainer extends ModContainer {

    private static final Logger LOGGER = LogManager.getLogger(ClojureModContainer.class);

    private String clojure;
    private Object modInstance;
    public IEventBus eventBus;
    public Class<?> clojureClass;

    public ClojureModContainer(final IModInfo info, final String clojure, final ModFileScanData scanData, final ModuleLayer gameLayer) {
        super(info);
        this.clojure = clojure;
        LOGGER.debug(Logging.LOADING,"Creating Clojure container for {} on classloader {} with layer {}", clojure, this.getClass().getClassLoader(), gameLayer);

        activityMap.put(ModLoadingStage.CONSTRUCT, this::constructMod);
        eventBus = new EventBus(BusBuilder.builder().setExceptionHandler(this::onEventFailed).setTrackPhases(false).markerType(IModBusEvent.class));

        configHandler = Optional.of(event -> eventBus.post(event.self()));

        contextExtension = () -> new ClojureModLoadingContext(this);

        try {
            Module layer = gameLayer.findModule(info.getOwningFile().moduleName()).orElseThrow();
            clojureClass = Class.forName("clojure.java.api.Clojure");
            LOGGER.trace(LogMarkers.LOADING, "Loaded clojure {} with {}", ClojureModWrapper.class.getName(), ClojureModWrapper.class.getClassLoader());
        } catch (Exception e) {
            LOGGER.error(LogMarkers.LOADING, "Failed to load clojure {}", clojure, e);
            throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e);
        }
    }

    private void onEventFailed(IEventBus eventBus, Event event, IEventListener[] listeners, int busId, Throwable throwable) {
        LOGGER.error(new EventBusErrorMessage(event, busId, listeners, throwable));
    }

    private void constructMod() {
        try {
            LOGGER.trace(LogMarkers.LOADING, "Loading mod instance {} of type {}",getModId(), ClojureModWrapper.class.getName());
            modInstance = new ClojureModWrapper(this.clojure);
            LOGGER.trace(LogMarkers.LOADING, "Loaded mod instance {} of type {}",getModId(), ClojureModWrapper.class.getName());
        } catch (Exception e) {
            LOGGER.error(LogMarkers.LOADING, "Failed to create mod instance. ModID: {}, class {}", getModId(), ClojureModWrapper.class.getName(), e);
            throw new ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", e, ClojureModWrapper.class);
        }
    }

    @Override
    public boolean matches(Object mod) {
        return mod == modInstance;
    }

    @Override
    public Object getMod() {
        return modInstance;
    }
}
