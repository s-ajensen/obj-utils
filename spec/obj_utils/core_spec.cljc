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
        (should= {:v [[0.0 0.0 0.0]]
                  :vt [[0.0 0.0]]
                  :f [[{:v 1 :vt 1}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "vt 0 0"
                                     "f 1/1/"))))

      (it "with multiple vertex/texture pairs"
        (should= {:v [[0.0 0.0 0.0]
                      [1.0 1.0 1.0]]
                  :vt [[0.0 0.0]]
                  :f [[{:v 1 :vt 1} {:v 2 :vt 1}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "v 1 1 1"
                                     "vt 0 0"
                                     "f 1/1/ 2/1/"))))

      (it "with single vertex/texture/normal tuple"
        (should= {:v [[0.0 0.0 0.0]]
                  :vt [[0.0 0.0]]
                  :vn [[1.0 0.0 0.0]]
                  :f [[{:v 1 :vt 1 :vn 1}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "vt 0 0"
                                     "vn 1 0 0"
                                     "f 1/1/1"))))

      (it "with multiple vertex/texture/normal tuples"
        (should= {:v [[0.0 0.0 0.0]
                      [1.0 1.0 1.0]]
                  :vt [[0.0 0.0]]
                  :vn [[1.0 0.0 0.0]]
                  :f [[{:v 1 :vt 1} {:v 2 :vt 1}]]}
                 (sut/parse (->lines "v 0 0 0"
                                     "v 1 1 1"
                                     "vt 0 0"
                                     "vn 1 0 0"
                                     "f 1/1/ 2/1/"))))
      )
    ) ;endregion
  )