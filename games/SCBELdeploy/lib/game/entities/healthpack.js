ig.module(
	'game.entities.healthpack'
)
.requires(
	'impact.entity'
)

.defines(function()
{
	EntityHealthpack = ig.Entity.extend(
	{
		type: ig.Entity.TYPE.NONE, // group him in A
		checkAgainst: ig.Entity.TYPE.A, // let monsters check
		collides: ig.Entity.COLLIDES.PASSIVE, // don't separate them on collision
		size:{x:35,y:35},
		offset:{x:6,y:5},
		thePlayer: null,
		triggered: false,
		healTimer: null,
		healRate: .3,
		healTick: 10,
		maxHeal: 50,
		totalHealed:0,
		animSheet: new ig.AnimationSheet('media/healthpack.png',50,50),

		
		init: function(x,y,settings)
		{
			this.parent(x,y,settings);	
			this.addAnim('idle',1,[0]);
			this.addAnim('used',1,[1]);	
			this.healTimer = new ig.Timer();	
			this.healTimer.pause();	
		},
		
		update:function()
		{
			if(!ig.global.wm)
			{
				try
				{
					this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
				}
				catch(err)
				{
					this.kill();
					console.log('error: healthpack');
				}
			}
			if(this.triggered)
			{
				this.healPlayer();
			}
		},
		
		check:function(other)
		{
			if(other)
			{
				this.triggered=true;
				this.healTimer.unpause();
				this.currentAnim = this.anims.used;
			}
		},

		healPlayer:function()
		{
			if(this.totalHealed < this.maxHeal && this.healTimer.delta() > this.healRate)
			{
				this.totalHealed+=this.healTick;
				try
				{
					this.thePlayer.health+=this.healTick;
					this.healTimer.reset();
					if(this.thePlayer.health >= 100)
					{
						this.thePlayer.health= 100;
						this.kill();
					}
				}
				catch(err)
				{
					console.log('error: healthpack accessing player object [NEGLIGIBLE');
				}
			}
			else if(this.totalHealed == this.maxHeal)
				this.kill();
		}
	});

});