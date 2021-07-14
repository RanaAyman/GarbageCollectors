# GarbageCollectors
Implementaion of two of the known algorithms used in Garbage Collectors:
* Mark & Compact GC
* Copy GC

## Mark & Compact GC
Mark-Sweep-Compact algorithms solve the shortcomings of Mark and Sweep by moving all marked – and thus alive – objects to the beginning of the memory region. The downside of this approach is an increased GC pause duration as we need to copy all objects to a new place and to update all references to such objects. The benefits to Mark and Sweep are also visible – after such a compacting operation new object allocation is again extremely cheap via pointer bumping. Using such approach the location of the free space is always known and no fragmentation issues are triggered either.

![Mark & Compact GC](Mark.png)

## Copy GC
Mark and Copy algorithms are very similar to the Mark and Compact as they too relocate all live objects. The important difference is that the target of relocation is a different memory region as a new home for survivors. Mark and Copy approach has some advantages as copying can occur simultaneously with marking during the same phase. The disadvantage is the need for one more memory region, which should be large enough to accommodate survived objects.

![CopyGC](Copy.png)
