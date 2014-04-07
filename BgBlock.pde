class BgBlock extends Block{

	float strokeSize = random(.8,2.1);

	BgBlock(float x, float y){
		super(x,y);
		c = colors[(int)random(0,colors.length)];
	}

	void display(){
			pushStyle();	
			strokeWeight(strokeSize);
			//stroke(c);
			fill(c);
			rect(position.x,position.y,ewidth , eheight);	
			popStyle();
		
			float wScale = map(position.x,minEffectRange , width, 0,.5); 
			ewidth += wScale;
			constrain(width, 20, 80);
			effect(0,0);
	}

}