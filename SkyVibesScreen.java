package nl.skyvibes;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SkyVibesScreen extends Screen {
    private Button galaxyButton;

    public SkyVibesScreen() {
        super(Component.literal("SkyVibes Settings"));
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 100;
        int top = this.height / 2 - 25;

        galaxyButton = this.addRenderableWidget(Button.builder(galaxyLabel(), button -> {
            SkyVibesConfig.setGalaxyEnabled(!SkyVibesConfig.isGalaxyEnabled());
            button.setMessage(galaxyLabel());
        }).bounds(left, top, 200, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> onClose())
                .bounds(left, top + 30, 200, 20).build());
    }

    private Component galaxyLabel() {
        return Component.literal("Galaxy theme: " + (SkyVibesConfig.isGalaxyEnabled() ? "ON" : "OFF"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.text(this.font, "SKYVIBES  v0.2", this.width / 2 - 52,
                this.height / 2 - 55, 0xFFB99AFF, true);
        graphics.text(this.font, "Galaxy rendering is coming next", this.width / 2 - 91,
                this.height / 2 + 38, 0xFFA8A8A8, true);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(null);
    }
}
