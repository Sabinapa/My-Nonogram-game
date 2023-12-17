package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.NonogramGame;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.config.GameConfig;

public class SettingsScreen extends ScreenAdapter
{
    private final NonogramGame game;

    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private boolean isTimerOn;
    private boolean isSoundOn;
    private boolean isMusicOn;

    private String gameMode;

    private SelectBox<String> difficultySelectBox;
    private Array<String> difficultyOptions = new Array<>(new String[]{"Easy", "Standard", "Hard"});


    public SettingsScreen(NonogramGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
        GameManager.initialize();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKINR);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMESTIL);

        // Naloži stanje časovnika iz nastavitev
        isTimerOn = GameManager.isTimerOn();
        isSoundOn = GameManager.isSoundOn();
        isMusicOn = GameManager.isMusicOn();
        gameMode = GameManager.getGameMode();

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

        // Shrani stanje časovnika v nastavitve
        GameManager.setTimerState(isTimerOn);
        GameManager.setSoundState(isSoundOn);
        GameManager.setMusicState(isMusicOn);
        GameManager.setGameMode(gameMode);
        GameManager.flush();

        stage.dispose();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Dodajte naslov
        Label title = new Label("Settings", skin, "title");
        title.setAlignment(Align.center);
        table.add(title).colspan(3).padBottom(20).row();

        // Dodajte CheckBox za vklop/izklop časovnika
        final CheckBox timerCheckBox = new CheckBox("Timer", skin);
        timerCheckBox.setChecked(isTimerOn);
        timerCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Posodobi stanje časovnika
                isTimerOn = timerCheckBox.isChecked();
            }
        });
        table.add(timerCheckBox).colspan(3).padTop(20).row();

        // Dodajte CheckBox za vklop/izklop zvoka
        final CheckBox soundCheckBox = new CheckBox("Sound", skin);
        soundCheckBox.setChecked(isSoundOn);
        soundCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Posodobi stanje zvoka
                isSoundOn = soundCheckBox.isChecked();
            }
        });
        table.add(soundCheckBox).colspan(3).padTop(20).row();

        // Dodajte CheckBox za vklop/izklop glasbe
        final CheckBox musicCheckBox = new CheckBox("Music", skin);
        musicCheckBox.setChecked(isMusicOn);
        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Posodobi stanje glasbe
                isMusicOn = musicCheckBox.isChecked();
            }
        });
        table.add(musicCheckBox).colspan(3).padTop(20).row();

        // Dodajte SelectBox za izbiro težavnostnega načina
        difficultySelectBox = new SelectBox<>(skin);
        difficultySelectBox.setItems(difficultyOptions);
        difficultySelectBox.setSelected(gameMode); // Nastavitev izbrane možnosti
        difficultySelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Posodobi stanje težavnostnega načina
                gameMode = difficultySelectBox.getSelected();
            }
        });
        table.add(new Label("Difficulty:", skin)).padTop(20).padRight(10);
        table.add(difficultySelectBox).padTop(20).colspan(3).row();

        // Gumb za vrnitev
        TextButton backButton = new TextButton("Back to Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        table.add(backButton).colspan(3).padTop(40);
    }

}
