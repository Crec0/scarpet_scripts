__command()->(
    __decay();
);

__config()->{
    'stay_loaded'->true,
    'commands' -> {
        '' -> ['__decay', 7, 10],
        '<radius>' -> ['__decay', 10],
        '<radius> <height>' -> ['__decay']
    },
    'arguments' -> {
        'radius' -> {'type'->'int', 'min'->1, 'max'->10, 'suggest' -> [7, 10]},
        'height' -> {'type'->'int', 'min'->1, 'max'->30, 'suggest' -> [7, 10]}
    }
};

__call_random_tick(blk) -> ( random_tick(blk) );

__decay(radius, height) -> {
    [x,y,z] = pos(player());
    scan(x,y,z, radius, height, radius,
        blk = _;
        if (blk ~ 'leaves', 
            schedule(0, '__call_random_tick', blk);
        )
    ),
};
