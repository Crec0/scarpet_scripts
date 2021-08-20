__config() -> (
   m(
      l('stay_loaded','true')
   )
);

__command() ->
(
   p = player();
   current_gamemode = p~'gamemode';
   if ( current_gamemode == 'spectator',
      __remove_camera_effects(p);
      if (config = __get_player_stored_takeoff_params(p~'name'),
         __restore_player_params(p, config);
         __remove_player_config(p~'name');
      );
   , current_gamemode == 'survival', // else if survival - switch to spectator
         __store_player_takeoff_params(p);
         __turn_to_camera_mode(p);
   );
   ''
);

__get_player_stored_takeoff_params(player_name) ->
(
   tag = load_app_data();
   if(!tag, return (null));
   player_tag = tag:player_name;
   if (!player_tag, return(null));
   config = m();
   config:'effects' = l();
   effects_tags = player_tag:'Effects.[]';
   if (effects_tags,
      // fixing vanilla list parser
      if (type(effects_tags)!='list',effects_tags = l(effects_tags));      
      for(effects_tags, etag = _;
         effect = m();
         effect:'name' = etag:'Name';
         effect:'amplifier' = etag:'Amplifier';
         effect:'duration' = etag:'Duration';
         config:'effects' += effect;
      );
   );
   config
);

__store_player_takeoff_params(player) ->
(
   tag = nbt('{}');
   for (player~'effect',
      l(name, amplifier, duration) = _;
      etag = nbt('{}');
      etag:'Name' = name;
      etag:'Amplifier' = amplifier;
      etag:'Duration' = duration;
      put(tag:'Effects', etag, _i);
   );
   apptag = load_app_data();
   if (!apptag, apptag = nbt('{}'));
   apptag:(player~'name') = tag;
   store_app_data(apptag);
   null
);

__restore_player_params(player, config) ->
(
   modify(player, 'gamemode', 'survival');
   for (config:'effects',
      modify(player, 'effect', _:'name', _:'duration', _:'amplifier')
   );
);

__remove_player_config(player_name) ->
(
   tag = load_app_data();
   delete(tag:player_name);
   store_app_data(tag);
);


__remove_camera_effects(player) ->
(
   modify(player, 'effect', 'night_vision', null);
   modify(player, 'effect', 'conduit_power', null);
   modify(player, 'gamemode', 'survival');
);

__turn_to_camera_mode(player) ->
(
   modify(player, 'effect');
   modify(player, 'effect', 'night_vision', 999999, 0, false, false);
   modify(player, 'effect', 'conduit_power', 999999, 0, false, false);
   modify(player, 'gamemode', 'spectator');
);

__on_player_disconnects(player, reason)->(
   
);
