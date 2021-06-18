
__config()->{
    'stay_loaded' -> true,
    'scope' -> 'global',    
    'commands' -> {
        'get <player> <category> <tag>' -> '__statistic_fetcher',
        'scoreboard <objective> <updatetime> <category> <tag>' -> '__scoreboard_manager',
        'scoreboard remove <objective>' -> '__scoreboard_remove'
    },
    'arguments' -> {
        'category' -> {'type' -> 'term', 'options' -> ['mined' ,'crafted', 'used', 'broken', 'picked_up', 'dropped', 'killed', 'killed_by']},
        'objective' -> {'type' -> 'term', 'options' -> scoreboard()},
        'updatetime' -> {'type' -> 'int', 'min' -> 1, 'max' -> 20000, 'suggest' -> [1, 10, 20]},
        'tag' -> {'type' -> 'text', 'suggest' -> ['axes', 'shovels', 'pickaxes', 'tools', 'zombie', 'creeper', 'enderman', 'planks', 'stone,cobblestone']}
    }
};

__flatten(listoflists) -> (
    return(reduce(listoflists, map(_, _a+=_);_a;, []))
);

global_item_map = {
    'pickaxes' -> item_list('fabric:pickaxes'),
    'axes' -> item_list('fabric:axes'),
    'hoes' -> item_list('fabric:hoes'),
    'shovels' -> item_list('fabric:shovels'),
    'swords' -> item_list('fabric:swords'),
    'shears' -> item_list('shears'),
};

global_item_map = global_item_map + {'tools' -> __flatten([global_item_map:'pickaxes', global_item_map:'axes', global_item_map:'hoes', global_item_map:'shovels', global_item_map:'shears'])};

global_current_objective = {};

__statistic_fetcher(player, category, tag) -> (
    tag_items = split(',', tag);
    if (length(tag_items) == 1 && global_item_map:tag != null,
        tag_items = global_item_map:tag;
    );
    player_stat = reduce(tag_items, _a + number(statistic(player, category, _)), 0);
    return(player_stat);
);

__update_objective() -> (
    map(player('all'), scoreboard(global_current_objective:'objective', _, __statistic_fetcher(_, global_current_objective:'category', global_current_objective:'criteria')));
    schedule(global_current_objective:'utime', '__update_objective');
);

__scoreboard_manager(objective, updatetime, category, criteria) -> (
    curr_obj = scoreboard() ~ objective;
    if (curr_obj == null || curr_obj != objective, scoreboard_add(objective));
    scoreboard_display('sidebar', objective);
    global_current_objective = {'objective' -> objective, 'category' -> category, 'criteria' -> criteria, 'utime' -> updatetime};
    __update_objective();
);

__scoreboard_remove(objective) -> (
    scoreboard_remove(objective);
);