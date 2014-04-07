import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class week3Game extends PApplet {

//color[] colors = {#F2B774, #FB6084, #FB0029,#A197C2,#597A74, #6D0D00, #C371F3, #1A3419,#878282, #E0E0E0};

//==== dark blue colors
//color[] colors = {#314E71,#284162,#2D4263,#23304B,#24283E,#222541,#474863,#40415C,#191930,#6F6A84,#686278};
int[] colors = {0xff314E71};

//==== Bright Purples -- http://www.deviantart.com/art/Moonchild-444646666
//color[] colors = {#201D4F,#22204F,#15244C,#182A60,#392763,#323268,#34336A,#35366A,#563282,#663289,#444E95,#555094,#5D528A,#345B99,#5B53A5,#4C5B9F,#515D91,#6D588F,#2E64AD,#70629F,#6F72AE,#5579BB,#7673B1,#8A71B8,#9C78C5,#578CC3,#957FB6,#628DC5,#9381C3,#8B85CB,#9884BD,#8393B8,#6B97DC,#789CC1,#8B98C8,#AE93C6,#BD8ED3,#C394BE,#B89AD3,#9EA6DB,#AEA4D3,#C2A7D8,#B2ACDF,#AFAEDC,#B8B0BA,#BEBBE6,#BBC2E1,#C7C9E5,#E3D1C8,#F6F6F8,#F6F7FA,#F6F8FB,#F7F8FB,#F8F8FA,#F8FAFC,#F8FCFD};

Keyboard keyboard;
Player player;
ArrayList<Level> levelsList;
int currentLevel = 0;

boolean paused = true;
boolean showStart = true;

ParticleSystem ps;

public void setup(){
	size(900,512,P2D);
	player = new Player(width/2, height/2);
	keyboard = new Keyboard();
    //rectMode(CENTER);
    colorMode(HSB);
    ps = new ParticleSystem();
    levelsList  = new ArrayList<Level>();
    levelsList.add(new Level1());
    levelsList.add(new Level2());
}

public void draw(){
	if(showStart){
		background(0);
		textSize(32);
		text("Week 3 game \nUse WASD and Arrow keys to move\nPress space to start", width/2 - 300, height/2);
	}
	if(paused){
		showStart = false;
		return;
	}
	drawBackground();
	//background(0,10);
	updateLevel();
	textSize(20);
   	fill(255);
	text(millis(), 50,50);
	//text(frameRate, 20,20);
		
	ps.playerParticle(player.position);
	ps.run();

	player.update();	
	player.display();
	// some pjs bug that doesn't break the reseting on the web
	//if(player.health <= 0) reset();
}

public void reset(){
	ps.playerDeath();
	player = new Player(width/2, height/2);
	levelsList.get(currentLevel).init();
}

public void drawTime(){
	float startTime = levelsList.get(currentLevel).startTime;
	fill(255);
	text(millis() - startTime, 100,100);
}

public void goToNextLevel(){
	currentLevel++;
	levelsList.get(currentLevel).init();
}

public void updateLevel(){
	levelsList.get(currentLevel).update(player);
}


public void drawBackground(){
	levelsList.get(currentLevel).drawLevelBackground();
}

public PVector randomForce(float speed){
		int count = (int) random(1,3);
		if(count == 1){
			return new PVector(0,speed);		
		}
		if(count == 2){
			return new PVector(0,-speed);		
		}
		if(count == 3){
			// left
			return new PVector(-speed,0);		
		}
		else{
				return new PVector(speed,0);		
		}
	}

// boolean DoBoxesIntersect(Entity a, Entity b) {
//   println(a.ewidth);
//   return (abs(a.position.x  - b.position.x) * 2 < (a.ewidth + b.ewidth)) &&
//          (abs(a.position.y - b.position.y) * 2 < (a.eheight + b.eheight));
// }

public boolean withinBoundingBox(Entity object1, Entity object2) {
 if (object1.position.x < object2.position.x + object2.ewidth  && object1.position.x + object1.ewidth  > object2.position.x &&
   object1.position.y < object2.position.y + object2.eheight && object1.position.y + object1.eheight > object2.position.y) {
   return true;
}
return false;
}

public PVector randomVector(){
  PVector pvec = new PVector(random(-1,1), random(-1,1), 0);
  pvec.normalize();
  return pvec;
}

public PVector randomVector(float range){
  PVector pvec = new PVector(random(-range,range), random(-range,range), 0);
  pvec.normalize();
  return pvec;
}

public void keyPressed(){
	if(key == ' ') paused = !paused;
    keyboard.pressKey(key, keyCode);
}

public void keyReleased(){
  	keyboard.releaseKey(key, keyCode);
}
class BgBlock extends Block{

	float strokeSize = random(.8f,2.1f);

	BgBlock(float x, float y){
		super(x,y);
		c = colors[(int)random(0,colors.length)];
	}

	public void display(){
			pushStyle();	
			strokeWeight(strokeSize);
			//stroke(c);
			fill(c);
			rect(position.x,position.y,ewidth , eheight);	
			popStyle();
		
			float wScale = map(position.x,minEffectRange , width, 0,.5f); 
			ewidth += wScale;
			constrain(width, 20, 80);
			effect(0,0);
	}

}
class Block extends Entity {
	// base block class 
	float mass  = 0;	
	float speed = 0;
	String type;
	float maxspeed = 80;


	float resetPointminY = 0;
	float resetPointmaxY = height;

	int[] yCoord;

	//float strokeSize = random(.2,2.1);

	boolean canIntersect = false;	

	boolean drawEffect = false;
	float minEffectRange = 0;

	Block(float x, float y){
		position = new PVector(x,y);
		velocity = new PVector(0,0);
		mass = random(2,3);
		eheight = ewidth = random(10,20) * mass;
		// bheight = random(,30);
		acceleration = new PVector(0,0);
		//c = color(random(120,150), 125,255);
		c = color(255);
	}


	public void canIntersect(boolean i){
		canIntersect = i;
	}

	public void setMass(float m){
		mass = m;
	}
	// SET MASS FIRST  
	public void setSize(float w,float h){
		this.eheight = w * mass;
		this.ewidth = h * mass;
	}

	public boolean collidesWith(Entity e){
		return withinBoundingBox(e, this);
	}

	// void setMass(float m){
	// 	mass = m;
	// }

	// the bigger the mass the slow the speed
	public void setSpeed(float s){
		speed = s / mass;
		acceleration.x = -speed;
	}

	public void display(){
			noStroke();
			fill(c);
			rect(position.x,position.y,ewidth , eheight);
			stroke(0, 30);
			float x = position.x;
			float y = position.y;
			line(x+ewidth, y, x + ewidth, y + eheight);
			line(x + ewidth, y + eheight, x, y + eheight); 
		if(drawEffect){
			// float wScale = map(position.x,minEffectRange , width, 0,1); 
			// ewidth += wScale;
//			effect(0,0);
		}	
	}

	public void setColor(int fc){
		fillColor = fc;
		c = color(fc);
	}

	public void setColor(int r,int g,int b){
		c = color(r,g,b);
	}

	public int getColor(){
		return c;
	}

	public void effect(float xo, float yo){
			pushStyle();
			float sw = 2;
			strokeWeight(.5f);
			stroke(c);
			noFill();
			// scale width based on velocity.x
			float wScale = map(position.x,minEffectRange , width, 0,50); 
			rect(position.x + xo, position.y + yo,ewidth +wScale ,eheight);
			popStyle();
	}

	public void run(){
		update();	
		display();
	}

	public void setResetPoints(int[] rp){
		yCoord = rp;
	}

	public void update(){
		velocity.add(acceleration);
		velocity.limit(maxspeed);
    	position.add(velocity);
    	velocity.mult(0);
    	if(outOfBounds()){
    		runWhenOutOfBounds();
    	}
	}

	// checks if the box is complety off screen 
	public boolean outOfBounds(){
		return position.x + ewidth < 0;
	}

	//think of better method name
	public void runWhenOutOfBounds(){
		position.x = random(width+ewidth, width+ewidth *2);
		position.y = random(height);
	}
		
	public void applyForce(PVector force) {
    	PVector f = PVector.div(force, mass);
    	acceleration.add(f);
  	}
}//end of Block class
abstract class Entity {
	PImage img;

	int fillColor = 255;
	int c;
	PVector position, velocity, acceleration;
	float ewidth = -1;
	float eheight = -1;
	float orientation;

	boolean isDead = false;

	//public PVector getSize() { return img == null ? new PVector(0,0) : new PVector(img.width,img.height); }
	public PVector getSize() {
		return ewidth == -1 || eheight == -1 ? null : new PVector(ewidth, eheight);
	}
	public abstract void update();

	public void display(){

	}

	// fix
	public boolean colidesWith(PVector pos){
		return false;
  	}



}
class ForceBlock extends Block{

	//static
	int count = 0;

	ForceBlock(float x, float y){
		super(x,y);
		setColor(0xff0000ee);
		type = "ForceBlock";
	}

	// PVector randomForce(float speed){
	// 	if(random(1.0) > .50){
	// 		// down
	// 		return new PVector(0,speed);		
	// 	}
	// 	if(random(1.0) > .50){
	// 		return new PVector(0,-speed);		
	// 	}
	// 	if(random(1.0) > .50){
	// 		// left
	// 		return new PVector(-speed,0);		
	// 	}else{
	// 			return new PVector(speed,0);		
	// 	}
	// }

	// void runWhenOutOfBounds(){
	// 	position.y = random(-eheight-20,-eheight);
	// 	position.x = random(width-ewidth);
	// }
}
class GridBlock{

	GridBlock(){

	}

	// void setResetPoints(int[] rp){
	// 	yCoord = rp;
	// }

	public void runWhenOutOfBounds(){
		// if(yCoord != null){
		// 	resetPointmaxY = yCoord[(int)random(0, yCoord.length)];
		// }
		// println(resetPointmaxY);
		// position.x = random(width+ewidth, width+ewidth *random(2,4) );
		// position.y = resetPointmaxY;
		// // setSpeed(random(speedRange));
	}


}
class HealthBlock extends Block{
	float healAmount;	
	float spawnTime;

	HealthBlock(float x, float y, float spawnTime){
		super(x,y);
		this.spawnTime = spawnTime;
		eheight = ewidth = random(8,14);
		mass = 2;
		setSpeed(random(10,20));
		setColor(0xff49F21B);
		maxspeed = 10;
		type = "HealthBlock";
	}	

	public void runWhenOutOfBounds(){
		// float startTime = millis();
		// if(millis() - startTime > spawnTime){
			position.x = random(width* random(6,8)+ewidth , width+ewidth *2 );
			position.y = random(height);
			//startTime = millis();
		//}
	}	
}
class Keyboard {
  Boolean holdingUp,holdingRight,holdingLeft,holdingDown,holdingZ,
  holdingW,holdingA,holdingS,holdingD,holdingM;
  
  Keyboard() {
    holdingUp=holdingRight=holdingLeft=holdingDown=holdingZ=holdingW=holdingA=holdingS=holdingD=holdingM=false;
  }

  public void pressKey(int key,int keyCode) {
    if (keyCode == UP) {
      holdingUp = true;
    }
    if (keyCode == LEFT) {
      holdingLeft = true;
    }
    if (keyCode == RIGHT) {
      holdingRight = true;
    }
    if (keyCode == DOWN) {
      holdingDown = true;
    }
    if (key == 'z' || key == 'Z') {
      holdingZ = true;      
    }
    if (key == 'w' || key == 'W') {
      holdingW = true;      
    }
    if (key == 'a' || key == 'A') {
      holdingA = true;      
    }
    if (key == 's' || key == 'S') {
      holdingS = true;      
    }
    if (key == 'd' || key == 'D') {
      holdingD = true;      
    }
    if (key == 'm' || key == 'M') {
      holdingM = true;      
    }
   
    /* // reminder: for keys with letters, check "key"
       // instead of "keyCode" !
    if (key == 'r') {
      // reset program?
    }
    if (key == ' ') {
      holdingSpace = true;
    }*/
  }
  public void releaseKey(int key,int keyCode) {
    if(key == 'z') {
      holdingZ = false;
    }
    if (keyCode == UP) {
      holdingUp = false;
    }
    if (keyCode == LEFT) {
      holdingLeft = false;
    }
    if (keyCode == RIGHT) {
      holdingRight = false;
    }
    if (keyCode == DOWN) {
      holdingDown = false;
    }
    if (key == 'z' || key == 'Z') {
      holdingZ = false;      
    }
    if (key == 'w' || key == 'W') {
      holdingW = false;      
    }
    if (key == 'a' || key == 'A') {
      holdingA = false;      
    }
    if (key == 's' || key == 'S') {
      holdingS = false;      
    }
    if (key == 'd' || key == 'D') {
      holdingD = false;      
    }
    if (key == 'm' || key == 'M') {
      holdingD = false;      
    }
  }
}
class KillerBlock extends Block{
	//damages player when it intersects
	//asdjf;asjd;lkajsd;fljk
	float speedRange = 0;


	KillerBlock(float x, float y, float speedRange){
		super(x,y);
		this.speedRange = speedRange;
		setColor(0xffee0000);
		type = "KillerBlock";
	}

	public void run(){
		display();
		update();
		if(velocity.mag() > 70){
			eheight +=0.50f;
		}
	}

	// void setResetPoint(float pmin, float pmax){
	// 	resetPointmin = pmin;
	// 	resetPointmax = pmax;
	// }

	public void runWhenOutOfBounds(){
		if(yCoord != null){
			resetPointmaxY = yCoord[(int)random(0, yCoord.length)];
			println(resetPointmaxY);

		}
		position.x = random(width+ewidth, width+ewidth *random(2,4) );
		position.y = random(resetPointminY,resetPointmaxY);
		// setSpeed(random(speedRange));
	}
}
class Level{
	// Level base class
	ArrayList<Block> blist;
	float endOfLevelTime = 100 * 1000;
	float lTime; // max speed time for end of level
	float shakeTime; // start of screen shake
	boolean endLevel = false;
	float startTime;

	public float getLTime(){
		return lTime;
	}

	public float getLevelTime(){
		return lTime;
	}

	// displaying and updating done in here
	public void update(Player p){

	}

	public void drawLevelBackground(){

	}

	public void init(){

	}


	public boolean levelIsOver(){
		return millis() - startTime > endOfLevelTime;
	}

	public void endLevelTransition(){

	}

}
class Level1 extends Level{
	int numKillerBlocks = 5;
	int numHealthBlocks = 3;
	int numForceBlocks = 3;
	float endTime = 30* 1000; // 
	int numBackgroundBlocks = 20;
	PVector mspeed;	
	Level1(){
		blist = new ArrayList<Block>();
		addBlocks();
		init();
	}

	public void init(){
		startTime = millis();
		
		lTime = 70 * 1000; // max speed time for end of level
		shakeTime = 50 * 1000; // start of screen shake	
		mspeed = new PVector(0.02f,.001f);
		randomBlocks();		
	}

	public void randomBlocks(){
		for(Block b : blist){
			b.position.set(random(width), random(height));
		}	
	}

	public void screenShake(){
		if(millis() > shakeTime){
			float a = map(millis() - startTime,shakeTime ,lTime+3*1000,0.0f,3.0f);
			//translate(random(-3.0,3.0),random(-3.0,3.0));
			translate(random(-a,a),random(-a,a));
		}
	}

	public void drawLevelBackground(){
		float a = map(millis() - startTime,0,lTime,255,20);
		fill(0,a);
		rect(0,0,width,height);
	}

	public void updateSpeed(){
		float a = map(millis() - startTime,0,lTime,0,.1f);
		mspeed.x =a;
	}

	public void update(Player player){
		// fill(255);
  //   	text(millis() - startTime, 60,45);	
		updateBlocks(player);
		player.applyForce(mspeed);
		updateSpeed();
		float x = map(millis()  - startTime,0,lTime,0,.9f);
		ps.setLevelForce(-x);
	}

	public void updateBlocks(Player player){
		screenShake();
		if(levelIsOver()){
			blist.clear();
			goToNextLevel();
		}
		for (Block b : blist) {
			
			b.run();
			updateBlockSpeed(b);
			if(millis() > lTime){
				// float a1 = map(millis()  - startTime,lTime, lTime+endTime,0.0,1.0);
			// color interC = lerpColor(b.getColor(), colors[(int)random(0, colors.length)], a1);
			float a1 = map(millis()  - startTime,lTime, lTime+endTime,110,125);
			b.setColor(color(random(a1,180),100+a1,255));
			}
			
			// if the block can't intersect then exit the 
			if(!b.canIntersect){
				//break completely exits the loop. 
				//Continue skips the statements after the continue statement and keeps looping.
				continue;
			}
			if(b.collidesWith(player) && b.type == "KillerBlock"){
					player.minusHealthBy(3);
					//player.setHealthColor((int)#ee0000);
					ps.killerBlockParticles(b.position);
			}
			if(b.collidesWith(player) && b.type == "ForceBlock"){
					ps.forceBlockParticles(player.position);
					mspeed.x = 100;
			}
			if(b.collidesWith(player) && b.type == "HealthBlock"){
				player.setHealthColor((int)0xff54FF96);
				player.minusHealthBy(-5);
				ps.healthBlockParticles(player.position);
			}
		}
	}

	public void updateBlockSpeed(Block b){
		float a = map(millis()  - startTime,0,lTime,0,70);
		//text(a, 100, 100);
		b.setSpeed(a);
	}

	public void addBlocks(){
		/// SET MASS FIRST
		for (int i = 0; i < numKillerBlocks; ++i) {
			Block b = new KillerBlock(random(0,width),random(height), 20);
			b.canIntersect(true);
			b.setSpeed(5);

			blist.add(b);
		}	
		for (int i = 0; i < numForceBlocks; ++i) {
			Block b1 = new ForceBlock(random(width),random(height));
			b1.canIntersect(true);
			b1.eheight +=random(10,20);
			b1.setSpeed(10);
			blist.add(b1);
		}
		for (int i = 0; i < numBackgroundBlocks; ++i) {
			Block b2 = new Block(random(width),random(height));
			b2.canIntersect(false);
			b2.setMass(random(2,4));
			b2.setSpeed(random(30,40));
			float rand = random(1,3);
			b2.setSize(rand, rand);
			blist.add(b2);
		}
		for (int i = 0; i < numHealthBlocks; ++i) {
			Block b2 = new HealthBlock(random(width,width*2),random(height), random(20));
			b2.canIntersect(true);
			blist.add(b2);
		}
	}


}// end of class
class Level2 extends Level{
	int numKillerBlocks = 15;
	int numHealthBlocks = 3;
	int numForceBlocks = 3;
	int numBackgroundBlocks = 20;
	PVector mspeed;	

	int[] yCoord;
	int gridSize = 40;
	int rows = height / gridSize;
	Level2(){

		//init();
	}



	public void init(){
		startTime = millis();
		blist = new ArrayList<Block>();
		lTime = 70 * 1000; // max speed time for end of level
		//shakeTime = 50 * 1000; // start of screen shake	
		mspeed = new PVector(0.02f,.001f);
		addBlocks();
		yCoord = new int[rows];
		for (int i = 0; i < rows; ++i) {
			yCoord[i] =i * gridSize;	
		}
	}

	public void drawLevelBackground(){
		fill(0,40);
		rect(0,0,width,height);	
	}	

	public void updateSpeed(){
		float a = map(millis(),0,lTime,0,.1f);
		mspeed.x =a;
	}

	public void addBlocks(){
		for (int i = 0; i < numBackgroundBlocks; ++i) {
			Block b2 = new BgBlock(random(width),random(height));
			b2.canIntersect(false);
			b2.setMass(random(2,4));
			b2.setSpeed(random(40,50));
			//b2.setColor(colors[(int)random(0,colors.length)]);
			float rand = random(30,40);
			b2.setSize(rand, 20);
			blist.add(b2);
		}
		for (int i = 0; i < numKillerBlocks; ++i) {
			Block b = new KillerBlock(random(0,width),random(height), 20);
		//	b.setResetPoints(yCoord);
			b.canIntersect(true);
			b.setSpeed(10);
			b.setMass(1);
			b.setSize(40, 40);
			blist.add(b);
		}
	}

	public void update(Player player){
		
		updateBlocks(player);
		player.applyForce(mspeed);
		updateSpeed();
		fill(255);
		text("Thanks For Playing \n this level is incomplete", width/2, height/2);
	}

	public void updateBlocks(Player player){
		for (Block b : blist) {
			b.run();
			if(b.collidesWith(player) && b.type == "KillerBlock"){
				player.minusHealthBy(3);
				player.setHealthColor((int)0xffee0000);
			}
		}
	}

}


// Simple Particle System
//From the nature of code

class Particle {
	// TODO
	//	Scale size with lifespan
	//	contructor with lifespan 
  PVector loc;
  PVector vel;
  float oLifespan;
  float lifespan = oLifespan = 50;
  float offset = .5f;
  //color[] colors = {#023F6A,#02477B,#024176,#02396D,#314E71,#284162,#2D4263,#23304B,#24283E,#222541,#474863,#40415C,#191930,#6F6A84,#686278};
  int[] cl = {0xff201D4F,0xff22204F,0xff15244C,0xff182A60,0xff392763,0xff323268,0xff34336A,0xff35366A,0xff563282,0xff663289,0xff444E95,0xff555094,0xff5D528A,0xff345B99,0xff5B53A5,0xff4C5B9F,0xff515D91,0xff6D588F,0xff2E64AD,0xff70629F,0xff6F72AE,0xff5579BB,0xff7673B1,0xff8A71B8,0xff9C78C5,0xff578CC3,0xff957FB6,0xff628DC5,0xff9381C3,0xff8B85CB,0xff9884BD,0xff8393B8,0xff6B97DC,0xff789CC1,0xff8B98C8,0xffAE93C6,0xffBD8ED3,0xffC394BE,0xffB89AD3,0xff9EA6DB,0xffAEA4D3,0xffC2A7D8,0xffB2ACDF,0xffAFAEDC,0xffB8B0BA,0xffBEBBE6,0xffBBC2E1,0xffC7C9E5,0xffE3D1C8,0xffF6F6F8,0xffF6F7FA,0xffF6F8FB,0xffF7F8FB,0xffF8F8FA,0xffF8FAFC,0xffF8FCFD,0xffF8F8FA,0xffF8FAFC,0xffF8FCFD,0xffF8F8FA,0xffF8FAFC,0xffF8FCFD};
  int c = cl[(int)random(0,cl.length)];
  float strokeSize = random(1,3.1f);
  float psize = 20;
  float minSize = 3;

	  Particle(float x, float y) {
      float r = 8 ;
     // vel = new PVector(random(- r , r ), random(- r , r ),random(- r , r ));
      loc = new PVector(x, y);
      //lifespan = 1000 + random(20,50);
      offset = random(0.1f, 0.4f);
	  }
    
    public void setVel(PVector _vel){
        vel = _vel;   
    }
    public void setSize(float _size){
      psize = _size;
    }
    public void setLifeTime(float lt){
      oLifespan = lifespan = lt; 
    }
    
    public void setColor(int _c){
      this.c = _c;
    } 
  
    public void run() {
      update();
     // render();
    }

    public void applyForce(PVector f) {
      vel.add(f);
    }

    // Method to update location
    public void update() {
         // denormalized floats cause significant performance issues
      if (abs(vel.x) + abs(vel.y) < 0.00000000001f){
        //println("defloat");
        vel.mult(0);
      } 
      if(psize < minSize) die();
      psize *= 0.98f;
      loc.add(vel);
      vel.mult(0.98f);
      //pushMatrix();
     // translate(loc.x, loc.y, loc.z);
      //rotate(1);
      render();
      //popMatrix();
      //vel.mult(0);

      // todo have life span based on millis()
      lifespan -= 1.0f;
    }

    public void render() {
      if(lifespan > 0){
        pushStyle();
          float a = map(lifespan, 0, oLifespan, 100, 255);
          strokeWeight(strokeSize);
          stroke(c,a);
          noFill();
          rect(loc.x, loc.y,psize + offset,psize + offset);
          //point(loc.x, loc.y,loc.z);
        popStyle();        
      }
    }

    public void checkBounds(){
       if (loc.x < 0) {vel.x = Math.abs(vel.x);
            } else if (loc.x > width) {vel.x = -Math.abs(vel.x);}
            if (loc.z < 0) {vel.y = Math.abs(vel.y);
            } else if (loc.y > height) {vel.y = -Math.abs(vel.y);}
           if(loc.y < 0) {vel.y = Math.abs(vel.y);}
    }

  public void die(){
      lifespan = 0;
  }

  // Is the particle still useful?
  public boolean isDead() {
    if (lifespan <= 0.0f) {
      return true;
    } 
    else {
      return false;
    }
  }
}
// A class to describe a group of Particles
// An ArrayList is used to manage the list of Particles 
class ParticleSystem {
  // add ship particles
  ArrayList<Particle> particles;    // An arraylist for all the particles

  PVector levelForce = new PVector(0,0);
  float pstartTime= 0;
  ParticleSystem() {
    particles = new ArrayList(); // Initialize the arraylist
  }
  public void run() {
      //pstartTime = millis();
      for (int i = particles.size()-1; i >= 0; i--) {
          Particle p = particles.get(i);
          p.run();
          p.applyForce(levelForce);
          if (p.isDead()) {
            particles.remove(i);
          }
      }
  }

  public void setLevelForce(float x){
    levelForce.x = x;
  }

  public void addParticle(float x, float y) {
    particles.add(new Particle(x,y));
  }
  
  public void addParticle(float x, float y, PVector vel) {
    // particles.add(new Particle(x,y,vel));
  }

  public void addParticle(PVector p) {
     particles.add(new Particle(p.x,p.y));
  }

  public void killerBlockParticles(PVector p){
      // PVector pos = PVector.add(p, randomVector(1));
      Particle particle = new Particle(p.x, p.y);
      particle.setLifeTime(100.0f);
      particle.setSize(50);
      particle.setVel(randomVector(1));
      particle.setColor(color(random(200,255), 200,255));
      particles.add(particle);
  }

  public void forceBlockParticles(PVector p ){
    for (int i = 0; i < 10; ++i) {
      Particle particle = new Particle(p.x, p.y);
      particle.setLifeTime(60.0f);
      particle.setSize(20);
      particle.setVel(randomVector(200));
      particle.setColor(color(random(140,170), 200,255));
      particles.add(particle);  
    }
  }

  public void healthBlockParticles(PVector p ){
    for (int i = 0; i < 20; ++i) {
      Particle particle = new Particle(p.x + random(-50,50), p.y + random(-50,50));
      particle.setLifeTime(60.0f);
      particle.setSize(random(30,50));
      particle.setVel(new PVector(-random(2,4), 0));
      particle.setColor(color(random(100,120), 200,255));
      particles.add(particle);  
    }
  }

  public void playerDeath(){
    for (int i = 0; i < 100; ++i) {
      Particle particle = new Particle(random(width),random(height));
      particle.setLifeTime(200.0f);
      particle.setSize(random(150,200));
      particle.setVel(randomVector(200));
      particles.add(particle);   
    }
  }

  public void playerParticle(PVector p){
    if(millis() - pstartTime > 50){
        Particle particle = new Particle(p.x - 20, p.y);
        particle.vel = new PVector(-random(10,15), 0);
        particle.setLifeTime(60.0f);
        particles.add(particle);
        pstartTime = millis();  
    }
  }
  


  public void applyForce(PVector f) {
    for (Particle p : particles) {
      p.applyForce(f);
    }
  }

  public void addParticle(Particle p) {
    particles.add(p);
  }

  // A method to test if the particle system still has particles
  public boolean dead() {
    if (particles.isEmpty()) {
      return true;
    } 
    else {
      return false;
    }
  }
  
  //(float x, float y, PVector _vel,float lifespan, float size, color _c)
  public void enemyExplode(float x, float y){
    int c = color(random(120,150), 200,255);
     for(int i = 0; i < 30; i++){
        PVector explodeVec = PVector.random2D();
        explodeVec.mult(8);
        //particles.add(new Particle(x,y,explodeVec,c, random(0.3,0.6)));
      } 
  }

  public void bulletExplode(float x, float y){
      for(int i = 0; i < 5; i++){
        int c = color(random(150,200), 200,255);
        PVector explodeVec = PVector.random2D();
        explodeVec.mult(10);
        //particles.add(new Particle(x,y,explodeVec,c,random(0.2,0.4)));
      }
  }

  public void sprayParticle(PVector pos, PVector sprayVel){
      int c = color(240,120, 240);
     // addParticle(new Particle(pos.x, pos.y, sprayVel,255f, 1f, c));
  }
  
}
class Player extends Entity{

  float maxspeed;
  float speed;
  float damping = 0.9f;
  float health = 150;
  float accelRate;
  int originalHealthColor;
  int healthStroke = 0xff54FF96;
  int healthColor = originalHealthColor = 0xff54FF96;
  Player(float x, float y) {
    position = new PVector(x, y);
    velocity = new PVector(0, 0);
    acceleration = new PVector(0,0);
    fillColor = 255;
    maxspeed = 8;
    ewidth = eheight = 20;
    speed = .9f;
    accelRate = 0.35f;
  }

  public void update() {
    inputUpDown();
    bounds(0);
    //inputCheck();
    //goToMouse();
    velocity.add(acceleration);
    velocity.mult(damping);
    velocity.limit(maxspeed);
    position.add(velocity);
    acceleration.mult(0);
  }

  public void setHealthColor(int c){
      healthStroke = c;
  }

  public void goToMouse(){
    PVector target = PVector.sub(new PVector(mouseX,mouseY), this.position); 
    float m = target.mag();
    if (m < 120) {
      float f = map(m,0,120,0,maxspeed);
      target.setMag(f);
    } else {
      target.setMag(maxspeed);
    }
    applyForce(target);
  }
  public void display() {
    fill(fillColor);
    rect(position.x, position.y, ewidth, eheight);
    drawHealthBar();
  }

  // draws speed and health
  public void drawHealthBar(){
    fill(healthColor);
    rect(60, 60, health, 10);
  }

  public void minusHealthBy(float damage){
    health -= damage;
  }


  public void applyForce(PVector force) {
    acceleration.add(force);
  }

  public void bounds(float b){
      float buffer = b;
      if(position.y > height) position.y = 0;
      if(position.y <     0) position.y = height ;
      if(position.x > width) position.x = 0;
      if(position.x <     0) position.x = width;  
  }

  public void inputUpDown() {
    //arrows input check
    if (keyboard.holdingLeft || keyboard.holdingA) {
      acceleration.x -= accelRate;
    } 
    else if ( keyboard.holdingRight || keyboard.holdingD) {
      acceleration.x += accelRate;
    }
    if (keyboard.holdingUp || keyboard.holdingW) {
      acceleration.y -= speed;
    }
    else if (keyboard.holdingDown ||keyboard.holdingS) {
      acceleration.y += speed;
    }
  }

  public void inputCheck() {
    //arrows input check
    if (keyboard.holdingLeft) {
      acceleration.x -= speed;
    } 
    else if ( keyboard.holdingRight ) {
      acceleration.x += speed;
    }
    if (keyboard.holdingUp) {
      acceleration.y -= speed;
    }
    else if (keyboard.holdingDown) {
      acceleration.y += speed;
    }
    // wasd input check
    if (keyboard.holdingA) {
      acceleration.x -= speed;
    } 
    else if ( keyboard.holdingD ) {
      acceleration.x += speed;
    }
    if (keyboard.holdingW) {
      acceleration.y -= speed;
    }
    else if (keyboard.holdingS) {
      acceleration.y += speed;
    }
  }


}// end of player class

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "week3Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
