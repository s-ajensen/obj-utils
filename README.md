# obj-utils
### A lightweight Clojure library for parsing/manipulating wavefront obj files

##### This library is a work in progress!

## Introduction

The core library exposes two fns, `parse` and `align-idxs`.

`parse` accepts an obj-formatted string and gives you a clojure data structure of all its parts:

```clojure
(require '[obj-utils.core :as obj])

(def obj-str
  "v 0 0 0
   v 1 0 0
   v 1 1 0
   v 0 1 0
   
   vt 0 1
   vt 1 1
   
   vn 0 0 1
   
   f 1/2/1 2/1/1 3/2/1
   f 1/2/1 3/2/1 4/1/1")

(def spec (obj/parse obj-str))
=> {:v [[0.0 0.0 0.0]
        [1.0 0.0 0.0]
        [1.0 1.0 0.0]
        [0.0 1.0 0.0]],
    :vt [[0.0 1.0]
         [1.0 1.0]],
    :vn [[0.0 0.0 1.0]],
    :f [[{:v 1, :vt 2, :vn 1}
         {:v 2, :vt 1, :vn 1}
         {:v 3, :vt 2, :vn 1}]
        [{:v 1, :vt 2, :vn 1}
         {:v 3, :vt 2, :vn 1}
         {:v 4, :vt 1, :vn 1}]]}
```

`align-idxs` then may be used to consume this structure, producing a map storing the vertex, texture, and normal data such that it may be put in a buffer and uploaded to a VBO on the graphics card:
```clojure
(obj/align-idxs spec)
=> {:vertices   [0.0 0.0 0.0 1.0 0.0 0.0 1.0 1.0 0.0 0.0 1.0 0.0],
    :normals    [0.0 0.0 1.0 0.0 0.0 1.0 0.0 0.0 1.0 0.0 0.0 1.0],
    :tex-coords [1.0 1.0 0.0 1.0 1.0 1.0 0.0 1.0],
    :idxs       [0 1 2 0 2 3]}
```

A few things worth mentioning
- The library is written entirely in .cljc files, so can be used in Clojure or ClojureScript
- When parsed, indices are left as 1-indexed, as they are in the obj file. However, once they have been aligned, they are 0-indexed.

## Planned feature queue
1. Add options to save verts as map instead of a vector
2. Add support for groups and other obj field types
