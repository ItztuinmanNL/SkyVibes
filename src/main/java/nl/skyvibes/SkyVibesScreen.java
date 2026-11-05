package nl.skyvibes;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.Random;

/** First interactive SkyVibes client-style menu, scaled to the Minecraft window. */
public final class SkyVibesScreen extends Screen {
    private static final int BG = 0xF20A0A1B;
    private static final int PANEL = 0xFF17152D;
    private static final int CARD = 0xFF24203F;
    private static final int PURPLE = 0xFFAD6EFF;
    private static final int MUTED = 0xFFADA4C7;
    private static final String[] TABS = {"Sky", "Lyrics", "PvP", "Fonts", "Themes", "Colors", "Animations"};
    private String tab = "Sky";
    private int x, y, w, h, sidebar, preview;

    public SkyVibesScreen() { super(Component.literal("SkyVibes")); }

    @Override
    protected void init() {
        w = Math.min(880, Math.max(320, this.width - 24));
        h = Math.min(510, Math.max(210, this.height - 24));
        x = (this.width - w) / 2;
        y = (this.height - h) / 2;
        sidebar = Math.min(150, Math.max(95, w / 5));
        preview = Math.min(220, Math.max(120, w / 4));
        createButtons();
    }

    private void createButtons() {
        clearWidgets();
        int tabHeight = Math.min(38, Math.max(22, (h - 84) / TABS.length));
        for (int i = 0; i < TABS.length; i++) {
            String name = TABS[i];
            addRenderableWidget(Button.builder(Component.literal(name), b -> {
                tab = name;
                createButtons();
            }).bounds(x + 10, y + 68 + i * tabHeight, sidebar - 20, tabHeight - 4).build());
        }
        addRenderableWidget(Button.builder(Component.literal("X"), b -> onClose())
                .bounds(x + w - 32, y + 10, 22, 20).build());
        if (!tab.equals("Sky")) return;
        int cx = x + sidebar + 12;
        int cw = w - sidebar - preview - 36;
        int bw = Math.max(58, (cw - 8) / 2);
        addRenderableWidget(Button.builder(Component.literal(SkyVibesConfig.skyEnabled() ? "Sky: ON" : "Sky: OFF"), b -> {
            SkyVibesConfig.toggleSky(); createButtons();
        }).bounds(cx, y + 68, cw, 22).build());
        String[] names = {"Galaxy", "Sunset", "Sakura", "Ocean", "Rainy"};
        for (int i = 0; i < names.length; i++) {
            String theme = names[i];
            int tx = cx + (i % 2) * (bw + 8);
            int ty = y + 116 + (i / 2) * 28;
            addRenderableWidget(Button.builder(Component.literal((theme.equals(SkyVibesConfig.theme()) ? "* " : "") + theme), b -> {
                SkyVibesConfig.setTheme(theme); createButtons();
            }).bounds(tx, ty, bw, 23).build());
        }
        addRenderableWidget(Button.builder(Component.literal("Stars: " + onOff(SkyVibesConfig.starsEnabled())), b -> {
            SkyVibesConfig.toggleStars(); createButtons();
        }).bounds(cx, y + 216, cw, 22).build());
        addRenderableWidget(Button.builder(Component.literal("Nebula: " + onOff(SkyVibesConfig.nebulaEnabled())), b -> {
            SkyVibesConfig.toggleNebula(); createButtons();
        }).bounds(cx, y + 246, cw, 22).build());
        addRenderableWidget(Button.builder(Component.literal("Planets: " + onOff(SkyVibesConfig.planetsEnabled())), b -> {
            SkyVibesConfig.togglePlanets(); createButtons();
        }).bounds(cx, y + 276, cw, 22).build());
    }

