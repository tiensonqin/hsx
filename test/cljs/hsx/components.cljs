(ns hsx.components
  (:require [io.factorhouse.hsx.core :as hsx]))

(defn button-reagent-args
  [value on-click]
  [:button {:on-click on-click} value])

(defn button
  [{:keys [onClick children]}]
  [button-reagent-args children onClick])

(def Button
  (hsx/reactify-component button))

(defn button-via-react-elem
  [{:keys [onClick children]}]
  [:> Button {:on-click onClick} children])

(def ButtonViaReactElem
  (hsx/reactify-component button-via-react-elem))

(def FragmentedButton
  (hsx/reactify-component
   (fn [{:keys [onClick buttonOneValue buttonTwoValue]}]
     [:div
      [:<>
       [button {:onClick onClick :children buttonOneValue}]
       [button {:onClick onClick :children buttonTwoValue}]]])))

(def SeqButton
  (hsx/reactify-component
   (fn [{:keys [onClick buttonOneValue buttonTwoValue]}]
     (let [buttons [{:onClick onClick :children buttonOneValue}
                    {:onClick onClick :children buttonTwoValue}]]
       [:div
        (for [props buttons]
          ^{:key (str "button-" (:children props))}
          [:> Button props])]))))

(hsx/defc callable-button
  [value on-click]
  [:button {:on-click on-click} value])

(def CallableButton
  (hsx/reactify-component
   (fn [{:keys [onClick children]}]
     (callable-button children onClick))))

(def CallableButtonList
  (hsx/reactify-component
   (fn [{:keys [onClick buttonOneValue buttonTwoValue]}]
     [:div
      (callable-button buttonOneValue onClick)
      (callable-button buttonTwoValue onClick)])))

(def ShorthandTags
  (hsx/reactify-component
   (fn [_props]
     [:.outer
      [:#target "Target"]
      [:div.#legacy-id "Legacy"]
      [:button.#action.primary "Action"]
      [:button.secondary#secondary-action "Secondary"]])))

(def EmptyVectorChild
  (hsx/reactify-component
   (fn [_props]
     [:div
      []
      [:span "Rendered"]])))

(def NestedVectorChild
  (hsx/reactify-component
   (fn [_props]
     [:div
      [[:strong "Title"]
       [:small "Description"]]])))

(def RichTextVectorChild
  (hsx/reactify-component
   (fn [_props]
     [:p ["If you lose your password, "
          [:span "keep a secure backup."]]])))

(def KeyedFragmentList
  (hsx/reactify-component
   (fn [{:keys [items]}]
     [:div
      (for [item items]
        ^{:key (:id item)}
        [:<>
         [:input {:data-testid (str "item-" (:id item))
                  :default-value (:value item)
                  :read-only true}]])])
   (fn [props]
     (let [items (js->clj (.-items props) :keywordize-keys true)]
       {:items items}))))
