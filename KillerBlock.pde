class KillerBlock extends Block{
	//damages player when it intersects
	//asdjf;asjd;lkajsd;fljk
	float speedRange = 0;


	KillerBlock(float x, float y, float speedRange){
		super(x,y);
		this.speedRange = speedRange;
		setColor(#ee0000);
		type = "KillerBlock";
	}

	void run(){
		display();
		update();
		if(velocity.mag() > 70){
			eheight +=0.50;
		}
	}

	// void setResetPoint(float pmin, float pmax){
	// 	resetPointmin = pmin;
	// 	resetPointmax = pmax;
	// }

	void runWhenOutOfBounds(){
		if(yCoord != null){
			resetPointmaxY = yCoord[(int)random(0, yCoord.length)];
			println(resetPointmaxY);

		}
		position.x = random(width+ewidth, width+ewidth *random(2,4) );
		position.y = random(resetPointminY,resetPointmaxY);
		// setSpeed(random(speedRange));
	}
}