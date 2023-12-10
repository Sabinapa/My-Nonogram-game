package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.NonogramGame;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.config.GameConfig;

public class LeaderboardScreen extends ScreenAdapter {

    private final NonogramGame game;

    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private TextureAtlas gameplayAtlas;

    public LeaderboardScreen(NonogramGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKINR);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        createUI();
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1f, 1f, 1f, 1f); // Bela barva ozadja

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);

        stage.addActor(table);

        // Naslov
        Label title = new Label("Leaderboard", skin, "title");
        title.setAlignment(Align.center);
        table.add(title).colspan(2).padBottom(20).row();

        // Lestvico rezultatov
        addLeaderboardEntry(table, "Player1", 1000);
        addLeaderboardEntry(table, "Player2", 800);
        addLeaderboardEntry(table, "Player3", 600);

        // Gumb za vrnitev
        TextButton backButton = new TextButton("Back to Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        table.add(backButton).colspan(2).padTop(40);
    }

    private void addLeaderboardEntry(Table table, String playerName, int score) {
        Label nameLabel = new Label(playerName, skin);
        table.add(nameLabel).padRight(100);

        Label scoreLabel = new Label(String.valueOf(score), skin);
        table.add(scoreLabel).padBottom(10).row();
    }


}
