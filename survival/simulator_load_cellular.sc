__config() -> {'scope' -> 'global',
    'command_permission' -> 'all',
    'allow_command_conflicts' -> 'true',
    'commands' -> {
        '' -> _() -> print('Command run.'),
        'world <worldsize> <continents>' -> _(w, c) -> without_updates(global_continents = min(c, (w-1)^2)-1;
            world_generate((w-1), min(c, (w-1)^2))),
        'reset <worldsize>' -> _(w) -> without_updates(volume((0,2,0), ((w-1),5,(w-1)), set(_, 'air')))
    },
    'arguments' -> {
        'worldsize' -> {
            'type' -> 'int',
            'suggest' -> [100]
        },
        'continents' -> {
            'type' -> 'int',
            'suggest' -> [12]
        }
    }
};
world_generate(worldsize, local_continents) -> (volume((0, 2, 0), (worldsize, 5, worldsize), set(_, 'air'));
    global_continent_list = {};
    global_continent_list += [round(rand(worldsize)), round(rand(worldsize))];
    loop(min(10000/sqrt(local_continents), 100),
        continent_adder(local_continents, worldsize);
        if(global_continents <= 0, break())
        );
    for(global_continent_list, set([_:0, 4, _:1],'glass'));
    continent_sculptor();
    volume([0, 2, 0], [worldsize, 2, worldsize], floor_decorator(_x, _z));
    chatprint([worldsize+1, global_continents, length(global_continent_list), length(global_continent_list)/local_continents])
);
continent_adder(local_continents, worldsize) -> (start = length(global_continent_list);
    loop(global_continents,
        seed_angle = rand(pi);
        seed_distance = 1.25 - rand(0.5);
        land = [round(get(global_continent_list, -1):0 +
            asin(seed_angle)*seed_distance*worldsize/sqrt(local_continents)) % worldsize,
            round(get(global_continent_list, -1):1 +
            acos(seed_angle)*seed_distance*worldsize/sqrt(local_continents)) % worldsize];
            global_continent_list += land);
    global_continents += (start - length(global_continent_list))
);
continent_sculptor() -> (
    for(global_continent_list, set([_:0, 4, _:1],'glass'));
    color_creation();
    for(global_continent_list, seed = [floor(rand(length(global_colors)))];
        seed += floor(rand(length(global_color_list:(seed:0))));
        set([_:0, 2, _:1], block(global_colors:(seed:0) + '_concrete'));
        set([_:0, 3, _:1], block(global_color_list:(seed:0):(seed:1) + '_stained_glass'));
        if(length(global_color_list:(seed:0)) == 1,
            delete(global_colors, get(seed, 0)));
        print(global_color_list);
        delete(global_color_list:(seed:0), get(seed, 1));
        print(global_color_list);
));
floor_decorator(x, z) -> (nearest([x, z]);
    blocks = ['red_wool', 'orange_wool', 'yellow_wool', 'lime_wool'];
    //blocks = ['magenta_glazed_terracotta[facing=west]', 'magenta_glazed_terracotta[facing=north]', 'magenta_glazed_terracotta[facing=east]', 'magenta_glazed_terracotta[facing=south]'];
    direction = min(global_nearest_var:0:0 - global_nearest_var:1:0,
        //x1 - x2
        global_nearest_var:0:1 - global_nearest_var:1:1,
        //z1 - z2
        global_nearest_var:1:0 - global_nearest_var:0:0,
        //x2 - x1
        global_nearest_var:1:1 - global_nearest_var:0:1,
        //z2 - z1
        -1);
    set([x, 2, z], block([global_nearest_var:1:0, 2, global_nearest_var:1:1]));
    set([x, 3, z], block([global_nearest_var:1:0, 3, global_nearest_var:1:1]));
    //decorator_arrows(x, z, direction, blocks)
    ''
);
decorator_arrows(x, z, direction, blocks) -> if(direction == global_nearest_var:0:0 - global_nearest_var:1:0,
    set([x, 2, z], block(blocks:0)),
    direction == global_nearest_var:0:1 - global_nearest_var:1:1,
    set([x, 2, z], block(blocks:1)),
    direction == global_nearest_var:1:0 - global_nearest_var:0:0,
    set([x, 2, z], block(blocks:2)),
    direction == global_nearest_var:1:1 - global_nearest_var:0:1,
    set([x, 2, z], block(blocks:3)));
nearest(pos) -> (distance = 32768;
    for(global_continent_list, distance1 = sqrt((pos:0-_:0)^2+(pos:1-_:1)^2);
        if(distance1 <= distance,
            nearest_pos1 = nearest_pos;
            nearest_pos = [_, distance1];
            distance = distance1));
    global_nearest_var = [pos, nearest_pos:0];
    if(abs(nearest_pos:1 - nearest_pos1:1) <= 0.1 && rand(1) < 1,
        global_nearest_var:1 = nearest_pos1:0)
);
color_creation() -> (global_colors = ['white', 'orange', 'magenta', 'light_blue', 'yellow', 'lime', 'pink', 'gray', 'light_gray', 'cyan', 'purple', 'blue', 'brown', 'green', 'red', 'black'];
    global_color_list = map(global_colors, copy(global_colors))
);
chatprint(list) -> run('say ' + join(', ', list))


[[white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, green, red, black]]
[[white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black], [white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, green, red, black]]