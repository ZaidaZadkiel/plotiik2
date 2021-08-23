
package org.zaidaplutik.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.decals.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.zaidaplutik.Tap;
import org.zaidaplutik.Utility.AlphaTestGroupStrategy;import org.zaidaplutik.Utility.AnimatedSprite;
import org.zaidaplutik.Utility.WeirdGroupStrategy;

import java.util.Comparator;

public class BoardOfTheDeadGame  implements Screen {
    private final Stage stage;
    private final Tap tap;
    private Tap parent;
//    OrthographicCamera camera;
    PerspectiveCamera camera;
    OrthographicCamera overlay;
    
    private Texture skeleton;
    private Texture spookyBg;
    private Texture heart;
    private Texture boardIcons;
    private Texture hitStar;
    private Texture gameover;
    private Texture badgood;

    private TextureRegion[][] icons;

    private AnimatedSprite skeledude = new AnimatedSprite("anim_birb.png", 92);

    private Rectangle skeletonRect = new Rectangle();;
    private Rectangle diceRect     = new Rectangle();;
    private Rectangle hitRect      = new Rectangle();;

    private int[] squares = new int[9*9];

    private String scoreText;
    private float  score     = 0;
    private int    hitpoints = 8;
    private int    direction = 1;
    private int    box       = 0;
    private int    boxview   = 1;
    private float  hitTime   = 10;

    Vector3 diceSpeed = new Vector3();
    Vector3 diceStart = new Vector3();
    float maxLength;

    private modes   mode = modes.DiceStart;
    private enum  modes{
        DiceStart, DiceRolling, DiceStop, GuyMoving, BadEvent, GoodEvent
    }

    public final static int TYPE_BOARD   = 0;
    public final static int TYPE_TRAP    = 1;
    public final static int TYPE_POWERUP = 2;

    public final static int TRAP_BEAR  = 0;
    public final static int TRAP_JAIL  = 1;
    public final static int TRAP_SNAKE = 2;

    public final static int PUP_HEART  = 0;
    public final static int PUP_SHIELD = 1;
    public final static int PUP_SWORD  = 2;


    private BitmapFont bf = new BitmapFont();
    ShapeRenderer sr      = new ShapeRenderer();

    int wasdqe[] = {0,0,0,0,0,0};
    int UDLR  [] = {0,0,0,0};
    Decal decal;
    Decal background;
    Decal pestBirb;
    Decal fence;
    Decal castle;
    
    Array<Decal> listDecals = new Array<>();
    DecalBatch  decalBatch;
    Texture castleTexture= new Texture("castle.png");
  
    WeirdGroupStrategy cameraStrategy;
    
