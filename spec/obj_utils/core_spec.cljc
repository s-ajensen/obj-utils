(ns obj-utils.core-spec
  (:require [obj-utils.core :as sut]
            [speclj.core :refer :all]))

(defn ->lines [& lines]
  (apply str (map #(str % "\n") lines)))

(describe "Wavefront .obj utils"

  (context "parses"

    (it "empty obj file"
      (should= {} (sut/<-obj "")))

    (it "with a single comment"
      (should= {} (sut/<-obj "# this is a comment")))

    (it "with many comments"
      (should= {} (sut/<-obj (->lines "# this is a comment"
                                      "#this is another comment"))))

    (it "with a single vertex"
      (should= {:v [[1.0 1.0 1.0]]}
               (sut/<-obj "v 1.0 1.0 1.0")))

    (it "with a two vertices"
      (should= {:v [[1.0 1.0 1.0]
                    [2.0 2.0 2.0]]}
               (sut/<-obj (->lines "v 1.0 1.0 1.0"
                                   "v 2.0 2.0 2.0"))))

    (it "with a two vertices"
      (should= {:v [[1.0 1.0 1.0]
                    [2.0 2.0 2.0]]}
               (sut/<-obj (->lines "v 1.0 1.0 1.0"
                                   "v 2.0 2.0 2.0"))))

    (it "with a many vertices"
      (should= {:v [[1.0 1.0 1.0]
                    [2.0 2.0 2.0]
                    [3.0 3.0 3.0]
                    [4.0 4.0 4.0]
                    [5.0 5.0 5.0]]}
               (sut/<-obj (->lines "v 1.0 1.0 1.0"
                                   "v 2.0 2.0 2.0"
                                   "v 3.0 3.0 3.0"
                                   "v 4.0 4.0 4.0"
                                   "v 5.0 5.0 5.0"))))

    ))