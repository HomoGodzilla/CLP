(ns clojure-intro.core
  (:require [clj-http.client :as http]
            [cheshire.core :as json])
  (:gen-class))

(def ids-artigos (range 1 11)) ; Queremos buscar 10 artigos

;; A URL base da API
(def url-base "https://jsonplaceholder.typicode.com/posts/")

;; Um átomo para guardar nossos resultados de forma segura entre as threads.
;; Começa como um vetor vazio.
(def resultados-concorrentes (atom []))


(defn buscar-artigo
  "Recebe um ID, busca o artigo na API e retorna o corpo da resposta."
  [id]
  (:body (http/get (str url-base id))))

(defn extrair-titulo
  "Recebe um JSON (string) e extrai o título dele."
  [json-str]
  (:title (json/parse-string json-str true))) ; o 'true' converte chaves para keywords

(defn buscar-e-processar [id]
  "Função que combina a busca e o processamento para um único ID."
  (-> id
      buscar-artigo
      extrair-titulo))


;; --- Abordagem Sequencial ---

(defn buscar-sequencialmente []
  (println "--- Iniciando Busca Sequencial ---")
  (println "Buscando" (count ids-artigos) "artigos, um por um...")
  (let [resultados (mapv buscar-e-processar ids-artigos)]
    (println "Títulos encontrados:")
    (doseq [titulo resultados]
      (println (str "\t- " titulo)))
    (println "--- Busca Sequencial Finalizada ---")))


;; --- Abordagem Concorrente ---

(defn buscar-concorrentemente []
  (println "\n--- Iniciando Busca Concorrente ---")
  (println "Disparando" (count ids-artigos) "requisições em paralelo...")

  ;; Para cada ID, criamos uma 'future'.
  ;; A 'future' executa o código em outra thread.
  ;; O resultado é uma lista de 'futures'.
  (let [futures (doall
                 (map (fn [id]
                        (future
                          (let [titulo (buscar-e-processar id)]
                            ;; 'swap!' é uma operação atômica e segura
                            ;; para modificar o valor de um átomo.
                            (swap! resultados-concorrentes conj titulo))))
                      ids-artigos))]

    ;; Agora, esperamos todas as 'futures' terminarem.
    ;; O '@' (ou 'deref') espera pela 'future' e pega seu valor.
    (doseq [f futures] @f)

    (println "Títulos encontrados (ordem pode variar):")
    (doseq [titulo @resultados-concorrentes] ; Pega o valor final do átomo
      (println (str "\t- " titulo)))
    (println "--- Busca Concorrente Finalizada ---")))


;; --- Função Principal ---

(defn -main
  [& args]
  (println "Demonstração de Concorrência em Clojure")
  (println "========================================")

  ;; Medindo o tempo da abordagem sequencial
  (time (buscar-sequencialmente))

  ;; Medindo o tempo da abordagem concorrente
  (time (buscar-concorrentemente)))