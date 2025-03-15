__config() ->
{
   'commands' -> {
      '<from_pos> <to_pos>' -> 'count_blocks',
      '<from_pos> <to_pos> <filter>' -> ['count_blocks_filter', false],
      '<from_pos> <to_pos> <filter> <waypoints_bool>' -> 'count_blocks_filter',
   },
   'arguments' -> {
   		'filter' -> { 'type' -> 'string', 'suggest' -> [
        	'"lava|water"',
            'lava',
            'water',
            'anti-worldeater',
            'anti-quarry'
        ]},
   }
};

count_blocks(from_pos, to_pos) ->
(
    stats = {};
    volume(from_pos, to_pos, stats:str(_) += 1 );
    tally(stats, sum(... values(stats)));
);

count_blocks_filter(from_pos, to_pos, filtr, waypoints) ->
(
	filtr = if(
    	filtr == 'anti-worldeater', '(?:anvil|(?:crying_)?obsidian|enchanting_table|ender_chest|reinforced_deepslate|respawn_anchor|trial_spawner|vault)',
    	filtr == 'anti-quarry', '(?:anvil|banners|barrel|beacon|bee(?:hive|_nest)|(?:blast_)?furnace|brewing_stand|(?:calibrated_)?sculk_sensor|campfire|(?:ender_|trapped_)?chest|chiseled_bookshelf|conduit|crafter|creaking_heart|(?:crying_)?obsidian|daylight_detector|dispenser|dropper|enchanting_table|grindstone|(?:hanging_)?sign|hopper|jukebox|lectern|lodestone|(?:moving_)?piston|nether_portal|reinforced_deepslate|respawn_anchor|sculk_(?:catalyst|shrieker)|smoker|(?:trial_)?spawner|vault)',
        filtr
    );
    stats = {};
    global_total = 0;
	mn = [min(from_pos:0, to_pos:0), min(from_pos:1, to_pos:1), min(from_pos:2, to_pos:2)];
    mx = [max(from_pos:0, to_pos:0), max(from_pos:1, to_pos:1), max(from_pos:2, to_pos:2)];
    
    dx = mx:0 - mn:0;
    dz = mx:2 - mn:2;
    
    MAX_STEP_X = 1024;
    MAX_STEP_Z = 1024;
    
    loop_x = ceil(dx / MAX_STEP_X);
    loop_z = ceil(dz / MAX_STEP_Z);
    
    total_iters = loop_x * loop_z;
    iter = 0;
    
    loop(loop_x,
    	iter_x = _;
    	loop(loop_z,
        	iter_z = _;
            start = [
            	mn:0 + iter_x * MAX_STEP_X,
				mn:1,
				mn:2 + iter_z * MAX_STEP_Z,
            ];
            end = [
            	min(mn:0 + iter_x * MAX_STEP_X + MAX_STEP_X, mx:0),
				mx:1,
				min(mn:2 + iter_z * MAX_STEP_Z + MAX_STEP_Z, mx:2),
            ];
			schedule(iter, _(outer(total_iters), outer(iter), outer(waypoints), outer(filtr), outer(stats), outer(start), outer(end)) -> (
            	print(str('Progress: %5.1f%% (Iteration %d of %d)', (iter + 1) / total_iters * 100, iter + 1, total_iters));
            	volume(start, end,
                    global_total += 1; 
                    if (_~filtr, 
                        stats:str(_) += 1;
                        [x,y,z]=pos(_);
                        if (waypoints,
                    	    print(str('xaero-waypoint:%s:X:%d:%d:%d:13:true:0:Internal-%s-waypoints:scarpet-destination', block(_), x, y, z, player()~'dimension'));
                        );    
                    )
                );
            ));
 			iter += 1;
        )
    );
    
    schedule(iter, 
    	_(outer(stats)) -> tally(stats, global_total)
    );
);

tally(stats, grand_total) ->
(
   total = sum(... values(stats));
   if (total == grand_total,
      print(format('wb    Count  |%Area | Block'));
      print('.---------+------+---------------');
      for ( sort_key(keys(stats), stats:_ ),
         print(str('%s | %05.2f%% | %s', dpad(stats:_, 9), 100*stats:_/total, _))
      );
      print('Total: '+total);
   ,
      print(format('wb    Count  |%Pool |%Area | Block'));
      print('.---------+------+------+---------------');
      for ( sort_key(keys(stats), stats:_ ),
         print(str('%s | %05.2f%% | %05.2f%% | %s', dpad(stats:_, 9), 100*stats:_/total, 100*stats:_/grand_total, _))
      );
      print(format('gi Total tally: '+total));
      print(format('gi Scanned area: '+grand_total));
   );
);

dpad(num, wid) ->
(
   strn = str(num);
   if (length(strn) < wid, strn = '...'*(wid-length(strn))+strn);
   strn;
)