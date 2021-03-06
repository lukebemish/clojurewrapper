package com.github.lukebemish.clojureunwrapper;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileLocator;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.stream.Stream;

public class ClojureUnwrapper extends AbstractJarFileLocator {
    private static final String NAME = "clojureunwrapper";
    private static final int BUFFER_SIZE = 8192;

    @Override
    public @NotNull String name() {
        return NAME;
    }

    @Override
    public void initArguments(Map<String, ?> arguments) {
        Path clojurePath = FMLPaths.GAMEDIR.get().resolve("clojure/");
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
    public Stream<Path> scanCandidates() {
        Path clojurePath = FMLPaths.GAMEDIR.get().resolve("clojure/");
        Path wrapperPath = clojurePath.resolve("wrapper.jar");
        Path loaderPath = clojurePath.resolve("loader.jar");

        return Stream.of(wrapperPath, loaderPath);
    }
}
