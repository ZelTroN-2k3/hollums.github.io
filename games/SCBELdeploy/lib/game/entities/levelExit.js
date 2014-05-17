ig.module(
	'game.entities.levelExit'
)
.requires(
	'impact.entity'
)

.defines(function()
{
	EntityLevelExit = ig.Entity.extend(
	{
		_wmDrawBox: true,
		_wmBoxColor: 'rgba(0,0,225,0.7)',
		_wmScalable: true,
		size: {x:8,y:8},

		level:null,
		checkAgainst: ig.Entity.TYPE.A,

		dialogue:'null',

		update: function(){},

		check: function(other)
		{
			if(other instanceof EntityPlayer)
			{
				if(this.dialogue=='Dialogue1')
				{
					ig.system.setGame(Dialogue1);
				}
				else if(this.dialogue=='Dialogue2')
				{
					ig.system.setGame(Dialogue2);
				}
				else if(this.dialogue=='Dialogue3')
				{
					ig.system.setGame(Dialogue3);
				}
				/*
				if(this.level)
				{
					var levelName = this.level.replace(/^(Level)?(\w)(\w*)/, function(m,l,a,b)
					{
						return a.toUpperCase() + b;
					});
					ig.game.loadLevelDeferred(ig.global['Level'+levelName]);
				}
				*/
			}
		}
	});
});