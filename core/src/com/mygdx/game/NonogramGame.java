package com.mygdx.game;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.common.Levels;
import com.mygdx.game.screen.IntroScreen;

import java.util.ArrayList;
import java.util.List;

public class NonogramGame extends Game {
	SpriteBatch batch;
	private AssetManager assetManager;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		assetManager = new AssetManager();
		assetManager.getLogger().setLevel(Logger.DEBUG);

		/*
		GameManager.INSTANCE.initialize(); // Kličemo initialize metodo
		GameManager gameManager = GameManager.INSTANCE;

		for (int i = 1; i <= 3; i++) {
			String playerName = "Player" + i;
			int score = i + 1; // Assigning scores (e.g., Player1: 100, Player2: 200, etc.)
			Levels newScore = new Levels(playerName, score);
			System.out.print(newScore.getPlayerName());
			gameManager.saveLevels(newScore);
		}
		 */

		batch = new SpriteBatch();
		setScreen(new IntroScreen(this));
	}
	@Override
	public void dispose() {
		assetManager.dispose();
		batch.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
