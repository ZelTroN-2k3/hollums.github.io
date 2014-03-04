ig.module(
	'game.entities.medivac'
)
.requires(
	'impact.entity'
)

.defines(function()
{
	EntityMedivac = ig.Entity.extend(
	{
		gravityFactor: 0,
		animSheet: new ig.AnimationSheet('media/medivac.png',180,180),
		size: {x:180,y:115},
		offset: {x:0,y:22},
		flip:false,
		name: 'medivac',
		checkAgainst: ig.Entity.TYPE.A,

		burnTimer: null,
		burnRate:.2,
		burning: 'false',

		maxVel: {x:100, y:150},

		zIndex:400,
		ropeLinks:null,

		holdingPlayer:false,

		init:function(x,y,settings)
		{
			this.addAnim('idle',1,[0]);
			this.addAnim('burning',.1,[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]);
			this.parent(x,y,settings);
			this.burnTimer = new ig.Timer();
			try
			{
				this.ropeLinks=ig.game.getEntitiesByType(EntityRopeLink);
			}
			catch(err)
			{}
		},

		update:function()
		{
			this.parent();
			if(this.burning=='true')
			{
				this.currentAnim = this.anims.burning;
			}
			else if(this.burning=='false' && !this.holdingPlayer)
			{
				this.currentAnim=this.anims.idle;
			}
			else
				this.vel.x+=2;
		},

		check: function(other)
		{
			if(this.burning=='true')
			{
				this.burnTimer.unpause();
				if(this.burnTimer.delta()>this.burnRate)
				{
					this.burnTimer.reset();
					other.receiveDamage(10,this);
				}
			}
			else if(this.burning=='false')
			{
				other.currentAnim.alpha=0;
				this.holdingPlayer=true;
				other.pos.x = this.pos.x;
				other.pos.y = this.pos.y;
				try
				{
					for(var i=0;i<this.ropeLinks.length;i++)
	            	{
	                	this.ropeLinks[i].currentAnim.alpha=0;
	            	}
            	}catch(err){}
			}
		}
	});

});