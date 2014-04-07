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


	void canIntersect(boolean i){
		canIntersect = i;
	}

	void setMass(float m){
		mass = m;
	}
	// SET MASS FIRST  
	void setSize(float w,float h){
		this.eheight = w * mass;
		this.ewidth = h * mass;
	}

	boolean collidesWith(Entity e){
		return withinBoundingBox(e, this);
	}

	// void setMass(float m){
	// 	mass = m;
	// }

	// the bigger the mass the slow the speed
	void setSpeed(float s){
		speed = s / mass;
		acceleration.x = -speed;
	}

	void display(){
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

	void setColor(int fc){
		fillColor = fc;
		c = color(fc);
	}

	void setColor(int r,int g,int b){
		c = color(r,g,b);
	}

	color getColor(){
		return c;
	}

	void effect(float xo, float yo){
			pushStyle();
			float sw = 2;
			strokeWeight(.5);
			stroke(c);
			noFill();
			// scale width based on velocity.x
			float wScale = map(position.x,minEffectRange , width, 0,50); 
			rect(position.x + xo, position.y + yo,ewidth +wScale ,eheight);
			popStyle();
	}

	void run(){
		update();	
		display();
	}

	void setResetPoints(int[] rp){
		yCoord = rp;
	}

	void update(){
		velocity.add(acceleration);
		velocity.limit(maxspeed);
    	position.add(velocity);
    	velocity.mult(0);
    	if(outOfBounds()){
    		runWhenOutOfBounds();
    	}
	}

	// checks if the box is complety off screen 
	boolean outOfBounds(){
		return position.x + ewidth < 0;
	}

	//think of better method name
	void runWhenOutOfBounds(){
		position.x = random(width+ewidth, width+ewidth *2);
		position.y = random(height);
	}
		
	void applyForce(PVector force) {
    	PVector f = PVector.div(force, mass);
    	acceleration.add(f);
  	}
}//end of Block class
