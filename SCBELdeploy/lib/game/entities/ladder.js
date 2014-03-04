/* ladder.js */
// http://impactjs.com/forums/help/creating-a-ladder-entity-in-impactjs/page/1
ig.module('game.entities.ladder')

.requires('impact.entity')

.defines(function() {

    EntityLadder = ig.Entity.extend({

        size: {
            x: 16,
            y: 16
        },

        // Width of ladders in game.
        real_width: 20,

        _wmDrawBox: true,
        _wmScalable: true,
        zIndex: 256,
        nature: 'ladder',
        gravityFactor: 0,

        init: function(x, y, settings) {
            this.parent(x, y, settings);
        },

        update: function() {},

    });

});