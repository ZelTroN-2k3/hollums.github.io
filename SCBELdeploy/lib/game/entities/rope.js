/* ladder.js */
// http://impactjs.com/forums/help/creating-a-ladder-entity-in-impactjs/page/1
ig.module('game.entities.rope')

.requires('impact.entity')

.defines(function() {

    EntityRope = ig.Entity.extend({
    	
    	links: null,  	
    	numberOfLinks: 25,
    	name: 'rope',
        size:{x:100,y:100},
    	swingRadius: 20,
		theta: 5*Math.PI/4,
		interval: .035,
    	direction:1,
		length:0,
    	swingTimer:null,
        swingActivated: false,
    	
    	init: function(x,y,settings)
    	{
    		this.parent(x,y,settings);
    		if(!ig.global.wm)
    		{
    			for(var i=0;i<this.numberOfLinks;i++)
    			{
    				ig.game.spawnEntity(EntityRopeLink,x,y,settings);
    			}
    			this.links = ig.game.getEntitiesByType(EntityRopeLink);
       			for(var i=0;i<this.links.length;i++)
    			{
    				this.links[i].pos.y=this.pos.y;
    				this.links[i].pos.x=this.pos.x;
    				this.links[i].pos.y+=i*this.links[i].size.y/2;
                    this.links[i].pos.y=this.pos.y-(i*16)*Math.sin(this.theta);
                    this.links[i].pos.x=this.pos.x+(i*16)*Math.cos(this.theta);
    				
    				this.links[i].setRope(this);
    			}
    		}
    		//this.length = this.links.length * 16;
    		this.startTimer();
    	},
    	
    	update: function(){
            if(this.swingActivated)
            {
        		if(this.swingTimer.delta()>=.0000000001)
        		{
        			this.swingTimer.reset();
        			this.theta+=this.interval*this.direction;
        			if(this.theta > Math.PI*2 || this.theta < Math.PI)
        			{
        				this.direction=this.direction*-1;
        			}
        			this.swing();
        		}
            }
    	},
    	
    	
    	startTimer: function()
    	{
    		this.swingTimer = new ig.Timer();
    	},
    	
    	draw: function()
		{
			
    		this.parent();
    	},
    	
    	moveRight: function()
    	{
    		for(var i=0;i<this.links.length;i++)
    		{
    			//this.links[i].pos.x+=10;
    		}
    	},
    	
    	swing: function()
    	{
    		var distance = 0;
			for(var i=1;i<this.links.length;i++)
			{
				var startX = this.links[i].pos.x;
				var startY = this.links[i].pos.y-this.links[i].size.y;
				
			 	this.links[i].pos.y=this.pos.y-(i*16)*Math.sin(this.theta);
			 	this.links[i].pos.x=this.pos.x+(i*16)*Math.cos(this.theta);
			}
            if(this.theta>=3*Math.PI/2)
            {
                this.swingActivated=false;
                this.kill();
            }
    	},
    	
    	getX: function()
    	{
    		var desiredLink = this.numberOfLinks-1;
    		return this.links[desiredLink].pos.x;
    	},
    	
    	getY: function()
    	{
    		var desiredLink = this.numberOfLinks-1;
    		return this.links[desiredLink].pos.y-this.links[desiredLink].size.y;
    	},

        kill:function()
        {
            this.parent();
        }

    });
    
    EntityRopeLink = ig.Entity.extend({
		checkAgainst: ig.Entity.TYPE.BOTH,
    	size: {x: 16,y: 16},
    	animSheet: new ig.AnimationSheet('media/rope.png', 16, 16),
    	rope: null,
    	gravityFactor:0,
    	
    	init: function(x, y, settings) 
    	{
            this.parent(x, y, settings);
            this.addAnim('idle', 1, [0]);
    	},
    	
    	update: function(){},
    	
    	setRope: function(thisRope)
    	{
    		this.rope = thisRope;
    	},
    	
    	getRope: function()
    	{
    		return this.rope;
    	}
    });
});