package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.*;
import com.mygdx.game.Screens.SkeledodgeGame;
import com.mygdx.game.Utility.HighScore;
import com.mygdx.game.Utility.MusicPlayer;
import com.mygdx.game.Utility.OptionsConfig;
import com.mygdx.game.Utility.TimePlayed;

//import sun.applet.Main;

public class Tap extends Game {
	public SpriteBatch batch;

	public enum Screens {
		MENUSCREEN,
		GAMESCREEN,
		OPTIONSCREEN,
		HALLOFFAMESCREEN,
		ABOUTSCREEN,
		SKELEDODGE,
		FLAPPYBIRD,
		BOARDOTD
	};

	OrthographicCamera camera;
	private Menu menu;
	private ChooseGame game;
	private SkeledodgeGame skeledodge;
	private BoardOfTheDeadGame boardofthedead;
	private Options options;
	private HallOfFame hallOfFame;
	private About about;
	public OptionsConfig optionsCfg;
	private Tap parent;
	//private final Stage stage;

	MusicPlayer arcade;
	HighScore score;
	TimePlayed time;
	Sound oof;
	Sound crash;

	boolean loader = true;

	public OptionsConfig getPreferences() {
		return this.optionsCfg;
	}

	final public static int oofSound = 1;
	final public static int crashSound = 2;
	public void playSfx(int which){
		switch(which){
			case oofSound:
				oof.play();
				break;
			case crashSound:
				crash.play();
				break;
			default:
				break;
		}
	}

	@Override
	public void pause() {
		super.pause();
		getPreferences().saveTime();
		getPreferences().saveHighScore();

	}

	@Override
	public void create() {

		time       = TimePlayed.returnInstance();
		score      = HighScore.returnInstance();
		arcade     = MusicPlayer.vratInstanci();
		menu       = new Menu(this);
		game       = new ChooseGame(this);
		skeledodge = new SkeledodgeGame(this);
		options    = new Options(this);
		optionsCfg = new OptionsConfig(this);
		hallOfFame = new HallOfFame(this);
		about      = new About(this);
		batch      = new SpriteBatch();

		oof           = Gdx.audio.newSound(Gdx.files.internal("oof.wav"));
		crash         = Gdx.audio.newSound(Gdx.files.internal("impact.wav"));
		arcade.muzika = Gdx.audio.newMusic(Gdx.files.internal("arcade.ogg"));

		Gdx.input.setCatchBackKey(true);

		if (optionsCfg.musicOn()) {
			arcade.muzika.setLooping(true);
			arcade.muzika.play();
			arcade.muzika.setVolume(0.0f);//optionsCfg.getMusicVolume());

		} else {
			arcade.muzika.stop();
		}

		if (loader == true) {
			optionsCfg.loadTime();
			optionsCfg.loadHighScore();
		}

		screenChanger(Screens.MENUSCREEN);
	}

	@Override
	public void render() {
		super.render();
		time.update(Gdx.graphics.getDeltaTime());

		if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
			if(this.screen != menu){
				setScreen(menu);
				return;
			}

			//this is menu, just quit
			Gdx.app.exit();
		}

//		System.out.println(time.getTime());
	}

	@Override
	public void dispose() {
		super.dispose();
		getPreferences().saveTime();
		getPreferences().saveHighScore();
	}

	public void screenChanger(Screens screen) {
		Screen old = this.screen;
		switch (screen) {
			case MENUSCREEN:
				if (menu == null) menu = new Menu(this);
				this.setScreen(menu);
				break;
			case GAMESCREEN:
				if (game == null) game = new ChooseGame(this);
				this.setScreen(game);
				break;

			case OPTIONSCREEN:
				if (options == null) options = new Options(this);
				this.setScreen(options);
				break;

			case HALLOFFAMESCREEN:
				if (hallOfFame == null) hallOfFame = new HallOfFame(this);
				this.setScreen(hallOfFame);
				break;

			case ABOUTSCREEN:
				if (about == null) about = new About(this);
				this.setScreen(about);
				break;

			case SKELEDODGE:
				this.setScreen(new SkeledodgeGame(this));
				break;
			case BOARDOTD:
				if (boardofthedead == null) boardofthedead = new BoardOfTheDeadGame(this);
				this.setScreen(boardofthedead);
				break;
		}

		if(old!=null && old != this.screen){
			System.out.println("changed screen");
			old.dispose();
		}
	}

}
