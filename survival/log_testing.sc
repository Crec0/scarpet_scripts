// log_testing.sc
// by Crec0
// total rewrite of Firnagzen's log_testing.sc shared in Tree huggers

// Counts the logs generated during a tree growth
// Useful in testing tree farms to see how often things generate at given position
// Leaves part need to be modified for nether trees because of shroomlight, vines and such. Maybe in v2?

global_LOGS_AND_STEMS = filter(filter(item_list(), _ ~ '\\w+(?:log|stem)$'), !(_ ~ '^stripped'));

global_status = true;
global_show_shapes = true;

global_render_offset = [0, 20, 0];
global_log_accumulator = {};
global_leaves_accumulator = {};

__config() -> {
	'commands' -> {
    	'start <from_pos> <to_pos> <log>' -> 'init_count',
        'stop' -> _() -> (global_status = false),
        'show' -> 'show_shapes',
        'hide' -> _() -> (global_show_shapes = false),
        'reset' -> _() -> (global_log_accumulator = {};global_leaves_accumulator = {};),
        'offset' -> _() -> print(player(), format('d Current Offset: ', str('c x: %d, y: %d, z: %d', ...global_render_offset))),
        'offset <x_int> <y_int> <z_int>' -> _(x, y, z) -> (global_render_offset = [x, y, z]),
    },
    'arguments' -> {
    	'log' -> {
        	'type' -> 'term',
            'options' -> global_LOGS_AND_STEMS,
        }
    }
};

init_count(from, to, log) -> (
    global_status = true;
    global_log_accumulator = {};
    global_leaves_accumulator = {};
    begin_counting(from, to, log);
    show_shapes();
);

begin_counting(from, to, log) -> (
	volume(from, to, 
        if(_ == log,
            global_log_accumulator:[_x + 0.5, _y + 0.5, _z + 0.5] += 1;
            set(_, 'air');
           ,
           _ == replace('dark_oak_log', 'log|stem', 'leaves'),
           	global_leaves_accumulator:[_x + 0.5, _y + 0.5, _z + 0.5] += 1;
            set(_, 'air');
        );
	);

	if (global_status,
    	schedule(1, 'begin_counting', from, to, log)
    );
);

show_shapes() -> (
	shapes = [];
	for(pairs(global_log_accumulator),
    	[key, val] = _; 
	    shapes += [
            'label', 
            100,
            'pos', map(range(3), key:_ + global_render_offset:_),
            'text', 'count', 
            'value', val, 
            'color', 0x00FFFFFF
        ];
    );
   	for(pairs(global_leaves_accumulator),
    	[key, val] = _; 
	    shapes += [
            'label', 
            100,
            'pos', map(range(3), key:_ + global_render_offset:_),
            'text', 'count', 
            'value', val, 
            'color', 0x0000FFFF
        ];
    );
    draw_shape(shapes);
    if (global_show_shapes,
    	schedule(1, 'show_shapes');
    );
);
