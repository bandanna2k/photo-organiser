# TODO

Cache hashes of album
Option to re-cache album caches

# PHOTO ORGANISER

1. Duplicates
   2. Clean-up with `find ? -empty delete`
2. Organise
3. File Sizes

## Duplicates

Input:
`find_duplicate_images <source> <dest> <archive_directory>`

Output:
```
mv /tmp/Pit/tmp/IMG0001.jpg /tmp/Album/2024/September/

mkdir /tmp/Archive/tmpr
mv /tmp/Pit/tmp/IMG0001_Copy.jpg /tmp/Archive/tmp/
```

## Organise

Input:
`organise <source> <dest>`

Output:
List of instructions
E.g.
`
mv /tmp/Pit/tmp/IMG0002.jpg /tmp/Album/2024/September/
`



FOLDER STRUCTURE
- Photos (Final folder)
- Duplicates (Where duplicates go)
- Awaiting (Where images to be sorted go)


WORKFLOW
- Collect common hashes
- Check if image needs optimising (is % saved better than 50%)
  - Decide on optimisation level
- Decide on image to keep (other to move to duplicates)
  - Selection, re-input into image selector
- Operations
  - Show
  - Test
  - Apply