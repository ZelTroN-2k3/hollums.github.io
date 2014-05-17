ig.module(
	'game.entities.zerglingTrigger'
)
.requires(
	'impact.entity'
)

.defines(function()
{
	EntityZerglingTrigger = ig.Entity.extend(
	{
		type: ig.Entity.TYPE.NONE, // group him in A
		checkAgainst: ig.Entity.TYPE.A, // let monsters check
		collides: ig.Entity.COLLIDES.PASSIVE, // don't separate them on collision
		thePlayer: null,
		size:{x:40,y:100},

		_wmDrawBox: true,
        _wmScalable: true,

        numberOfZerglings: 1,
        spawnTimer: null,
        triggered: false,
        spawnCount: 0,
        spawnRate: 1,

        distanceFromPlayer: 550,

        type: 'zergling',
		
		init: function(x,y,settings)
		{
			this.parent(x,y,settings);		
			this.spawnTimer = new ig.Timer();	
			this.spawnTimer.pause();	
		},
		
		update:function()
		{
			if(!ig.global.wm && this.thePlayer==null)
			{
				try
				{
					this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
				}
				catch(err)
				{
					this.kill();
				}
			}
			if(this.triggered)
			{
				this.spawnZerglings();
			}
		},
		
		check:function(other)
		{
			if(other && this.thePlayer!=null)
			{
				this.triggered=true;
				this.spawnTimer.unpause();
			}
		},

		spawnZerglings:function()
		{
			if(this.spawnCount < this.numberOfZerglings && this.spawnTimer.delta() > this.spawnRate)
			{
				if(this.type=='zergling')
					ig.game.spawnEntity(EntityZergling, this.pos.x-this.distanceFromPlayer,this.pos.y-this.size.y/2, {seenPlayer:true});
				if(this.type=='zealot')
					ig.game.spawnEntity(EntityZealot, this.pos.x-this.distanceFromPlayer,this.pos.y-this.size.y/2, {seenPlayer:true});
				this.spawnCount++;
				this.spawnTimer.reset();
			}
			else if(this.spawnCount == this.numberOfZerglings)
				this.kill();
		}
	});

});