ig.module( 
	'game.main' 
)
.requires(
	'impact.game',
	'impact.font',
	'game.levels.firstLevel',
	'game.levels.secondLevel',
	'game.levels.thirdLevel'
)
.defines(function(){

MyGame1 = ig.Game.extend({

	// add gravity to the game
	gravity: 300,
	currentLevel: 'LevelFirstLevel',

	init: function() {
		ig.music.add('media/sounds/levelOneMusic.*');
		ig.music.volume = 0.7;
		try{
			ig.music.next();
		}catch(err)
		{
			ig.music.play();
		}
		// Initialize your game here; bind keys etc.
		ig.input.bind(ig.KEY.A, 'left');
		ig.input.bind(ig.KEY.D, 'right');
		ig.input.bind(ig.KEY.W, 'jump');
		ig.input.bind(ig.KEY.SPACE, 'shoot');
		ig.input.bind(ig.KEY.TAB,'switch');
		ig.input.bind(ig.KEY.F,'fly');
		ig.input.bind(ig.KEY.C,'kick');
		ig.input.bind(ig.KEY.G, 'action');
		ig.input.bind(ig.KEY.T, 'throw');
		ig.game.loadLevelDeferred(ig.global[this.currentLevel]);
	},
	
	update: function() {
		// Update all entities and backgroundMaps
		// Screen follows the player
		var player = this.getEntitiesByType(EntityPlayer)[0];
		if(player)
		{
			this.screen.x = player.pos.x - ig.system.width/2;
			this.screen.y = player.pos.y - ig.system.height/2;
			player.screen.x = this.screen.x;
			player.screen.y = this.screen.y;
		}
		this.parent();
		
		// Add your own, additional update code here
	},
	
	draw: function() {
		// Draw all entities and backgroundMaps
		this.parent();
		
		
		// Add your own drawing code here
		var x = ig.system.width/2,
			y = ig.system.height/2;
	}
});

MyGame2 = ig.Game.extend({

	// add gravity to the game
	gravity: 300,
	currentLevel: 'LevelSecondLevel',

	init: function() {
		ig.music.add('media/sounds/levelTwoMusic.*');
		ig.music.volume = 0.4;
		try{
			ig.music.next();
		}catch(err)
		{
			ig.music.play();
		}
		// Initialize your game here; bind keys etc.
		ig.input.bind(ig.KEY.A, 'left');
		ig.input.bind(ig.KEY.D, 'right');
		ig.input.bind(ig.KEY.W, 'jump');
		ig.input.bind(ig.KEY.SPACE, 'shoot');
		ig.input.bind(ig.KEY.TAB,'switch');
		ig.input.bind(ig.KEY.F,'fly');
		ig.input.bind(ig.KEY.C,'kick');
		ig.input.bind(ig.KEY.G, 'action');
		ig.input.bind(ig.KEY.T, 'throw');
		ig.game.loadLevelDeferred(ig.global[this.currentLevel]);
	},
	
	update: function() {
		// Update all entities and backgroundMaps
		// Screen follows the player
		var player = this.getEntitiesByType(EntityPlayer)[0];
		if(player)
		{
			this.screen.x = player.pos.x - ig.system.width/2;
			this.screen.y = player.pos.y - ig.system.height/2;
			player.screen.x = this.screen.x;
			player.screen.y = this.screen.y;
		}
		this.parent();
		
		// Add your own, additional update code here
	},
	
	draw: function() {
		// Draw all entities and backgroundMaps
		this.parent();
		
		
		// Add your own drawing code here
		var x = ig.system.width/2,
			y = ig.system.height/2;
	}
});

MyGame3 = ig.Game.extend({

	// add gravity to the game
	gravity: 300,
	currentLevel: 'LevelThirdLevel',

	init: function() {
		ig.music.add('media/sounds/levelThreeMusic.*');
		ig.music.volume = 0.4;
		try{
			ig.music.next();
		}catch(err)
		{
			ig.music.play();
		}
		// Initialize your game here; bind keys etc.
		ig.input.bind(ig.KEY.A, 'left');
		ig.input.bind(ig.KEY.D, 'right');
		ig.input.bind(ig.KEY.W, 'jump');
		ig.input.bind(ig.KEY.SPACE, 'shoot');
		ig.input.bind(ig.KEY.TAB,'switch');
		ig.input.bind(ig.KEY.F,'fly');
		ig.input.bind(ig.KEY.C,'kick');
		ig.input.bind(ig.KEY.G, 'action');
		ig.input.bind(ig.KEY.T, 'throw');
		ig.game.loadLevelDeferred(ig.global[this.currentLevel]);
	},
	
	update: function() {
		// Update all entities and backgroundMaps
		// Screen follows the player
		var player = this.getEntitiesByType(EntityPlayer)[0];
		if(player)
		{
			this.screen.x = player.pos.x - ig.system.width/2;
			this.screen.y = player.pos.y - ig.system.height/2;
			player.screen.x = this.screen.x;
			player.screen.y = this.screen.y;
		}
		this.parent();
		
		// Add your own, additional update code here
	},
	
	draw: function() {
		// Draw all entities and backgroundMaps
		this.parent();
		
		
		// Add your own drawing code here
		var x = ig.system.width/2,
			y = ig.system.height/2;
	}
});

StartScreen = ig.Game.extend(
{
	background: new ig.Image('media/dialogues/title_screen.png'),


	init: function()
	{
		ig.music.add('media/sounds/menu_music.*');
		ig.music.play();
		ig.input.bind(ig.KEY.SPACE,'start');
	},

	update: function()
	{
		if(ig.input.pressed('start'))
		{
			ig.system.setGame(OpeningScreen);
		}
		this.parent();
	},

	draw: function()
	{
		this.parent();
		this.background.draw(0,0);
		var x = ig.system.width/2;
		var y = ig.system.height-10;
	}
});

OpeningScreen = ig.Game.extend(
{
	// background: new ig.Image('media/dialogues/opening_screen1.png'),
	background: new ig.Image('media/dialogues/opening_screen4.png'),
	talkTimer: null,
	readTime: 3,
	init: function()
	{
		ig.input.bind(ig.KEY.SPACE,'start');
		this.talkTimer = new ig.Timer();
	},

	update: function()
	{
		if(ig.input.pressed('start'))
		{
			ig.system.setGame(MyGame1);
		}
		/*
		if(this.talkTimer.delta()>this.readTime*1)
			this.background = new ig.Image('media/dialogues/opening_screen2.png');
		if(this.talkTimer.delta()*1 >= this.readTime*1 && this.talkTimer.delta()>this.readTime*2)
			this.background = new ig.Image('media/dialogues/opening_screen3.png');
		if(this.talkTimer.delta()*2 >= this.readTime*2 && this.talkTimer.delta()>this.readTime*3)
			this.background = new ig.Image('media/dialogues/opening_screen4.png');
			*/
		this.parent();
	},

	draw: function()
	{
		this.parent();
		this.background.draw(0,0);
		var x = ig.system.width/2;
		var y = ig.system.height-10;
	}
});

Dialogue1 = ig.Game.extend(
{
	//background: new ig.Image('media/dialogues/dialogue1_screen1.png'),
	background: new ig.Image('media/dialogues/dialogue1_screen8.png'),
	talkTimer: null,
	readTime: 2.5,
	init: function()
	{
		ig.input.bind(ig.KEY.SPACE,'start');
		this.talkTimer = new ig.Timer();
	},

	update: function()
	{
		if(ig.input.pressed('start'))
		{
			ig.system.setGame(MyGame2);
		}/*
		if(this.talkTimer.delta()>this.readTime*1)
			this.background = new ig.Image('media/dialogues/dialogue1_screen2.png');
		if(this.talkTimer.delta()>this.readTime*2)
			this.background = new ig.Image('media/dialogues/dialogue1_screen3.png');
		if(this.talkTimer.delta()>this.readTime*3)
			this.background = new ig.Image('media/dialogues/dialogue1_screen4.png');
		if(this.talkTimer.delta()>this.readTime*4)
			this.background = new ig.Image('media/dialogues/dialogue1_screen5.png');
		if(this.talkTimer.delta()>this.readTime*5)
			this.background = new ig.Image('media/dialogues/dialogue1_screen6.png');
		if(this.talkTimer.delta()>this.readTime*6)
			this.background = new ig.Image('media/dialogues/dialogue1_screen7.png');
		if(this.talkTimer.delta()>this.readTime*7)
			this.background = new ig.Image('media/dialogues/dialogue1_screen8.png');
			*/
		this.parent();
	},

	draw: function()
	{
		this.parent();
		this.background.draw(0,0);
		var x = ig.system.width/2;
		var y = ig.system.height-10;
	}
});

Dialogue2 = ig.Game.extend(
{
	//background: new ig.Image('media/dialogues/dialogue2_screen1.png'),
	background: new ig.Image('media/dialogues/dialogue2_screen5.png'),
	talkTimer: null,
	readTime: 2.5,
	init: function()
	{
		ig.input.bind(ig.KEY.SPACE,'start');
		this.talkTimer = new ig.Timer();
	},

	update: function()
	{
		if(ig.input.pressed('start'))
		{
			ig.system.setGame(MyGame3);
		}
		/*
		if(this.talkTimer.delta()>this.readTime*1)
			this.background = new ig.Image('media/dialogues/dialogue2_screen2.png');
		if(this.talkTimer.delta()>this.readTime*2)
			this.background = new ig.Image('media/dialogues/dialogue2_screen3.png');
		if(this.talkTimer.delta()>this.readTime*3)
			this.background = new ig.Image('media/dialogues/dialogue2_screen4.png');
		if(this.talkTimer.delta()>this.readTime*4)
			this.background = new ig.Image('media/dialogues/dialogue2_screen5.png');
			*/
		this.parent();
	},

	draw: function()
	{
		this.parent();
		this.background.draw(0,0);
		var x = ig.system.width/2;
		var y = ig.system.height-10;
	}
});

Dialogue3 = ig.Game.extend(
{
	//background: new ig.Image('media/dialogues/dialogue3_screen1.png'),
	background: new ig.Image('media/dialogues/dialogue3_screen6.png'),
	talkTimer: null,
	readTime: 2.5,
	init: function()
	{
		ig.input.bind(ig.KEY.SPACE,'start');
		this.talkTimer = new ig.Timer();
	},

	update: function()
	{
		if(ig.input.pressed('start'))
		{
			ig.system.setGame(StartScreen);
			ig.music.next();
		}
		/*
		if(this.talkTimer.delta()>this.readTime*1)
			this.background = new ig.Image('media/dialogues/dialogue3_screen2.png');
		if(this.talkTimer.delta()>this.readTime*2)
			this.background = new ig.Image('media/dialogues/dialogue3_screen3.png');
		if(this.talkTimer.delta()>this.readTime*3)
			this.background = new ig.Image('media/dialogues/dialogue3_screen4.png');
		if(this.talkTimer.delta()>this.readTime*4)
			this.background = new ig.Image('media/dialogues/dialogue3_screen5.png');
		if(this.talkTimer.delta()>this.readTime*5)
			this.background = new ig.Image('media/dialogues/dialogue3_screen6.png');
			*/
		this.parent();
	},

	draw: function()
	{
		this.parent();
		this.background.draw(0,0);
		var x = ig.system.width/2;
		var y = ig.system.height-10;
	}
});


// Start the Game with 60fps, a resolution of 320x240, scaled
// up by a factor of 2
ig.main( '#canvas', StartScreen, 60, 800, 480, 1 );

});
