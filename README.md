# Posts Manager API 📝

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-green)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue)](https://www.apache.org/licenses/LICENSE-2.0)

O projeto foi desenvolvido com o objetivo de **praticar e consolidar conhecimentos** em:
- Implementação de autenticação com JWT (JSON Web Token)
- Integração de segurança com **Spring Security 6+**
- Configuração de  **controle de acesso baseado em roles** (`ADMIN`, `BASIC`).
- Configuração de políticas de segurança em APIs REST
---

## ✨ Funcionalidades implementadas:
### 🔒 Módulo de Segurança
- **Autenticação JWT** com tempo de expiração configurável.
- Configuração customizada do `SecurityFilterChain`.
- Criptografia de senhas com `BCryptPasswordEncoder`.
- Proteção de endpoints baseado em roles (`ADMIN`, `BASIC`).

### 📝 Gerenciamento de Posts
- Criação, deleção e listagem de posts (apenas para usuários autenticados).
- Acesso restrito a usuários autenticados.

### 👩‍💻 Gerenciamento de Usuários
- Criação e autenticação de usuário.
- Listagem de usuários restrita aos administradores.

### 📚 Documentação
- Integração com **SpringDoc OpenAPI**.

---

## 🛠️ Tecnologias

- **Backend**:
  - Java
  - Spring Boot
  - Spring Security + JWT
  - Spring Data JPA
  - SpringDoc OpenAPI (Swagger UI)

- **Banco de Dados**:
  - MySQL via Docker (ambiente de desenvolvimento)

- **Ferramentas**:
  - Maven
  - Docker
---
