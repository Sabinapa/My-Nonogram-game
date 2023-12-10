package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors
{
    public static final AssetDescriptor<Skin> UI_SKINR =
            new AssetDescriptor<Skin>(AssetPaths.UI_SKINR, Skin.class);

    public static final AssetDescriptor<TextureAtlas> GAMEPLAY =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAMEPLAY, TextureAtlas.class);

    private AssetDescriptors() {
    }
}
