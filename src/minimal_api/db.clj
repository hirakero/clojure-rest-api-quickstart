(ns minimal-api.db)

(def todos
  (atom {"todo1" {"task" "build an API"}
         "todo2" {"task" "????"}
         "todo3" {"task" "profit!"}}))
