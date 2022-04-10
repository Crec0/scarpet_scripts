// log_testing.sc
// by Crec0
// total rewrite of Firnagzen's log_testing.sc shared in Tree huggers

// Counts the logs generated during a tree growth
// Useful in testing tree farms to see how often things generate at given position

global_LOGS_AND_STEMS = filter(filter(item_list(), _ ~ '\\w+(?:log|stem)$'), !(_ ~ '^stripped'));

global_argument_dict = {
	'log' -> 'log|stem',
    'leaves' -> 'leaves|wart_block|mushroom_block',
    'vines' -> 'vines',
    'shroom' -> 'shroomlight',
    'roots' -> 'mangrove_roots',
    'benis' -> 'bee_nest'
};

global_status = true;
global_show_shapes = false;

global_render_offset = [0, 40, 0];
global_accumulator = {};
global_counter = {};

global_total = 0;

__config() -> {
	'commands' -> {
    	'start <from_pos> <to_pos> <log>' -> 'init_count',
        'stop' -> _() -> (print(player(), format('g Log yeeting stopped.')); global_status = false),
        'show' -> _() -> (
        		print(player(), format('g Showing the shape for max blocks'));
                global_show_shapes = false;
                schedule(20, _() -> (
                	global_show_shapes = true;
                	show_shape(null);
                ))),
        'show <produce>' -> _(produce) -> (
        		re = global_argument_dict:produce;
                print(player(), format('g Showing the shape for ' + re));
                global_show_shapes = false;
                schedule(20, _(r) -> (
                	global_show_shapes = true;
                	show_shape(r);
                ), re);
            ),
        'reset' -> 'reset',
        'offset' -> _() -> print(player(), format('d Current Offset: ', str('c x: %d, y: %d, z: %d', ...global_render_offset))),
        'offset <x_int> <y_int> <z_int>' -> _(x, y, z) -> (global_render_offset = [x, y, z]),
    },
    'arguments' -> {
    	'log' -> {
        	'type' -> 'term',
            'options' -> global_LOGS_AND_STEMS,
        },
        'produce' -> {
        	'type' -> 'term',
            'options' -> keys(global_argument_dict),
        }
    }
};

reset() -> (
	global_accumulator = {};
    global_counter = {};
    global_show_shapes = false;
    global_status = true;
    global_total = 0;
);

init_count(from, to, log) -> (
	print(player(), format('g Starting log yeeting from ' + from + ' to ' + to + ' for ' + log));
    reset();
    begin_counting(from, to, log);
);

get_produced_blocks(log) -> (
	if (
    	log == 'crimson_stem',
	    	{'nether_wart_block', 'shroomlight', 'weeping_vines', 'weeping_vines_plant', log},
        log == 'warped_stem',
        	{'warped_wart_block', 'shroomlight', log},
        log == 'mushroom_stem',
        	{'red_mushroom_block', 'brown_mushroom_block', log},
        log == 'mangrove_log',
            {'vine', 'mangrove_roots', 'bee_nest', 'mangrove_leaves', 'mangrove_propagule', log},
        {replace(log, 'log|stem', 'leaves'), log}
    );
);

get_block(block) -> (
	if (
    	block ~ 'log|stem',
        	if (global_accumulator:block > global_total / 4, 'red_concrete', 'orange_concrete'),
        block ~ 'leaves|wart_block|mushroom_block',
        	if (global_accumulator:block > global_total / 4, 'blue_stained_glass', 'cyan_stained_glass'),
        block ~ 'vines|propagule',
        	if (global_accumulator:block > global_total / 4, 'red_stained_glass', 'yellow_stained_glass'),
        block ~ 'air|bee_nest',
        	block,
        if (global_accumulator:block > global_total / 4, 'slime_block', 'honey_block'),
    )
);

get_max_block(map) -> (
	max_b = null;
    max_v = -1;
    for(pairs(map),
    	[k,v] = _;
        if (
			v > max_v,
            	max_b = k;
            	max_v = v;
        );
    );
    max_b;
);

show_shape(block) -> (
	for(pairs(global_counter),
    	[k,v] = _;
        show_block(k, if(block == null, get_max_block(v), block));
    );
    if (global_show_shapes,
    	schedule(10, 'show_shape', block)
    );
);

show_block(p, block) -> (
	b = if(
    		join('', global_counter:p) ~ block == null,
        		'air',
			block
        );
	set(p + global_render_offset, get_block(b));
);

begin_counting(from, to, log) -> (
	produce = get_produced_blocks(log);
    volume(from, to,
        global_total += 1;
    	s = str(_);
        if(produce ~ s,
        	global_accumulator:s += 1;
            set(_, 'air');
            p = [_x, _y, _z];
            if (global_counter:p == null,
            	global_counter:p = {};
            );
            global_counter:p:s += 1;
        );
	);

	if (global_status,
    	schedule(1, 'begin_counting', from, to, log)
    );
);
