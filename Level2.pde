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



	void init(){
		startTime = millis();
		blist = new ArrayList<Block>();
		lTime = 70 * 1000; // max speed time for end of level
		//shakeTime = 50 * 1000; // start of screen shake	
		mspeed = new PVector(0.02,.001);
		addBlocks();
		yCoord = new int[rows];
		for (int i = 0; i < rows; ++i) {
			yCoord[i] =i * gridSize;	
		}
	}

	void drawLevelBackground(){
		fill(0,40);
		rect(0,0,width,height);	
	}	

	void updateSpeed(){
		float a = map(millis(),0,lTime,0,.1);
		mspeed.x =a;
	}

	void addBlocks(){
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

	void update(Player player){
		
		updateBlocks(player);
		player.applyForce(mspeed);
		updateSpeed();
		fill(255);
		text("Thanks For Playing \n this level is incomplete", width/2, height/2);
	}

	void updateBlocks(Player player){
		for (Block b : blist) {
			b.run();
			if(b.collidesWith(player) && b.type == "KillerBlock"){
				player.minusHealthBy(3);
				player.setHealthColor((int)#ee0000);
			}
		}
	}

}