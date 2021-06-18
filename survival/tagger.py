import json
from pprint import pprint
from operator import iconcat
from functools import reduce

with open('block_tags.json', 'r') as block_file, open('item_tags.json', 'r') as item_file:
    block_data = json.load(block_file)
    item_data = json.load(item_file)



def getUniqueTags(data):
    return sorted(set(reduce(iconcat, map(lambda a: list(a.values())[0], data))))


block_tags = getUniqueTags(block_data)
item_tags = getUniqueTags(item_data)

print(block_tags, item_tags, sep='\n')
