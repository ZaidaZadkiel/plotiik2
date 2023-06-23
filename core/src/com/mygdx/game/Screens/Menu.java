package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Tap;

import java.util.Arrays;

public class Menu implements Screen, InputProcessor {
  private Tap parent;
  OrthographicCamera camera = new OrthographicCamera();;
  private Viewport viewport = new ExtendViewport(2000, 2000, camera) ;
  boolean slider = false;

  private class Touchable{
    Rectangle r = new Rectangle();
    touchAction action;


    Touchable(float x, float y, float w, float h){
      this.r.set(x,y,w,h);
    }

    boolean contains(float worldX, float worldY){
      return r.contains(worldX,worldY);
    }

    boolean touching(float worldX, float worldY){
      if(action != null && contains(worldX,worldY) ){
        return action.touched();
      }
      return false;
    }
  }

  private interface touchAction{
    boolean touched();
  }

  private class Anim {
    public String name;
    public float fps=1f/6f;
    float x;
    float y;
    Texture [] tex;
    private int length = 1;
    private int i = 0;
    private Animation<Texture> frameset;
    private float elapsed=0;
    public float[] frametimes = null;

    public Touchable touchable;

    Anim(String name, float x, float y, Texture[] tex, float fps, Animation.PlayMode p){
      this(name, x,y,tex);
      this.fps = fps;
      frameset.setPlayMode(p);
    }

    Anim(String name, float x, float y, Texture[] tex){
      this.x = x;
      this.y = y;
      this.tex = tex;
      this.length = tex.length;
      frameset = new Animation<>(fps, new Array<Texture>(tex), Animation.PlayMode.LOOP_PINGPONG);
      touchable = new Touchable(x,y,tex[0].getWidth(), tex[0].getHeight());
      this.name = name;
    }

    private float timingslength;
    void setFrametimes(float[] timings) throws Exception {
      this.frametimes = timings;
      if(timings.length != tex.length){
        throw new Exception("frame timings length does not match frameset length: timings "+timings.length+", frameset: "+tex.length);
      }
      this.frametimes = new float[(timings.length*2)-1];
      float c = 0;
      for (int j = 0; j < timings.length; j++) {
        c+=timings[j];
        frametimes[j] = c;
        System.out.println(j+", "+timings[j]+"-"+c);
      }
      System.out.println("ch");
      for (int j = timings.length-2; j > 0; j--) {
        c+=timings[j];
        frametimes[frametimes.length-j-1] = c;
        System.out.println((frametimes.length-j-1)+", "+timings[j]+"-"+c);
      }
//      c+=timings[0];
//      frametimes[frametimes.length-1] = c;
      timingslength=c;
      System.out.println("timings: "+ Arrays.toString(timings) +", frametimes: "+Arrays.toString(frametimes));
    }

    /** draw the sprite current frame at x,y */
    void draw(SpriteBatch b){
      if(frametimes!=null){
        float f = ((this.elapsed) % (timingslength));

        int i = 0;
        for (; i< frametimes.length-1; i++) {
          if(frametimes[i]>f) break;
        }

        int l = (int) (
          Math.abs(
            i - (Math.floor((double) i /(tex.length)) ) * (frametimes.length-1)
          )
        )%tex.length;
        draw(b, this.tex[l]);

        return;
      }

      draw(b, frameset.getKeyFrame(elapsed));
    }

    private void draw(SpriteBatch batch, Texture t){
      batch.draw(t,this.touchable.r.x,this.touchable.r.y);
    }

    /** Viewport: the screen size bounds */
    void resize(Viewport view){
//      Vector2 r = new Vector2(this.x, this.y);

      touchable.r.setPosition(this.x * (view.getWorldWidth()/view.getScreenWidth()), this.y);
    }

    /** elapsed: absolute time; call to change frame to appropriate time */
    void update(float elapsed){
      this.elapsed = elapsed;
      touchable.r.setPosition(this.x, this.y);
    }
  }

  Vector3 pos = new Vector3();

  Texture[] background = {
    new Texture("menu/threescreens-0.jpg"),
    new Texture("menu/threescreens-1.jpg"),
    new Texture("menu/threescreens-2.jpg"),
  };

