# obj-utils
### A lightweight Clojure library for parsing/manipulating wavefront obj files

##### This library is a work in progress!

## Introduction

The core library exposes a single fn, `parse`, which accepts an obj-formatted string and gives you a clojure data structure of all its parts:

```clojure
(require '[obj-utils.core :as obj])

(def obj-str
  "v 0 0 0
   v 1 0 0
   v 1 1 0
   v 0 1 0
   
   # I'm a comment
   
   vt 0 1
   vt 1 1
   
   vn 0 0 1
   
   f 1/2/1 2/1/1 3/2/1
   f 1/2/1 3/2/1 4/1/1")

(obj/parse obj-str)
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

A few things worth mentioning
- The library is written entirely in .cljc files, so can be used in Clojure or ClojureScript
- Face indices are left as 1-indexed, as they are in the obj file

## Planned feature queue
1. Transform clojure data structure to primitive arrays for loading into vbos
2. Add options to save verts as map instead of a vector
3. Add support for groups and other obj field types
