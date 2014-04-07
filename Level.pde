class Level{
	// Level base class
	ArrayList<Block> blist;
	float endOfLevelTime = 100 * 1000;
	float lTime; // max speed time for end of level
	float shakeTime; // start of screen shake
	boolean endLevel = false;
	float startTime;

	float getLTime(){
		return lTime;
	}

	float getLevelTime(){
		return lTime;
	}

	// displaying and updating done in here
	void update(Player p){

	}

	void drawLevelBackground(){

	}

	void init(){

	}


	boolean levelIsOver(){
		return millis() - startTime > endOfLevelTime;
	}

	void endLevelTransition(){

	}

}