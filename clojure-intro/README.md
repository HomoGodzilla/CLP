# clojure-intro

Este projeto demonstra como buscar dados de uma API REST de forma sequencial e concorrente usando Clojure.

## Descrição

O código busca os títulos de 10 artigos da API [JSONPlaceholder](https://jsonplaceholder.typicode.com/posts/). Ele apresenta duas abordagens:

- **Sequencial:** Busca cada artigo um após o outro.
- **Concorrente:** Dispara múltiplas requisições em paralelo usando `future` e armazena os resultados em um átomo.

## Principais funções

- `buscar-artigo`: Busca um artigo pelo ID na API.
- `extrair-titulo`: Extrai o título do artigo a partir do JSON retornado.
- `buscar-e-processar`: Combina busca e extração do título.
- `buscar-sequencialmente`: Executa as buscas de forma sequencial.
- `buscar-concorrentemente`: Executa as buscas de forma concorrente.
- `-main`: Função principal que executa ambas as abordagens e mede o tempo de execução.

## Como executar

1. Instale as dependências (`clj-http` e `cheshire`).
2. Execute o projeto com:

    ```bash
    lein run
    ```

## Saída esperada

O programa imprime os títulos dos artigos buscados, mostrando a diferença de tempo entre as abordagens sequencial e concorrente.

## Requisitos

- [Leiningen](https://leiningen.org/) ou [Clojure CLI](https://clojure.org/guides/getting_started)
- Conexão com a internet