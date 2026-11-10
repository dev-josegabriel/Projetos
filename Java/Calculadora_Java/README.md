<div align="center">

# 🧮 Java Calculator

**Calculadora interativa de console desenvolvida em Java puro**

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Status](https://img.shields.io/badge/Status-Concluído-00C851?style=for-the-badge)]()
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

</div>

---

## 📌 Sobre o projeto

**Java Calculator** é uma calculadora de linha de comando desenvolvida como exercício prático de lógica em Java. O programa permite ao usuário realizar operações matemáticas básicas de forma interativa, com validação de entrada e loop de execução contínua — sem precisar reiniciar o programa a cada cálculo.

> Projeto desenvolvido durante os estudos de Java com base no curso **Java COMPLETO** de Nélio Alves.

---

## ✨ Funcionalidades

| Operação         | Símbolo | Suporte    |
|------------------|---------|------------|
| Soma             | `+`     | ✅         |
| Subtração        | `-`     | ✅         |
| Multiplicação    | `×`     | ✅         |
| Divisão          | `÷`     | ✅         |
| Divisão por zero | —       | ✅ Tratado |
| Opção inválida   | —       | ✅ Validado|

- 🔁 Loop contínuo de execução — o usuário decide quando sair
- 🛡️ Validação de operação inválida com mensagem de erro amigável
- 🌍 Locale configurado para `US` (ponto como separador decimal)

---

## 🖥️ Demonstração

```
== CALCULADORA ==
Digite um Número: 15
Digite outro Número: 4

Qual operação deseja escolher?
[1] - SOMA
[2] - SUBTRAÇÃO
[3] - MULTIPLICAÇÃO
[4] - DIVISÃO
OPERAÇÃO: 4

RESULTADO:
DIVISÃO = 3.75

Deseja continuar (s/n)? s

== CALCULADORA ==
Digite um Número: 10
Digite outro Número: 0
OPERAÇÃO: 4
N EXISTE DIVISAO POR ZERO

Deseja continuar (s/n)? n
```

---

## 🚀 Como executar

### Pré-requisitos

- [JDK 11+](https://www.oracle.com/java/technologies/downloads/) instalado e configurado no PATH

### Clonando o repositório

```bash
git clone https://github.com/josegabriel2006/Projetos_Java.git
cd Projetos_Java
```

### Compilando

```bash
javac -d out src/util/Calculator.java
```

### Executando

```bash
java -cp out util.Calculator
```

---

## 🗂️ Estrutura do projeto

```
Projetos_Java/
├── src/
│   └── util/
│       └── Calculator.java   # Classe principal da calculadora
└── README.md
```

---

## 🛠️ Tecnologias utilizadas

- **Java 21** — linguagem principal
- **`java.util.Scanner`** — leitura de entrada do usuário via console
- **`java.util.Locale`** — padronização de formatação numérica

---

## 📚 Aprendizados aplicados

- Estruturas de controle: `switch`, `if/else`, `while`
- Leitura de dados via `Scanner`
- Tratamento de casos extremos (divisão por zero, opção inválida)
- Organização de código em pacotes (`package util`)
- Casting explícito (`(double)`) para precisão em divisões inteiras

---

## 👤 Autor

**José Gabriel**  
Estudante de Engenharia de Software · Goiânia, Brasil

[![GitHub](https://img.shields.io/badge/GitHub-josegabriel2006-181717?style=flat-square&logo=github)](https://github.com/josegabriel2006)

---

<div align="center">

Feito com ☕ e Java

</div>
