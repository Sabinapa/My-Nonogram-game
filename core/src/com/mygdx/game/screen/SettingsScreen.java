package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.NonogramGame;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.config.GameConfig;

public class SettingsScreen extends ScreenAdapter
{
    private final NonogramGame game;

    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private boolean isTimerOn = true;
    private boolean isSoundOn = true;


    public SettingsScreen(NonogramGame game) {
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

        // Dodajte naslov
        Label title = new Label("Settings", skin, "title");
        title.setAlignment(Align.center);
        table.add(title).colspan(2).padBottom(20).row();

        // Dodajte gumb za vklop/izklop časovnika
        final TextButton timerButton = new TextButton("Timer: ON", skin);
        timerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Preklopi stanje časovnika
                isTimerOn = !isTimerOn;
                updateTimerButtonText(timerButton, isTimerOn);
            }
        });

        // Prilagoditev stanja gumba glede na trenutno stanje časovnika
        updateTimerButtonText(timerButton, isTimerOn);
        table.add(timerButton).colspan(2).padTop(20).row();

        // Dodajte gumb za vklop/izklop časovnika
        final TextButton SoundButton = new TextButton("Sound: ON", skin);
        SoundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Preklopi stanje časovnika
                isSoundOn = !isSoundOn;
                updateSoundButtonText(SoundButton, isSoundOn);
            }
        });

        // Prilagoditev stanja gumba glede na trenutno stanje časovnika
        updateSoundButtonText(SoundButton, isSoundOn);
        table.add(SoundButton).colspan(2).padTop(20).row();

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

    private void updateTimerButtonText(TextButton timerButton, boolean isTimerOn) {
        String buttonText = isTimerOn ? "Timer: ON" : "Timer: OFF";
        timerButton.setText(buttonText);
    }

    private void updateSoundButtonText(TextButton SoundButton, boolean isSoundOn) {
        String buttonText = isSoundOn ? "Sound: ON" : "Sound: OFF";
        SoundButton.setText(buttonText);
    }

}
