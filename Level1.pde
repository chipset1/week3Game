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

	void init(){
		startTime = millis();
		
		lTime = 70 * 1000; // max speed time for end of level
		shakeTime = 50 * 1000; // start of screen shake	
		mspeed = new PVector(0.02,.001);
		randomBlocks();		
	}

	void randomBlocks(){
		for(Block b : blist){
			b.position.set(random(width), random(height));
		}	
	}

	void screenShake(){
		if(millis() > shakeTime){
			float a = map(millis() - startTime,shakeTime ,lTime+3*1000,0.0,3.0);
			//translate(random(-3.0,3.0),random(-3.0,3.0));
			translate(random(-a,a),random(-a,a));
		}
	}

	void drawLevelBackground(){
		float a = map(millis() - startTime,0,lTime,255,20);
		fill(0,a);
		rect(0,0,width,height);
	}

	void updateSpeed(){
		float a = map(millis() - startTime,0,lTime,0,.1);
		mspeed.x =a;
	}

	void update(Player player){
		// fill(255);
  //   	text(millis() - startTime, 60,45);	
		updateBlocks(player);
		player.applyForce(mspeed);
		updateSpeed();
		float x = map(millis()  - startTime,0,lTime,0,.9);
		ps.setLevelForce(-x);
	}

	void updateBlocks(Player player){
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
				player.setHealthColor((int)#54FF96);
				player.minusHealthBy(-5);
				ps.healthBlockParticles(player.position);
			}
		}
	}

	void updateBlockSpeed(Block b){
		float a = map(millis()  - startTime,0,lTime,0,70);
		//text(a, 100, 100);
		b.setSpeed(a);
	}

	void addBlocks(){
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