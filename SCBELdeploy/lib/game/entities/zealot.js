ig.module(
	'game.entities.zealot'
)
.requires(
	'impact.entity'
)
.defines(function()
{
	EntityZealot = ig.Entity.extend(
	{
		name: 'zealot',
		health: 150,
		healthbar:null,
		
		// need to expand .png file to allow wider spacing
		animSheet: new ig.AnimationSheet('media/zealot.png',105,158),
		size: {x:80,y:110},
		offset: {x:11,y:19}, // pinpoint collisions, blank area around character
		flip: false,
		maxVel: {x:100, y:150},
		friction: {x:200,y:0},
		accelGround:400,
		accelAir:200,
		jump:200,
		speed:100,
		type: ig.Entity.TYPE.B, // group him in A
		checkAgainst: ig.Entity.TYPE.A, // let monsters check
		collides: ig.Entity.COLLIDES.PASSIVE, // don't separate them on collision
		
		attackSpeed: 1,
		attacking: false,
		attackTimer: null,
		damage:30,
		
		thePlayer: null,

		dead: false,
		deathTime:.5,
		
		seenPlayer: false,
		aggroDistance: 400,

		attackSFX: new ig.Sound('media/sounds/zealot_swing.*'),
		deathSFX: new ig.Sound('media/sounds/zealot_death.*'),

		init:function(x,y,settings)
		{
			this.parent(x,y,settings);
			this.addAnim('idle',1,[0]);
			this.addAnim('run',0.1,[1,2,3,4]);
			this.addAnim('attack',this.attackSpeed/8,[5,6,8,8,9,10,11,12]);
			this.addAnim('fall',1,[0]);
			this.addAnim('die',this.deathTime/5,[13,14,15,16,17,17]);
			
			this.attackTimer = new ig.Timer();
			if(!ig.global.wm)
			{
				this.healthbar = ig.game.spawnEntity(EntityEnemyHealthbar,this.pos.x,this.pos.y,{maxHealth:this.health,theEnemy:this,offset:{x:0,y:20}});
			}
		},
		
		update:function()
		{
			// near an edge? return!
			this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
			this.checkForPlayer();
			if(this.thePlayer && this.seenPlayer)
			{
				this.attacking = this.touches(this.thePlayer);
				if(!this.dead)
				{
					if(!this.attacking && this.standing)
					{
						this.run();
						this.attackTimer.pause();
					}
					else if(!this.standing)
					{
						this.currentAnim = this.anims.fall;
					}
					else
					{
						this.attack();
					}
				} // not dead
				if(this.health<=0)
					this.die();
			}//if player not dead
			else
				this.currentAnim=this.anims.idle;
			this.parent();
		},
		
		run: function()
		{
			if(!ig.game.collisionMap.getTile(
				this.pos.x + (this.flip ? +4: this.size.x-4),
				this.pos.y + this.size.y+1)
				)
			{
				this.flip = !this.flip;
			}
			var xdir = this.flip ? -1: 1;
			this.vel.x = this.speed * xdir;
			this.currentAnim = this.anims.run;
			this.currentAnim.flip.x = this.flip;
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
				this.seenPlayer=false;

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
		
		attack:function()
		{
			if(this.attackTimer.delta()==0)
			{
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
		
		setVelX:function(value)
		{
			this.vel.x=0;
			this.friction.x=800;
			this.vel.x = value;
		},
		
		setVelY:function(value)
		{
			this.gravityFactor=1;
			this.vel.y = value;
		},
		
		die:function()
		{
			this.deathSFX.play();
			if(!this.dead)
				this.anims.die.rewind();
			this.dead=true;
			this.healthbar.kill();
			this.currentAnim=this.anims.die;
			if(this.currentAnim.frame==5)
			{
				this.kill();
			}
		},
		
		receiveDamage: function(amount,from)
		{
			this.health-=amount;
			this.healthbar.visible=true;

			if(this.health > 0)
			{
				if(from.name && from.name=='grenadeParticle' || from.name=='banelingParticle')
					ig.game.spawnEntity(EntityDeathExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/3, {particles:1, colorOffset:1});
				else
					ig.game.spawnEntity(EntityDeathExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/3, {particles:10, colorOffset:1});
			}
		},
	});
});
