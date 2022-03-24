# Scarpet_Apps

Scripts for scarpet (carpet mod)

### survival/log_testing.sc

i should probably add it,  
For the setup you want something like the image. 
commands from bottom to top,
chain blocks are set to `unconditional` and `always active`  
`setblock ~ ~4 ~ minecraft:dirt`  
`setblock ~ ~4 ~ minecraft:birch_sapling`  
`script run loop(40, place_item('bone_meal', x, y+3, z))`  
```
I will assume you know how to install it. So after installation,

/log_testing start x1 y1 z1 x2 y2 z2 <log type>
x1 y1 z1 is one corner of the where you think tree gonna grow.
x2 y2 z2 is the other corner. 

/log_testing show
this will show the general shape of the tree

/log_testing show <part> 
to show only specific part of the tree like only leaves or only logs,...

/log_testing reset
to reset the everything
( you have to manually destroy the blocks it sets)

/log_testing stop
to stop the script but not reset it. data will be preserved until server restart or reload or reset

/log_testing offset 
to check the current offset of where the overlay tree is drawn

/log_testing offset x y z
to set the offset to desired location
```
![](https://cdn.discordapp.com/attachments/694965918643781634/956428715812864040/unknown.png)

block colors have meaning too.

script will count the total attempts. if the stuff is grown >50% of the attempts, the colors are 
red_concrete for log/stem
blue_stained_glass for leaves/wart_blocks
red_stained_glass for vines
slime_block for whatever else

if its <50%
orange concrete for log/stem
cyan_stained_glass for leaves/wart_blocks
yellow_stained_glass for vines
honey_block for whatever else