  Anim fren = new Anim(
    "fren",
    0,1500,
    new Texture[] {
      new Texture("menu/nightfren/frame0000.png"),
      new Texture("menu/nightfren/frame0001.png"),
      new Texture("menu/nightfren/frame0002.png"),
      new Texture("menu/nightfren/frame0003.png"),
      new Texture("menu/nightfren/frame0004.png")
    }
  );
  Anim skeletodd = new Anim(
    "skeletodd",
    -1000,250,
    new Texture[] {
      new Texture("menu/skeletodd/frame0000.png"),
      new Texture("menu/skeletodd/frame0001.png"),
      new Texture("menu/skeletodd/frame0002.png"),
      new Texture("menu/skeletodd/frame0003.png"),
      new Texture("menu/skeletodd/frame0004.png")
    }
  );

  Anim pestbirb = new Anim(
    "pestbirb",
    200,100,
    new Texture[] {
      new Texture("menu/pestbirb/frame0000.png"),
      new Texture("menu/pestbirb/frame0001.png"),
      new Texture("menu/pestbirb/frame0002.png"),
      new Texture("menu/pestbirb/frame0003.png"),
      new Texture("menu/pestbirb/frame0004.png")
    }
  );

  Anim title = new Anim(
    "title",
    -400,1600,
    new Texture[] {
      new Texture("menu/menu/frame0000.png"),
      new Texture("menu/menu/frame0001.png"),
      new Texture("menu/menu/frame0002.png"),
    }
  );
  Anim papyr = new Anim(
    "papyr",
    -700,1200,
    new Texture[] {
      new Texture("menu/title/frame0000.png"),
      new Texture("menu/title/frame0001.png"),
      new Texture("menu/title/frame0002.png"),
      new Texture("menu/title/frame0003.png"),
      new Texture("menu/title/frame0004.png")
    },
    1f/3f,
    Animation.PlayMode.NORMAL
  );

  Anim spider = new Anim(
    "spider",
    -4200f,1000,
    new Texture[]{
      new Texture("menu/ui/spider/frame0001.png"),
      new Texture("menu/ui/spider/frame0000.png"),
    }
  );
  Anim windowed = new Anim(
    "windowed",
    -3500,700,
    new Texture[]{
      new Texture("menu/ui/options/windowed0001.png"),
      new Texture("menu/ui/options/windowed0000.png"),
    }
  );
  Anim fullscreen = new Anim(
    "fullscreen",
    -3500,900,
    new Texture[]{
      new Texture("menu/ui/options/fullscreen0001.png"),
      new Texture("menu/ui/options/fullscreen0000.png"),
    }
  );
  Anim music = new Anim(
    "music",
    -3500,1300,
    new Texture[]{
      new Texture("menu/ui/options/music0001.png"),
      new Texture("menu/ui/options/music0000.png"),
    }
  );
  Anim sounds = new Anim(
    "sounds",
    -3500,1100,
    new Texture[]{
      new Texture("menu/ui/options/sounds0001.png"),
      new Texture("menu/ui/options/sounds0000.png"),
    }
  );
  Anim options = new Anim(
    "options",
    -3800,1700,
    new Texture[]{
      new Texture("menu/ui/options/options0001.png"),
      new Texture("menu/ui/options/options0000.png"),
    }
  );

  Anim dodge = new Anim(
    "dodge",
    2700,1400,
    new Texture[]{
      new Texture("menu/ui/games/dodge0001.png"),
      new Texture("menu/ui/games/dodge0000.png"),
    }
  );

  Anim escape = new Anim(
    "escape",
    2700,250,
    new Texture[]{
      new Texture("menu/ui/games/escape0001.png"),
      new Texture("menu/ui/games/escape0000.png"),
    }
  );
  Anim botd = new Anim(
    "botd",
    2700,600,
    new Texture[]{
      new Texture("menu/ui/games/botd0001.png"),
      new Texture("menu/ui/games/botd0000.png"),
    }
  );



//  Anim selectgame = new Anim(
//    "selectgame",
//    2500,0,
//    new Texture[]{
//      new Texture("menu/ui/frame0000-2.png"),
//      new Texture("menu/ui/frame0001-2.png")
//    }
//  );
//
//  Anim optionsmenu = new Anim(
//    "optionsmenu",
//    -4000,-100,
//    new Texture[]{
//      new Texture("menu/ui/frame0000-0.png"),
//      new Texture("menu/ui/frame0001-0.png")
//    }
//  );

