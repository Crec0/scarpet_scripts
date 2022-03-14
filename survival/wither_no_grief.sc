// simple script to stop wither from griefing your world
// author: Crec0#0420 (discord)

__config()->{
    'stay_loaded' -> true,
    'scope' -> 'global'
};

__on_explosion_outcome(pos, power, source, causer, mode, fire, blocks, entities) -> (
    if (causer != 'Wither', exit());

    blocks_data = [];
    for(blocks,
        blocks_data += {
            'pos' -> pos(_),
            'block' -> block(_),
            'state' -> block_state(_),
            'data' -> block_data(_)
        }
    );

    radius = power + 1;

    schedule(0, _(outer(pos), outer(radius), outer(blocks_data)) -> (
        for(entity_area('item', pos, [radius, radius, radius]), 
            modify(_, 'remove');
        );
        without_updates(
            for(blocks_data,
                set(_:'pos', _:'block', _:'state', _:'data');
            )
        )
    ));
);
