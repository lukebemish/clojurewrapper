package com.github.lukebemish.clojureloader;


import com.github.lukebemish.clojurewrapper.util.Pair;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.forgespi.language.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ClojureLoader implements IModLanguageProvider {
    public static final String NAME = "clojurewrapper";
    private static final Logger LOGGER = LogManager.getLogger(ClojureLoader.class);

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Consumer<ModFileScanData> getFileVisitor() {
        LOGGER.info("Visiting files...");
        return scanData -> {
            var clojures = scanData.getIModInfoData().stream().flatMap((x)->x.getMods().stream())
                    .filter((x)->x instanceof IConfigurable)
                    .map((x)->new Pair<IModInfo,Optional<String>>(x,((IConfigurable) x).getConfigElement("clojure")))
                    .filter((x)->(x.last().isPresent()))
                    .map((x)-> new ClojureLanguageLoader(x.last().get(), x.first().getModId()))
                    .peek((x)->LOGGER.debug("Found entry-point \"{}\" for ID \"{}\"",x.clojure,x.modid))
                    .collect(Collectors.toMap(
                            (x) -> x.modid,
                            (x) -> x,
                            (a,b) -> {
                                LOGGER.warn("Found duplicated clojure entries for ID \"{}\": \"{}\" and \"{}\", only the first will be kept", a.modid, a.clojure, b.clojure);
                                return a;
                            },
                            LinkedHashMap::new
                    ));
            scanData.addLanguageLoader(clojures);
        };
    }

    @Override
    public <R extends ILifecycleEvent<R>> void consumeLifecycleEvent(Supplier<R> consumeEvent) {}

    private static class ClojureLanguageLoader implements IModLanguageLoader {
        public final String clojure;
        public final String modid;

        ClojureLanguageLoader(String clojure, String modid) {
            this.clojure = clojure;
            this.modid = modid;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T loadMod(IModInfo info, ModFileScanData modFileScanResults, ModuleLayer layer) {
            var threadLoader = Thread.currentThread().getContextClassLoader();

            try {
                var clojureContainer = Class.forName("com.github.lukebemish.clojureloader.ClojureModContainer", true, threadLoader);
                LOGGER.debug(LogMarkers.LOADING, "Loading ClojureModContainer from classloader {} - got {}}",threadLoader,clojureContainer.getClassLoader());
                var constructor = clojureContainer.getConstructor(IModInfo.class, String.class, ModFileScanData.class, ModuleLayer.class);
                return (T)constructor.newInstance(info, clojure, modFileScanResults, layer);
            } catch (InvocationTargetException e) {
                LOGGER.fatal(LogMarkers.LOADING, "Failed to build mod", e);
                var targetException = e.getTargetException();
                if (targetException instanceof ModLoadingException toThrow) {
                    throw toThrow;
                } else {
                    throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                failTerribly(info, e);
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        private void failTerribly(IModInfo info, Exception exception) throws RuntimeException {
            LOGGER.fatal(LogMarkers.LOADING, "Unable to load ClojureModContainer, failing terribly...", exception);
            try {
                // ModLoadingException
                Class<RuntimeException> mle = (Class<RuntimeException>) Class.forName("net.minecraftforge.fml.ModLoadingException", true, Thread.currentThread().getContextClassLoader());
                // ModLoadingStage
                Class<ModLoadingStage> mls = (Class<ModLoadingStage>) Class.forName("net.minecraftforge.fml.ModLoadingStage", true, Thread.currentThread().getContextClassLoader());
                // Throw a new ModLoadingException containing the exception
                throw mle.getConstructor(IModInfo.class, mls, String.class, Throwable.class).newInstance(info, java.lang.Enum.valueOf(mls, "CONSTRUCT"), "fml.modloading.failedtoloadmodclass", exception);
            } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Couldn't load core FML classes. Something is very, very broken...");
            }
        }
    }
}
