ig.module(
	'game.entities.baneling'
)
.requires(
	'impact.entity',
	'impact.sound'
)
.defines(function()
{
	EntityBaneling = ig.Entity.extend(
	{
		health:10,
		healthbar:null,
		healthbarOffset: {x:0,y:10},
		
		name: 'baneling',
		animSheet: new ig.AnimationSheet('media/baneling.png',120,92),
		size: {x:78,y:23},
		offset: {x:18,y:70}, // pinpoint collisions, blank area around character
		flip: false,
		type: ig.Entity.TYPE.B, // group him in A
		checkAgainst: ig.Entity.TYPE.BOTH, // let monsters check
		collides: ig.Entity.COLLIDES.PASSIVE, // don't separate them on collision
		
		deathExplosion:70,
		
		deathSFX: new ig.Sound('media/sounds/baneling.*'),

		init:function(x,y,settings)
		{
			this.parent(x,y,settings);
			this.addAnim('idle',1,[0]);
		},
		
		update:function()
		{
			// near an edge? return!
			this.parent();
		},
		
		kill:function()
		{
			this.deathSFX.play();
			ig.game.spawnEntity(EntityBanelingExplosion, this.pos.x+this.size.x/2, this.pos.y+this.size.y/4, {particles:this.deathExplosion, colorOffset:1});
			this.parent();
		},

		check: function(other)
		{
			this.kill();
		}
	});

	EntityBanelingExplosion = ig.Entity.extend(
	{
		lifetime: 1,
		callBack: null,
		particles: 70,
		init: function(x,y,settings)
		{
			this.parent(x,y,settings);
			for(var i=0;i<this.particles;i++)
			{
				ig.game.spawnEntity(EntityBanelingParticle, x, y, {colorOffset:settings.colorOffset ? settings.colorOffset:1});
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
	
	EntityBanelingParticle = ig.Entity.extend(
	{
		size: {x:2,y:2},
		maxVel: {x:800,y:800},
		lifetime: 2,
		fadetime: 1,
		bounciness: 0,
		vel: {x: 100, y:30},
		friction: {x:100, y:0},
		collides: ig.Entity.COLLIDES.NONE,
		checkAgainst: ig.Entity.TYPE.BOTH,
		colorOffset: 0,
		totalColors: 7,
		animSheet: new ig.AnimationSheet('media/blood.png',2,2),

		name: 'banelingParticle',
		flip: false,

		explosionHeight: 350,
		explosionRadius: 100,
		
		init: function(x,y,settings)
		{
			this.parent(x,y,settings);
			var frameID = Math.round(Math.random()*this.totalColors) + (this.colorOffset*(this.totalColors+1));
			this.addAnim('idle', 0.2, [frameID]);
			this.pos.x-=30;
			this.vel.x = (Math.random()*this.explosionRadius-1);
			this.vel.y = -(Math.random()*this.explosionHeight-1);
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
		},

		check: function(other)
		{
			other.receiveDamage(1,this);
			this.kill();
		}
	});
});