class Player extends Entity{

  float maxspeed;
  float speed;
  float damping = 0.9;
  float health = 150;
  float accelRate;
  int originalHealthColor;
  int healthStroke = #54FF96;
  int healthColor = originalHealthColor = #54FF96;
  Player(float x, float y) {
    position = new PVector(x, y);
    velocity = new PVector(0, 0);
    acceleration = new PVector(0,0);
    fillColor = 255;
    maxspeed = 8;
    ewidth = eheight = 20;
    speed = .9;
    accelRate = 0.35;
  }

  void update() {
    inputUpDown();
    bounds(0);
    //inputCheck();
    //goToMouse();
    velocity.add(acceleration);
    velocity.mult(damping);
    velocity.limit(maxspeed);
    position.add(velocity);
    acceleration.mult(0);
  }

  void setHealthColor(int c){
      healthStroke = c;
  }

  void goToMouse(){
    PVector target = PVector.sub(new PVector(mouseX,mouseY), this.position); 
    float m = target.mag();
    if (m < 120) {
      float f = map(m,0,120,0,maxspeed);
      target.setMag(f);
    } else {
      target.setMag(maxspeed);
    }
    applyForce(target);
  }
  void display() {
    fill(fillColor);
    rect(position.x, position.y, ewidth, eheight);
    drawHealthBar();
  }

  // draws speed and health
  void drawHealthBar(){
    fill(healthColor);
    rect(60, 60, health, 10);
  }

  void minusHealthBy(float damage){
    health -= damage;
  }


  void applyForce(PVector force) {
    acceleration.add(force);
  }

  void bounds(float b){
      float buffer = b;
      if(position.y > height) position.y = 0;
      if(position.y <     0) position.y = height ;
      if(position.x > width) position.x = 0;
      if(position.x <     0) position.x = width;  
  }

  void inputUpDown() {
    //arrows input check
    if (keyboard.holdingLeft || keyboard.holdingA) {
      acceleration.x -= accelRate;
    } 
    else if ( keyboard.holdingRight || keyboard.holdingD) {
      acceleration.x += accelRate;
    }
    if (keyboard.holdingUp || keyboard.holdingW) {
      acceleration.y -= speed;
    }
    else if (keyboard.holdingDown ||keyboard.holdingS) {
      acceleration.y += speed;
    }
  }

  void inputCheck() {
    //arrows input check
    if (keyboard.holdingLeft) {
      acceleration.x -= speed;
    } 
    else if ( keyboard.holdingRight ) {
      acceleration.x += speed;
    }
    if (keyboard.holdingUp) {
      acceleration.y -= speed;
    }
    else if (keyboard.holdingDown) {
      acceleration.y += speed;
    }
    // wasd input check
    if (keyboard.holdingA) {
      acceleration.x -= speed;
    } 
    else if ( keyboard.holdingD ) {
      acceleration.x += speed;
    }
    if (keyboard.holdingW) {
      acceleration.y -= speed;
    }
    else if (keyboard.holdingS) {
      acceleration.y += speed;
    }
  }


}// end of player class

