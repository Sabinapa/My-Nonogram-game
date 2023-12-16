package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors
{
    public static final AssetDescriptor<Skin> UI_SKINR =
            new AssetDescriptor<Skin>(AssetPaths.UI_SKINR, Skin.class);

    public static final AssetDescriptor<TextureAtlas> GAMESTIL =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAMESTIL, TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> GAMEATLAS =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAMEATLAS, TextureAtlas.class);
    public static final AssetDescriptor<Music> MUSIC_MENU =
            new AssetDescriptor<>(AssetPaths.BACKGROUND_MUSIC, Music.class);

    public static final AssetDescriptor<Music> MUSIC_GAME =
            new AssetDescriptor<>(AssetPaths.GAME_MUSIC, Music.class);


    private AssetDescriptors() {
    }
}
