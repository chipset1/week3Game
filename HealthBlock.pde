class HealthBlock extends Block{
	float healAmount;	
	float spawnTime;

	HealthBlock(float x, float y, float spawnTime){
		super(x,y);
		this.spawnTime = spawnTime;
		eheight = ewidth = random(8,14);
		mass = 2;
		setSpeed(random(10,20));
		setColor(#49F21B);
		maxspeed = 10;
		type = "HealthBlock";
	}	

	void runWhenOutOfBounds(){
		// float startTime = millis();
		// if(millis() - startTime > spawnTime){
			position.x = random(width* random(6,8)+ewidth , width+ewidth *2 );
			position.y = random(height);
			//startTime = millis();
		//}
	}	
}