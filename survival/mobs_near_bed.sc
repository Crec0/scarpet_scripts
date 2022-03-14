// glows the mobs who prevent the player from sleeping
// Author : Crec0#0420 (discord)

__config()->{
    'stay_loaded' -> true,
};

__on_player_interacts_with_block(player, hand, block, face, hitvec)->(
    if (block ~ '_bed' && ((12544 <= (day_time() % 24000) <= 23461) || weather() == 'thunder'),
        bed = block;
        if (block_state(bed, 'part') == 'foot',
            neighbour_blocks = neighbours(bed);
            bed = neighbour_blocks:(neighbour_blocks ~ bed);
        );
        for(entity_area('monster', pos(bed), [8, 5, 8]),
            modify(_, 'effect', 'glowing', 200, 1, false, false);
        );  
    );
);