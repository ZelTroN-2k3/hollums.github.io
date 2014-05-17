ig.module(
	'game.entities.zergling'
)
.requires(
	'impact.entity',
	'impact.sound'
)
.defines(function()
{
	EntityZergling = ig.Entity.extend(
	{
		health:40,
		healthbar:null,
		healthbarOffset: {x:0,y:10},
		
		name: 'zergling',
		animSheet: new ig.AnimationSheet('media/zergling.png',120,107),
		size: {x:105,y:59},
		offset: {x:10,y:20}, // pinpoint collisions, blank area around character
		flip: false,
		maxVel: {x:100, y:150},
		friction: {x:600,y:0},
		accelGround:400,
		accelAir:200,
		jump:200,
		speed:400,
		type: ig.Entity.TYPE.B, // group him in A
		checkAgainst: ig.Entity.TYPE.A, // let monsters check
		collides: ig.Entity.COLLIDES.PASSIVE, // don't separate them on collision
		
		groundTime: .3,
		groundTimer:null,
		onGround:true,
		airTimer: null,
		inAir:false,
		airTime:10,
		
		attackSpeed: .4,
		attacking: false,
		attackTimer: null,
		damage: 5,
		
		thePlayer: null,
		
		deathExplosion:50,

		seenPlayer: false,
		aggroDistance: 400,
		
		deathSFX: new ig.Sound('media/sounds/zerg_death.*'),
		attackSFX: new ig.Sound('media/sounds/zerg_attack.*'),

		init:function(x,y,settings)
		{
			this.parent(x,y,settings);
			this.addAnim('idle',1,[0]);
			this.addAnim('land',this.groundTime/2,[0,1]);
			this.addAnim('jump',.1,[3,4,5,4]);
			this.addAnim('attack',this.attackSpeed/8,[6,7,8,9,10,11,12]);
			this.groundTimer = new ig.Timer();
			
			this.attackTimer = new ig.Timer();
			if(!ig.global.wm)
				this.healthbar = ig.game.spawnEntity(EntityEnemyHealthbar,this.pos.x,this.pos.y,{maxHealth:this.health,theEnemy:this,offset:{x:this.healthbarOffset.x,y:this.healthbarOffset.y}});
		},
		
		update:function()
		{
			this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
			// near an edge? return!
			if(!ig.game.collisionMap.getTile(
				this.pos.x + (this.flip ? +4: this.size.x-4),
				this.pos.y + this.size.y+1)
				)
			{
				//this.flip = !this.flip;
			}
			this.checkForPlayer();
			if(this.thePlayer && this.seenPlayer)
			{
				this.attacking = this.touches(this.thePlayer);
				if(!this.attacking)
				{
					this.run();
					this.attackTimer.pause();
				}
				else
				{
					this.attack();
				}
			}
			else
				this.currentAnim=this.anims.idle;
			this.parent();
		},
		
		handleMovementTrace: function(res)
		{
			this.parent(res);
			// collision with a wall? return!
			if(res.collision.x)
			{
				this.flip = !this.flip;
			}
		},

		checkForPlayer: function()
		{
			if(this.thePlayer && Math.abs(this.pos.x - this.thePlayer.pos.x) < this.aggroDistance
					&& Math.abs(this.pos.y+this.size.y/2 - (this.thePlayer.pos.y+this.thePlayer.size.y/2)) < 50)
			{
				this.seenPlayer=true;
				if(this.thePlayer.pos.x < this.pos.x)
					this.flip = true;
			}
			if(this.thePlayer && this.thePlayer.pos.x > this.pos.x)
				this.flip = false;
			if(this.thePlayer && this.thePlayer.pos.x < this.pos.x)
				this.flip = true;
			if(!this.thePlayer)
				this.seenPlayer =false;

		},
		
		attack:function()
		{
			
			if(this.attackTimer.delta()==0)
			{
				this.gravityFactor=100;
				this.vel.x=0;
			}
			this.attackTimer.unpause();
			this.currentAnim = this.anims.attack;
			this.currentAnim.flip.x = this.thePlayer.pos.x < this.pos.x;
			if(this.attackTimer.delta()>this.attackSpeed/2)
			{
				this.attackSFX.play();
				this.thePlayer.receiveDamage(this.damage);
				this.attackTimer.reset();
			}
		},
		
		run: function()
		{
			var xdir = this.flip ? -1: 1;
			if(this.inAir && this.vel.y==0 && this.standing)
			{
				this.groundTimer.unpause();
				this.onGround=true;
				this.inAir=false;
				this.currentAnim = this.anims.land;
				this.vel.x = this.speed * xdir;
			}
			else if(!this.standing)
			{
				this.currentAnim = this.anims.jump;
			}
			else if(this.groundTimer.delta() > this.groundTime)
			{
				this.pos.y-=50;
				this.vel.x = this.speed*xdir;
				this.groundTimer.reset();
				this.groundTimer.pause();
				this.inAir=true;
				this.onGround=false;
			}
			this.currentAnim.flip.x = this.flip;
		},
		
		setVelX:function(value)
		{
			this.vel.x = value;
		},
		
		setVelY:function(value)
		{
			this.gravityFactor=1;
			this.vel.y = value;
		},
		
		kill:function()
		{
			this.deathSFX.play();
			ig.game.spawnEntity(EntityDeathExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/4, {particles:this.deathExplosion, colorOffset:1});
			this.parent();
			//this.thePlayer.attacker=null;
			this.healthbar.kill();
		},
		
		receiveDamage: function(amount,from)
		{
			this.healthbar.visible=true;
			this.parent(amount,from);
			if(this.health > 0)
			{
				if(from.name && from.name=='grenadeParticle' || from.name=='banelingParticle')
					ig.game.spawnEntity(EntityDeathExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/3, {particles:1, colorOffset:1});
				else
					ig.game.spawnEntity(EntityDeathExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/3, {particles:10, colorOffset:1});
			}
		},
	});

	EntityEnemyHealthbar= ig.Entity.extend(
	{
		animSheet: new ig.AnimationSheet('media/enemyhealthbar.png',180,180),
		size:{x:120,y:120},
		offset:{x:0,y:0},
		player: null,
		screen: {x:0,y:0},
		maxVel: {x:0,y:0},
		theEnemy: null,
		collides: ig.Entity.COLLIDES.PASSIVE,
		gravityFactor: 0,
		maxHealth: 100,
		currentHealth: 100,
		zIndex: 805,
		visible: false,
		init:function(x,y,settings)
		{
			this.parent(x,y,settings);
			this.addAnim('full',1,[0]);
			this.addAnim('damage1',1,[1]);
			this.addAnim('damage2',1,[2]);
			this.addAnim('damage3',1,[3]);
			this.addAnim('damage4',1,[4]);
			this.addAnim('damage5',1,[5]);
			this.addAnim('damage6',1,[6]);
			this.addAnim('damage7',1,[7]);
			this.addAnim('dead',1,[8]);
			this.currentAnim=null;
			//ig.game.spawnEntity(EntityFuelbar, this.pos.x, this.pos.y, {});
		},
		
		update:function()
		{
			if(this.theEnemy && this.visible)
			{
				this.pos.x = this.theEnemy.pos.x+this.theEnemy.size.x/2-10*this.size.x/15+this.offset.x;
				this.pos.y = this.theEnemy.pos.y-this.theEnemy.size.y/2-this.size.y/2+this.offset.y;
				this.currentHealth = this.theEnemy.health;
				var healthPercentage = (this.currentHealth / this.maxHealth)*100;
				var increments = 100/7;
				
				
				if(this.theEnemy.health==0)
					this.currentAnim=this.anims.dead;
				else if(healthPercentage==100)
				{
					this.currentAnim = this.anims.full;
				}
				else if(healthPercentage < 100 && healthPercentage >= (100-increments*1))
				{
					this.currentAnim = this.anims.damage1;
				}
				else if(healthPercentage < (100-increments*1) && healthPercentage >= (100-increments*2))
				{
					this.currentAnim = this.anims.damage2;
				}
				else if(healthPercentage < (100-increments*2) && healthPercentage >= (100-increments*3))
				{
					this.currentAnim = this.anims.damage3;
				}
				else if(healthPercentage < (100-increments*3) && healthPercentage >= (100-increments*4))
				{
					this.currentAnim = this.anims.damage4;
				}
				else if(healthPercentage < (100-increments*4) && healthPercentage >= (100-increments*5))
				{
					this.currentAnim = this.anims.damage5;
				}
				else if(healthPercentage < (100-increments*5) && healthPercentage >= (100-increments*6))
				{
					this.currentAnim = this.anims.damage6;
				}
				else if(healthPercentage < (100-increments*6) && healthPercentage >= (100-increments*7))
				{
					this.currentAnim = this.anims.damage7;
				}
				else
				{
					this.currentAnim = this.anims.dead;
				}
			}
			this.parent();
		}
	});

});