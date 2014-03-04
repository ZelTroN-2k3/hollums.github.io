ig.module(
	'game.entities.player'
)
.requires(
	'impact.entity',
	'game.entities.healthbar'
)
.defines(function()
{
	EntityPlayer = ig.Entity.extend(
	{
		health: 100,
		healthbar: null,
		fuel:200,
		fuelRegen:.25,
		fuelTimer:null,
		maxFuel: 200,
		fuelbar:null,
		
		screen: {x:0,y:0},
		animSheet: new ig.AnimationSheet('media/player.png',90,100),
		size: {x:50,y:100},
		offset: {x:25,y:-2}, // pinpoint collisions, blank area around character
		flip: false,
		maxVel: {x:100, y:150},
		friction: {x:600,y:0},
		accelGround:400,
		accelAir:200,
		jump:200,
		thrust:50,
		type: ig.Entity.TYPE.A, // group him in A
		checkAgainst: ig.Entity.TYPE.B, // let monsters check
		collides: ig.Entity.COLLIDES.PASSIVE, // don't separate them on collision
		
		climbing:false,
		swinging:false,
		climbSpeed: 2,
		over_ladder: false,
		over_rope: false,
        ladder: null,
        ropeLink:null,
        rope: null,

        falling: false,
        kicking: false,
        kickTime: null,
        kickDuration: .3,
        kickForce:600,
        zIndex: 300,

        swimming: false,
        drownTimer: null,
        drownTime: 3,
		
		direction:1,
		
		attacker: null,
		heavyDamage: 20,
		lightDamage: 2,
		
		fireRate:.2,
		fireTimer:null,
		damage:10,
		
		startPosition: null,
		invincible: true,
		invincibleDelay: 2,
		invincibleTimer: null,


		shootSFX: new ig.Sound('media/sounds/marine_shooting.*'),
		deathSFX: new ig.Sound('media/sounds/marine_death.*'),
		jetpackSFX: new ig.Sound('media/sounds/jetpack.*'),
		
		init: function(x,y,settings)
		{
			this.parent(x,y,settings); // entity parent object must have starting x and y
			this.setupAnimation();
			this.fireTimer=new ig.Timer();
			this.startPosition = {x:x,y:y};
			this.invincibleTimer = new ig.Timer();
			this.fuelTimer = new ig.Timer();
			this.drownTimer = new ig.Timer();
			this.makeInvincible();
			if(!ig.global.wm)
			{
				try
				{
					this.healthbar = ig.game.spawnEntity(EntityHealthbar,0,0,{});
					this.fuelbar = ig.game.getEntitiesByType(EntityFuelbar)[0];
					this.healthbar.maxHealth=this.health;
					this.fuelbar.maxFuel=this.fuel;
				}
				catch(err)
				{
					console.log(err.message);
				}
				
			}
		},


		makeInvincible: function()
		{
			this.invincible = true;
			this.invincibleTimer.reset();
		},
		
		setupAnimation: function()
		{
			this.addAnim('idle',1,[0]);
			this.addAnim('run',0.1,[0,1,2,3,4,5,6,7]);
			this.addAnim('shoot',0.1,[8,9,10,11]);
			this.addAnim('jump',1,[12,13]);
			this.addAnim('fly',0.1,[14,15,16,17,18,19,20,21,22]);
			this.addAnim('climb',.2,[23,24]);
			this.addAnim('kick',this.kickDuration/14,[25,26,26,26,27,27,27,28,28,28,28,28,28,28]);
			this.addAnim('fall',.1,[29,30,31]);
			this.addAnim('swing',1,[24]);
			this.addAnim('swim',.1,[32,33,34,35,36,37,38,39]);
			this.addAnim('idle_swim',1,[32]);
			// ADD FALLING, BLOCKING, SWIMMING, KICKING
		},
		
		update: function()
		{
			this.checkSituations();
			this.respondToInput();	
			this.parent();
			this.direction=this.flip ? -1:1;
			if( this.invincibleTimer.delta() > this.invincibleDelay && !this.swimming) 
			{
                this.invincible = false;
                this.currentAnim.alpha = 1;
            }
		},

		draw: function()
		{
            if(this.invincible)
                this.currentAnim.alpha = this.invincibleTimer.delta()/this.invincibleDelay * 1 ;
            if(this.currentAnim==this.anims.idle_swim)
            {
            	this.drownTimer.unpause();
            	this.currentAnim.alpha=1/(1+this.drownTimer.delta()/this.drownTime)*1;
            	if(this.drownTimer.delta()>this.drownTime)
            		this.kill();
            }
            this.parent();
        },
		
		respondToInput: function()
		{
			// move left or right
			// if this is standing, then accelerate via ground speed
			// if this is not standing, then accelerate at air speed.
			var accel = this.standing ? this.accelGround: this.accelAir;
			if(this.kicking)
			{
				this.kicking = this.kickTime.delta() < this.kickDuration;
				if(!this.kicking)
				{
					this.kickTime = null;
					this.currentAnim = this.anims.idle;

					this.anims.kick.update();
				}
			}
			
			if(ig.input.state('left') && this.currentAnim != this.anims.shoot && !this.climbing && !this.kicking)
			{
				this.accel.x = -accel;
				this.flip = true;
			}
			else if(ig.input.state('right') && this.currentAnim != this.anims.shoot && !this.climbing && !this.kicking)
			{
				this.accel.x = accel;
				this.flip = false;
			}
			else
			{
				this.accel.x = 0;
			}
			
			if(!this.falling)
			{
				this.anims.fall.rewind();
				this.anims.fall.update();
			}
			if(this.falling)
			{
				this.currentAnim = this.anims.fall;
				if(this.anims.fall.frame==2)
				{
					this.anims.fall.gotoFrame(2);
				}
			}
			
			// swing
			if(this.over_rope && ig.input.state('action') /*&& !this.rope==null && !this.ropeLink==null*/)
			{
				this.pos.x = this.ropeLink.pos.x;
				this.pos.y = this.ropeLink.pos.y;
				this.rope.swingActivated = true;
				this.currentAnim = this.anims.swing;
				this.gravityFactor = 0;
				this.swinging = true;
			}
			
			// jump
			
			if(this.over_ladder && ig.input.state('jump') && !this.topOfLadder())
			{
				this.vel.y = 0;
				this.gravityFactor=0;
				this.pos.y-=this.climbSpeed;
				this.currentAnim = this.anims.climb;
				this.climbing = true;
			}

			else if(!(this.over_ladder && ig.input.state('jump') && !this.topOfLadder()) && !this.swimming)
			{
				this.climbing = false;
				this.gravityFactor=1;
			}
			
			if(this.standing && ig.input.pressed('kick') && !this.kicking)
			{	
				this.vel.x = 0;
				try
				{
					if(this.attacker!=null && this.touches(this.attacker))
					{
						this.attacker.attacking=false;
						this.attacker.setVelY(-this.kickForce);
						this.attacker.setVelX(this.kickForce*this.direction);
						this.attacker=null;
					}
					this.kickTime = new ig.Timer();
					this.currentAnim = this.anims.kick;
					this.kicking=true;

					this.anims.kick.rewind();
				}catch(err)
				{
					console.log('error: kick, null attacker');
				}
			}
			
			// jump
			if((this.standing && ig.input.pressed('jump') && this.over_ladder) || (this.standing && ig.input.pressed('jump') && !this.topOfLadder()) && !this.swimming)
			{
				this.vel.y = -this.jump;
			}
			else if(this.swimming && ig.input.state('jump'))
			{
				this.vel.y--;
			}
			// fly
			if(ig.input.state('fly') && this.fuel!=0)
			{
				this.fuel--;
				this.gravityFactor = 0;
				this.currentAnim=this.anims.fly;
				this.falling = false;
				this.vel.y-=10;
				this.pos.x+=this.vel.x/this.thrust;
				this.flying=true;
				this.jetpackSFX.play();
			}

			// shoot
			else if(ig.input.state('shoot') && !this.falling && this.standing)
			{

				this.currentAnim=this.anims.shoot;
				if(this.fireTimer.delta()>this.fireRate)
				{
					this.shootSFX.play();
					ig.game.spawnEntity(EntityBullet,this.pos.x+this.size.x/2, this.pos.y+14*this.size.y/78, {damage: this.damage,flip:this.flip});
					this.fireTimer.reset();
				}
			}
			else if(ig.input.pressed('throw'))
			{
				ig.game.spawnEntity( EntityGrenade, this.pos.x, this.pos.y, {flip:this.flip} );
			}
			// set the current animation, based on the player's speed
			else if(this.vel.y < 0 && !this.swimming)
			{
				this.currentAnim = this.anims.jump;
			}
			
			else if(this.vel.x != 0 && !this.falling)
			{
				if(this.swimming)
				{
					this.currentAnim=this.anims.swim;
					this.drownTimer.reset();
				}
				else
					this.currentAnim = this.anims.run;
			}
			else if(this.vel.x == 0 && this.vel.y == 0
				 && !this.climbing && !this.kicking)
			{
				if(this.swimming)
				{
					this.currentAnim = this.anims.idle_swim;
					this.pos.y++;
				}
				else
					this.currentAnim = this.anims.idle;
				this.falling=false;
			}
			// move!
			this.currentAnim.flip.x = this.flip;
		},
		
		checkSituations: function()
		{
			this.check_for_ladder();
			this.check_for_falling();
			this.check_for_rope();
			this.popOffLadder();
			if(!ig.input.state('shoot'))
				this.shootSFX.stop();
			if(!ig.input.state('fly'))
			{
				this.jetpackSFX.stop();
				this.flying=false;
			}
			if(!ig.input.state('fly') && this.fuel <= this.maxFuel && this.fuelTimer.delta() >= this.fuelRegen)
			{
				this.fuel++;
				this.fuelTimer.reset();
			}
			if(this.standing)
			{
				this.swimming = false;
				this.gravityFactor = 1;
			}
			else if(this.swimming)
			{
				this.gravityFactor=0;
				this.vel.y=0;
				this.falling=false;
				this.currentAnim = this.anims.swim;
				this.friction.x = 100;
			}
		},
		
		// http://impactjs.com/forums/help/creating-a-ladder-entity-in-impactjs/page/1
		check_for_ladder: function() {
            this.ladder = null;
            this.over_ladder = false;
            
            var ladders;
            try
            {
            	ladders = ig.game.getEntitiesByType(EntityLadder);
            }
            catch(err)
            {
            	return false;
            }
            if(ladders!=null)
            {
            	this.size.y += 1; // because 1px under still counts
	            for(var i=0; i<ladders.length; i++) {
	                if(this.touches(ladders[i])) {
	                    this.ladder = ladders[i];
	                    this.over_ladder = true;
	                    break;
	                }
	            }
            	this.size.y -= 1;
            }
            else
            	return false;
        },
        
        check_for_rope: function() {
            //this.ladder = null;
            this.over_rope = false;
            var ropeLinks;
            try
            {
				ropeLinks = ig.game.getEntitiesByType(EntityRopeLink);
            }
            catch(err)
            {
            	return false;
            }
            
            
            if(ropeLinks)
            {
	            //this.size.y += 1; // because 1px under still counts
	            try
	            {
	        		this.rope=ropeLinks[0].getRope();
	        	}
	        	catch(err)
	        	{
	        		return false;
	        	}
	            for(var i=0; i<ropeLinks.length; i++) {
	                if(this.touches(ropeLinks[i])) {
	                    this.ropeLink = ropeLinks[i];
	                    this.over_rope= true;
	                    break;
	                }
	            }
	            if(!this.over_rope)
	            {
	            	this.rope = null;
	            	this.ropeLink = null;
	            	this.swinging=false;
	            }
	        }
            else
            	return;
        },
		
		check_for_falling: function()
		{
			if(this.vel.y > 0 && !this.climbing)
			{
				this.currentAnim = this.anims.jump;
				this.falling = true;
			}	
			else
			{
				this.falling = false;
			}
		},
		
		topOfLadder: function()
		{	
			if(this.ladder!=null)
				return this.pos.y + this.size.y - 2 <= this.ladder.pos.y;
			else
			{
				if(this.currentAnim == this.anims.climb)
					this.currentAnim = this.anims.idle;
				return false;
			}
		},
		
		popOffLadder: function()
		{	
			if(this.climbing && this.ladder!=null && this.pos.y+this.size.y-10 <=this.ladder.pos.y)
			{	
				this.pos.y-=30;
				this.climbing=false;
			}
		},
		
		receiveDamage: function(amount,from)
		{
			if(!this.invincible)
				this.health-=amount;
			if(this.invincible)
				return;
			if(this.health > 0)
			{
				try
				{
					if(this.attacker.name=='zealot')
					{
						ig.game.spawnEntity(EntityDeathExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/4, {particles:this.heavyDamage, colorOffset:0});
					}		
					else if(this.attacker.name=='zergling')
					{
						ig.game.spawnEntity(EntityDeathExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/2, {particles:this.lightDamage, colorOffset:0});
					}
					else if(from.name && from.name == 'medivac')
						ig.game.spawnEntity(EntityDeathExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/2, {particles:this.lightDamage, colorOffset:0});
					else if(from.name && from.name == 'grenadeParticle')
						ig.game.spawnEntity(EntityDeathExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/2, {particles:1, colorOffset:0});
				}
				catch(err)
				{}

			}
			else if(this.health<=0)
			{
				//this.healthbar.currentAnim = this.healthbar.anims.dead;
				this.kill();
			}
			//this.parent(amount,from);
		},
		
		kill:function()
		{
			var x = this.startPosition.x;
			var y = this.startPosition.y;
			this.deathSFX.play();
			this.parent();
			ig.game.spawnEntity(EntityDeathExplosion, this.pos.x, this.pos.y,
			{callBack:function(){ig.game.spawnEntity(EntityPlayer,x,y)},particles:300});
		},
		
		check: function(other)
		{
			if(other)
			{
				this.attacker = other;
			}
		}
	});
	
	EntityDeathExplosion = ig.Entity.extend(
	{
		lifetime: 1,
		callBack: null,
		particles: 25,
		init: function(x,y,settings)
		{
			this.parent(x,y,settings);
			for(var i=0;i<this.particles;i++)
			{
				ig.game.spawnEntity(EntityDeathExplosionParticle, x, y, {colorOffset:settings.colorOffset ? settings.colorOffset:0});
			}
			this.idleTimer = new ig.Timer();
		},
		
		update: function()
		{
			if(this.idleTimer.delta() > this.lifetime)
			{
				this.kill();
				if(this.callBack)
					this.callBack();
				return;
			}
		}
	});
	
	EntityDeathExplosionParticle = ig.Entity.extend(
	{
		size: {x:2,y:2},
		maxVel: {x:160,y:200},
		lifetime: 2,
		fadetime: 1,
		bounciness: 0,
		vel: {x: 100, y:30},
		friction: {x:100, y:0},
		collides: ig.Entity.COLLIDES.LITE,
		colorOffset: 0,
		totalColors: 7,
		animSheet: new ig.AnimationSheet('media/blood.png',2,2),
		
		init: function(x,y,settings)
		{
			this.parent(x,y,settings);
			var frameID = Math.round(Math.random()*this.totalColors) + (this.colorOffset*(this.totalColors+1));
			this.addAnim('idle', 0.2, [frameID]);
			this.vel.x = (Math.random()*2-1)*this.vel.x;
			this.vel.y = (Math.random()*2-1)*this.vel.y;
			this.idleTimer = new ig.Timer();
		},
		
		update: function()
		{
			if(this.idleTimer.delta() > this.lifetime)
			{
				this.kill();
				return;
			}
			this.currentAnim.alpha = this.idleTimer.delta().map(this.lifetime-this.fadetime, this.lifetime, 1, 0);
			this.parent();
		}
	});
	
	EntityBullet = ig.Entity.extend(
	{
		size: {x: 5, y: 3},
		maxVel: {x:500, y:0},
		type: ig.Entity.TYPE.NONE,
		checkAgainst: ig.Entity.TYPE.B,
		collides: ig.Entity.COLLIDES.PASSIVE,
		damage:0,
		startPosition: {x:0},
		distanceTraveled: 0,
		range: 450,
		
		init: function(x,y,settings)
		{
			this.parent(x+(settings.flip ? -4: 8), y+8, settings);
			this.startPosition.x = this.pos.x;
			this.vel.x = this.accel.x = (settings.flip ? -this.maxVel.x: this.maxVel.x);
		},
		
		update:function()
		{
			this.distanceTraveled=Math.abs(this.startPosition.x-this.pos.x);
			if(this.distanceTraveled>=this.range)
			{
				this.kill();
			}
			this.parent();
		},
		// checks collisions with collisionMap
		handleMovementTrace: function(res)
		{	
			this.parent(res);
			if(res.collision.x || res.collision.y)
			{
				this.kill();
			}
		},
		
		check: function(other)
		{
			other.receiveDamage(this.damage,this);
			this.kill();
		}
	});

    EntityGrenade = ig.Entity.extend({
        size: {x: 8, y: 8},
        offset: {x: 4, y: 4},
        animSheet: new ig.AnimationSheet( 'media/grenade.png', 16, 16 ),
        type: ig.Entity.TYPE.NONE,
        checkAgainst: ig.Entity.TYPE.BOTH,
        collides: ig.Entity.COLLIDES.PASSIVE,
        maxVel: {x: 200, y: 200},
        bounciness: 0.6,
        bounceCounter: 0,
        blastPower: 75,
        explodeSFX: new ig.Sound('media/sounds/explosion.*'),
        init: function( x, y, settings ) {
            //this.parent( x + (settings.flip ? -4:7), y, settings );
            this.parent( x + (settings.flip ? -50:50), y, settings );
            this.vel.x = (settings.flip ? -this.maxVel.x : this.maxVel.x);
            this.vel.y = -(50 + (Math.random()*100));
            this.addAnim( 'idle', 0.2, [0,1] );
        },
        handleMovementTrace: function( res ) {
        	this.parent( res );
        	if( res.collision.x || res.collision.y ) {
        		// only bounce 3 times
        		this.bounceCounter++;
        		if( this.bounceCounter > 3 ) {
        			this.kill();
        		}
        	}
        },
        check: function( other ) {
        	other.receiveDamage( 0, this );
        	this.kill();
        },
        kill: function(){
            for(var i = 0; i < this.blastPower; i++)
                ig.game.spawnEntity(EntityGrenadeParticle, this.pos.x, this.pos.y);
            this.explodeSFX.play();
            this.parent();
        }
    });

	EntityGrenadeParticle = ig.Entity.extend({
        size: {x: 4, y: 4},
        maxVel: {x: 160, y: 200},
        lifetime: 1,
        fadetime: 1,
        bounciness: 0.3,
        vel: {x: 40, y: 50},
        friction: {x:20, y: 20},
        checkAgainst: ig.Entity.TYPE.BOTH,
        collides: ig.Entity.COLLIDES.LITE,
        animSheet: new ig.AnimationSheet( 'media/explosion.png', 2, 2 ),

        blastRadius: 7,
        blastHeight: 20,
        damage: 1,

        name:'grenadeParticle',

        init: function( x, y, settings ) {
            this.parent( x, y, settings );
            this.vel.x = (Math.random() * this.blastRadius - 1) * this.vel.x;
            this.vel.y = (Math.random() * this.blastHeight - 1) * this.vel.y;
            this.idleTimer = new ig.Timer();
            var frameID = Math.round(Math.random()*7);
            this.addAnim( 'idle', 0.2, [frameID] );
        },
        update: function() {
            if( this.idleTimer.delta() > this.lifetime ) {
                this.kill();
                return;
            }
            this.currentAnim.alpha = this.idleTimer.delta().map(
                this.lifetime - this.fadetime, this.lifetime,
                1, 0
            );
            this.parent();
        },

        check: function(other)
        {
        	other.receiveDamage(this.damage,this);
        	this.kill();
        }
    });
});