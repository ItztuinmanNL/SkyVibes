package nl.skyvibes;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public final class SkyVibesClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("skyvibes", "main"));

    @Override
    public void onInitializeClient() {
        SkyVibesConfig.load();

        KeyMapping openMenu = KeyBindingHelper.registerKeyMapping(new KeyMapping(
                "key.skyvibes.open_menu",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_RIGHT_SHIFT,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.consumeClick()) {
                if (client.player != null && client.gui.screen() == null) {
                    client.gui.setScreen(new SkyVibesScreen());
                }
            }
        });
        System.out.println("[SkyVibes] Menu keybind registered.");
    }
}