  Anim hand = new Anim(
    "optionshand",
    -1600,150,
    new Texture[]{
      new Texture("menu/ui/hand/frame0001.png"),
      new Texture("menu/ui/hand/frame0000.png"),
    }
  );

  Anim tomb = new Anim(
    "playtomb",
    1100,100,
    new Texture[]{
      new Texture("menu/ui/frame0000-1.png")
    }
  );



  Anim anims[] = {
    skeletodd,
    pestbirb,
    title,
    hand,
    tomb,
    spider,
    windowed,
    fullscreen,
    music,
    options,
    sounds,
    dodge,
    escape,
    botd,
    fren
  };

  BitmapFont bf = new BitmapFont();
  SpriteBatch batch = new SpriteBatch();

  @Override
  public void show() {
    Gdx.input.setInputProcessor(this);
    System.out.println(Gdx.graphics.getWidth()+", "+Gdx.graphics.getHeight());

    System.out.println(Gdx.graphics.getDisplayMode());
    if(Gdx.app.getType() == Application.ApplicationType.Android){
      Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    } else {
      Gdx.graphics.setWindowedMode(
        Gdx.graphics.getDisplayMode().width  / 2,
        Gdx.graphics.getDisplayMode().height / 2
      );
    }
    bf.getData().scale(4f);
    changeBackground(0);

    float[] t={
      1/0.5f,
      1/6f,
      1/6f,
      1/6f,
      1/0.5f
    };
    try{
      pestbirb.setFrametimes(t);
      skeletodd.setFrametimes(t);
    }catch (Exception e){
      System.out.println(e);
    }

    pestbirb.touchable.action = new touchAction() {
      @Override
      public boolean touched() {
        selected=pestbirb;
        return true;
      }
    };

    skeletodd.touchable.action = new touchAction() {
      @Override
      public boolean touched() {
        selected=skeletodd;
        return true;
      }
    };

    hand.touchable.action = new touchAction() {
      @Override
      public boolean touched() {
        changeBackground(-1);
        return true;
      }
    };
    tomb.touchable.action = new touchAction() {
      @Override
      public boolean touched() {
        changeBackground(1);
        return true;
      }
    };

    spider.touchable.action = new touchAction() {
      @Override
      public boolean touched() {
        selected=spider;
        return true;
      }
    };
//    System.out.println("fren "+fren[i].getWidth()+", "+fren[i].getHeight());
  }


  float f  = -1;
  float pb = 0;
  float c  = 0;
  float bx = 0;

  ShapeRenderer shape = new ShapeRenderer();
  Vector3 frenpos = new Vector3(100,100,0);
  Vector2 tmp = new Vector2();
  Anim selected=pestbirb;

  float bc = 0;
  Vector2 bvo = new Vector2(0,0); //background vec origin
  Vector2 bvt = new Vector2(0,0); //backgroudn vec target

  int currentBackground = 1;
  /** side=-1,0,+1; sets one of the three background screens */
  void changeBackground(int side){
    int newbg = (int) Math.signum(side%background.length);
//    if(newbg != currentBackground){
      bc = c;
      currentBackground = newbg;
      bvo.x = camera.position.x;
      bvt.x = ((viewport.getWorldWidth())*currentBackground) ;
//      System.out.println("target "+ bvt.x);
//    }
  }

