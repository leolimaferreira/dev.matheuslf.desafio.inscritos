## 🧠 Desafio Técnico – Sistema de Gestão de Projetos e Demandas

### 📘 Contexto
Sua missão é desenvolver uma **API RESTful em Java com Spring Boot** para gerenciar **projetos e tarefas (demandas)** de uma empresa.  
O sistema será utilizado por um time de desenvolvimento para organizar suas entregas, acompanhar o status das tarefas e realizar análises simples.

---

## 🎯 Requisitos Técnicos

### 🧱 1. Modelagem de Domínio

A modelagem pode ser modificada pelo inscrito. Porém, precisa ser justificado o motivo.

#### `Project`
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID/Long | Identificador |
| `name` | String (3–100) | **Obrigatório** |
| `description` | String | Opcional |
| `startDate` | Date | Início do projeto |
| `endDate` | Date | Opcional |

#### `Task`
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID/Long | Identificador |
| `title` | String (5–150) | **Obrigatório** |
| `description` | String | Detalhes da tarefa |
| `status` | Enum | TODO / DOING / DONE |
| `priority` | Enum | LOW / MEDIUM / HIGH |
| `dueDate` | Date | Data limite |
| `projectId` | FK(Project) | Relacionamento |

---

### 🌐 2. Endpoints REST

| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| **POST** | `/projects` | Criar novo projeto (`name` obrigatório) |
| **GET** | `/projects` | Listar todos os projetos (paginação) |
| **PUT** | `/projects/{id}` | Atualizar projeto (apenas campos não nulos) |
| **DELETE** | `/projects/{id}` | Deletar projeto (com validações) |
| **POST** | `/tasks` | Criar nova tarefa vinculada a um projeto |
| **GET** | `/tasks?status=&priority=&projectId=` | Buscar tarefas com filtros opcionais |
| **PUT** | `/tasks/{id}` | Atualizar tarefa (apenas campos não nulos) |
| **PUT** | `/tasks/{id}/status` | Atualizar apenas o status da tarefa |
| **DELETE** | `/tasks/{id}` | Remover tarefa |

---

## 🔒 Validações de Negócio Implementadas

### 📋 Validações de Projeto
- ✅ Não permitir deletar projeto com tarefas ativas (status == DOING)
- ✅ Validar unicidade de `name` em projetos
- ✅ Não permitir atualizar `endDate` para data anterior a `startDate`
- ✅ Não permitir criar projeto com `startDate` no passado
- ✅ Comprimento mínimo e máximo de `name` (3-100 caracteres)
- ✅ Descrição obrigatória

### 📝 Validações de Tarefa
- ✅ Não permitir criar tarefa sem projeto válido
- ✅ Não permitir atualizar tarefa para projeto que não existe
- ✅ Não permitir criar tarefa com `dueDate` anterior à data atual
- ✅ Não permitir atualizar `dueDate` para data anterior a hoje
- ✅ Validar que `dueDate` não seja anterior a `startDate` do projeto
- ✅ Não permitir criar tarefa com prioridade HIGH sem descrição obrigatória
- ✅ Não permitir atualizar tarefa para projeto cujo `endDate` já passou
- ✅ Validar limite máximo de tarefas HIGH por projeto (máximo 5)
- ✅ Apenas tarefas em status TODO podem ser movidas para DOING
- ✅ Apenas tarefas em status DOING podem ser movidas para DONE

### 🎯 Tratamento de Erros
- ✅ Erros de validação retornam **400 Bad Request** com detalhes dos campos
- ✅ Recursos não encontrados retornam **404 Not Found**
- ✅ Conflitos de negócio retornam **409 Conflict**
- ✅ Operações não suportadas retornam **405 Method Not Allowed**

---

## ✅ Requisitos Obrigatórios
- 🧑‍💻 **Java 17+** e **Spring Boot 3+**  
- 🧠 **Spring Data JPA** com **Specifications** para queries dinâmicas
- 🗄️ Banco Relacional (**PostgreSQL**)  
- ✔️ **Bean Validation** com anotações customizadas
- 🧪 **Testes Automatizados**  
  - Unitários (Services mockados com MockMvc)
  - Integração com cobertura de código (**JaCoCo**)
