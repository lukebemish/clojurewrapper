package com.github.lukebemish.clojureunwrapper;

import cpw.mods.jarhandling.SecureJar;
import cpw.mods.modlauncher.api.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClojureUnwrapper implements ITransformationService {
    private static final String NAME = "clojureunwrapper";
    private static final int BUFFER_SIZE = 8192;

    @Override
    public @NotNull String name() {
        return NAME;
    }

    @Override
    public void initialize(IEnvironment environment) {

    }

    @Override
    public List<Resource> beginScanning(IEnvironment environment) {
        List<Resource> out = new ArrayList<>();

        Path clojurePath = environment.getProperty(IEnvironment.Keys.GAMEDIR.get()).orElseThrow(() -> new RuntimeException("No game path found"))
                .resolve("clojure/");
        Path wrapperPath = clojurePath.resolve("wrapper.jar");
        Path loaderPath = clojurePath.resolve("loader.jar");

        if (notMatching("/jars/wrapper.jar", wrapperPath)) {
            try {
                InputStream isWrapper = getClass().getResourceAsStream("/jars/wrapper.jar");
                if (isWrapper == null) throw new IOException("Couldn't find clojure runtime wrapper jar!");
                if (!Files.exists(clojurePath)) {
                    Files.createDirectory(clojurePath);
                }
                FileOutputStream outputStream = new FileOutputStream(wrapperPath.toFile(), false);
                int read;
                byte[] bytes = new byte[BUFFER_SIZE];
                while ((read = isWrapper.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        if (notMatching("/jars/loader.jar", loaderPath)) {
            try {
                InputStream isLoader = getClass().getResourceAsStream("/jars/loader.jar");
                if (isLoader == null) throw new IOException("Couldn't find clojure language loader jar!");
                if (!Files.exists(clojurePath)) {
                    Files.createDirectory(clojurePath);
                }
                FileOutputStream outputStream = new FileOutputStream(loaderPath.toFile(), false);
                int read;
                byte[] bytes = new byte[BUFFER_SIZE];
                while ((read = isLoader.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        out.add(new Resource(IModuleLayerManager.Layer.PLUGIN, List.of(SecureJar.from(loaderPath))));
        out.add(new Resource(IModuleLayerManager.Layer.GAME, List.of(SecureJar.from(wrapperPath))));

        return out;
    }

    private static boolean notMatching(String internal, Path external) {
        if (!Files.exists(external) || Files.isDirectory(external)) return true;
        try {
            FileInputStream ex = new FileInputStream(external.toFile());
            byte[] exHash = checksum(ex);
            InputStream in = ClojureUnwrapper.class.getResourceAsStream(internal);
            if (in==null) return false; //in this case, we don't want to try and copy the input...
            byte[] inHash = checksum(in);
            return !MessageDigest.isEqual(exHash, inHash);
        } catch (IOException | NoSuchAlgorithmException e) {
            return true;
        }
    }

    private static byte[] checksum(InputStream is) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[BUFFER_SIZE];
        int numOfBytesRead;
        while( (numOfBytesRead = is.read(buffer)) > 0){
            md.update(buffer, 0, numOfBytesRead);
        }
        return md.digest();
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {

    }

    @Override
    public @NotNull List<ITransformer> transformers() {
        return List.of();
    }
}
