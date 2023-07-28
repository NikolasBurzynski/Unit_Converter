# Unit Converter

This was a project that I started after seeing a Jane Street example technical interview on YouTube.
The problem statement was:

> Given a list of unit conversions, write a program that can take these unit conversions and allow a user to make unit conversion queries. 
> If the unit conversion is not possible with the given information, return an error to the user.


The way it works is by reading an input.txt file which is formatted with unit conversions, one per line.
The format of these conversions is "X BASE RES" such that there are "X" BASE units in 1 RES Unit. 
After reading in the input.txt file, the code crafts a graph where units are nodes and directed edges
are the amount of that unit are in 1 of the incident node. Upon receiving a query, the code checks to make sure
that it has seen both units before, if not it re-queries the user. Then it begins a BFS from the starting unit node,
keeping track of the current unit conversion value of each of the nodes. If it runs into the node of the target unit
the code returns the converted value. If the BFS ends without finding the target unit, such a conversion does not exist
and the code returns an error statement to the user.

This was my first project coding in Kotlin :D