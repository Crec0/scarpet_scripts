__config()->{
    'stay_loaded'->true
};


generate_web(player) -> 
{
    block_hit = query(player, 'trace', 50, 'exact');
    if (block_hit,
        look_vec = query(player, 'look');
        modify(player, 'motion', [0, 0, 0]);
        modify(player, 'accelerate', (look_vec*3));
    );
};

PID(setpoint, actual) -> {
    kp = 3;
    ki = 2;
    kd = 1;
    integral = 0;
    error = 0;

    loop(32,
        error = setpont - actual;
        integral = integral + (error * 50);
        derivative = (error - error) / 50;
        error = error;
    );

    output = (kp * error) + (ki * integral) + (kd * derivative);
    return (output);
};


__on_player_breaks_block(player, block)->{
    p = pos(block);
    b = block(block);
    s = block_state(block);
    n = block_data(block);
    if (s && n,
        print('s and n');
        set(p, b, s, n),
        s,
        print('s');
        set(p, b, s),
        print('none');
        set(p, b)
    );
};

__on_tick()-> {
    if (global_dest,
        
    )
};


__on_player_swings_hand(player, hand) -> {
    if(hand ~ 'main',
        generate_web(player);
    );
};
