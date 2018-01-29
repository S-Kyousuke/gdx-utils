package com.github.skyousuke.gdxutils;

// Taken from BayazitDecomposer.cs (FarseerPhysics.Common.Decomposition.BayazitDecomposer)
// at http://farseerphysics.codeplex.com

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class BayazitDecomposer {

    public static Array<Polygon> convexPartition(Polygon polygon) {
        Array<Polygon> newPolygons = new Array<Polygon>(false, 16);
        Array<Array<Vector2>> newPolygonsVertices = convexPartition(polygonToVertices(polygon));
        for (int i = 0; i < newPolygonsVertices.size; i++) {
            newPolygons.addAll(verticesToPolygon(newPolygonsVertices.get(i)));
        }
        return newPolygons;
    }

    private static Array<Vector2> polygonToVertices(Polygon polygon) {
        float[] vertices = polygon.getTransformedVertices();
        Array<Vector2> polygonVertices = new Array<Vector2>(vertices.length / 2);
        for (int j = 0; j < vertices.length; j += 2) {
            Vector2 point = Pools.obtain(Vector2.class);
            point.set(vertices[j], vertices[j + 1]);
            polygonVertices.add(point);
        }
        return polygonVertices;
    }

    private static Polygon verticesToPolygon(Array<Vector2> vertices) {
        float[] newVertices = new float[vertices.size * 2];
        for (int i = 0; i < newVertices.length; i += 2) {
            newVertices[i] = vertices.get(i / 2).x;
            newVertices[i + 1] = vertices.get(i / 2).y;
        }
        return new Polygon(newVertices);
    }

    //TODO: hard to maintain. need to refactor someday :)
    public static Array<Array<Vector2>> convexPartition(Array<Vector2> vertices) {

        if (!isCounterClockWise(vertices)) {
            vertices.reverse();
        }

        Array<Array<Vector2>> list = new Array<Array<Vector2>>();
        float d;
        float lowerDist;
        float upperDist;
        Vector2 p;
        Vector2 lowerInt = new Vector2();
        Vector2 upperInt = new Vector2();
        int lowerIndex = 0;
        int upperIndex = 0;

        Array<Vector2> lowerPoly;
        Array<Vector2> upperPoly;

        for (int i = 0; i < vertices.size; ++i) {
            if (reflex(i, vertices)) {
                lowerDist = upperDist = Float.MAX_VALUE;
                for (int j = 0; j < vertices.size; ++j) {
                    // if line intersects with an edge
                    if (left(at(i - 1, vertices), at(i, vertices),
                            at(j, vertices))
                            && rightOn(at(i - 1, vertices), at(i, vertices),
                            at(j - 1, vertices))) {
                        // find the point of intersection
                        p = findIntersectPoint(at(i - 1, vertices), at(i, vertices),
                                at(j, vertices), at(j - 1, vertices));
                        if (right(at(i + 1, vertices), at(i, vertices), p)) {
                            // make sure it's inside the poly
                            d = at(i, vertices).dst2(p);
                            if (d < lowerDist) {
                                // keep only the closest intersection
                                lowerDist = d;
                                lowerInt = p;
                                lowerIndex = j;
                            }
                        }
                    }
                    if (left(at(i + 1, vertices), at(i, vertices),
                            at(j + 1, vertices))
                            && rightOn(at(i + 1, vertices), at(i, vertices),
                            at(j, vertices))) {
                        p = findIntersectPoint(at(i + 1, vertices), at(i, vertices),
                                at(j, vertices), at(j + 1, vertices));
                        if (left(at(i - 1, vertices), at(i, vertices), p)) {
                            d = at(i, vertices).dst2(p);
                            if (d < upperDist) {
                                upperDist = d;
                                upperIndex = j;
                                upperInt = p;
                            }
                        }
                    }
                }
                // if there are no vertices to connect to, choose a point in the
                // middle
                if (lowerIndex == (upperIndex + 1) % vertices.size) {
                    Vector2 sp = new Vector2((lowerInt.x + upperInt.x) / 2,
                            (lowerInt.y + upperInt.y) / 2);
                    lowerPoly = copy(i, upperIndex, vertices);
                    lowerPoly.add(sp);
                    upperPoly = copy(lowerIndex, i, vertices);
                    upperPoly.add(sp);
                } else {
                    double highestScore = 0;
                    double bestIndex = lowerIndex;
                    while (upperIndex < lowerIndex)
                        upperIndex += vertices.size;
                    for (int j = lowerIndex; j <= upperIndex; ++j) {
                        if (canSee(i, j, vertices)) {
                            double score = 1 / (at(i, vertices).dst2(at(j, vertices)) + 1);
                            if (reflex(j, vertices)) {
                                if (rightOn(at(j - 1, vertices),
                                        at(j, vertices), at(i, vertices))
                                        && leftOn(at(j + 1, vertices),
                                        at(j, vertices),
                                        at(i, vertices))) {
                                    score += 3;
                                } else {
                                    score += 2;
                                }
                            } else {
                                score += 1;
                            }
                            if (score > highestScore) {
                                bestIndex = j;
                                highestScore = score;
                            }
                        }
                    }
                    lowerPoly = copy(i, (int) bestIndex, vertices);
                    upperPoly = copy((int) bestIndex, i, vertices);
                }
                list.addAll(convexPartition(lowerPoly));
                list.addAll(convexPartition(upperPoly));
                return list;
            }
        }
        // polygon is already convex
        list.add(vertices);
        // The polygons are not guaranteed to be with collinear points. We
        // remove
        // them to be sure.
        for (int i = 0; i < list.size; i++) {
            list.set(i, collinearSimplify(list.get(i)));
        }
        // Remove empty vertice collections
        for (int i = list.size - 1; i >= 0; i--) {
            if (list.get(i).size == 0)
                list.removeIndex(i);
        }
        return list;
    }

    private static Vector2 at(int i, Array<Vector2> vertices) {
        int s = vertices.size;
        return vertices.get(i < 0 ? s - (-i % s) : i % s);
    }

    private static Array<Vector2> copy(int i, int j, Array<Vector2> vertices) {
        Array<Vector2> p = new Array<Vector2>();
        while (j < i)
            j += vertices.size;

        for (; i <= j; ++i) {
            p.add(at(i, vertices));
        }
        return p;
    }

    private static float getSignedArea(Array<Vector2> vectors) {
        int i;
        float area = 0;
        for (i = 0; i < vectors.size; i++) {
            int j = (i + 1) % vectors.size;
            area += vectors.get(i).x * vectors.get(j).y;
            area -= vectors.get(i).y * vectors.get(j).x;
        }
        area /= 2.0f;
        return area;
    }

    private static boolean isCounterClockWise(Array<Vector2> vectors) {
        return vectors.size < 3 || (getSignedArea(vectors) > 0.0f);
    }

    private static boolean canSee(int i, int j, Array<Vector2> vertices) {
        if (reflex(i, vertices)) {
            if (leftOn(at(i, vertices), at(i - 1, vertices), at(j, vertices))
                    && rightOn(at(i, vertices), at(i + 1, vertices),
                    at(j, vertices)))
                return false;
        } else {
            if (rightOn(at(i, vertices), at(i + 1, vertices), at(j, vertices))
                    || leftOn(at(i, vertices), at(i - 1, vertices),
                    at(j, vertices)))
                return false;
        }
        if (reflex(j, vertices)) {
            if (leftOn(at(j, vertices), at(j - 1, vertices), at(i, vertices))
                    && rightOn(at(j, vertices), at(j + 1, vertices),
                    at(i, vertices)))
                return false;
        } else {
            if (rightOn(at(j, vertices), at(j + 1, vertices), at(i, vertices))
                    || leftOn(at(j, vertices), at(j - 1, vertices),
                    at(i, vertices)))
                return false;
        }
        for (int k = 0; k < vertices.size; ++k) {
            if ((k + 1) % vertices.size == i || k == i
                    || (k + 1) % vertices.size == j || k == j) {
                continue; // ignore incident edges
            }
            if (intersectLines(at(i, vertices), at(j, vertices), at(k, vertices), at(k + 1, vertices))) {
                return false;
            }
        }
        return true;
    }

    private static Vector2 findIntersectPoint(Vector2 point1, Vector2 point2,
                                              Vector2 point3, Vector2 point4) {

        final Vector2 intersectPoint = Pools.obtain(Vector2.class);
        Intersector.intersectLines(point1, point2, point3, point4, intersectPoint);
        return intersectPoint;
    }

    private static boolean intersectLines(Vector2 point1, Vector2 point2,
                                          Vector2 point3, Vector2 point4) {

        Vector2 intersectPoint = Pools.obtain(Vector2.class);
        final boolean intersect = Intersector.intersectLines(point1, point2, point3, point4, intersectPoint);
        Pools.free(intersectPoint);
        return intersect;
    }

    // precondition: ccw
    private static boolean reflex(int i, Array<Vector2> vertices) {
        return right(i, vertices);
    }

    private static boolean right(int i, Array<Vector2> vertices) {
        return right(at(i - 1, vertices), at(i, vertices), at(i + 1, vertices));
    }

    private static boolean left(Vector2 a, Vector2 b, Vector2 c) {
        return area(a, b, c) > 0;
    }

    private static boolean leftOn(Vector2 a, Vector2 b, Vector2 c) {
        return area(a, b, c) >= 0;
    }

    private static boolean right(Vector2 a, Vector2 b, Vector2 c) {
        return area(a, b, c) < 0;
    }

    private static boolean rightOn(Vector2 a, Vector2 b, Vector2 c) {
        return area(a, b, c) <= 0;
    }

    private static float area(Vector2 a, Vector2 b, Vector2 c) {
        return a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y);
    }

    private static Array<Vector2> collinearSimplify(Array<Vector2> vertices) {
        if (vertices.size < 3)
            return vertices;

        Array<Vector2> simplified = new Array<Vector2>();
        for (int i = 0; i < vertices.size; i++) {
            int prevId = i - 1;
            if (prevId < 0)
                prevId = vertices.size - 1;
            int nextId = i + 1;
            if (nextId >= vertices.size)
                nextId = 0;
            Vector2 prev = vertices.get(prevId);
            Vector2 current = vertices.get(i);
            Vector2 next = vertices.get(nextId);

            if (BayazitDecomposer.area(prev, current, next) == 0)
                continue;

            simplified.add(current);
        }
        return simplified;
    }
}