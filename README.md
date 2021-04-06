# train

Input:  A directed graph where a node represents a town and an edge
represents a route between two towns.  The weighting of the edge represents
the distance between the two towns.  A given route will never appear more
than once, and for a given route, the starting and ending town will not be
the same town.

Output: For test input 1 through 5, if no such route exists, output 'NO
SUCH ROUTE'.  Otherwise, follow the route as given; do not make any extra
stops!  For example, the first problem means to start at city A, then
travel directly to city B (a distance of 5), then directly to city C (a
distance of 4).

1. The distance of the route A-B-C.
2. The distance of the route A-D.
3. The distance of the route A-D-C.
4. The distance of the route A-E-B-C-D.
5. The distance of the route A-E-D.
6. The number of trips starting at C and ending at C with a maximum of 3
stops.  In the sample data below, there are two such trips: C-D-C (2
stops). and C-E-B-C (3 stops).
7. The number of trips starting at A and ending at C with exactly 4 stops.
In the sample data below, there are three such trips: A to C (via B,C,D); A
to C (via D,C,D); and A to C (via D,E,B).
8. The length of the shortest route (in terms of distance to travel) from A
to C.
9. The length of the shortest route (in terms of distance to travel) from B
to B.
10. The number of different routes from C to C with a distance of less than
30.  In the sample data, the trips are: CDC, CEBC, CEBCDC, CDCEBC, CDEBC,
CEBCEBC, CEBCEBCEBC.

Test Input:

For the test input, the towns are named using the first few letters of the
alphabet from A to D.  A route between two towns (A to B) with a distance
of 5 is represented as AB5.

Graph: AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7

Expected Output:

Output #1: 9<br />
Output #2: 5<br />
Output #3: 13<br />
Output #4: 22<br />
Output #5: NO SUCH ROUTE<br />
Output #6: 2<br />
Output #7: 3<br />
Output #8: 9<br />
Output #9: 9<br />
Output #10: 7



## Usage

To run the demo with default input

    lein run

To run with args

    lein run "Graph: AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3"

To run test

    lein test
TODO:
 1) When both graph and n are big, there will be performance issues. we need a better way to take from iterating list
 2) When doing test, Clojure spec should be used to generate input-path.

## License

Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
