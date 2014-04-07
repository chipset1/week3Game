class ForceBlock extends Block{

	//static
	int count = 0;

	ForceBlock(float x, float y){
		super(x,y);
		setColor(#0000ee);
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