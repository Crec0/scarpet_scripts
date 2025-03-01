__config()->{
    'stay_loaded'->true,
    'commands' -> {
        '<switch>' ->  '__flip_switch',
    },
    'arguments' -> {
        'switch' -> {
            'type' -> 'string',
            'options' -> [ 'overworld', 'nether', 'both' ]
        }
    }
};

__flip_switch(kind) -> (
    spawn_z = if (kind == 'overworld', -8.5, -7.5);
    schedule(20 *  0.0, _(outer(spawn_z)) -> run('player Switch spawn at -2736.50 128 ' + spawn_z + ' facing 90 20 in minecraft:the_nether in survival'));
    schedule(20 *  2.0, _() -> run('player Switch use'));
    if (kind == 'both',
        schedule(20 *  3.0, _() -> run('player Switch move right'));
        schedule(20 *  3.2, _() -> run('player Switch stop'));
        schedule(20 *  4.0, _() -> run('player Switch use'));
    );
    schedule(20 * 30.0, _() -> run('player Switch kill'));
);
