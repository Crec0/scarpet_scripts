get_page_data() -> (
    pages = [];
    for(range(20),
        pages:_ = '{"text":"' + 'a' * 256 + '"}';
    );
    pages;
);

get_book_data() -> (
    book_data = {
        'pages' -> get_page_data(),
        'title' -> 'dupe brrrr',
        'author' -> 'Joe mama'
    };
);

__command() -> (
    p = player();
    slot = p ~ 'selected_slot';

    item_stack = inventory_get(p, slot);
    if (!item_stack, exit());

    [name, count, nbt] = item_stack;
    if (name == 'writable_book',
        inventory_set(p, slot, 1, 'minecraft:written_book', encode_nbt(get_book_data()));
    );
);
