// A class to describe a group of Particles
// An ArrayList is used to manage the list of Particles 
class ParticleSystem {
  // add ship particles
  ArrayList<Particle> particles;    // An arraylist for all the particles

  PVector levelForce = new PVector(0,0);
  float pstartTime= 0;
  ParticleSystem() {
    particles = new ArrayList(); // Initialize the arraylist
  }
  void run() {
      //pstartTime = millis();
      for (int i = particles.size()-1; i >= 0; i--) {
          Particle p = particles.get(i);
          p.run();
          p.applyForce(levelForce);
          if (p.isDead()) {
            particles.remove(i);
          }
      }
  }

  void setLevelForce(float x){
    levelForce.x = x;
  }

  void addParticle(float x, float y) {
    particles.add(new Particle(x,y));
  }
  
  void addParticle(float x, float y, PVector vel) {
    // particles.add(new Particle(x,y,vel));
  }

  void addParticle(PVector p) {
     particles.add(new Particle(p.x,p.y));
  }

  void killerBlockParticles(PVector p){
      // PVector pos = PVector.add(p, randomVector(1));
      Particle particle = new Particle(p.x, p.y);
      particle.setLifeTime(100.0);
      particle.setSize(50);
      particle.setVel(randomVector(1));
      particle.setColor(color(random(200,255), 200,255));
      particles.add(particle);
  }

  void forceBlockParticles(PVector p ){
    for (int i = 0; i < 10; ++i) {
      Particle particle = new Particle(p.x, p.y);
      particle.setLifeTime(60.0);
      particle.setSize(20);
      particle.setVel(randomVector(200));
      particle.setColor(color(random(140,170), 200,255));
      particles.add(particle);  
    }
  }

  void healthBlockParticles(PVector p ){
    for (int i = 0; i < 20; ++i) {
      Particle particle = new Particle(p.x + random(-50,50), p.y + random(-50,50));
      particle.setLifeTime(60.0);
      particle.setSize(random(30,50));
      particle.setVel(new PVector(-random(2,4), 0));
      particle.setColor(color(random(100,120), 200,255));
      particles.add(particle);  
    }
  }

  void playerDeath(){
    for (int i = 0; i < 100; ++i) {
      Particle particle = new Particle(random(width),random(height));
      particle.setLifeTime(200.0);
      particle.setSize(random(150,200));
      particle.setVel(randomVector(200));
      particles.add(particle);   
    }
  }

  void playerParticle(PVector p){
    if(millis() - pstartTime > 50){
        Particle particle = new Particle(p.x - 20, p.y);
        particle.vel = new PVector(-random(10,15), 0);
        particle.setLifeTime(60.0);
        particles.add(particle);
        pstartTime = millis();  
    }
  }
  


  void applyForce(PVector f) {
    for (Particle p : particles) {
      p.applyForce(f);
    }
  }

  void addParticle(Particle p) {
    particles.add(p);
  }

  // A method to test if the particle system still has particles
  boolean dead() {
    if (particles.isEmpty()) {
      return true;
    } 
    else {
      return false;
    }
  }
  
  //(float x, float y, PVector _vel,float lifespan, float size, color _c)
  void enemyExplode(float x, float y){
    color c = color(random(120,150), 200,255);
     for(int i = 0; i < 30; i++){
        PVector explodeVec = PVector.random2D();
        explodeVec.mult(8);
        //particles.add(new Particle(x,y,explodeVec,c, random(0.3,0.6)));
      } 
  }

  void bulletExplode(float x, float y){
      for(int i = 0; i < 5; i++){
        color c = color(random(150,200), 200,255);
        PVector explodeVec = PVector.random2D();
        explodeVec.mult(10);
        //particles.add(new Particle(x,y,explodeVec,c,random(0.2,0.4)));
      }
  }

  void sprayParticle(PVector pos, PVector sprayVel){
      color c = color(240,120, 240);
     // addParticle(new Particle(pos.x, pos.y, sprayVel,255f, 1f, c));
  }
  
}