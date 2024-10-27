package top.fifthlight.touchcontroller.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface ClientOpenChatScreenMixin {
    @Invoker("openChatScreen")
    void openChatScreen(String text);
}
