package com.mygdx.game.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.NonogramGame;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.config.GameConfig;

public class IntroScreen extends ScreenAdapter
{
    public static final float INTRO_DURATION_IN_SEC = 3f;

    private final NonogramGame game;

    private final AssetManager assetManager;

    private Viewport viewport;
    private TextureAtlas gameplayAtlas;

    private float duration = 0f;

    private Stage stage;

    public IntroScreen(NonogramGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        // load assets
        assetManager.load(AssetDescriptors.UI_SKINR);
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.finishLoading();   // blocks until all assets are loaded

        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(createKeyhole());
        // Animacijo metuljev
        stage.addActor(createButterflyAnimation());

        // Dodajte naslov v sredino
        Label title = new Label("NONOGRAM", assetManager.get(AssetDescriptors.UI_SKINR));
        title.setPosition(
                viewport.getWorldWidth() / 2f - title.getWidth() / 2f,
                viewport.getWorldHeight() / 2f - title.getHeight() / 2f
        );
        stage.addActor(title);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1f, 1f, 1f, 0f);

        duration += delta;

        // go to the MenuScreen after INTRO_DURATION_IN_SEC seconds
        if (duration > INTRO_DURATION_IN_SEC) {
            game.setScreen(new MenuScreen(game));
        }

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

    private Actor createKeyhole() {
        Image circle = new Image(gameplayAtlas.findRegion(RegionNames.CIRCLE));

        // Nastavi velikost slike
        float initialScale = 1.0f;
        circle.setBounds(0, 0, circle.getWidth() * initialScale, circle.getHeight() * initialScale);

        // Nastavi os za povečanje v sredino slike
        circle.setOrigin(circle.getWidth() / 2f, circle.getHeight() / 2f);

        // Namesti sliko na sredino zaslona
        circle.setPosition(viewport.getWorldWidth() / 2f - circle.getWidth() / 2f,
                viewport.getWorldHeight() / 2f - circle.getHeight() / 2f);

        // Ustvari animacijo povečevanja in zmanjšanja
        float scaleFactor = 2.0f;
        float duration = 1.0f; // Čas trajanja ene faze animacije (v sekundah)

        SequenceAction sequenceAction = Actions.sequence(
                Actions.scaleTo(scaleFactor, scaleFactor, duration), // Poveča
                Actions.scaleTo(initialScale, initialScale, duration) // Zmanjša
        );

        // Ponovi animacijo neskončno
        circle.addAction(Actions.forever(sequenceAction));
        return circle;
    }

    private Group createButterflyAnimation() {
        Group butterflies = new Group();

        Image butterfly1 = createButterfly();
        Image butterfly2 = createButterfly();

        // Nastavi začetne pozicije metuljev izven zaslona
        float startX1 = -butterfly1.getWidth();
        float startY1 = viewport.getWorldHeight() / 2f - butterfly1.getHeight() / 2f;
        float startX2 = viewport.getWorldWidth();
        float startY2 = viewport.getWorldHeight() / 2f - butterfly2.getHeight() / 2f;

        butterfly1.setPosition(startX1, startY1);
        butterfly2.setPosition(startX2, startY2);

        // Ustvari animacijo za premikanje metuljev na sredino
        float duration = 1.5f;

        Action moveButterfly1 = Actions.sequence(
                Actions.moveTo((viewport.getWorldWidth() / 2f) - butterfly1.getWidth() / 2f,
                        (viewport.getWorldHeight() / 2f) - butterfly1.getHeight() / 2f, duration),
                Actions.removeActor()
        );

        Action moveButterfly2 = Actions.sequence(
                Actions.moveTo((viewport.getWorldWidth() / 2f) - butterfly2.getWidth() / 2f,
                        (viewport.getWorldHeight() / 2f) - butterfly2.getHeight() / 2f, duration),
                Actions.removeActor()
        );

        butterfly1.addAction(moveButterfly1);
        butterfly2.addAction(moveButterfly2);

        butterflies.addActor(butterfly1);
        butterflies.addActor(butterfly2);

        return butterflies;
    }

    private Image createButterfly() {
        Image butterfly = new Image(gameplayAtlas.findRegion(RegionNames.BUTERFLY));
        butterfly.setOrigin(Align.center);

        return butterfly;
    }



}