  void renderBackground(SpriteBatch batch){
    float w = viewport.getWorldWidth()/2;
    if((c-bc)<5f){
      bvo.lerp(bvt, (c-bc)/5f);
//      bx = (bvo.x%(w*3)+(w*3))%(w*3);
      bx=bvo.x;
    }

    batch.draw(background[0],
      camera.position.x < 0 ? (w*-3) : (w*3),0,
      viewport.getWorldWidth(), viewport.getWorldHeight()
    );

    batch.draw(background[1],
      -w,0,
      viewport.getWorldWidth(), viewport.getWorldHeight()
    );

    batch.draw(background[2],
      camera.position.x < -viewport.getScreenWidth() ? (w*-5) : (w),0,
      viewport.getWorldWidth(), viewport.getWorldHeight()
    );


//    batch.draw(background[0],
//      ((bx-(w)) % (w*3))-w,0,
//      viewport.getWorldWidth(), viewport.getWorldHeight()
//    );
//
//    batch.draw(background[1],
//      ((bx) % (w*3))-w,0,
//      viewport.getWorldWidth(), viewport.getWorldHeight()
//    );
//
//    batch.draw(background[2],
//      ((bx+w) % (w*3))-w,0,
//      viewport.getWorldWidth(), viewport.getWorldHeight()
//    );
  }
  float spiderlerp = 0;
  Vector2 spiderorigin = new Vector2();
  Vector2 spidertarget = new Vector2();
  @Override
  public void render(float delta) {
    c += delta;
//    for (Anim anim : anims) {
//      anim.update(c);
//    }

    if(skeletodd==selected){
      skeletodd.y=100;
      pestbirb.y=250;
    } else {
      skeletodd.y=250;
      pestbirb.y=100;
    }
    //pestbirb.x=1800+bx;

    camera.position.x = bx;
    camera.update();

    batch.setProjectionMatrix(viewport.getCamera().combined);
    shape.setProjectionMatrix(viewport.getCamera().combined);

    Gdx.gl.glClearColor(0,0,0,0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    float x = viewport.getWorldWidth();
    float y = viewport.getWorldHeight();

    batch.begin();
    batch.setColor(1,1,1,1);
      renderBackground(batch);
//      title.draw(batch);
      bf.draw(batch, "Select your player character", 120,120);
      bf.draw(batch, "Selected anim " + selected.name, -1200,120);
    batch.end();

    if(c>3f){
      if(pb<1f){
        pb+=delta;
      } else {
        papyr.update(papyr.elapsed+delta);
        if(papyr.elapsed > papyr.frameset.getAnimationDuration() ){
          papyr.update(papyr.frameset.getAnimationDuration()-papyr.fps);
        }
      }

      int srcFunc = batch.getBlendSrcFunc();
      int dstFunc = batch.getBlendDstFunc();

      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

      shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(1,1,0,c%0.5f);
        shape.ellipse(selected.x, selected.y, selected.tex[0].getWidth(),selected.tex[0].getHeight());


        shape.setColor(Color.BLACK);
        shape.rectLine(
          (viewport.getScreenWidth()*-4.26f),
          1550f,
          spider.x+(spider.tex[0].getWidth()/2f),
          spider.y+(spider.tex[0].getHeight()/2f),
          10f
        );
      shape.end();

      batch.begin();
        batch.setColor(1,1,1,pb);

        if(!spiderdragging && c-spiderlerp <5f){
          spiderorigin.lerp(spidertarget, (c-spiderlerp)/5f);
          spider.x=spiderorigin.x;
          spider.y=spiderorigin.y;
//          spider.update(c);
        }
        for (Anim anim : anims) {
          anim.update(c);
          anim.draw(batch);
        }
//        papyr.draw(batch);
//
//        pestbirb.draw(batch);
//        skeletodd.draw(batch);
//
//        hand.draw(batch);
//        spider.draw(batch);
//        tomb.draw(batch);

        tmp.set(pos.x, pos.y);
        viewport.unproject(tmp);
        frenpos.rotate(5f,1,1,-1);
        fren.x = tmp.x+frenpos.x-(fren.tex[0].getWidth()/2f);
        fren.y = tmp.y+frenpos.y-(fren.tex[0].getHeight()/2f);
//        fren.draw(batch);

      batch.end();

      batch.setBlendFunction(srcFunc, dstFunc);

    }

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    shape.begin(ShapeRenderer.ShapeType.Filled);
      if(f<1f){
        f += delta * (0.5f);
        shape.setColor(0,0,0,1f-f);
        shape.rect(viewport.getWorldWidth()*-0.5f,0, viewport.getWorldWidth(), viewport.getWorldHeight());
      }
    shape.end();

    shape.begin(ShapeRenderer.ShapeType.Line);
      for (Anim a: anims) {
        shape.rect(a.touchable.r.x,a.touchable.r.y,a.touchable.r.width, a.touchable.r.height);
      }
    shape.end();

  }

  @Override
  public void resize(int width, int height) {
    System.out.println("resize: w"+width + ", h"+ height+" @"+Gdx.graphics.getDensity());
    viewport.update(width, height, true);
//    selectgame.x = (viewport.getWorldWidth()) + ((float) selectgame.tex[0].getWidth() /2) ;
//    optionsmenu.x = -(viewport.getWorldWidth()) + ((float) optionsmenu.tex[0].getWidth() /2) ;
    for (Anim a: anims) {
      a.resize(viewport);
    }


//    frenratio = (
//        Math.min(viewport.getWorldHeight(), viewport.getWorldWidth())
//      / (float)fren[0].getHeight()
//    ) / Gdx.graphics.getDensity();
//    System.out.println("frenratio: "+viewport.getWorldHeight() + ", "+ fren[0].getHeight()+" - "+frenratio );

    batch.setProjectionMatrix(viewport.getCamera().combined);
    shape.setProjectionMatrix(viewport.getCamera().combined);
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {

  }

  public Menu(Tap tap) {
    parent = tap;
  }

  @Override
  public boolean keyDown(int keycode) {
    if(keycode == Input.Keys.LEFT){
      changeBackground(currentBackground-1);
    }
    if(keycode == Input.Keys.RIGHT){
      changeBackground(currentBackground+1);
    }

    if(keycode == Input.Keys.SPACE){
      bx = 0;
    }
//    if(currentBackground==1 && (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT)){
//      selected=(selected==skeletodd) ? pestbirb : skeletodd;
//    }
    return false;
  }


  void touchPosition(int screenX, int screenY, int button){

    pos.set(screenX, screenY, 0); //fren pos

    tmp.set(screenX, screenY);
    viewport.unproject(tmp);

    if(button!=0){
      for (Anim a: anims) {
        if(a == fren) continue;
        //check touches
        a.touchable.touching(tmp.x, tmp.y);

      }

//      if(screenX < (Gdx.graphics.getWidth()/2)){
//        selected=skeletodd;
//      }else{
//        selected=pestbirb;
//      }
    }
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  Vector2 screendrag = new Vector2();
  Vector2 worlddrag = new Vector2();
  boolean spiderdragging = false;

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    worlddrag.set(camera.position.x, camera.position.y);
    tmp.set(screenX, screenY);
    viewport.unproject(tmp);
    if(selected==spider && spider.touchable.contains(tmp.x, tmp.y)){
      spiderdragging = true;
      return true;
    }
    touchPosition(screenX,screenY, 1); /* left button = 0 */
    return false;
  }


  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if(spiderdragging){
      spiderorigin.set(spider.x, spider.y);
      spidertarget.set((viewport.getScreenWidth()*-4.26f),1550f);
      spiderlerp = c;
    }
    spiderdragging = false;

    tmp.set(camera.position.x, camera.position.y);
    viewport.project(tmp);

    float x = worlddrag.x-camera.position.x;
    if(Math.abs(x)> (viewport.getScreenWidth()/3f)){
      changeBackground(currentBackground+ (int)Math.signum(-x) );
    } else {
      changeBackground(currentBackground);
    }

//    System.out.println(x + ", "+(viewport.getScreenWidth()/3f));
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    pos.set(screenX, screenY, 0); //fren pos

    tmp.set(screenX, screenY);
    viewport.unproject(tmp);
    if(selected==spider && spiderdragging){
      spider.x = tmp.x-(spider.tex[0].getWidth()/2f);
      spider.y = tmp.y-(spider.tex[0].getWidth()/2f);
      return false;
    }

    /* magic */
    screendrag.set(camera.position.x,camera.position.y);
    viewport.project(screendrag);
    screendrag.sub(Gdx.input.getDeltaX(),screenY-screendrag.y);
    viewport.unproject(screendrag);

    bx = screendrag.x;
    bc = c-5f;
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    tmp.set(screenX, screenY);
    viewport.unproject(tmp);
    if(selected==spider && spiderdragging){
//      System.out.println("spider");
      spider.x = tmp.x-(spider.tex[0].getWidth()/2f);
      spider.y = tmp.y-(spider.tex[0].getWidth()/2f);
      return false;
    }
    touchPosition(screenX,screenY, 0);
    return false;
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    return false;
  }
}
