const Player = require('../../lib/jasmine_examples/Player');
const Song = require('../../lib/jasmine_examples/Song');

describe('Song', function () {
    let player;
    let song;

    beforeEach(function () {
        player = new Player();
        song = new Song();
    });

    it('should be able to play by player', function () {
        player.play(song);
        expect(player.isPlaying).toBeTruthy();
    });
});
