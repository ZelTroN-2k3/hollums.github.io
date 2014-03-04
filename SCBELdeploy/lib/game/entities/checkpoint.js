ig.module(
	'game.entities.checkpoint'
)
.requires(
	'impact.entity'
)

.defines(function()
{
	EntityCheckpoint = ig.Entity.extend(
	{
		type: ig.Entity.TYPE.NONE, // group him in A
		checkAgainst: ig.Entity.TYPE.A, // let monsters check
		collides: ig.Entity.COLLIDES.PASSIVE, // don't separate them on collision
		size:{x:1,y:1},
		bombTimer: null,
		thePlayer: null,
		size:{x:40,y:100},

		_wmDrawBox: true,
        _wmScalable: true,
		
		init: function(x,y,settings)
		{
			this.parent(x,y,settings);
			this.bombTimer = new ig.Timer();
			this.bombTimer.pause();
				
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
					console.log('error:checkpoint');
					this.kill();
				}
			}
		},
		
		check:function(other)
		{
			if(other)
			{
				try
				{
					this.thePlayer.startPosition = {x:this.pos.x,y:this.pos.y+(2*this.size.y/3)};
				}
				catch(err)
				{
					console.log('error: checkpoint setting player start position [NEGLIGIBLE]');
				}
			}
		},
	});
});