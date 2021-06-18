__config()->{
    'stay_loaded'->true,
    'commands' -> {
        '' ->  '__flip_switch',
    }
};

__flip_switch() -> (
    schedule(0, _() -> run('player switch spawn at -244.50 83.00 175.52 facing 180 27 in minecraft:overworld'));
    schedule(20*5, _() -> run('player switch use'));
    schedule(20*10, _() -> run('player switch kill'));
);