
__config()->{
    'commands' -> {
        '<entity> <enchantment> <int>' -> 'enchant_item'
    }
};


enchant_item(entity, ench, lvl) -> (
    lvl = if(lvl, lvl, 1);
    data = parse_nbt(query(entity, 'nbt'));
    data:'Item':'tag':'Enchantments' += {'id' -> ench, 'lvl' -> lvl};
    data = encode_nbt(data, true);
    modify(entity, 'nbt_merge', data);
);