//color[] colors = {#F2B774, #FB6084, #FB0029,#A197C2,#597A74, #6D0D00, #C371F3, #1A3419,#878282, #E0E0E0};

//==== dark blue colors
//color[] colors = {#314E71,#284162,#2D4263,#23304B,#24283E,#222541,#474863,#40415C,#191930,#6F6A84,#686278};
color[] colors = {#314E71};

//==== Bright Purples -- http://www.deviantart.com/art/Moonchild-444646666
//color[] colors = {#201D4F,#22204F,#15244C,#182A60,#392763,#323268,#34336A,#35366A,#563282,#663289,#444E95,#555094,#5D528A,#345B99,#5B53A5,#4C5B9F,#515D91,#6D588F,#2E64AD,#70629F,#6F72AE,#5579BB,#7673B1,#8A71B8,#9C78C5,#578CC3,#957FB6,#628DC5,#9381C3,#8B85CB,#9884BD,#8393B8,#6B97DC,#789CC1,#8B98C8,#AE93C6,#BD8ED3,#C394BE,#B89AD3,#9EA6DB,#AEA4D3,#C2A7D8,#B2ACDF,#AFAEDC,#B8B0BA,#BEBBE6,#BBC2E1,#C7C9E5,#E3D1C8,#F6F6F8,#F6F7FA,#F6F8FB,#F7F8FB,#F8F8FA,#F8FAFC,#F8FCFD};

Keyboard keyboard;
Player player;
ArrayList<Level> levelsList;
int currentLevel = 0;

boolean paused = true;
boolean showStart = true;

ParticleSystem ps;

void setup(){
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

void draw(){
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

void reset(){
	ps.playerDeath();
	player = new Player(width/2, height/2);
	levelsList.get(currentLevel).init();
}

void drawTime(){
	float startTime = levelsList.get(currentLevel).startTime;
	fill(255);
	text(millis() - startTime, 100,100);
}

void goToNextLevel(){
	currentLevel++;
	levelsList.get(currentLevel).init();
}

void updateLevel(){
	levelsList.get(currentLevel).update(player);
}


void drawBackground(){
	levelsList.get(currentLevel).drawLevelBackground();
}

PVector randomForce(float speed){
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

boolean withinBoundingBox(Entity object1, Entity object2) {
 if (object1.position.x < object2.position.x + object2.ewidth  && object1.position.x + object1.ewidth  > object2.position.x &&
   object1.position.y < object2.position.y + object2.eheight && object1.position.y + object1.eheight > object2.position.y) {
   return true;
}
return false;
}

PVector randomVector(){
  PVector pvec = new PVector(random(-1,1), random(-1,1), 0);
  pvec.normalize();
  return pvec;
}

PVector randomVector(float range){
  PVector pvec = new PVector(random(-range,range), random(-range,range), 0);
  pvec.normalize();
  return pvec;
}

void keyPressed(){
	if(key == ' ') paused = !paused;
    keyboard.pressKey(key, keyCode);
}

void keyReleased(){
  	keyboard.releaseKey(key, keyCode);
}
