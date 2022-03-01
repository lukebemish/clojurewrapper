package lukebemish.clojuremixin.fabric;

import lukebemish.clojuremixin.ClojureMixin;
import net.fabricmc.api.ModInitializer;

public class ClojureMixinFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ClojureMixin.init();
    }
}