    public BoardOfTheDeadGame(Tap tap) {
      parent   = tap;
      this.tap = tap;

      stage      = new Stage(new FitViewport(600, 700));
      skeleton   = new Texture("anim.png");
      spookyBg   = new Texture("boardofthedead.jpg");
      boardIcons = new Texture("boardicons.png");
      hitStar    = new Texture("hit.png");
      badgood    = new Texture("badgood.png");
      heart      = new Texture("heart_pixel_art_32x32.png");
      gameover   = new Texture("deadskeleton.jpg");

      icons = new TextureRegion(boardIcons).split(64,64);

     
      
//        decal = Decal.newDecal(100,100, icons[0][0] );
//        decal.setPosition(10,10,0);
//        listDecals.add(decal);

      int bad_size = 25;
      int bad[] = new int[bad_size];
      for(int i = 0; i != bad_size; i++) {
          bad[i] = MathUtils.random(0, 9*9);
      }

      int good_size = 15;
      int good[] = new int[good_size];
      for(int i = 0; i != good_size; i++) {
          good[i] = MathUtils.random(0, 9*9);
      }

      for(int x = 0; x!=9*9; x++){
          squares[x] = TYPE_BOARD;

          for(int b = 0; b != bad_size; b++) {
              if(bad[b] == x){
                  squares[x] = TYPE_TRAP;
                  break;
              }
          }

          for(int g = 0; g != good_size; g++) {
              if(good[g] == x){
                  squares[x] = TYPE_POWERUP;
                  break;
              }
          }

//            squares[x] = TYPE_POWERUP;
      }

      overlay = new OrthographicCamera();
      overlay.setToOrtho(false,600,700);
      
      camera      = new PerspectiveCamera(300, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
      camera.near = 40f;
      camera.far  = 800f;
//        camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getWidth()/2,190.0f);
      camera.position.set(0,-305,80.0f);
      camera.fieldOfView = 100;
      
      cameraStrategy = new WeirdGroupStrategy(camera);
      
      decalBatch = new DecalBatch(cameraStrategy);
//      decalBatch = new DecalBatch(new AlphaTestGroupStrategy(camera));

//      decalBatch = new DecalBatch(new CameraGroupStrategy(camera, new Comparator<Decal>() {
//        @Override
//        public int compare (Decal o1, Decal o2) {
//          float dist1 = camera.position.dst(0, 0, o1.getPosition().z);
//          float dist2 = camera.position.dst(0, 0, o2.getPosition().z);
//          return (int)Math.signum(dist2 - dist1);
//        }
//      }));

      background = Decal.newDecal(new TextureRegion(new Texture("floor.jpg"))); //flat on the floor
      background.setPosition(0,0,-15f);
      background.setBlending(DecalMaterial.NO_BLEND, 0);
      System.out.println("backround opaque " + (background.getMaterial().isOpaque() ? "yep": "nop") );
      
      castle = Decal.newDecal(new TextureRegion(castleTexture)); //flat on the floor
      castle.setRotation(0,50,0);
      castle.setPosition(0,300, 50);
      castle.setScale(0.5f);
      castle.setBlending(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);
      
//      float halfHeight = castle.getTextureRegion().getRegionHeight()/2.0f;
//      castle.getVertices()[Decal.Z1] += 15;//halfHeight;
//      castle.getVertices()[Decal.Z2] += 15;//halfHeight;
//      castle.getVertices()[Decal.Z3] += 15;//halfHeight;
//      castle.getVertices()[Decal.Z4] += 15;//halfHeight;
      
      System.out.println("castle opaque " + (castle.getMaterial().isOpaque() ? "yep": "nop") );

      
//      background = Decal.newDecal(new TextureRegion(spookyBg) ); //flat on the floor
      pestBirb = Decal.newDecal(skeledude.getFrame());
      pestBirb.setRotation(0,50,0);
//      pestBirb = makeNewDecal(skeledude.getFrame(), skeletonRect.x,skeletonRect.y, 5,0,-22);
      pestBirb.setBlending(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);
      pestBirb.setScale(0.5f);
      System.out.println("pestbirb opaque " + (pestBirb.getMaterial().isOpaque() ? "yep": "nop") );

      TextureRegion fenceregion = new TextureRegion(new Texture("fence.png"));
      for (int i = 0; i < 7; i++) {
        makeNewDecal(fenceregion,
          -290+(100*i),
          -300,
          -2,
          0,
          -100).setScale(0.15f);
      }
  
      TextureRegion[][] grassregion = new TextureRegion(new Texture("grass.png")).split(64,64);
      for (int i = 0; i < 500; i++) {
        makeNewDecal(
                      grassregion[MathUtils.randomBoolean()?0:1][MathUtils.random(3)],
                  MathUtils.random( background.getWidth() )-(background.getWidth()/2),
                  MathUtils.random( background.getHeight() )-(background.getHeight()/2),
                  -4,
              0,
              0
        ).setScale(0.5f);
        
        
      }
//      makeNewDecal(fenceregion, -230,-250,250,0,-100).setScale(0.1f);
//      makeNewDecal(fenceregion, -160,-250,250,0,-100).setScale(0.1f);
//      makeNewDecal(fenceregion, -160,-250,250,0,-100).setScale(0.1f);
      
      
    
        skeletonRect.set(15, 10, 64, 64);
        hitRect.set(0, 0, 64, 64);

        diceRect.set(
            (camera.viewportWidth /2.0f) - 32,
            (camera.viewportHeight/2.0f) - 32,
            64,64);

        score();
      skeledude.set(AnimatedSprite.Set.Running, true);
    }
  
    Decal makeNewDecal(AnimatedSprite sprite, float x, float y, float z, float centerX, float centerY){
      return makeNewDecal(sprite.getFrame(), x, y, z, centerX, centerY);
    }
  
    Decal makeNewDecal(String internalPath, float x, float y, float z, float centerX, float centerY){
      //TODO cache texture region
      return makeNewDecal(new TextureRegion(new Texture(internalPath)), x,y,z,centerX,centerY);
    }
    
    Decal makeNewDecal(TextureRegion region, float x, float y, float z, float centerX, float centerY){
      Decal decal;
      decal = Decal.newDecal( region,true );
      decal.setBlending(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);

//      decal.transformationOffset = new Vector2(0, -(region.getRegionHeight()/2.0f));
      
      decal.setRotation(0,50,0);
      decal.setPosition(x,y,z);
      listDecals.add(decal);
      return decal;
    }
    
    boolean minimap = false;
    
    int gl_func[] = {
      GL20.GL_ALWAYS,
      GL20.GL_NEVER,
      GL20.GL_LESS,
      GL20.GL_EQUAL,
      GL20.GL_LEQUAL,
      GL20.GL_GREATER,
      GL20.GL_NOTEQUAL,
      GL20.GL_GEQUAL
    };
    String gl_funcName[] = {
      "GL20.GL_ALWAYS",
      "GL20.GL_NEVER",
      "GL20.GL_LESS",
      "GL20.GL_EQUAL",
      "GL20.GL_LEQUAL",
      "GL20.GL_GREATER",
      "GL20.GL_NOTEQUAL",
      "GL20.GL_GEQUAL"
    };
    int choosenGLfunc = 4;
    
    float camerax = 0.0f;
    @SuppressWarnings("DefaultLocale")
    @Override
    public void render(float delta) {
        camerax += 10*delta;
        Vector3 v = camera.position;
//        v.add(0,-0.1f,0.2f);
//        camera.position.set(v);
        
//        camera.lookAt(Gdx.graphics.getWidth()/2, Gdx.graphics.getWidth()/2,0);
//        camera.lookAt(skeletonRect.x, skeletonRect.y,0);
      
    

      
      if(hitpoints <= 0) {
          endgame(delta);
          return;
      }

      Gdx.gl.glClearColor(0f, 0f, 0f, 1);
//      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//      Gdx.gl.glDepthMask(true);
//      Gdx.gl.glDepthRangef(0, 1);
      Gdx.gl.glDepthFunc(gl_func[choosenGLfunc]);
      Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//      Gdx.gl.glDisable(GL20.GL_CULL_FACE);
//      Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
//      Gdx.gl20.glEnable(GL20.GL_BLEND);

      // setup drawing to stencil buffer
      
//      Gdx.gl20.glEnable(GL20.GL_BLEND);
//      Gdx.gl20.glEnable(GL20.GL_ALP);
//      Gdx.gl20.glStencilFunc(GL20.GL_ALWAYS, 0x1, 1); //0xffffffff);
//      Gdx.gl20.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE);
//      Gdx.gl20.glColorMask(false, false, false, false);
//      Gdx.gl20.glDepthMask(false);
//
//
//      Gdx.gl20.glColorMask(true, true, true, true);
//      Gdx.gl20.glDepthMask(true);
//      Gdx.gl20.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);
      
      camera.position.x = pestBirb.getX();
      camera.direction.set(0,1, -0.9f);
      camera.up.set(0,-1,1);
      
      if(wasdqe[0] == 1) camera.position.y+=2;
      if(wasdqe[1] == 1) camera.position.x-=2;
      if(wasdqe[2] == 1) camera.position.y-=2;
      if(wasdqe[3] == 1) camera.position.x+=2;
      if(wasdqe[4] == 1) camera.position.z-=2;
      if(wasdqe[5] == 1) camera.position.z+=2;

      if(UDLR[0] == 1) skeletonRect.y += 0.5f;
      if(UDLR[1] == 1) skeletonRect.y -= 0.5f;
      if(UDLR[2] == 1) {skeletonRect.x -= 0.5f;  }
      if(UDLR[3] == 1) {skeletonRect.x += 0.5f; pestBirb.setRotation(0,50,0); }
      
      
      camera.update();
      skeledude.update(delta);
//      play(delta);
  
      parent.batch.begin();
      parent.batch.draw(spookyBg,0,0);
      parent.batch.end();
      
//      decalBatch.flush();
  
      pestBirb.setTextureRegion(skeledude.getFrame());
      pestBirb.setPosition(skeletonRect.x, skeletonRect.y, 0);
      pestBirb.setRotationX(camerax)

      
//      castle.translateZ(0.05f);
      
//      cameraStrategy.setBuffer(false);
//      cameraStrategy.setBuffer(true);
//      Gdx.gl20.glColorMask(false, false, false, false);
      Gdx.gl20.glDisable(GL20.GL_BLEND);

      cameraStrategy.setBuffer(true);
//
      decalBatch.add(castle);
      decalBatch.add(background);
//      decalBatch.flush();
      
//      cameraStrategy.setBuffer(false);
//      Gdx.gl20.glEnable(GL20.GL_BLEND);
      decalBatch.add(pestBirb);
//      Gdx.gl20.glColorMask(true, true, true, true);
//      cameraStrategy.setBuffer(false);

//      decalBatch.add(castle);
//        decalBatch.flush();
//      cameraStrategy.setBuffer(false);

      //      decalBatch.add(pestBirb);
//      decalBatch.add(fence);
  
      for (int i = 0; i < listDecals.size; i++) {
        Decal d = listDecals.get(i);
//        d.translateY(0.5f * MathUtils.randomSign());
//        d.setZ(0);
        decalBatch.add(listDecals.get(i));
      }
      
      if(decalBatch!=null) decalBatch.flush();

      parent.batch.begin();
      
       bf.draw(parent.batch, String.format(
            "x: %8.2f y: %8.2f w: %8.2f z: %8.2f\n"
//            + "Space to switch depth test: %s\n"
//            + "WASD camera, arrows birb\n"
//            + "depth func %s"
          ,
          pestBirb.getRotation().x,
          pestBirb.getRotation().y,
          pestBirb.getRotation().w,
          pestBirb.getRotation().z
//          cameraStrategy.getBuffer() ? "using buffar" : "buffer off",
//          gl_funcName[choosenGLfunc]
        ), 10,690);
      
//        bf.draw(parent.batch, String.format(
//            "x: %8.2f y: %8.2f z: %8.2f\n"
//            + "Space to switch depth test: %s\n"
//            + "WASD camera, arrows birb\n"
//            + "depth func %s",
//          camera.position.x,
//          camera.position.y,
//          camera.position.z,
//          cameraStrategy.getBuffer() ? "using buffar" : "buffer off",
//          gl_funcName[choosenGLfunc]
//        ), 10,690);
      parent.batch.end();

      if(minimap) {
        overlay.zoom = 5f;
        overlay.update();
        sr.setProjectionMatrix(overlay.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        
  //        sr.setColor(Color.BLUE);
          for (int i = 0; i < listDecals.size; i++) {
            drawthing(listDecals.get(i), sr,Color.BROWN);
          }
          drawthing(background, sr, Color.WHITE);
          drawthing(pestBirb, sr, Color.RED);
          drawthing(castle, sr, Color.BLUE);
          sr.line(camera.position.x,
                  camera.position.y,
                  camera.position.x,
              camera.position.y+50);
        sr.end();
      }
//        shader.end();

//        modelBatch.begin(camera);

//        skeledude.getFrame().getTexture().bind();
//        shader.begin();
//        shader.setUniformMatrix("u_projTrans", camera.combined);
//        shader.setUniformi("u_texture", 0);
//        mesh.transform(camera.projection);
//        mesh.render(shader, GL20.GL_TRIANGLE_STRIP);
//        mesh.transform(camera.projection.inv());
//        shader.end();

    }
    
    void drawthing(Decal d, ShapeRenderer sr, Color c){
      sr.setColor(c);
      float vertices[] = d.getVertices();
      sr.line(
        vertices[Decal.X1], vertices[Decal.Y1],
        vertices[Decal.X2], vertices[Decal.Y2]
      );
      
      sr.line(
        vertices[Decal.X2], vertices[Decal.Y2],
        vertices[Decal.X4], vertices[Decal.Y4]
      );
      
      sr.line(
        vertices[Decal.X3], vertices[Decal.Y3],
        vertices[Decal.X4], vertices[Decal.Y4]
      );

      sr.line(
        vertices[Decal.X3], vertices[Decal.Y3],
        vertices[Decal.X1], vertices[Decal.Y1]
      );
      
      sr.line(vertices[Decal.X1], d.getY(), vertices[Decal.X2], d.getY());
    }

    public void endgame(float delta) {
        parent.batch.begin();
          parent.batch.draw(gameover,0,0);
        parent.batch.end();
    }

    public void play(float delta) {
//        Gdx.gl.glClearColor(0f, 0f, 0.5f, 300);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.getViewport().apply();

        updateThings(delta);

        parent.batch.setProjectionMatrix(camera.combined);

        parent.batch.begin();
            parent.batch.draw(spookyBg,0,0);
            parent.batch.draw(spookyBg,spookyBg.getWidth(),0);

            int n = boxview;
            for(int y= 0; y!= 9; y++){
                for(int x = 0; x!=9; x++){
                    if(n > 0 ) parent.batch.draw(getIcon(TYPE_BOARD,squares[y*9+x]),10+(x*64),y*64);
                    n--;
                }
            }

            parent.batch.draw(skeledude.getFrame(),
                (direction==-1
                    ? skeletonRect.x + 62
                    : skeletonRect.x - 30
                ),
                skeletonRect.y,
                92*direction,92
            );

            bf.draw(parent.batch, scoreText, 10,690);

            if(hitTime < 0.2f) parent.batch.draw(hitStar,hitRect.x, hitRect.y, hitRect.width, hitRect.height);
            if(hasJail){
                parent.batch.draw(
                getIcon(TYPE_TRAP, TRAP_JAIL),
                    (direction==-1
                    ? skeletonRect.x + 62
                    : skeletonRect.x - 30
                ),
                skeletonRect.y,
                92*direction,92);
            }
//            parent.batch.draw(badgood,skeletonRect.x,skeletonRect.y,0,badgood.getHeight()/2,badgood.getWidth(), badgood.getHeight()/2);
        parent.batch.end();

        sr.setProjectionMatrix(camera.combined);

        sr.begin(ShapeRenderer.ShapeType.Filled);
            drawDice(sr);

            if(mode == modes.DiceStart){
                sr.setColor(Color.ROYAL);
                sr.circle(diceStart.x, diceStart.y, diceSpeed.dst(0,0,0)/10);
            }
        sr.end();


//        sr.begin(ShapeRenderer.ShapeType.Filled);
//            sr.setColor(Color.RED);
//            sr.line(
//                diceStart.x,
//                diceStart.y,
//                diceStart.x+diceSpeed.x,
//                diceStart.y+diceSpeed.y
//            );//diceSpeed.x, -diceSpeed.y);
////            sr.line(0,600, diceSpeed.x, -diceSpeed.y);
////            sr.line(120, 120, diceRect.x, diceRect.y);
//        sr.end();

        parent.batch.begin();
            if(hitTime < 0.2f) parent.batch.draw(hitStar,hitRect.x, hitRect.y, hitRect.width, hitRect.height);
            for(int i=0; i!= hitpoints; i++) parent.batch.draw(heart, 10 + (10*i) + (32*i), 640);

            if(hasSword)  parent.batch.draw(getIcon(TYPE_POWERUP, PUP_SWORD),  camera.viewportWidth-40, camera.viewportHeight-40, 32, 32);
            if(hasShield) parent.batch.draw(getIcon(TYPE_POWERUP, PUP_SHIELD), camera.viewportWidth-80, camera.viewportHeight-40, 32, 32);
            if(hasSnake)  parent.batch.draw(getIcon(TYPE_TRAP,    TRAP_SNAKE), camera.viewportWidth-40, camera.viewportHeight-80, 32, 32);
            if(hasJail)   parent.batch.draw(getIcon(TYPE_TRAP,    TRAP_JAIL),  camera.viewportWidth-80, camera.viewportHeight-80, 32, 32);

            if(mode==modes.BadEvent){
                parent.batch.draw(
                    badgood,
                    skeletonRect.x+32,skeletonRect.y+64,128,64,
                    0,0,badgood.getWidth(), badgood.getHeight()/2,
                    false,false);

                if(trap>=0) {
                    parent.batch.setColor(1,1,1,1.1f-(eventTime) ); //1.5 is the event time duration
                    parent.batch.draw(
                        getIcon(TYPE_TRAP,trap),
                        skeletonRect.x-20,
                        skeletonRect.y+10 + (eventTime*64));
                    parent.batch.setColor(1,1,1,1);
                }

            }

            if(mode==modes.GoodEvent){
                parent.batch.draw(
                        badgood,skeletonRect.x+32,skeletonRect.y+64,128,64,
                        0,badgood.getHeight()/2,badgood.getWidth(), badgood.getHeight()/2,
                        false,false);

                if(powerup>=0){
                    parent.batch.setColor(1,1,1,1.1f-(eventTime) ); //1.5 is the event time duration
                    parent.batch.draw(
                        getIcon(TYPE_POWERUP,powerup),
                        skeletonRect.x-20,
                        skeletonRect.y+10 + (eventTime*64) );
                    parent.batch.setColor(1,1,1,1);
                }
            }
        parent.batch.end();


    }


    private TextureRegion getIcon(int type, int index){
        return icons[type][index];
    }

    private int   old_box = box; // remembers the last box we step on
    private float old_box_x; // this is meant to be a value between -1 .. 0 .. +1
    private int   steps;

    float eventTime;
    int   snakeSteps;
    private void updateThings(float delta){
        //calculate the position of the guy depending on what was the last box it step on
        // 64 is the width of the box, 9.0f is the count of boxes per row
        // y+10 is to center approximately the guy vertically in the box
        // x + 30 tries to center the guy horizontaly
        // 64*old_box_x gives one number between 0 to 64 pixels which is the distance the guy walks
        skeletonRect.y = (64*(int)(old_box / 9.0f)) + 10; //int to discard the decimals
        skeletonRect.x = (64*     (old_box % 9.0f)) + 30 + (64*old_box_x);
        skeledude.update(delta);

        switch(mode){
            case BadEvent:
                badEvent(delta);
                break;
            case GoodEvent:
                goodEvent(delta);
                break;
            case GuyMoving:
                updateGuyPosition(delta);
                break;
            case DiceStart:
                count=0;
                break;
            case DiceRolling:
                roll(delta);
                break;
            case DiceStop:
                if(hasJail){
                    mode = modes.DiceStart;
                    if(number < 3){
                        skeledude.set(AnimatedSprite.Set.Hit, false);
                        hitpoints--;
                    } else {
                        hasJail = false;
                    }
                } else {
                    boxview = Math.max(boxview, box+number+2);
                    mode    = modes.GuyMoving;

                    if(hasSnake) {
                        moveLeft();
                    } else {
                        moveRight();
                    }
                }
                break;
        }

        score();
    }

    int     powerup   =-1;
    int     hitAction = 0;
    boolean hasSword  = false;
    boolean hasShield = false;
    void goodEvent(float delta){
        eventTime+=delta;
        if(eventTime>1.5f){
            mode      = modes.DiceStart;
            eventTime = 0;
            powerup   = -1;
        }

        eventTime+=delta;

        if(powerup<0){
            powerup   = MathUtils.random(0,2);
            hitAction = 0;
        }

        //this might fail in cases where frame rate is too uneven
        if(eventTime>.7f && hitAction == 0){
            hitAction = 1;
            skeledude.set(AnimatedSprite.Set.Attacking, false);

            switch(powerup){
                case PUP_HEART : hitpoints++;    break;
                case PUP_SHIELD: hasShield=true; break;
                case PUP_SWORD : hasSword =true; break;
            }
        }

        if(eventTime>1.5f){
            mode = modes.DiceStart;
            powerup = -1;
            eventTime = 0;
        }

    }

    int trap=-1;
    boolean hasSnake=false;
    boolean hasJail =false;
    void badEvent(float delta){
        eventTime+=delta;

        if(trap < 0){
            trap = MathUtils.random(0,2);
            hitAction = 0;
        }

        //this might fail in cases where frame rate is too uneven
        if(eventTime>.7f && hitAction == 0){
            hitAction=1; //prevent entering this block

            AnimatedSprite.Set n = AnimatedSprite.Set.Hit;
            if(hasSword)  n = AnimatedSprite.Set.Attacking;
            if(hasShield) n = AnimatedSprite.Set.Attacking;
            skeledude.set(n, false);

            switch(trap){
                case TRAP_BEAR :
                    if(!hasShield) hitpoints--; // reduce health points after the event so that the game over screen doesnt interrupt
                    hasShield = false;
                    break;
                case TRAP_JAIL :
                    //roll above a 3 to move
                    hasJail=true;
                    break;
                case TRAP_SNAKE:
                    if(hasSword) {
                        hasSword  = false;
                        direction = 1;
                        hasSnake  = false;
                    } else {
                        //go back, if already going back go forward
                        direction  = -direction;
                        hasSnake   = !hasSnake;
                        snakeSteps = MathUtils.random(1,3);
                    }
                    break;
            }
        }

        if(eventTime>1.5f){
            mode = modes.DiceStart;
            eventTime = 0;
            trap = -1;
        }
    }

    void moveLeft(){
        if(box==0){
            //we arrived at box 0, so we wont go further left. Heal snake and go right
            hasSnake =false;
            moveRight();
            return;
        }
        box--;
        direction = -1;
        skeledude.set(AnimatedSprite.Set.Running, true);
    }

    void moveRight(){
        box++;
        direction = 1;
        skeledude.set(AnimatedSprite.Set.Running, true);
    }

    int number = 5;
    float lastRoll = 0.0f;
    float what = 0;
    void roll(float delta){
        lastRoll += delta;
        hitTime  += delta;
        if(mode==modes.DiceRolling){
            score =  0.25f - ( (diceSpeed.dst2(0, 0, 0) / maxLength) );
            if( lastRoll >  score  ) {
                what = lastRoll;
                number = MathUtils.random(0,5);
                lastRoll = 0;
            }

            float damp = 0.95f;
            diceSpeed.x *= damp;
            diceSpeed.y *= damp;
        }

        if(diceSpeed.dst2(0,0,0) < 1) {
            mode = modes.DiceStop;
            hitTime=10;
        }

        float newX, newY;
        newX = diceRect.x + (diceSpeed.x*0.5f);
        newY = diceRect.y + (diceSpeed.y*0.5f);

        if(newX - 32 < 0 || newX + 32 > camera.viewportWidth) {
            hitRect.x = (newX < 300
                ? -32
                : camera.viewportWidth-32);
            hitTime=0;
            hitRect.y = newY-32;

            newX = diceRect.x - (diceSpeed.x * 0.35f);
            diceSpeed.x *= -1;
        }
        if(newY - 32 < 0 || newY + 32 > camera.viewportHeight) {
            hitRect.y = (newY < 300
                ? -32
                : camera.viewportHeight-32);
            hitTime=0;
            hitRect.x = newX-32;

            newY = diceRect.y - (diceSpeed.y * 0.35f);
            diceSpeed.y *= -1;
        }

        diceRect.x = newX;
        diceRect.y = newY;

        count += delta;
        if(count > 3) {
            mode = modes.DiceStop;
        }
    }
    float count;

    int dots[][][] = {
        {{32,32}},
        {{53,53},{12,12}},
        {{53,53},{12,12},{32,32}},
        {{53,53},{12,12},{53,12},{12,53}},
        {{53,53},{12,12},{53,12},{12,53},{32,32}},
        {{53,53},{12,12},{53,12},{12,53},{12,32},{53,32}}
    };

    private void drawDice(ShapeRenderer sr){
        float x = diceRect.x-32, y = diceRect.y-32;
        sr.setColor(Color.BLACK);
        sr.rect(x-5, y-5, diceRect.width+10, diceRect.height+10);
        sr.setColor(Color.WHITE);
        sr.rect(x, y, diceRect.width, diceRect.height);
        sr.setColor(Color.BLACK);
        for(int dotlist[] : dots[number]) {
            sr.circle(x+dotlist[0],y+dotlist[1],6);
        }
    }

    private void updateGuyPosition(float delta){
        AnimatedSprite.Set n = skeledude.set();

        //we want to be able to change the next value for old_box_x depending on several conditions
        float newVal = (old_box_x + (delta*direction) );

        //when old_box is different from box, we are moving on the board
        if( (old_box==box) &&
            (box%9 == 0 || box%9 == 8) && // box%9 is 0 or 8 when its on the edge of a row
            (Math.signum(direction) == Math.signum(old_box_x)) // when old_box_x is different sign from direction, we've past the center
          ){
            //stop walking and reset position to the center of the box
            n = AnimatedSprite.Set.Idle;
            newVal=0;
        }

        //when newVal is smaller than 1 we're walking towards the next box
        if(Math.abs(newVal)>1){
            //assume we finished walking
            n = AnimatedSprite.Set.Idle;

            //we have past the center of the next box
            newVal  = 0; //reset to the center

            //walking from one edge towards outside of the screen
            if( (box%9==0 && old_box%9==8) ||
                (box%9==8 && old_box%9==0)
            ){
                //we make the guy start showing from outside of the screen, on the opposite direction its walking to
                newVal = -direction;
                n = AnimatedSprite.Set.Running; //keep walking
            }

            old_box = box; //mark we arrived to the new box
        }

        if(n == AnimatedSprite.Set.Idle){
            snakeSteps--;
            if(snakeSteps<=0){
                hasSnake = false;
                direction = 1;
            }

            if(steps < number) {
                steps++;
                box = (hasSnake ? box-1 : box+1);
                n   = AnimatedSprite.Set.Running;
            } else {
                mode  = modes.DiceStart;
                steps = 0;
                n     = AnimatedSprite.Set.Idle;

                if(squares[old_box] == TYPE_BOARD)   mode = modes.DiceStart;
                if(squares[old_box] == TYPE_TRAP)    mode = modes.BadEvent;
                if(squares[old_box] == TYPE_POWERUP) mode = modes.GoodEvent;
            }
        }

        //set() disallows reseting animation only when the new animation is the same
        //since we are going to do two conditional changes, we use an intermediate variable
        skeledude.set(n, true);

        old_box_x=newVal; //set the current position of the guy

    };

    private void score(){
        scoreText =  String.format("%s: %4.2f s:%d n:%d %4d->%1d ss:%d "+(direction > 0 ? "right" : "left"), mode.toString(), score, steps, number, old_box%9, (box%9), snakeSteps );
//        scoreText =  String.format(Locale.getDefault(), "Score: %2s %d %d b:%d o:%d", mode.toString(), steps, number, box, old_box);
    }

    @Override
    public void resize(int width, int height) {
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
        stage.dispose();
        skeleton.dispose();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode){
                  case Input.Keys.W: wasdqe[0]=1; return true;
                  case Input.Keys.A: wasdqe[1]=1; return true;
                  case Input.Keys.S: wasdqe[2]=1; return true;
                  case Input.Keys.D: wasdqe[3]=1; return true;
                  case Input.Keys.Q: wasdqe[4]=1; return true;
                  case Input.Keys.E: wasdqe[5]=1; return true;
                  
                  case Input.Keys.UP:    UDLR[0]=1; return true;
                  case Input.Keys.DOWN:  UDLR[1]=1; return true;
                  case Input.Keys.LEFT:  UDLR[2]=1; return true;
                  case Input.Keys.RIGHT: UDLR[3]=1; return true;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
//                if(mode == modes.GuyMoving) return GuyMovingKeyUp(keycode);
//                if(mode == modes.DiceStart) return diceStartKeyUp(keycode);

//                Gdx.app.log("cam", camera.position.toString());
                switch (keycode){
                    case Input.Keys.W: wasdqe[0]=0; return true;
                    case Input.Keys.A: wasdqe[1]=0; return true;
                    case Input.Keys.S: wasdqe[2]=0; return true;
                    case Input.Keys.D: wasdqe[3]=0; return true;
                    case Input.Keys.Q: wasdqe[4]=0; return true;
                    case Input.Keys.E: wasdqe[5]=0; return true;
  
                  case Input.Keys.UP:    UDLR[0]=0; return true;
                  case Input.Keys.DOWN:  UDLR[1]=0; return true;
                  case Input.Keys.LEFT:  UDLR[2]=0; return true;
                  case Input.Keys.RIGHT: UDLR[3]=0; return true;
                  case Input.Keys.P: {
                    choosenGLfunc = (choosenGLfunc+1) % gl_func.length;
                    return true;
                  }
                  case Input.Keys.O: {
                    choosenGLfunc = (choosenGLfunc = choosenGLfunc-1 % gl_func.length) < 0
                                      ? choosenGLfunc+gl_func.length
                                      : choosenGLfunc;
                    return true;
                  }
                  case Input.Keys.SPACE: cameraStrategy.setBuffer(!cameraStrategy.getBuffer());
                }
                return false;
            }

            public boolean diceStartKeyUp(int keycode){
                switch(keycode){
                    case Input.Keys.A:
                        if(number>0) number--;
                        return true;
                    case Input.Keys.S:
                        if(number<5) number++;
                        return true;
                    case Input.Keys.ENTER:
                        touchDown((int)camera.viewportWidth/2, (int)camera.viewportHeight/2, 1, 1);
                        touchDragged((int)camera.viewportWidth/2, (int)camera.viewportHeight/2, 1);
                        touchUp((int)camera.viewportWidth/2, (int)camera.viewportHeight/2, 1, 1);
                        return true;
                }
                return true;
            }

            public boolean GuyMovingKeyUp(int keycode) {
                if(keycode==Input.Keys.L){
                    old_box=box;
                    old_box_x=0;
                    return true;
                }

                if(box!=old_box) {
                    Gdx.app.log("keyup", "box != oldbox");
                    return true;
                }

                switch(keycode){
                    case Input.Keys.A:
                        if(box>0) {
                            box--;
                            direction=-1;
                            skeledude.set(AnimatedSprite.Set.Running, true);
                            return true;
                        }
                        break;
                    case Input.Keys.S:
                        box++;
                        direction=1;
                        skeledude.set(AnimatedSprite.Set.Running, true);
                        return true;
                    case Input.Keys.D:
                        //prevent restarting attack
                        if(skeledude.set() == AnimatedSprite.Set.Attacking) return true;
                        skeledude.set(AnimatedSprite.Set.Attacking, false);
                        return true;
                    case Input.Keys.F:
                        if(skeledude.set() == AnimatedSprite.Set.Hit) return true;
                        skeledude.set(AnimatedSprite.Set.Hit, false);
                        return true;
                }
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(mode == modes.DiceStart ){
//                    mode=modes.DiceStart;
                    x.set(screenX, screenY, 0);
                    camera.unproject(x);

                    diceStart.set(x.x,x.y,0);
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(mode==modes.DiceStart) {
                    mode = modes.DiceRolling;
                    maxLength = diceSpeed.dst2(0,0,0);
                }
                return false;
            }

            Vector3 x = new Vector3();
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(mode==modes.DiceStart){
                    x.set(screenX, screenY, 0);
                    camera.unproject(x);

                    diceSpeed.set(x.x-diceStart.x, x.y-diceStart.y,0);
                }
                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            
        });

    }


}