    private String onOff(boolean state) { return state ? "ON" : "OFF"; }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        // Draw background BEFORE the widgets so the controls remain clickable and visible.
        g.fill(0, 0, this.width, this.height, 0xB8000010);
        g.fill(x, y, x + w, y + h, BG);
        g.outline(x, y, w, h, PURPLE);
        g.fill(x, y + 47, x + w, y + 48, 0xFF4B3374);
        g.fill(x + sidebar, y + 48, x + sidebar + 1, y + h, 0xFF32294B);
        g.text(this.font, "SkyVibes", x + 17, y + 15, PURPLE, true);
        g.text(this.font, "v0.3 | Minecraft 26.2", x + 87, y + 16, MUTED, false);
        g.text(this.font, tab, x + sidebar + 14, y + 53, 0xFFFFFFFF, true);
        int px = x + w - preview - 9;
        int py = y + 78;
        int pw = preview - 8;
        int ph = Math.max(105, h - 142);
        g.fill(px - 5, py - 24, px + pw + 5, py + ph + 37, PANEL);
        g.outline(px - 5, py - 24, pw + 10, ph + 61, 0xFF49336B);
        g.text(this.font, "PREVIEW", px + 4, py - 16, 0xFFE8DEFF, true);
        // This is an intentionally stylized preview, not a screenshot from in-world sky rendering.
        for (int row = 0; row < ph; row++) {
            float t = row / (float)Math.max(1, ph);
            int r = (int)(18 + 60 * t), gr = (int)(12 + 21 * t), b = (int)(46 + 75 * t);
            int color = 0xFF000000 | (r << 16) | (gr << 8) | b;
            g.fill(px, py + row, px + pw, py + row + 1, color);
        }
        Random random = new Random(6339);
        if (SkyVibesConfig.starsEnabled()) {
            for (int i = 0; i < 90; i++) {
                int sx = px + random.nextInt(Math.max(1, pw));
                int sy = py + random.nextInt(Math.max(1, ph * 3 / 4));
                int color = i % 4 == 0 ? 0xFFDC9EFF : 0xFFDDD9FF;
                g.fill(sx, sy, sx + 1 + (i % 9 == 0 ? 1 : 0), sy + 1, color);
            }
        }
        if (SkyVibesConfig.nebulaEnabled()) {
            for (int i = 0; i < 16; i++) {
                int yy = py + ph / 4 + i * 2;
                int xx = px + pw / 4 + (i * pw / 42);
                g.fill(xx, yy, Math.min(px + pw, xx + pw / 4), yy + 1, 0xFF743A9A);
            }
        }
        if (SkyVibesConfig.planetsEnabled()) {
            int mx = px + pw * 4 / 5, my = py + ph / 5;
            g.fill(mx - 3, my - 3, mx + 4, my + 4, 0xFFC6B6F5);
            g.outline(mx - 4, my - 4, 9, 9, 0xFF856BCA);
        }
        g.text(this.font, SkyVibesConfig.theme(), px + 6, py + ph + 11, 0xFFFFFFFF, true);
        g.text(this.font, SkyVibesConfig.isGalaxyEnabled() ? "Sky tint: active" : "Sky tint: inactive", px + 6, py + ph + 24, MUTED, false);
        if (!tab.equals("Sky")) {
            g.text(this.font, tab + " settings are planned", x + sidebar + 15, y + 114, MUTED, false);
            g.text(this.font, "for a future version.", x + sidebar + 15, y + 128, MUTED, false);
        } else {
            g.text(this.font, "Choose a sky theme", x + sidebar + 13, y + 98, 0xFFE4D5FC, true);
            g.text(this.font, "Galaxy changes the in-game", x + sidebar + 13, y + 327, MUTED, false);
            g.text(this.font, "sky color on your client.", x + sidebar + 13, y + 340, MUTED, false);
            g.text(this.font, "Other themes: preview only.", x + sidebar + 13, y + 360, MUTED, false);
        }
        super.extractRenderState(g, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() { this.minecraft.gui.setScreen(null); }
}
