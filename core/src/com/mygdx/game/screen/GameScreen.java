package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.NonogramGame;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.common.Levels;
import com.mygdx.game.config.GameConfig;
import com.badlogic.gdx.utils.Timer.Task;

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

    private Sound clapSound;

    private TextureRegion heart;

    private boolean useCross = true;

    private int wrongCount = 0;

    private Table topRow;

    private float timeLeftEasy = 60;
    private float timeLeftStandard = 120;

    private float timeLeft = 0;

    private int winEasy = 16;

    private int winStandard = 20;
    private int countWin = 0;

    private int[][] easyLevelMatrix = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    };

    private int[][] standardLevelMatrix = {
            {1, 0, 0, 0},
            {0, 1, 1, 0},
            {1, 0, 1, 0},
            {0, 0, 0, 1},
            {0, 0, 0, 1}
    };

    private int[][] easyLevelRows = {
            {3},
            {1, 2},
            {2, 1},
            {3}
    };
    private int[][] easyLevelColumns = {
            {3},
            {1, 2},
            {2, 1},
            {3}
    };

    private int[][] standardLevelColumns = {
            {1, 2},
            {1, 3},
            {1, 2},
            {3}
    };

    private int[][] standardLevelRows = {
            {3},
            {1, 1},
            {1, 1},
            {3},
            {3}
    };


    public GameScreen(NonogramGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
        GameManager.initialize();
    }

    @Override
    public void show()
    {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        gameplayStage = new Stage(viewport, game.getBatch());
        hudStage = new Stage(hudViewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKINR);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMESTIL);
        gameAtlas = assetManager.get(AssetDescriptors.GAMEATLAS);

        // Nalaganje glasbe
        clapSound = assetManager.get(AssetDescriptors.CLAP_SOUND);
        backgroundMusic =  assetManager.get(AssetDescriptors.MUSIC_GAME);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f); // Nastavite glasnost glede na potrebe

        // Predvajanje glasbe
        if (GameManager.isMusicOn()) {
            backgroundMusic.play();
        }

        if(GameManager.getGameMode() == "Easy")
        {
            timeLeft = timeLeftEasy;
        }
        else if(GameManager.getGameMode() == "Standard")
        {
            timeLeft = timeLeftStandard;
        }

        // Nastavitev časovnika, če je vklopljen
        if (GameManager.isTimerOn()) {
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    updateTimer();
                }
            }, 1, 1, (int) timeLeft);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }

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

    private void updateTimer() {
        timeLeft--;

        if (timeLeft <= 0) {
            handleGameOver();
        }

        // Update the timer label text
        Label timerLabel = (Label) topRow.findActor("timerLabel");
        if (timerLabel != null) {
            timerLabel.setText("Time: " + String.format("%.0f", timeLeft));
        }
    }

    private void handleGameOver() {
        System.out.println("Game Over!");
        final Dialog gameOverDialog = new Dialog("Game Over", skin) {
            @Override
            protected void result(Object object) {
                if (object instanceof Boolean) {
                    boolean result = (Boolean) object;
                    if (result) {
                        game.setScreen(new GameScreen(game));
                    } else {
                        game.setScreen(new MenuScreen(game));
                    }
                }
            }
        };

        gameOverDialog.text("You lost! Try again?");

        TextButton okButton = new TextButton("OK", skin);
        gameOverDialog.button(okButton, true);

        TextButton noButton = new TextButton("No", skin);
        gameOverDialog.button(noButton, false);

        gameOverDialog.show(hudStage);
    }

    private void updateHeartImage()
    {
        Image heartsImage = (Image) topRow.findActor("heartsImage");
        if (heartsImage != null) {
            if (wrongCount == 0) {
                heart = gameAtlas.findRegion(RegionNames.HEART3);
            } else if (wrongCount == 1) {
                heart = gameAtlas.findRegion(RegionNames.HEART2);
            } else if (wrongCount == 2) {
                heart = gameAtlas.findRegion(RegionNames.HEART);
            }
            else if(wrongCount == 3)
            {
                handleGameOver();
            }

            heartsImage.setDrawable(new TextureRegionDrawable(heart));
        }
    }

    private void position() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        //rootTable.setDebug(true);

        // Vrstica za gumb za nazaj, srčke in časovnik
        topRow = new Table();
        //topRow.setDebug(true);

        //HEARTS
        heart = gameAtlas.findRegion(RegionNames.HEART3);
        Image heartsImage = new Image(heart);
        heartsImage.setName("heartsImage");

        //TIMER
        Label timerLabel = null;
        if(GameManager.isTimerOn())
        {
            timerLabel = new Label("Time: " + String.format("%.0f", timeLeft), skin);
            timerLabel.setName("timerLabel"); // Set a unique name for the label
        }
        topRow.add(heartsImage).pad(10).padLeft(200); // Dodajte srčke v levi kot
        topRow.add().expandX(); // Prilagodite prostor med srčki in časovnikom
        topRow.add(timerLabel).pad(10).right(); // Dodajte časovnik v desni kot

        int[][] levelMatrixRows = null;
        int[][] levelMatrixColumns = null;

        if (GameManager.getGameMode().equals("Easy")) {
            levelMatrixRows = easyLevelRows;
            levelMatrixColumns = easyLevelColumns;
        } else if (GameManager.getGameMode().equals("Standard")) {
            levelMatrixRows = standardLevelRows;
            levelMatrixColumns = standardLevelColumns;
        }

        // ROWS LEVO OD TABELE
        Table rowsTable = new Table();
        //rowsTable.setDebug(true);  // Nastavi za prikaz robu
        for (int i = 0; i < levelMatrixRows.length; i++) {
            int[] rowValues = levelMatrixRows[i];
            StringBuilder rowText = new StringBuilder();

            for (int j = 0; j < rowValues.length; j++) {
                rowText.append(rowValues[j]);
                if (j < rowValues.length - 1) {
                    rowText.append(" ");
                }
            }

            Label rowLabel = new Label(rowText.toString(), skin);
            rowsTable.add(rowLabel).pad(13).left();
            rowsTable.row();
        }

        // Tabela za prikaz vsebine easyLevelColumns
        Table columnsTable = new Table();
        //columnsTable.setDebug(true);  // Nastavi za prikaz robu
        for (int i = 0; i < levelMatrixColumns.length; i++) {
            int[] columnValues = levelMatrixColumns[i];
            Table columnValuesTable = new Table();

            for (int j = 0; j < columnValues.length; j++) {
                int value = columnValues[j];
                Label columnLabel = new Label(String.valueOf(value), skin);
                columnValuesTable.add(columnLabel).pad(3);
                columnValuesTable.row();
            }
            columnsTable.add(columnValuesTable).pad(14);
        }


        // GAME TABLE
        Table gameTable = new Table();
        if(GameManager.getGameMode() == "Easy")
        {
            gameTable.add(createGrid(4, 4, 50)).pad(10).center();
        }
        else if(GameManager.getGameMode() == "Standard")
        {
            gameTable.add(createGrid(5, 4, 50)).pad(10).center();
        }

        // Skupna tabela, ki vsebuje obe tabeli
        Table contentTable = new Table();
        //contentTable.setDebug(true);  // Nastavi za prikaz robu
        contentTable.add(columnsTable).padTop(10).padRight(-320);  // Dodajte tabelo stolpcev nad gameTable
        contentTable.row();
        contentTable.add(rowsTable).padTop(5).padLeft(10);  // Dodajte tabelo vrst levo od gameTable
        contentTable.add(gameTable).pad(10);

        // vrstice v korenasto tabelo
        rootTable.top().add(topRow);
        rootTable.row().expandY();
        rootTable.add(contentTable).expand();

        addShapeSelectionButtons(rootTable);

        // korenasta tabela v glavno odri pod hudStage
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
        final TextureRegion wrong = gameAtlas.findRegion(RegionNames.WRONG);
        int value = 0;

        // Loop za dodajanje celic v mrežo
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++)
            {
                if(GameManager.getGameMode() == "Easy")
                {
                    value = easyLevelMatrix[row][col];
                }
                else if(GameManager.getGameMode() == "Standard")
                {
                    value = standardLevelMatrix[row][col];
                }
                final Image cellImage = new Image(emptyRegion);
                cellImage.setTouchable(Touchable.enabled); // Omogoči dotik na posamezni celici
                final int finalValue = value;
                cellImage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (useCross && finalValue == 1)
                        {
                            cellImage.setDrawable(new TextureRegionDrawable(cross));
                            countWin++;
                            if (checkIfWon()) {
                                handleGameWon();
                            }
                        }
                        else if (!useCross && finalValue == 0)
                        {
                            cellImage.setDrawable(new TextureRegionDrawable(square));
                            countWin++;
                            if (checkIfWon()) {
                                handleGameWon();
                            }
                        }
                        else if(useCross && finalValue == 0)
                        {
                            cellImage.setDrawable(new TextureRegionDrawable(wrong));

                            SequenceAction sequenceAction = Actions.sequence();
                            sequenceAction.addAction(Actions.delay(1.0f));
                            sequenceAction.addAction(Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    // Počaka po določenem času in nato spremeni sliko nazaj
                                    cellImage.setDrawable(new TextureRegionDrawable(square));
                                }
                            }));
                            cellImage.addAction(sequenceAction);
                            wrongCount++;
                            GameManager.setWrong(wrongCount);
                            updateHeartImage();
                            countWin++;
                            if (checkIfWon()) {
                                handleGameWon();
                            }
                        }
                        else if(!useCross && finalValue == 1)
                        {
                            cellImage.setDrawable(new TextureRegionDrawable(wrong));

                            SequenceAction sequenceAction = Actions.sequence();
                            sequenceAction.addAction(Actions.delay(1.0f));
                            sequenceAction.addAction(Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    // Počaka po določenem času in nato spremeni sliko nazaj
                                    cellImage.setDrawable(new TextureRegionDrawable(cross));
                                }
                            }));
                            cellImage.addAction(sequenceAction);
                            wrongCount++;
                            GameManager.setWrong(wrongCount);
                            System.out.println(GameManager.getWrong());
                            updateHeartImage();
                            countWin++;
                            if (checkIfWon()) {
                                handleGameWon();
                            }
                        }
                    }
                });

                grid.add(cellImage);
            }
            grid.row();  // Preklopi na naslednjo vrsto
        }

        // Dodaj mrežo v glavno tabelo
        table.add(grid).expand().align(Align.center);
        return table;
    }

    private boolean checkIfWon()
    {
        boolean won = false;
        if(GameManager.getGameMode() == "Easy")
        {
            if (countWin == winEasy) {
                won = true;
            }
            else
            {
                won = false;
            }
        }
        else if(GameManager.getGameMode() == "Standard")
        {
            if (countWin == winStandard) {
                won = true;
            }
            else
            {
                won = false;
            }
        }
        return won;

    }

    private void handleGameWon()
    {
        System.out.println("Congratulations! You won!");
        Timer.instance().clear();
        //final float timeSpent = 60 - timeLeft;

        final Dialog inputDialog = new Dialog("Congratulations! You won!", skin) {
            @Override
            protected void result(Object object) {
                if (object instanceof String) {
                    String username = (String) object;
                    System.out.println("Entered username: " + username);
                }
            }
        };

        inputDialog.getContentTable().add("Enter your username:");
        final TextField usernameField = new TextField("", skin);
        inputDialog.getContentTable().row();
        inputDialog.getContentTable().add(usernameField).pad(10);

        TextButton okButton = new TextButton("OK", skin);
        inputDialog.getButtonTable().add(okButton);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inputDialog.hide(); // Pošlji vrednost v result metodo
                Levels newScore = new Levels(usernameField.getText(), 1);
                System.out.print(newScore.getPlayerName());
                GameManager.saveLevels(newScore);

                if(GameManager.getGameMode() == "Easy")
                {
                    GameManager.setGameMode("Standard");
                    game.setScreen(new GameScreen(game));
                }
                else if(GameManager.getGameMode() == "Standard")
                {
                    game.setScreen(new MenuScreen(game));
                }

            }
        });

        // Prikaz dialoga
        inputDialog.show(hudStage);

    }


    private void addShapeSelectionButtons(Table rootTable)
    {
            final TextureRegion Kriz = gameAtlas.findRegion(RegionNames.IZBIRAK);
            final TextureRegion Kvadrat = gameAtlas.findRegion(RegionNames.IZBIRAKV);

            TextureRegionDrawable imageDrawable1 = new TextureRegionDrawable(Kriz);
            TextureRegionDrawable imageDrawable2 = new TextureRegionDrawable(Kvadrat);

            Image image1 = new Image(imageDrawable1);
            Image image2 = new Image(imageDrawable2);

        image1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    if(GameManager.isSoundOn())
                    {
                        clapSound.play();
                    }
                    useCross = true;
                }
            });

        image2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(GameManager.isSoundOn())
                    {
                        clapSound.play();
                    }
                    useCross = false;
                }
            });

        // Dodajte gumbe na odri
        Table table = new Table();
        //table.setDebug(true);
        table.add(image1).pad(10);
        table.add(image2).pad(10);

        rootTable.row().bottom();
        rootTable.add(table).padBottom(10);
        }

}
