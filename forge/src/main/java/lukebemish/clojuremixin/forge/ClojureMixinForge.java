package lukebemish.clojuremixin.forge;

import lukebemish.clojuremixin.ClojureMixin;
import net.minecraftforge.fml.common.Mod;

@Mod(ClojureMixin.MOD_ID)
public class ClojureMixinForge {
    public ClojureMixinForge() {
        ClojureMixin.init();
    }
}
