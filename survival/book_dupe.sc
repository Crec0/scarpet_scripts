__config() -> {
    'commands' -> {
        '' -> _() -> _generate_book_if_possible(20),
        '<numOfPages>' -> '_generate_book_if_possible'
    },
    'arguments' -> {
        'numOfPages' -> {
            'type' -> 'int',
            'min' -> 1,
            'max' -> 100,
            'suggest' -> [1, 10, 20, 40]
        }
    }
};

_get_page_data(num) -> (
    pages = [];
    pages:0 = 'ࠀ' * 21845;
    for(range(1, num),
        pages:_ = '{"text":"' + 'a' * 256 + '"}';
    );
    pages;
);

_get_book_data(num) -> (
    book_data = {
        'pages' -> _get_page_data(num),
        'title' -> 'dupe brrrr',
        'author' -> 'Joe mama'
    };
);

_generate_book_if_possible(num) -> (
    p = player();
    slot = p ~ 'selected_slot';

    item_stack = inventory_get(p, slot);
    if (!item_stack, exit());

    [name, count, nbt] = item_stack;
    if (name == 'writable_book',
        inventory_set(p, slot, 1, 'minecraft:written_book', encode_nbt(_get_book_data(num)));
    );
);

