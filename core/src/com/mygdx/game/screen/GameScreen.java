package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.CellState;
import com.mygdx.game.NonogramGame;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.config.GameConfig;

public class GameScreen extends ScreenAdapter
{
    private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);

    private final NonogramGame game;
    private final AssetManager assetManager;
    private Viewport viewport;

    private Viewport hudViewport;

    private Stage gameplayStage;

    private Stage hudStage;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private TextureAtlas gameAtlas;

    private Music backgroundMusic;

    private boolean useCross = true;

    public GameScreen(NonogramGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
        GameManager.initialize();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        gameplayStage = new Stage(viewport, game.getBatch());
        hudStage = new Stage(hudViewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKINR);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMESTIL);
        gameAtlas = assetManager.get(AssetDescriptors.GAMEATLAS);

        // Nalaganje glasbe
        backgroundMusic =  assetManager.get(AssetDescriptors.MUSIC_GAME);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f); // Nastavite glasnost glede na potrebe

        // Predvajanje glasbe
        if (GameManager.isMusicOn()) {
            backgroundMusic.play();
        }

        position();


        Gdx.input.setInputProcessor(new InputMultiplexer(gameplayStage, hudStage));
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1f, 1f, 1f, 1f); // Bela barva ozadja

        // update
        gameplayStage.act(delta);
        hudStage.act(delta);

        // draw
        gameplayStage.draw();
        hudStage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose()
    {
        backgroundMusic.dispose();
        gameplayStage.dispose();
        hudStage.dispose();
    }

    private void position() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);

        // Vrstica za gumb za nazaj, srčke in časovnik
        Table topRow = new Table();
        TextButton backButton = new TextButton("Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        //ImageButton heartsButton = new ImageButton(heartButtonStyle);  // Prilagoditev stila srčkov

        topRow.add(backButton).pad(10);
        //topRow.add(heartsButton).pad(10);


        // Vrstica za igro
        Table gameTable = new Table();
        gameTable.add(createGrid(5, 5, 50)).pad(10);  // Prilagoditev glede na vaše potrebe


        // Dodaj vse vrstice v korenasto tabelo
        rootTable.top().add(topRow).padTop(10).padRight(200);
        rootTable.row().expandY();
        rootTable.add(gameTable).expand();

        addShapeSelectionButtons(rootTable);

        // Dodaj korenasto tabelo v glavno odri pod hudStage
        hudStage.addActor(rootTable);

    }

    private Actor createGrid(int rows, int columns, final float cellSize) {
        final Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)

        final Table grid = new Table();
        grid.defaults().size(cellSize);   // all cells will be the same size
        //grid.setDebug(true, true);

        final TextureRegion emptyRegion = gameAtlas.findRegion(RegionNames.HOVER);
        final TextureRegion cross = gameAtlas.findRegion(RegionNames.CROSS);
        final TextureRegion square = gameAtlas.findRegion(RegionNames.SQARE);

        // Loop za dodajanje celic v mrežo
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                final Image cellImage = new Image(emptyRegion);
                cellImage.setTouchable(Touchable.enabled); // Omogoči dotik na posamezni celici
                cellImage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (useCross) {
                            cellImage.setDrawable(new TextureRegionDrawable(cross));
                        } else {
                            cellImage.setDrawable(new TextureRegionDrawable(square));
                        }
                    }
                });

                grid.add(cellImage);  // Dodaj celico v mrežo z določenim odmikom (pad)
            }
            grid.row();  // Preklopi na naslednjo vrsto
        }

        // Dodaj mrežo v glavno tabelo
        table.add(grid).expand().align(Align.center);
        return table;
    }

    private void addShapeSelectionButtons(Table rootTable)
    {
            final TextureRegion Kriz = gameAtlas.findRegion(RegionNames.IZBIRAK); // Nastavite vašo prvo sliko
            final TextureRegion Kvadrat = gameAtlas.findRegion(RegionNames.IZBIRAKV); // Nastavite vašo drugo sliko

            TextureRegionDrawable imageDrawable1 = new TextureRegionDrawable(Kriz);
            TextureRegionDrawable imageDrawable2 = new TextureRegionDrawable(Kvadrat);

            Image image1 = new Image(imageDrawable1);
            Image image2 = new Image(imageDrawable2);

            // Dodajte poslušalce za obdelavo klike
        image1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    useCross = true;
                }
            });

        image2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    useCross = false;
                }
            });

        // Dodajte gumbe na odri
        Table table = new Table();
        table.add(image1).pad(10);
        table.add(image2).pad(10);

        // Postavitev tabele na sredino hudStage
        //table.center();
        //table.setFillParent(true);

        rootTable.row().bottom().padBottom(20); // Dodajte odmik pod zadnjo vrstico
        rootTable.add(table).padBottom(100); // Dodajte slike pod mrežo
        }


}
