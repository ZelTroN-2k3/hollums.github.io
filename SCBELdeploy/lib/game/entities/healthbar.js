ig.module(
	'game.entities.healthbar'
)
.requires(
	'impact.entity'
)
.defines(function()
{
	EntityHealthbar= ig.Entity.extend(
	{
		animSheet: new ig.AnimationSheet('media/healthbar.png',240,240),
		player: null,
		screen: {x:0,y:0},
		maxVel: {x:0,y:0},
		//size: {x:225,y:80},
		//offset: {x:0,y:90},
		thePlayer: null,
		collides: ig.Entity.COLLIDES.PASSIVE,
		gravityFactor: 0,
		maxHealth: 100,
		currentHealth: 100,
		zIndex: 805,
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
			this.addAnim('damage8',1,[8]);
			this.addAnim('damage9',1,[9]);
			this.addAnim('damage10',1,[10]);
			this.addAnim('damage11',1,[11]);
			this.addAnim('damage12',1,[12]);
			this.addAnim('damage13',1,[13]);
			this.addAnim('damage14',1,[14]);
			this.addAnim('damage15',1,[15]);
			this.addAnim('dead',1,[16]);
			if(!ig.global.wm)
			{
	 			this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
	 			ig.game.spawnEntity(EntityFuelbar, this.pos.x, this.pos.y, {});
	 		}
			if(this.thePlayer)
			{
				this.pos.x = this.thePlayer.pos.x - ig.system.width/4;
				this.pos.y = this.thePlayer.pos.y - ig.system.height+ig.system.height/3;
			}
		},
		
		update:function()
		{
			this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
			if(this.thePlayer)
			{
				this.pos.x = this.thePlayer.pos.x - ig.system.width/2;
				this.pos.y = this.thePlayer.pos.y - ig.system.height + ig.system.height/3;
				this.currentHealth = this.thePlayer.health;
				var healthPercentage = (this.currentHealth / this.maxHealth)*100;
				var increments = 100/15;
				
				
				if(this.thePlayer.health==0)
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
				else if(healthPercentage < (100-increments*7) && healthPercentage >= (100-increments*8))
				{
					this.currentAnim = this.anims.damage8;
				}
				else if(healthPercentage < (100-increments*8) && healthPercentage >= (100-increments*9))
				{
					this.currentAnim = this.anims.damage9;
				}
				else if(healthPercentage < (100-increments*9) && healthPercentage >= (100-increments*10))
				{
					this.currentAnim = this.anims.damage10;
				}
				else if(healthPercentage < (100-increments*10) && healthPercentage >= (100-increments*11))
				{
					this.currentAnim = this.anims.damage11;
				}
				else if(healthPercentage < (100-increments*11) && healthPercentage >= (100-increments*12))
				{
					this.currentAnim = this.anims.damage12;
				}
				else if(healthPercentage < (100-increments*12) && healthPercentage >= (100-increments*13))
				{
					this.currentAnim = this.anims.damage13;
				}
				else if(healthPercentage < (100-increments*13) && healthPercentage >= (100-increments*14))
				{
					this.currentAnim = this.anims.damage14;
				}
				else if(healthPercentage < (100-increments*14) && healthPercentage >= (100-increments*15))
				{
					this.currentAnim = this.anims.damage15;
				}
				else
					this.currentAnim = this.anims.dead;
			}
			else
				this.currentAnim = this.anims.dead;
			this.parent();
		}
	});
	
	EntityFuelbar= ig.Entity.extend(
	{
		animSheet: new ig.AnimationSheet('media/fuelbar.png',240,240),
		player: null,
		screen: {x:0,y:0},
		maxVel: {x:0,y:0},
		thePlayer: null,
		collides: ig.Entity.COLLIDES.PASSIVE,
		gravityFactor: 0,
		maxFuel:100,
		currentFuel: 100,
		zIndex: 820,
		init:function(x,y,settings)
		{
			this.parent(x,y,settings);
			this.addAnim('full',1,[0]);
			this.addAnim('fuel1',1,[1]);
			this.addAnim('fuel2',1,[2]);
			this.addAnim('fuel3',1,[3]);
			this.addAnim('fuel4',1,[4]);
			this.addAnim('fuel5',1,[5]);
			this.addAnim('fuel6',1,[6]);
			this.addAnim('fuel7',1,[7]);
			this.addAnim('fuel8',1,[8]);
			this.addAnim('fuel9',1,[9]);
			this.addAnim('fuel10',1,[10]);
			this.addAnim('fuel11',1,[11]);
			this.addAnim('fuel12',1,[12]);
			this.addAnim('fuel13',1,[13]);
			this.addAnim('fuel14',1,[14]);
			this.addAnim('fuel15',1,[15]);
			this.addAnim('fuel16',1,[16]);
			this.addAnim('fuel17',1,[17]);
			this.addAnim('fuel18',1,[18]);
			this.addAnim('fuel19',1,[19]);
			this.addAnim('fuel20',1,[20]);
			this.addAnim('fuel21',1,[21]);
			this.addAnim('dead',1,[23]);
	 		this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
			if(this.thePlayer)
			{
				this.pos.x = this.thePlayer.pos.x - ig.system.width/4;
				this.pos.y = this.thePlayer.pos.y - ig.system.height+ig.system.height/3;
			}
		},
		
		update:function()
		{
			this.thePlayer = ig.game.getEntitiesByType(EntityPlayer)[0];
			if(this.thePlayer)
			{
				this.pos.x = this.thePlayer.pos.x - ig.system.width/2;
				this.pos.y = this.thePlayer.pos.y - ig.system.height + ig.system.height/3;
				this.currentFuel = this.thePlayer.fuel;
				var fuelPercentage = (this.currentFuel / this.maxFuel)*100;
				var increments = 100/21;
				
				
				if(this.thePlayer.health==0)
					this.currentAnim=this.anims.dead;
				else if(fuelPercentage>=100)
				{
					this.currentAnim = this.anims.full;
				}
				else if(fuelPercentage < 100 && fuelPercentage >= (100-increments*1))
				{
					this.currentAnim = this.anims.fuel1;
				}
				else if(fuelPercentage < (100-increments*1) && fuelPercentage >= (100-increments*2))
				{
					this.currentAnim = this.anims.fuel2;
				}
				else if(fuelPercentage < (100-increments*2) && fuelPercentage >= (100-increments*3))
				{
					this.currentAnim = this.anims.fuel3;
				}
				else if(fuelPercentage < (100-increments*3) && fuelPercentage >= (100-increments*4))
				{
					this.currentAnim = this.anims.fuel4;
				}
				else if(fuelPercentage < (100-increments*4) && fuelPercentage >= (100-increments*5))
				{
					this.currentAnim = this.anims.fuel5;
				}
				else if(fuelPercentage < (100-increments*5) && fuelPercentage >= (100-increments*6))
				{
					this.currentAnim = this.anims.fuel6;
				}
				else if(fuelPercentage < (100-increments*6) && fuelPercentage >= (100-increments*7))
				{
					this.currentAnim = this.anims.fuel7;
				}
				else if(fuelPercentage < (100-increments*7) && fuelPercentage >= (100-increments*8))
				{
					this.currentAnim = this.anims.fuel8;
				}
				else if(fuelPercentage < (100-increments*8) && fuelPercentage >= (100-increments*9))
				{
					this.currentAnim = this.anims.fuel9;
				}
				else if(fuelPercentage < (100-increments*9) && fuelPercentage >= (100-increments*10))
				{
					this.currentAnim = this.anims.fuel10;
				}
				else if(fuelPercentage < (100-increments*10) && fuelPercentage >= (100-increments*11))
				{
					this.currentAnim = this.anims.fuel11;
				}
				else if(fuelPercentage < (100-increments*11) && fuelPercentage >= (100-increments*12))
				{
					this.currentAnim = this.anims.fuel12;
				}
				else if(fuelPercentage < (100-increments*12) && fuelPercentage >= (100-increments*13))
				{
					this.currentAnim = this.anims.fuel13;
				}
				else if(fuelPercentage < (100-increments*13) && fuelPercentage >= (100-increments*14))
				{
					this.currentAnim = this.anims.fuel14;
				}
				else if(fuelPercentage < (100-increments*14) && fuelPercentage >= (100-increments*15))
				{
					this.currentAnim = this.anims.fuel15;
				}
				else if(fuelPercentage < (100-increments*15) && fuelPercentage >= (100-increments*16))
				{
					this.currentAnim = this.anims.fuel16;
				}
				else if(fuelPercentage < (100-increments*16) && fuelPercentage >= (100-increments*17))
				{
					this.currentAnim = this.anims.fuel17;
				}
				else if(fuelPercentage < (100-increments*17) && fuelPercentage >= (100-increments*18))
				{
					this.currentAnim = this.anims.fuel18;
				}
				else if(fuelPercentage < (100-increments*18) && fuelPercentage >= (100-increments*19))
				{
					this.currentAnim = this.anims.fuel19;
				}else if(fuelPercentage < (100-increments*19) && fuelPercentage >= (100-increments*20))
				{
					this.currentAnim = this.anims.fuel20;
				}
				else if(fuelPercentage < (100-increments*20) && fuelPercentage >= (100-increments*21))
				{
					this.currentAnim = this.anims.fuel21;
				}
				else
					this.currentAnim = this.anims.dead;
			}
			this.parent();
		}
	});
});