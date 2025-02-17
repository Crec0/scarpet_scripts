// Glotz's request
// Tick warps until a given threshold is reached for total items consumed by the hopper counter.
// by Crec0

// You will see a lot of 'schedule' spam for 'run' command, its due to https://github.com/gnembon/fabric-carpet/issues/2023
// One limitation of this bug is that now because of schedule, we cannot tick unfreeze automatically :/

__config()->{
    'stay_loaded'->true,
    'commands' -> {
        '<color> <threshold>' -> ['__run_until_hit'],
        'reset' -> ['__reset'],
        'stop' -> ['__stop', true]
    },
    'arguments' -> {
        'color' -> {'type'->'string', 'options' -> [
            'white',
            'orange',
            'magenta',
            'light_blue',
            'yellow',
            'lime',
            'pink',
            'gray',
            'light_gray',
            'cyan',
            'purple',
            'blue',
            'brown',
            'green',
            'red',
            'black',
        ]},
        'threshold' -> {'type'->'int', 'min'->1, 'max'->1024, 'suggest' -> [5, 10, 15, 20]}
    }
};

global_ticks = 0;
global_mspt = 0;
global_running = false;

__run_until_hit(color, threshold) -> (
    global_running = !global_running;
    if (
        global_running,
            __reset();
            print(format('eb Started test ', 'e for "' + color + '" counter with threshold of ' + threshold + ' items'));

            schedule(0, _(outer(color)) -> (
                // Stop previous tick warp if one was running
                run('tick sprint stop'); 
                // 365 irl days tick warp. should be fine. idk other way to make it "infinite".
                run('tick sprint 630720000t'); 
                run('counter ' + color + ' reset');
            ));

            __run_and_check(color, threshold);
        , // else
            __stop(true);
    );
);

__run_and_check(color, threshold) -> (
    if (!global_running, return());

    schedule(0, _(outer(color), outer(threshold)) -> (
        current_total = number(run('counter ' + color):1:0 ~ ': (\\d+),');
        global_ticks += 1;
        global_mspt = (global_mspt + system_info('server_last_tick_times'):0) / 2;

        if (current_total < threshold, return());

        __stop(false);
        schedule(0, _() -> run('tick freeze'));

        p = player();

        print(p, format(
            'db Threshold for ' + color + ' counter hit!\n',
            'c Game has been frozen!\n',
            'l Ticks: ' + global_ticks + '\n',
            'l Avg MSPT: ' + str('%.2f', global_mspt)
        ));
    ));

    // schedule for next iteration
    schedule(1, '__run_and_check', color, threshold)
);

__stop(is_abort) -> (
    if (is_abort, print(format('rb Test aborted')));
    global_running = false;
    schedule(0, _() -> run('tick sprint stop'));
);

__reset() -> (
    global_ticks = 0;
    global_mspt = 0;
);