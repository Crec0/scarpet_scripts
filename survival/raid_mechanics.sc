// raid center, connected workstations, detection sphere, bad omen sphere
// illager type
// 3x3x3 subchunk to start
// 5x5x5 subchunk to shift
// 64 sphere to search
// 96 sphere to join
// 112 sphere to fail


//  public PoiManager getPoiManager() {
//       return this.chunkMap.getPoiManager();
//    }

//    public ServerChunkCache getChunkSource() {
//       return this.chunkSource;
//    }

//    public PoiManager getPoiManager() {
//       return this.getChunkSource().getPoiManager();
//    }

//    public boolean isVillage(BlockPos blockPos) {
//       return this.isCloseToVillage(blockPos, 1);
//    }

//    public boolean isVillage(SectionPos sectionPos) {
//       return this.isVillage(sectionPos.center());
//    }

//    public boolean isCloseToVillage(BlockPos blockPos, int i) {
//       if (i > 6) {
//          return false;
//       } else {
//          return this.sectionsToVillage(SectionPos.of(blockPos)) <= i;
//       }
//    }

//    public int sectionsToVillage(SectionPos sectionPos) {
//       return this.getPoiManager().sectionsToVillage(sectionPos);
//    }


//    public int sectionsToVillage(SectionPos sectionPos) {
//       this.distanceTracker.runAllUpdates();
//       return this.distanceTracker.getLevel(sectionPos.asLong());
//    }

__command()-> __get_all_poi();

__config()->{
    'stay_loaded' -> true,
};

global_poi = l(
    '_bed',
    'grindstone',
    'blast_furnace',
    'smoker',
    'cartography_table',
    'brewing_stand',
    'composter',
    'barrel',
    'fletching_table',
    'cauldron',
    'water_cauldron',
    'lectern',
    'stonecutter',
    'loom',
    'smithing_table',
    'grindstone'
);

global_init_range = 10;

__normalize(v) -> (
    len = sqrt(v:0*v:0 + v:1*v:1 + v:2*v:2);
    v:0 = v:0 / len; 
    v:1 = v:1 / len; 
    v:2 = v:2 / len;
);

__dot(x, y) -> (
    d = x:0*y:0 + x:1*y:1 + x:2*y:2;
    return (max(0, d));
);

__get_all_poi() -> (
    ps = pos(player());
    poi_64 = map(filter(poi(ps, 64), ! has(m('beehive', 'bee_nest', 'nether_portal'), _:0)), _:2);
    for (poi_64,
        print(_);
        [x, y, z] = _:2;
        draw_shape(
            'box', 20*30,
            'color', 0x00000000,
            'fill', 0xf890a49f, 
            'from', [x, y, z],
            'to', l(x + 1, y + 1, z + 1),
        );
    );
);



   