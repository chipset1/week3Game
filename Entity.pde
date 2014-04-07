abstract class Entity {
	PImage img;

	int fillColor = 255;
	color c;
	PVector position, velocity, acceleration;
	float ewidth = -1;
	float eheight = -1;
	float orientation;

	boolean isDead = false;

	//public PVector getSize() { return img == null ? new PVector(0,0) : new PVector(img.width,img.height); }
	PVector getSize() {
		return ewidth == -1 || eheight == -1 ? null : new PVector(ewidth, eheight);
	}
	abstract void update();

	void display(){

	}

	// fix
	boolean colidesWith(PVector pos){
		return false;
  	}



}
