ig.module(
	'game.entities.water'
)
.requires(
	'impact.entity'
)

.defines(function()
{
	EntityWater = ig.Entity.extend(
	{
		_wmDrawBox: true,
		_wmBoxColor: 'rgba(0,0,225,0.7)',
		_wmScalable: true,
		size: {x:8,y:8},

		level:null,
		checkAgainst: ig.Entity.TYPE.A,
		thePlayer: null,

		dialogue:'null',

		init:function(x,y,settings)
		{
			try{
				this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
			}catch(err)
			{
				this.thePlayer=null;
			}
			this.parent(x,y,settings);
		},

		update: function()
		{
			if(this.thePlayer==null)
			{
				try
				{
					this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
				}catch(err)
				{
					this.thePlayer=null;
				}
			}
			if(!this.touches(this.thePlayer))
			{
				this.thePlayer.swimming=false;
			}
			//this.parent();
		},
		check: function(other)
		{
			if(!other.swinging && !other.climbing)
			{
				other.swimming=true;
			}
		}
	});
});