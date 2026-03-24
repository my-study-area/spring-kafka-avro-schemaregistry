# Spring Gravity - Kafka Task Management

Este projeto é um MVP de um sistema de gerenciamento de tarefas (Todo List) focado em **Arquitetura Event-Driven**, seguindo os princípios de **Clean Architecture** (Ports and Adapters) com **Spring Boot 3.4.3**, **Kafka** e **Avro**.

## 🚀 Arquitetura

O sistema recebe eventos de criação de tarefas via Kafka, persiste em um banco de dados H2 em memória e valida os contratos através do **Confluent Schema Registry**.

### Componentes:
- **Broker (Kafka)**: Motor de mensageria.
- **Schema Registry**: Governança de contratos Avro.
- **Kafka UI**: Interface gráfica para gerenciamento de tópicos e schemas (Porta 9000).
- **H2 Database**: Banco de dados volátil para persistência rápida (Console na porta 8080).
- **Stimulus Script**: Gerador de carga realista em Python usando Faker.

---

## 🛠 Pré-requisitos

- **Java 17+**
- **Maven 3.8+**
- **Docker & Docker Compose**
- **Python 3.12.0** (para o script de estímulo)

---

## 🏁 Como Executar

### 1. Subir a Infraestrutura (Containers)
Na raiz do projeto, execute:
```bash
docker compose up -d
```
Aguarde o serviço `kafka-setup` finalizar (ele criará o tópico e registrará o schema automaticamente). 
Acompanhe via: `docker logs -f kafka-setup`

### 2. Executar a Aplicação Spring Boot
```bash
mvn spring-boot:run
```

### 3. Configurar ambiente Python (Estímulo)
Crie um ambiente virtual e instale as dependências:
```bash
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

---

## 🧪 Validando a Solução

### Passo 1: Gerar Estímulos (Dados Fakes)
Execute o script Python para enviar uma tarefa aleatória diretamente para o Kafka:
```bash
python stimulus.py
```

### Passo 2: Verificar Processamento (Logs)
Observe os logs da aplicação Java. Você deverá ver mensagens como:
`Recebido evento do Kafka...`
`Task salva no H2 com sucesso...`

### Passo 3: Consultar Banco de Dados (H2)
1. Acesse: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
2. **JDBC URL**: `jdbc:h2:mem:taskdb`
3. **User**: `sa` | **Password**: `password`
4. Execute: `SELECT * FROM tasks;`

---

## 📂 Interfaces Gráficas Úteis
- **Gerenciamento Kafka**: [http://localhost:9000](http://localhost:9000)
- **Schema Registry**: [http://localhost:8081](http://localhost:8081)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## 📝 Contrato de Evento (Avro)
O sistema exige que todo evento possua o envelope de rastreabilidade:
- `eventId`: UUID único do evento.
- `transactionId`: ID da transação de negócio.
- `correlationId`: ID para rastreabilidade cross-service.
- `timestamp`: Momento da ocorrência.
- `payload`: Dados da tarefa (`taskId`, `title`, `description`, `status`).
