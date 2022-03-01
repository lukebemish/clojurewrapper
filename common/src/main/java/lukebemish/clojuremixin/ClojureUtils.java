package lukebemish.clojuremixin;

import clojure.lang.Symbol;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class ClojureUtils {
    @ExpectPlatform
    public static Object sideSpecific(Symbol symbol) {
        throw new AssertionError();
    }
}