- ⚠️ Tratamento de erros com `@ControllerAdvice`  
- 📦 Uso de **DTOs** para transferência de dados
- 📘 **README** explicando como rodar o projeto

---

## 📁 Estrutura do Projeto

```
src/
├── main/java/dev/matheuslf/desafio/inscritos/
│   ├── InscritosApplication.java          
│   ├── controller/
│   │   ├── GenericController.java
│   │   ├── annotation/                  
│   │   │   ├── ValidPriority.java
│   │   │   └── ValidStatus.java
│   │   └── impl/
│   │       ├── ProjectController.java
│   │       └── TaskController.java
│   ├── dto/
│   │   ├── error/                         
│   │   │   ├── FieldError.java
│   │   │   └── ResponseError.java
│   │   ├── project/
│   │   │   ├── ProjectRequestDTO.java
│   │   │   ├── ProjectResponseDTO.java
│   │   │   └── UpdateProjectDTO.java
│   │   └── task/
│   │       ├── TaskRequestDTO.java
│   │       ├── TaskResponseDTO.java
│   │       └── UpdateTaskDTO.java
│   ├── entities/                          
│   │   ├── Project.java
│   │   ├── Task.java
│   │   └── enums/
│   │       ├── Priority.java
│   │       └── Status.java
│   ├── exception/                         
│   │   ├── ConflictException.java
│   │   ├── DescriptionNeededException.java
│   │   ├── InvalidFieldException.java
│   │   ├── InvalidStatusChangeException.java
│   │   ├── InvalidTaskDueDateException.java
│   │   ├── InvalidTimeExpendedWithTaskException.java
│   │   ├── NotFoundException.java
│   │   ├── NumberOfHighTasksExceedException.java
│   │   ├── ProjectEndedException.java
│   │   ├── ProjectWithActiveTasksException.java
│   │   └── handler/
│   │       └── GlobalExceptionHandler.java
│   ├── mapper/                           
│   │   ├── ProjectMapper.java
│   │   └── TaskMapper.java
│   ├── repository/                       
│   │   ├── ProjectRepository.java
│   │   ├── TaskRepository.java
│   │   └── specs/
│   │       └── TaskSpecs.java
│   ├── service/                           
│   │   ├── ProjectService.java
│   │   └── TaskService.java
│   └── validator/                        
│       ├── PriorityValidator.java
│       ├── ProjectValidator.java
│       ├── StatusValidator.java
│       └── TaskValidator.java
├── main/resources/
│   └── application.yml                   
└── test/java/dev/matheuslf/desafio/inscritos/
    └── Testes unitários e de integração
```

---

## 🚀 Como Executar

### Pré-requisitos
- **Java 17+** instalado
- **Maven 3.6+** instalado
- **PostgreSQL** rodando localmente (porta padrão 5432)

### Passos para Executar

1. **Clone o repositório**
   ```bash
   git clone <repository-url>
   cd desafio-inscritos
   ```

2. **Configure o banco de dados**
   - Crie um banco PostgreSQL chamado `desafio_inscritos`
   - Atualize as credenciais em `src/main/resources/application.yml` se necessário

3. **Execute a aplicação**
   ```bash
   # Com Maven
   ./mvnw spring-boot:run
   
   # Ou via IDE
   # Execute a classe InscritosApplication.java
   ```

4. **A API estará disponível em**
   ```
   http://localhost:8080
   ```

### Testes

```bash
# Executar todos os testes
./mvnw clean test

# Executar com relatório de cobertura JaCoCo
./mvnw clean test jacoco:report

# Visualizar relatório (abra em navegador)
./target/site/jacoco/index.html
```

---

## 🏅 Funcionalidades Implementadas

### Projetos
- ✅ CRUD completo com validações
- ✅ Atualização parcial (apenas campos não nulos)
- ✅ Listagem com paginação
- ✅ Validações de datas (startDate no passado, endDate)
- ✅ Unicidade de nome
- ✅ Proteção contra deleção com tarefas ativas

