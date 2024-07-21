(ns obj-utils.core-spec
  (:require [obj-utils.core :as sut]
            [speclj.core :refer :all]))

(defn ->lines [& lines]
  (apply str (map #(str % "\n") lines)))

(describe "Wavefront .obj utils"

  ; region parse
  (context "parses"

    (it "empty obj file"
      (should= {} (sut/parse "")))

    (it "with a single comment"
      (should= {} (sut/parse "# this is a comment")))

    (it "with many comments"
      (should= {} (sut/parse (->lines "# this is a comment"
                                      "#this is another comment"))))

    (context "vertices"

      (it "one"
        (should= {:v [[1.0 1.0 1.0]]}
                 (sut/parse "v 1.0 1.0 1.0")))

      (it "two"
        (should= {:v [[1.0 1.0 1.0]
                      [2.0 2.0 2.0]]}
                 (sut/parse (->lines "v 1.0 1.0 1.0"
                                     "v 2.0 2.0 2.0"))))

      (it "many"
        (should= {:v [[1.0 1.0 1.0]
                      [2.0 2.0 2.0]
                      [3.0 3.0 3.0]
                      [4.0 4.0 4.0]
                      [5.0 5.0 5.0]]}
                 (sut/parse (->lines "v 1.0 1.0 1.0"
                                     "v 2.0 2.0 2.0"
                                     "v 3.0 3.0 3.0"
                                     "v 4.0 4.0 4.0"
                                     "v 5.0 5.0 5.0"))))
      )

    (context "texture coordinates"

      (context "u v"

        (it "one"
          (should= {:vt [[1.0 1.0]]}
                   (sut/parse "vt 1.0 1.0")))

        (it "two"
          (should= {:vt [[0.0 0.0]
                         [1.0 1.0]]}
                   (sut/parse (->lines "vt 0.0 0.0"
                                       "vt 1.0 1.0"))))

        (it "many"
          (should= {:vt [[0.0 0.0]
                         [1.0 1.0]
                         [2.0 2.0]
                         [3.0 3.0]
                         [4.0 4.0]]}
                   (sut/parse (->lines "vt 0.0 0.0"
                                       "vt 1.0 1.0"
                                       "vt 2.0 2.0"
                                       "vt 3.0 3.0"
                                       "vt 4.0 4.0"))))
        )

      (context "u v w"

        (it "one"
          (should= {:vt [[1.0 1.0 1.0]]}
                   (sut/parse "vt 1.0 1.0 1.0")))

        (it "two"
          (should= {:vt [[0.0 0.0 0.0]
                         [1.0 1.0 1.0]]}
                   (sut/parse (->lines "vt 0.0 0.0 0.0"
                                       "vt 1.0 1.0 1.0"))))

        (it "many"
          (should= {:vt [[0.0 0.0 0.0]
                         [1.0 1.0 1.0]
                         [2.0 2.0 2.0]
                         [3.0 3.0 3.0]
                         [4.0 4.0 4.0]]}
                   (sut/parse (->lines "vt 0.0 0.0 0.0"
                                       "vt 1.0 1.0 1.0"
                                       "vt 2.0 2.0 2.0"
                                       "vt 3.0 3.0 3.0"
                                       "vt 4.0 4.0 4.0"))))
        )
      )

    (context "normals"

      (it "one"
        (should= {:vn [[1.0 1.0 1.0]]}
                 (sut/parse "vn 1.0 1.0 1.0")))

      (it "two"
        (should= {:vn [[1.0 1.0 1.0]
                       [2.0 2.0 2.0]]}
                 (sut/parse (->lines "vn 1.0 1.0 1.0"
                                     "vn 2.0 2.0 2.0"))))

      (it "many"
        (should= {:vn [[1.0 1.0 1.0]
                       [2.0 2.0 2.0]
                       [3.0 3.0 3.0]
                       [4.0 4.0 4.0]
                       [5.0 5.0 5.0]]}
                 (sut/parse (->lines "vn 1.0 1.0 1.0"
                                     "vn 2.0 2.0 2.0"
                                     "vn 3.0 3.0 3.0"
                                     "vn 4.0 4.0 4.0"
                                     "vn 5.0 5.0 5.0"))))
      )

    (context "faces"

      (it "with a single vertex"
        (should= {:v [[0.0 0.0 0.0]]
                  :f [[{:v 1}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "f 1//"))))

      (it "with a multiple vertices"
        (should= {:v [[0.0 0.0 0.0]
                      [1.0 1.0 1.0]
                      [2.0 2.0 2.0]]
                  :f [[{:v 1} {:v 2} {:v 3}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "v 1 1 1"
                                     "v 2 2 2"
                                     "f 1// 2// 3//"))))

      (it "with single vertex/texture pair"
        (should= {:v  [[0.0 0.0 0.0]]
                  :vt [[0.0 0.0]]
                  :f  [[{:v 1 :vt 1}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "vt 0 0"
                                     "f 1/1/"))))

      (it "with multiple vertex/texture pairs"
        (should= {:v  [[0.0 0.0 0.0]
                       [1.0 1.0 1.0]]
                  :vt [[0.0 0.0]]
                  :f  [[{:v 1 :vt 1} {:v 2 :vt 1}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "v 1 1 1"
                                     "vt 0 0"
                                     "f 1/1/ 2/1/"))))

      (it "with single vertex/texture/normal tuple"
        (should= {:v  [[0.0 0.0 0.0]]
                  :vt [[0.0 0.0]]
                  :vn [[1.0 0.0 0.0]]
                  :f  [[{:v 1 :vt 1 :vn 1}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "vt 0 0"
                                     "vn 1 0 0"
                                     "f 1/1/1"))))

      (it "with multiple vertex/texture/normal tuples"
        (should= {:v  [[0.0 0.0 0.0]
                       [1.0 1.0 1.0]]
                  :vt [[0.0 0.0]]
                  :vn [[1.0 0.0 0.0]]
                  :f  [[{:v 1 :vt 1} {:v 2 :vt 1}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "v 1 1 1"
                                     "vt 0 0"
                                     "vn 1 0 0"
                                     "f 1/1/ 2/1/"))))
      )
    )                                                       ;endregion

  ; region ->vecs
  (context "builds vectors for VBOs"

    (it "single face with single vertex"
      (should= {:vertices [0.0 0.0 0.0]
                :idxs     [0]}
               (sut/align-idxs
                 {:v [[0.0 0.0 0.0]]
                  :f [[{:v 1}]]})))

    (it "single face with 3 vertices"
      (should= {:vertices [0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0]
                :idxs     [0 1 2]}
               (sut/align-idxs
                 {:v [[0.0 0.0 0.0]
                      [1.0 0.0 0.0]
                      [0.0 1.0 0.0]]
                  :f [[{:v 1} {:v 2} {:v 3}]]})))

    (it "single face with 3 vertices, out of order"
      (should= {:vertices [0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0]
                :idxs     [2 0 1]}
               (sut/align-idxs
                 {:v [[0.0 0.0 0.0]
                      [1.0 0.0 0.0]
                      [0.0 1.0 0.0]]
                  :f [[{:v 3} {:v 1} {:v 2}]]})))

    (it "two faces with shared vertices"
      (should= {:vertices [0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0]
                :idxs     [0 1 2 0 1 2]}
               (sut/align-idxs
                 {:v [[0.0 0.0 0.0]
                      [1.0 0.0 0.0]
                      [0.0 1.0 0.0]]
                  :f [[{:v 1} {:v 2} {:v 3}]
                      [{:v 1} {:v 2} {:v 3}]]})))

    (it "single vertex/texture coord/face tuple"
      (should= {:vertices   [0.0 0.0 0.0]
                :tex-coords [0.0 0.0]
                :idxs       [0]}
               (sut/align-idxs
                 {:v  [[0.0 0.0 0.0]]
                  :vt [[0.0 0.0]]
                  :f  [[{:v 1 :vt 1}]]})))

    (it "single face with 3 vertices, 1 texture coord"
      (should= {:vertices   [0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0]
                :tex-coords [0.0 0.0 0.0 0.0 0.0 0.0]
                :idxs       [0 1 2]}
               (sut/align-idxs
                 {:v  [[0.0 0.0 0.0]
                       [1.0 0.0 0.0]
                       [0.0 1.0 0.0]]
                  :vt [[0.0 0.0]]
                  :f  [[{:v 1 :vt 1} {:v 2 :vt 1} {:v 3 :vt 1}]]})))

    (it "two faces with 3 identical vertices, 1 texture coord"
      (should= {:vertices   [0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0]
                :tex-coords [0.0 0.0 0.0 0.0 0.0 0.0]
                :idxs       [0 1 2 0 1 2]}
               (sut/align-idxs
                 {:v  [[0.0 0.0 0.0]
                       [1.0 0.0 0.0]
                       [0.0 1.0 0.0]]
                  :vt [[0.0 0.0]]
                  :f  [[{:v 1 :vt 1} {:v 2 :vt 1} {:v 3 :vt 1}]
                       [{:v 1 :vt 1} {:v 2 :vt 1} {:v 3 :vt 1}]]})))

    (it "two faces with 3 different vertices, 1 texture coord"
      (should= {:vertices   [0.0 0.0 0.0 1.0 0.0 0.0
                             0.0 1.0 0.0 0.0 0.0 1.0
                             1.0 1.0 0.0 0.0 1.0 1.0]
                :tex-coords [0.0 0.0 0.0 0.0
                             0.0 0.0 0.0 0.0
                             0.0 0.0 0.0 0.0]
                :idxs       [0 1 2 5 4 3]}
               (sut/align-idxs
                 {:v  [[0.0 0.0 0.0]
                       [1.0 0.0 0.0]
                       [0.0 1.0 0.0]
                       [0.0 0.0 1.0]
                       [1.0 1.0 0.0]
                       [0.0 1.0 1.0]]
                  :vt [[0.0 0.0]]
                  :f  [[{:v 1 :vt 1} {:v 2 :vt 1} {:v 3 :vt 1}]
                       [{:v 6 :vt 1} {:v 5 :vt 1} {:v 4 :vt 1}]]})))

    (it "two faces with 3 different vertices, 2 texture coords"
      (should= {:vertices   [0.0 0.0 0.0 1.0 0.0 0.0
                             0.0 1.0 0.0 0.0 0.0 1.0
                             1.0 1.0 0.0 0.0 1.0 1.0]
                :tex-coords [1.0 0.0 0.0 0.0
                             0.0 0.0 1.0 0.0
                             1.0 0.0 0.0 0.0]
                :idxs       [0 1 2 3 4 5]}
               (sut/align-idxs
                 {:v  [[0.0 0.0 0.0]
                       [1.0 0.0 0.0]
                       [0.0 1.0 0.0]
                       [0.0 0.0 1.0]
                       [1.0 1.0 0.0]
                       [0.0 1.0 1.0]]
                  :vt [[0.0 0.0]
                       [1.0 0.0]]
                  :f  [[{:v 1 :vt 2} {:v 2 :vt 1} {:v 3 :vt 1}]
                       [{:v 4 :vt 2} {:v 5 :vt 2} {:v 6 :vt 1}]]})))

    (it "two faces with 3 different vertices out of order, 2 texture coords"
      (should= {:vertices   [0.0 0.0 0.0 1.0 0.0 0.0
                             0.0 1.0 0.0 0.0 0.0 1.0
                             1.0 1.0 0.0 0.0 1.0 1.0]
                :tex-coords [1.0 0.0 0.0 0.0
                             0.0 0.0 1.0 0.0
                             1.0 0.0 0.0 0.0]
                :idxs       [0 1 2 5 4 3]}
               (sut/align-idxs
                 {:v  [[0.0 0.0 0.0]
                       [1.0 0.0 0.0]
                       [0.0 1.0 0.0]
                       [0.0 0.0 1.0]
                       [1.0 1.0 0.0]
                       [0.0 1.0 1.0]]
                  :vt [[0.0 0.0]
                       [1.0 0.0]]
                  :f  [[{:v 1 :vt 2} {:v 2 :vt 1} {:v 3 :vt 1}]
                       [{:v 6 :vt 1} {:v 5 :vt 2} {:v 4 :vt 2}]]})))

    (it "two faces with 3 different vertices, 2 texture coords, 2 normals"
      (should= {:vertices   [0.0 0.0 0.0 1.0 0.0 0.0
                             0.0 1.0 0.0 0.0 0.0 1.0
                             1.0 1.0 0.0 0.0 1.0 1.0]
                :tex-coords [1.0 0.0 0.0 0.0
                             0.0 0.0 1.0 0.0
                             1.0 0.0 0.0 0.0]
                :normals    [1.0 0.0 0.0 0.0 0.0 1.0
                             0.0 0.0 1.0 1.0 0.0 0.0
                             1.0 0.0 0.0 0.0 0.0 1.0]
                :idxs       [0 1 2 5 4 3]}
               (sut/align-idxs
                 {:v  [[0.0 0.0 0.0]
                       [1.0 0.0 0.0]
                       [0.0 1.0 0.0]
                       [0.0 0.0 1.0]
                       [1.0 1.0 0.0]
                       [0.0 1.0 1.0]]
                  :vt [[0.0 0.0]
                       [1.0 0.0]]
                  :vn [[1.0 0.0 0.0]
                       [0.0 0.0 1.0]]
                  :f  [[{:v 1 :vt 2 :vn 1} {:v 2 :vt 1 :vn 2} {:v 3 :vt 1 :vn 2}]
                       [{:v 6 :vt 1 :vn 2} {:v 5 :vt 2 :vn 1} {:v 4 :vt 2 :vn 1}]]})))
    )                                                       ; endregion
  )