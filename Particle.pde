

// Simple Particle System
//From the nature of code

class Particle {
	// TODO
	//	Scale size with lifespan
	//	contructor with lifespan 
  PVector loc;
  PVector vel;
  float oLifespan;
  float lifespan = oLifespan = 50;
  float offset = .5;
  //color[] colors = {#023F6A,#02477B,#024176,#02396D,#314E71,#284162,#2D4263,#23304B,#24283E,#222541,#474863,#40415C,#191930,#6F6A84,#686278};
  color[] cl = {#201D4F,#22204F,#15244C,#182A60,#392763,#323268,#34336A,#35366A,#563282,#663289,#444E95,#555094,#5D528A,#345B99,#5B53A5,#4C5B9F,#515D91,#6D588F,#2E64AD,#70629F,#6F72AE,#5579BB,#7673B1,#8A71B8,#9C78C5,#578CC3,#957FB6,#628DC5,#9381C3,#8B85CB,#9884BD,#8393B8,#6B97DC,#789CC1,#8B98C8,#AE93C6,#BD8ED3,#C394BE,#B89AD3,#9EA6DB,#AEA4D3,#C2A7D8,#B2ACDF,#AFAEDC,#B8B0BA,#BEBBE6,#BBC2E1,#C7C9E5,#E3D1C8,#F6F6F8,#F6F7FA,#F6F8FB,#F7F8FB,#F8F8FA,#F8FAFC,#F8FCFD,#F8F8FA,#F8FAFC,#F8FCFD,#F8F8FA,#F8FAFC,#F8FCFD};
  color c = cl[(int)random(0,cl.length)];
  float strokeSize = random(1,3.1);
  float psize = 20;
  float minSize = 3;

	  Particle(float x, float y) {
      float r = 8 ;
     // vel = new PVector(random(- r , r ), random(- r , r ),random(- r , r ));
      loc = new PVector(x, y);
      //lifespan = 1000 + random(20,50);
      offset = random(0.1, 0.4);
	  }
    
    void setVel(PVector _vel){
        vel = _vel;   
    }
    void setSize(float _size){
      psize = _size;
    }
    void setLifeTime(float lt){
      oLifespan = lifespan = lt; 
    }
    
    void setColor(color _c){
      this.c = _c;
    } 
  
    void run() {
      update();
     // render();
    }

    void applyForce(PVector f) {
      vel.add(f);
    }

    // Method to update location
    void update() {
         // denormalized floats cause significant performance issues
      if (abs(vel.x) + abs(vel.y) < 0.00000000001f){
        //println("defloat");
        vel.mult(0);
      } 
      if(psize < minSize) die();
      psize *= 0.98;
      loc.add(vel);
      vel.mult(0.98);
      //pushMatrix();
     // translate(loc.x, loc.y, loc.z);
      //rotate(1);
      render();
      //popMatrix();
      //vel.mult(0);

      // todo have life span based on millis()
      lifespan -= 1.0;
    }

    void render() {
      if(lifespan > 0){
        pushStyle();
          float a = map(lifespan, 0, oLifespan, 100, 255);
          strokeWeight(strokeSize);
          stroke(c,a);
          noFill();
          rect(loc.x, loc.y,psize + offset,psize + offset);
          //point(loc.x, loc.y,loc.z);
        popStyle();        
      }
    }

    void checkBounds(){
       if (loc.x < 0) {vel.x = Math.abs(vel.x);
            } else if (loc.x > width) {vel.x = -Math.abs(vel.x);}
            if (loc.z < 0) {vel.y = Math.abs(vel.y);
            } else if (loc.y > height) {vel.y = -Math.abs(vel.y);}
           if(loc.y < 0) {vel.y = Math.abs(vel.y);}
    }

  void die(){
      lifespan = 0;
  }

  // Is the particle still useful?
  boolean isDead() {
    if (lifespan <= 0.0) {
      return true;
    } 
    else {
      return false;
    }
  }
}