### Tarefas
- ✅ CRUD completo com validações complexas
- ✅ Atualização parcial (apenas campos não nulos)
- ✅ Filtros por status, prioridade e projeto
- ✅ Transição de status validada (TODO → DOING → DONE)
- ✅ Limite de tarefas HIGH por projeto
- ✅ Validações de datas (dueDate, comparação com projeto)
- ✅ Descrição obrigatória para prioridade HIGH

---

## 🔐 Segurança e Validações

- ✅ Validação em camada de Controller (Bean Validation)
- ✅ Validação em camada de Service (regras de negócio)
- ✅ Anotações customizadas para enums (@ValidStatus, @ValidPriority)
- ✅ Tratamento centralizado de exceções
- ✅ Mensagens de erro descriptivas

---

## 🏅 Diferenciais Implementados
- ✅ **JaCoCo** para relatório de cobertura de testes
- ✅ **Spring Data JPA Specifications** para queries dinâmicas e reutilizáveis
- ✅ **Anotações customizadas** para validação de enums
- ✅ **Exceções customizadas** com tratamento específico por tipo
- ✅ **DTOs separados** para requisição, resposta e atualização parcial
- ✅ **Tratamento de lazy loading** em relacionamentos JPA com Jackson
- ✅ **Testes unitários** com MockMvc e Mockito
- ✅ **Validação em múltiplas camadas** (Controller, Service, Validator)
- ✅ **Paginação** implementada nos endpoints GET

---

## 📊 Cobertura de Testes (JaCoCo)

O projeto inclui configuração de cobertura de testes com JaCoCo. Após executar `./mvnw clean test`, visualize o relatório em:

```
./target/site/jacoco/index.html
```

O relatório mostra cobertura de classes, métodos, linhas de código e branches.

---

## 🔧 Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| Java | 17+ | Linguagem |
| Spring Boot | 3+ | Framework |
| Spring Data JPA | - | ORM e Persistência |
| PostgreSQL | Latest | Banco de Dados |
| Bean Validation | - | Validação de Dados |
| JUnit 5 | Latest | Testes |
| Mockito | Latest | Mocks para Testes |
| JaCoCo | Latest | Cobertura de Testes |

---

## 📝 Exemplos de Uso

### Criar um Projeto
```json
POST /projects
Content-Type: application/json

{
  "name": "Projeto X",
  "description": "Descrição do projeto",
  "startDate": "2025-01-01",
  "endDate": "2025-12-31"
}
```

### Criar uma Tarefa
```json
POST /tasks
Content-Type: application/json

{
  "title": "Implementar feature",
  "description": "Descrição da tarefa",
  "priority": "HIGH",
  "dueDate": "2025-06-30",
  "projectId": "uuid-do-projeto"
}
```

### Atualizar Tarefa (Parcial)
```json
PUT /tasks/{id}
Content-Type: application/json

{
  "status": "DOING",
  "priority": "MEDIUM"
}
```

---

## 🐛 Troubleshooting

### Erro ao conectar com PostgreSQL
- Verifique se o PostgreSQL está rodando na porta 5432
- Valide as credenciais em `application.yml`
- Confirme que o banco `desafio_inscritos` foi criado

### Erro de Lazy Loading
Se receber erro de Hibernate proxy ao serializar:
- Verifique as configurações de `@JsonIgnore` nas DTOs
- Confirme se está usando `@Transactional` nos serviços

### Testes falhando
- Execute `./mvnw clean test` para limpar build anterior
- Verifique se todas as dependências foram instaladas

---

## 📚 Documentação Adicional

Para mais informações:
- Verifique `src/main/resources/application.yml` para configurações
- Consulte `pom.xml` para dependências do projeto

---

## 🛠️ Tags
`#Java` `#SpringBoot` `#Backend` `#DesafioTecnico`  
`#API` `#RestAPI` `#PostgreSQL` `#JPA`  
`#MapStruct` `#JaCoCo` `#CleanCode` `#SoftwareEngineering`

---

### 💡 Notas Importantes

> Este é um projeto de demonstração técnica focado em:
> - **Arquitetura limpa** e bem organizada
> - **Validações robustas** de negócio
> - **Cobertura de testes** abrangente
> - **Código maintível** e escalável

---

### 🧾 Licença
Este projeto foi desenvolvido exclusivamente para o **processo seletivo SIS Innov & Tech** e não deve ser utilizado para fins comerciais.

---
