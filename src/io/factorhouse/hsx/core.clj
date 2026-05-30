(ns io.factorhouse.hsx.core
  (:require [cljs.core :as core]))

(defn- parse-sig
  [name fdecl]
  (let [[fdecl m] (if (string? (first fdecl))
                    [(next fdecl) {:doc (first fdecl)}]
                    [fdecl {}])
        [fdecl m] (if (map? (first fdecl))
                    [(next fdecl) (conj m (first fdecl))]
                    [fdecl m])
        fdecl (if (vector? (first fdecl))
                (list fdecl)
                fdecl)
        [fdecl m] (if (map? (last fdecl))
                    [(butlast fdecl) (conj m (last fdecl))]
                    [fdecl m])
        m (conj {:arglists (list 'quote (#'core/sigs fdecl))} m)
        m (conj (or (meta name) {}) m)]
    [(with-meta name m) fdecl]))

(defmacro component
  "Macro used to create a named Hsx component:

  (def c (component :MyComponent (constantly [:div \"Hello world\"])))"
  [display-name component-f]
  `(let [comp#         (fn ~(symbol display-name) [props#]
                         (let [elem-args# (obj-get props# "args")
                               comp#      (apply ~component-f elem-args#)]
                           (create-element comp#)))
         display-name# ~(name display-name)]
	     (set-display-name comp# display-name#)
	     (map->Component {:proxy        comp#
	                      :display-name display-name#
	                      :proxy-memo   (react-memo comp# (are-props-equal? =))})))

(defmacro defc
  "Defines a named HSX function component.

  Calling the component as a function returns a React element instead of running
  the component body immediately, so React hooks stay inside the component
  boundary:

  (defc item [text]
    [:li text])

  (defc list-view []
    [:ul
     (item \"one\")
     (item \"two\")])"
  {:arglists '([name doc-string? attr-map? [params*] prepost-map? body]
               [name doc-string? attr-map? ([params*] prepost-map? body) + attr-map?])}
  [sym & fdecl]
  (let [[fname fdecl] (parse-sig sym fdecl)]
    `(def ~fname
       (let [render-fn# (fn ~sym ~@fdecl)
             comp#      (fn ~sym [& args#]
                          (if (callable-component-props? (first args#))
                            (create-element (apply render-fn# (obj-get (first args#) "args")))
                            (create-callable-component-element (cons '~sym args#) ~sym args# nil)))
             memo-comp# (react-memo comp# (are-props-equal? =))]
         (mark-callable-component! comp# memo-comp# ~(name sym))
         comp#))))
