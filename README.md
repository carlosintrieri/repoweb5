# 🏢 AutoManager - Sistema de Gestão de Lojas Automotivas

Sistema distribuído de **microserviços** para gestão completa de lojas automotivas (Toyota e Volkswagen), com **LOJA como recurso central** e comunicação entre serviços.

---

## 🎯 Sobre o Projeto

Sistema desenvolvido para permitir que **CIOs e CEOs** de grupos automotivos (Toyota e Volkswagen) acessem informações de todas as unidades em tempo real através de APIs RESTful protegidas por JWT.

### **Diferencial:**
- ✅ **LOJA é o centro** - todos os recursos pertencem a uma loja
- ✅ **Microserviços se comunicam** - orquestração de dados
- ✅ **HATEOAS** - links entre recursos
- ✅ **Dashboard agregado** - visão geral de todas as lojas
- ✅ **JWT** - autenticação e autorização

---

## 🏗️ Arquitetura

```
                      ┌──────────────────┐
                      │  Lojas Service   │ ⭐ CENTRAL
                      │     (8082)       │
                      └────────┬─────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
┌───────▼────────┐  ┌──────────▼─────────┐  ┌────────▼───────┐
│    Clientes    │  │   Funcionários     │  │   Produtos     │
│ valida c/Lojas │  │  valida c/Lojas    │  │ valida c/Lojas │
│     (8083)     │  │      (8084)        │  │     (8085)     │
└────────────────┘  └────────────────────┘  └────────────────┘
        │                      │                      │
        └──────────────────────┼──────────────────────┘
                               │
                      ┌────────▼─────────┐
                      │      Vendas      │ 🎯 ORQUESTRADOR
                      │ chama TODOS os   │
                      │  outros serviços │
                      │      (8086)      │
                      └──────────────────┘
                               │
                      ┌────────▼─────────┐
                      │    Veículos      │
                      │ valida c/Lojas   │
                      │      (8087)      │
                      └──────────────────┘
                               
┌──────────────────────────────────────────┐
│          Auth Service (8081)             │
│       Autenticação JWT                   │
└──────────────────────────────────────────┘
```

---

## 📦 Microserviços

| Serviço | Porta | Função | Comunica com |
|---------|-------|--------|--------------|
| **Auth** | 8081 | Autenticação JWT | - |
| **Lojas** | 8082 | ⭐ CENTRAL - Gestão de lojas + Dashboard | Todos |
| **Clientes** | 8083 | Clientes por loja | Lojas |
| **Funcionários** | 8084 | Funcionários por loja | Lojas |
| **Produtos** | 8085 | Produtos por loja | Lojas |
| **Vendas** | 8086 | 🎯 ORQUESTRADOR - Vendas detalhadas | Lojas, Clientes, Funcionários, Veículos |
| **Veículos** | 8087 | Veículos por loja | Lojas |

---

## 🚀 Como Executar

### **Pré-requisitos:**
- Java 17+
- Maven 3.6+

### **Executar os 7 Serviços:**

Abra **7 terminais** e execute em ordem:

```bash
# Terminal 1 - Auth (PRIMEIRO)
cd auth-service
./mvnw spring-boot:run

# Terminal 2 - Lojas (SEGUNDO - é o central)
cd lojas-service
./mvnw spring-boot:run

# Terminais 3-7 - Outros serviços (em qualquer ordem)
cd clientes-service && ./mvnw spring-boot:run
cd funcionarios-service && ./mvnw spring-boot:run
cd produtos-service && ./mvnw spring-boot:run
cd vendas-service && ./mvnw spring-boot:run
cd veiculos-service && ./mvnw spring-boot:run
```

### **Verificar:**

```bash
curl http://localhost:8081/api/auth/teste        # Auth Service OK
curl http://localhost:8082/api/lojas/teste       # Lojas Service OK  
curl http://localhost:8083/api/lojas/1/clientes/teste
curl http://localhost:8084/api/lojas/1/funcionarios/teste
curl http://localhost:8085/api/lojas/1/produtos/teste
curl http://localhost:8086/api/lojas/1/vendas/teste
curl http://localhost:8087/api/lojas/1/veiculos/teste
```

---

## 📝 Exemplos de Requisições (POST)

### **1️⃣ Criar Loja**

```bash
curl -X POST http://localhost:8082/api/lojas \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Toyota Center Campinas",
    "cnpj": "12.345.678/0001-99",
    "endereco": "Av. das Amoreiras, 500",
    "cidade": "Campinas",
    "estado": "SP",
    "telefone": "(19) 3456-7890",
    "email": "campinas@toyotacenter.com.br",
    "ativa": true
  }'
```

**Campos obrigatórios:**
- `nome` (String) - Nome da loja
- `cnpj` (String) - CNPJ da loja
- `endereco` (String) - Endereço completo
- `cidade` (String) - Cidade
- `estado` (String) - Estado (sigla)
- `telefone` (String) - Telefone de contato
- `email` (String) - E-mail da loja
- `ativa` (Boolean) - Se a loja está ativa (true/false)

---

### **2️⃣ Criar Cliente**

```bash
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria da Silva Santos",
    "cpf": "123.456.789-00",
    "telefone": "(11) 98765-4321",
    "email": "maria.santos@email.com",
    "endereco": "Rua das Flores, 123, Apto 45",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }'
```

**Campos obrigatórios:**
- `nome` (String) - Nome completo do cliente
- `cpf` (String) - CPF do cliente
- `telefone` (String) - Telefone de contato
- `email` (String) - E-mail do cliente
- `endereco` (String) - Endereço completo
- `cidade` (String) - Cidade
- `estado` (String) - Estado (sigla)
- `ativo` (Boolean) - Se o cliente está ativo (true/false)

**Nota:** O campo `lojaId` é preenchido automaticamente a partir da URL (`/lojas/1/clientes`)

---

### **3️⃣ Criar Funcionário**

```bash
curl -X POST http://localhost:8084/api/lojas/1/funcionarios \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carlos Eduardo Oliveira",
    "cpf": "987.654.321-00",
    "email": "carlos.oliveira@toyotamoema.com.br",
    "telefone": "(11) 91234-5678",
    "cargo": "Vendedor Sênior",
    "salario": 4500.00,
    "dataAdmissao": "2024-01-15",
    "ativo": true
  }'
```

**Campos obrigatórios:**
- `nome` (String) - Nome completo do funcionário
- `cpf` (String) - CPF do funcionário
- `email` (String) - E-mail corporativo
- `telefone` (String) - Telefone de contato
- `cargo` (String) - Cargo/função (ex: Vendedor, Gerente, Atendente)
- `salario` (Double) - Salário (usar ponto para decimais)
- `dataAdmissao` (String) - Data de admissão no formato YYYY-MM-DD
- `ativo` (Boolean) - Se o funcionário está ativo (true/false)

**Nota:** O campo `lojaId` é preenchido automaticamente a partir da URL

---

### **4️⃣ Criar Produto**

```bash
curl -X POST http://localhost:8085/api/lojas/1/produtos \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Óleo Motor Sintético 5W30",
    "descricao": "Óleo sintético de alta performance para motores modernos",
    "categoria": "Lubrificantes",
    "fabricante": "Mobil",
    "preco": 89.90,
    "quantidadeEstoque": 150,
    "ativo": true
  }'
```

**Campos obrigatórios:**
- `nome` (String) - Nome do produto
- `descricao` (String) - Descrição detalhada
- `categoria` (String) - Categoria (ex: Lubrificantes, Peças, Acessórios)
- `fabricante` (String) - Fabricante/marca
- `preco` (Double) - Preço unitário (usar ponto para decimais)
- `quantidadeEstoque` (Integer) - Quantidade em estoque
- `ativo` (Boolean) - Se o produto está ativo (true/false)

**Nota:** O campo `lojaId` é preenchido automaticamente a partir da URL

---

### **5️⃣ Criar Veículo**

```bash
curl -X POST http://localhost:8087/api/lojas/1/veiculos \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Corolla XEI 2.0 Flex",
    "placa": "ABC-1D23",
    "chassi": "9BWZZZ377VT004251",
    "cor": "Prata",
    "tipo": "Sedan",
    "combustivel": "Flex",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 145000.00
  }'
```

**Campos obrigatórios:**
- `marca` (String) - Marca do veículo (ex: Toyota, Volkswagen, Honda)
- `modelo` (String) - Modelo completo
- `placa` (String) - Placa do veículo
- `chassi` (String) - Número do chassi
- `cor` (String) - Cor do veículo
- `tipo` (String) - Tipo (ex: Sedan, SUV, Hatch, Pickup)
- `combustivel` (String) - Tipo de combustível (ex: Flex, Gasolina, Diesel, Híbrido, Elétrico)
- `status` (String) - Status (DISPONIVEL, VENDIDO, RESERVADO, MANUTENCAO)
- `ano` (Integer) - Ano de fabricação
- `quilometragem` (Integer) - Quilometragem atual
- `preco` (Double) - Preço de venda (usar ponto para decimais)

**Nota:** O campo `lojaId` é preenchido automaticamente a partir da URL


---

### **6️⃣ Criar Venda**

```bash
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "funcionarioId": 2,
    "veiculoId": 1,
    "valorTotal": 145000.00,
    "formaPagamento": "FINANCIAMENTO",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-10",
    "observacoes": "Cliente optou por financiamento em 60x. Entrada de 30%."
  }'
```

**Campos obrigatórios:**
- `clienteId` (Long) - ID do cliente (deve existir na loja)
- `funcionarioId` (Long) - ID do funcionário/vendedor (deve existir na loja)
- `veiculoId` (Long) - ID do veículo vendido (deve existir na loja)
- `valorTotal` (Double) - Valor total da venda (usar ponto para decimais)
- `formaPagamento` (String) - Forma de pagamento (ex: DINHEIRO, CARTAO, FINANCIAMENTO, PIX)
- `status` (String) - Status da venda (PENDENTE, CONCLUIDA, CANCELADA)
- `dataVenda` (String) - Data da venda no formato YYYY-MM-DD
- `observacoes` (String) - Observações adicionais (opcional)

**Nota:** O campo `lojaId` é preenchido automaticamente a partir da URL

**Consultar venda detalhada (orquestração):**
```bash
curl -H "Authorization: Bearer SEU_TOKEN" \
  http://localhost:8086/api/lojas/1/vendas/1/detalhada
```
*Retorna a venda com dados completos da loja, cliente, funcionário e veículo*

---

## 💡 Dicas para Testes

### **Ordem recomendada para criar dados:**

1. **Primeiro:** Criar Loja (para ter um lojaId)
2. **Segundo:** Criar Cliente, Funcionário e Veículo (todos precisam de lojaId)
3. **Terceiro:** Criar Venda (precisa de clienteId, funcionarioId e veiculoId)

---

## 📋 EXEMPLOS COMPLETOS POR CATEGORIA

### **🏢 LOJAS - Múltiplos Exemplos**

#### **Exemplo 1: Loja Toyota em São Paulo**
```bash
curl -X POST http://localhost:8082/api/lojas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Toyota Vila Mariana",
    "cnpj": "10.234.567/0001-89",
    "endereco": "Av. Domingos de Morais, 2500",
    "cidade": "São Paulo",
    "estado": "SP",
    "telefone": "(11) 3456-7890",
    "email": "vilamariana@toyota.com.br",
    "ativa": true
  }'
```

#### **Exemplo 2: Loja Volkswagen em Campinas**
```bash
curl -X POST http://localhost:8082/api/lojas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Volkswagen Shopping Iguatemi",
    "cnpj": "11.345.678/0001-90",
    "endereco": "Av. Iguatemi, 777",
    "cidade": "Campinas",
    "estado": "SP",
    "telefone": "(19) 3234-5678",
    "email": "iguatemi@vw.com.br",
    "ativa": true
  }'
```

#### **Exemplo 3: Loja Honda no Rio de Janeiro**
```bash
curl -X POST http://localhost:8082/api/lojas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Honda Barra da Tijuca",
    "cnpj": "12.456.789/0001-01",
    "endereco": "Av. das Américas, 3000",
    "cidade": "Rio de Janeiro",
    "estado": "RJ",
    "telefone": "(21) 3987-6543",
    "email": "barra@honda.com.br",
    "ativa": true
  }'
```

---

### **👥 CLIENTES - Múltiplos Perfis**

#### **Exemplo 1: Cliente pessoa física - Comprador de carro novo**
```bash
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Eduarda Santos",
    "cpf": "123.456.789-00",
    "telefone": "(11) 98765-4321",
    "email": "maria.eduarda@email.com",
    "endereco": "Rua das Palmeiras, 456, Apto 802",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }'
```

#### **Exemplo 2: Cliente empresarial - Frota**
```bash
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Transportadora Rapidex Ltda",
    "cpf": "98.765.432/0001-10",
    "telefone": "(11) 3456-7890",
    "email": "comercial@rapidex.com.br",
    "endereco": "Av. Industrial, 2000, Galpão 5",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }'
```

#### **Exemplo 3: Cliente jovem - Primeiro carro**
```bash
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Lucas Henrique Oliveira",
    "cpf": "111.222.333-44",
    "telefone": "(11) 99876-5432",
    "email": "lucas.h@gmail.com",
    "endereco": "Rua São João, 789",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }'
```

#### **Exemplo 4: Cliente de outra cidade**
```bash
curl -X POST http://localhost:8083/api/lojas/2/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Ana Paula Costa",
    "cpf": "555.666.777-88",
    "telefone": "(19) 98888-7777",
    "email": "ana.costa@outlook.com",
    "endereco": "Av. Campinas, 1500, Casa 3",
    "cidade": "Campinas",
    "estado": "SP",
    "ativo": true
  }'
```

#### **Exemplo 5: Cliente VIP - Histórico de compras**
```bash
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Roberto Carlos Ferreira",
    "cpf": "999.888.777-66",
    "telefone": "(11) 91111-2222",
    "email": "roberto.ferreira@executive.com",
    "endereco": "Alameda Santos, 2500, Cobertura",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }'
```

---

### **👨‍💼 FUNCIONÁRIOS - Diferentes Cargos**

#### **Exemplo 1: Gerente de Vendas**
```bash
curl -X POST http://localhost:8084/api/lojas/1/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carlos Eduardo Silva",
    "cpf": "987.654.321-00",
    "email": "carlos.silva@toyotamoema.com.br",
    "telefone": "(11) 91234-5678",
    "cargo": "Gerente de Vendas",
    "salario": 8500.00,
    "dataAdmissao": "2020-03-15",
    "ativo": true
  }'
```

#### **Exemplo 2: Vendedor Sênior**
```bash
curl -X POST http://localhost:8084/api/lojas/1/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Patricia Alves Santos",
    "cpf": "456.789.123-45",
    "email": "patricia.alves@toyotamoema.com.br",
    "telefone": "(11) 98765-4321",
    "cargo": "Vendedora Sênior",
    "salario": 5500.00,
    "dataAdmissao": "2021-06-10",
    "ativo": true
  }'
```

#### **Exemplo 3: Vendedor Júnior**
```bash
curl -X POST http://localhost:8084/api/lojas/1/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Felipe Costa Oliveira",
    "cpf": "321.654.987-00",
    "email": "felipe.costa@toyotamoema.com.br",
    "telefone": "(11) 99999-8888",
    "cargo": "Vendedor Júnior",
    "salario": 3200.00,
    "dataAdmissao": "2024-01-08",
    "ativo": true
  }'
```

#### **Exemplo 4: Atendente**
```bash
curl -X POST http://localhost:8084/api/lojas/1/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Juliana Martins Souza",
    "cpf": "147.258.369-55",
    "email": "juliana.martins@toyotamoema.com.br",
    "telefone": "(11) 98888-7777",
    "cargo": "Atendente de Showroom",
    "salario": 2800.00,
    "dataAdmissao": "2023-09-20",
    "ativo": true
  }'
```

#### **Exemplo 5: Mecânico**
```bash
curl -X POST http://localhost:8084/api/lojas/1/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "José Roberto Lima",
    "cpf": "789.456.123-99",
    "email": "jose.roberto@toyotamoema.com.br",
    "telefone": "(11) 97777-6666",
    "cargo": "Mecânico Especialista",
    "salario": 4500.00,
    "dataAdmissao": "2019-11-05",
    "ativo": true
  }'
```

#### **Exemplo 6: Assistente Administrativo**
```bash
curl -X POST http://localhost:8084/api/lojas/2/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Amanda Rodrigues Pereira",
    "cpf": "852.963.741-22",
    "email": "amanda.rodrigues@vwcampinas.com.br",
    "telefone": "(19) 98765-4321",
    "cargo": "Assistente Administrativo",
    "salario": 3000.00,
    "dataAdmissao": "2022-04-12",
    "ativo": true
  }'
```

---

### **📦 PRODUTOS - Diferentes Categorias**

#### **Categoria: Lubrificantes**

```bash
# Produto 1: Óleo Motor Sintético
curl -X POST http://localhost:8085/api/lojas/1/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Óleo Motor Sintético 5W30 Mobil 1",
    "descricao": "Óleo sintético de alta performance para motores modernos. Aprovado API SN/CF",
    "categoria": "Lubrificantes",
    "fabricante": "Mobil",
    "preco": 89.90,
    "quantidadeEstoque": 150,
    "ativo": true
  }'

# Produto 2: Óleo Transmissão
curl -X POST http://localhost:8085/api/lojas/1/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Óleo para Transmissão Automática ATF",
    "descricao": "Óleo para transmissão automática, recomendado para veículos Toyota e Honda",
    "categoria": "Lubrificantes",
    "fabricante": "Castrol",
    "preco": 65.00,
    "quantidadeEstoque": 80,
    "ativo": true
  }'
```

#### **Categoria: Peças de Reposição**

```bash
# Produto 3: Filtro de Óleo
curl -X POST http://localhost:8085/api/lojas/1/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Filtro de Óleo Tecfil PSL140",
    "descricao": "Filtro de óleo original para motores 1.6 e 2.0. Compatível com Corolla",
    "categoria": "Filtros",
    "fabricante": "Tecfil",
    "preco": 35.50,
    "quantidadeEstoque": 200,
    "ativo": true
  }'

# Produto 4: Pastilha de Freio
curl -X POST http://localhost:8085/api/lojas/1/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Jogo de Pastilhas de Freio Dianteira",
    "descricao": "Pastilhas de freio cerâmicas para eixo dianteiro. Alta durabilidade",
    "categoria": "Freios",
    "fabricante": "Bosch",
    "preco": 180.00,
    "quantidadeEstoque": 45,
    "ativo": true
  }'

# Produto 5: Bateria
curl -X POST http://localhost:8085/api/lojas/1/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Bateria Automotiva 60Ah Moura",
    "descricao": "Bateria selada 60Ah, 12V. Garantia de 18 meses",
    "categoria": "Elétrica",
    "fabricante": "Moura",
    "preco": 450.00,
    "quantidadeEstoque": 30,
    "ativo": true
  }'
```

#### **Categoria: Acessórios**

```bash
# Produto 6: Tapete Automotivo
curl -X POST http://localhost:8085/api/lojas/1/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Jogo de Tapetes Personalizados",
    "descricao": "Tapetes emborrachados com logo da montadora. Kit com 4 peças",
    "categoria": "Acessórios",
    "fabricante": "Tapetes Premium",
    "preco": 250.00,
    "quantidadeEstoque": 60,
    "ativo": true
  }'

# Produto 7: Protetor de Caçamba
curl -X POST http://localhost:8085/api/lojas/2/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Protetor de Caçamba Volkswagen Saveiro",
    "descricao": "Protetor plástico para caçamba. Sob medida para Saveiro",
    "categoria": "Acessórios",
    "fabricante": "Volkswagen Original",
    "preco": 890.00,
    "quantidadeEstoque": 15,
    "ativo": true
  }'

# Produto 8: Rack de Teto
curl -X POST http://localhost:8085/api/lojas/1/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Rack de Teto Thule Wing Bar",
    "descricao": "Rack aerodinâmico de alumínio. Capacidade 75kg",
    "categoria": "Acessórios",
    "fabricante": "Thule",
    "preco": 1250.00,
    "quantidadeEstoque": 8,
    "ativo": true
  }'
```

---

### **🚗 VEÍCULOS - Diferentes Tipos e Marcas**

#### **Categoria: Sedan**

```bash
# Veículo 1: Toyota Corolla
curl -X POST http://localhost:8087/api/lojas/1/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Corolla XEI 2.0 Flex Automático",
    "placa": "ABC-1D23",
    "chassi": "9BWZZZ377VT004251",
    "cor": "Prata Celestial",
    "tipo": "Sedan",
    "combustivel": "Flex",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 145000.00
  }'

# Veículo 2: Honda Civic
curl -X POST http://localhost:8087/api/lojas/1/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Honda",
    "modelo": "Civic EXL 2.0 CVT",
    "placa": "DEF-4G56",
    "chassi": "9BWZZZ456VT007892",
    "cor": "Preto",
    "tipo": "Sedan",
    "combustivel": "Gasolina",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 185000.00
  }'
```

#### **Categoria: SUV**

```bash
# Veículo 3: Toyota SW4
curl -X POST http://localhost:8087/api/lojas/1/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "SW4 Diamond 2.8 Diesel 4x4 AT",
    "placa": "GHI-7J89",
    "chassi": "9BWZZZ789VT012345",
    "cor": "Branco Pérola",
    "tipo": "SUV",
    "combustivel": "Diesel",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 395000.00
  }'

# Veículo 4: Volkswagen T-Cross
curl -X POST http://localhost:8087/api/lojas/2/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Volkswagen",
    "modelo": "T-Cross Highline 1.4 TSI AT",
    "placa": "JKL-0M12",
    "chassi": "9BWAA45U7MP000123",
    "cor": "Cinza Platina",
    "tipo": "SUV",
    "combustivel": "Gasolina",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 138000.00
  }'
```

#### **Categoria: Hatchback**

```bash
# Veículo 5: Volkswagen Polo
curl -X POST http://localhost:8087/api/lojas/2/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Volkswagen",
    "modelo": "Polo Track 1.0 TSI Automático",
    "placa": "MNO-3P45",
    "chassi": "9BWAA11U5MP000456",
    "cor": "Vermelho Flash",
    "tipo": "Hatchback",
    "combustivel": "Gasolina",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 92000.00
  }'

# Veículo 6: Toyota Yaris
curl -X POST http://localhost:8087/api/lojas/1/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Yaris XLS 1.5 Flex CVT",
    "placa": "QRS-6T78",
    "chassi": "9BWZZZ234VT009876",
    "cor": "Azul Safira",
    "tipo": "Hatchback",
    "combustivel": "Flex",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 105000.00
  }'
```

#### **Categoria: Pickup**

```bash
# Veículo 7: Toyota Hilux
curl -X POST http://localhost:8087/api/lojas/1/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Hilux SRX 2.8 Diesel 4x4 AT",
    "placa": "TUV-9W01",
    "chassi": "9BWZZZ567VT015678",
    "cor": "Preto Mica",
    "tipo": "Pickup",
    "combustivel": "Diesel",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 315000.00
  }'

# Veículo 8: Volkswagen Saveiro
curl -X POST http://localhost:8087/api/lojas/2/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Volkswagen",
    "modelo": "Saveiro Robust 1.6 CS",
    "placa": "XYZ-2A34",
    "chassi": "9BWAA22U3MP000789",
    "cor": "Branco Cristal",
    "tipo": "Pickup",
    "combustivel": "Flex",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 88000.00
  }'
```

#### **Categoria: Seminovo**

```bash
# Veículo 9: Toyota Corolla Seminovo
curl -X POST http://localhost:8087/api/lojas/1/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Corolla GLI 1.8 Flex Automático",
    "placa": "BCD-5E67",
    "chassi": "9BWZZZ890VT018901",
    "cor": "Prata",
    "tipo": "Sedan",
    "combustivel": "Flex",
    "status": "DISPONIVEL",
    "ano": 2022,
    "quilometragem": 28000,
    "preco": 98000.00
  }'
```

---

### **💰 VENDAS - Diferentes Cenários**

#### **Cenário 1: Venda à vista - Carro popular**
```bash
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 3,
    "funcionarioId": 3,
    "veiculoId": 6,
    "valorTotal": 105000.00,
    "formaPagamento": "DINHEIRO",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-10",
    "observacoes": "Pagamento à vista. Cliente ganhou desconto de 5%."
  }'
```

#### **Cenário 2: Venda financiada - SUV Premium**
```bash
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 5,
    "funcionarioId": 1,
    "veiculoId": 3,
    "valorTotal": 395000.00,
    "formaPagamento": "FINANCIAMENTO",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-09",
    "observacoes": "Financiamento em 60x. Entrada de 30%. Troca por veículo usado (avaliado em R$ 50.000)."
  }'
```

#### **Cenário 3: Venda para empresa - Frota**
```bash
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 2,
    "funcionarioId": 1,
    "veiculoId": 7,
    "valorTotal": 315000.00,
    "formaPagamento": "PIX",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-08",
    "observacoes": "Venda corporativa para frota. Emitida Nota Fiscal. Cliente solicitou mais 3 unidades para próximo mês."
  }'
```

#### **Cenário 4: Venda de seminovo**
```bash
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "funcionarioId": 2,
    "veiculoId": 9,
    "valorTotal": 98000.00,
    "formaPagamento": "FINANCIAMENTO",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-07",
    "observacoes": "Veículo seminovo 2022. Cliente negociou garantia estendida por 12 meses."
  }'
```

#### **Cenário 5: Venda em negociação**
```bash
curl -X POST http://localhost:8086/api/lojas/2/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 4,
    "funcionarioId": 6,
    "veiculoId": 4,
    "valorTotal": 138000.00,
    "formaPagamento": "FINANCIAMENTO",
    "status": "PENDENTE",
    "dataVenda": "2024-12-11",
    "observacoes": "Aguardando aprovação do financiamento. Previsão de liberação em 3 dias úteis."
  }'
```

#### **Cenário 6: Venda com cartão**
```bash
curl -X POST http://localhost:8086/api/lojas/2/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 4,
    "funcionarioId": 6,
    "veiculoId": 5,
    "valorTotal": 92000.00,
    "formaPagamento": "CARTAO",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-06",
    "observacoes": "Pagamento em 12x sem juros no cartão. Cliente adquiriu kit de acessórios."
  }'
```

---

### **Script completo de exemplo:**

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

echo "Token: $TOKEN"

# 2. Criar Loja (já existem 2 criadas automaticamente, mas pode criar mais)
curl -X POST http://localhost:8082/api/lojas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Volkswagen Taubaté",
    "cnpj": "11.222.333/0001-44",
    "endereco": "Av. Brasil, 1000",
    "cidade": "Taubaté",
    "estado": "SP",
    "telefone": "(12) 3344-5566",
    "email": "taubate@vw.com.br",
    "ativa": true
  }'

# 3. Criar Cliente
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "José da Silva",
    "cpf": "111.222.333-44",
    "telefone": "(12) 99999-8888",
    "email": "jose@email.com",
    "endereco": "Rua X, 100",
    "cidade": "Taubaté",
    "estado": "SP",
    "ativo": true
  }'

# 4. Criar Funcionário
curl -X POST http://localhost:8084/api/lojas/1/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Ana Vendedora",
    "cpf": "555.666.777-88",
    "email": "ana@loja.com",
    "telefone": "(12) 98888-7777",
    "cargo": "Vendedora",
    "salario": 3500.00,
    "dataAdmissao": "2024-06-01",
    "ativo": true
  }'

# 5. Criar Veículo
curl -X POST http://localhost:8087/api/lojas/1/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Volkswagen",
    "modelo": "Nivus Highline TSI",
    "placa": "XYZ-9A87",
    "chassi": "9BWAA45U7MP000001",
    "cor": "Branco",
    "tipo": "SUV",
    "combustivel": "Gasolina",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 125000.00
  }'

# 6. Criar Venda
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "funcionarioId": 1,
    "veiculoId": 1,
    "valorTotal": 125000.00,
    "formaPagamento": "FINANCIAMENTO",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-10",
    "observacoes": "Primeira venda do dia!"
  }'

# 7. Ver dashboard geral
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8082/api/lojas/dashboard

# 8. Ver venda detalhada (com orquestração)
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8086/api/lojas/1/vendas/1/detalhada
```

---

## 🎯 CENÁRIOS COMPLETOS DE USO

### **Cenário 1: Montar uma loja completa do zero**

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

# 2. Criar a loja
LOJA_ID=3  # Assumindo que já existem 2 lojas
curl -X POST http://localhost:8082/api/lojas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Honda São José dos Campos",
    "cnpj": "33.444.555/0001-66",
    "endereco": "Av. Shishima Hifumi, 2911",
    "cidade": "São José dos Campos",
    "estado": "SP",
    "telefone": "(12) 3939-4040",
    "email": "sjc@honda.com.br",
    "ativa": true
  }'

# 3. Contratar equipe (3 funcionários)
# Gerente
curl -X POST http://localhost:8084/api/lojas/$LOJA_ID/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Marcos Paulo Gerente",
    "cpf": "100.200.300-40",
    "email": "marcos@hondasjc.com.br",
    "telefone": "(12) 99100-2003",
    "cargo": "Gerente Geral",
    "salario": 9000.00,
    "dataAdmissao": "2024-12-01",
    "ativo": true
  }'

# Vendedor 1
curl -X POST http://localhost:8084/api/lojas/$LOJA_ID/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carla Vendas Silva",
    "cpf": "200.300.400-50",
    "email": "carla@hondasjc.com.br",
    "telefone": "(12) 99200-3004",
    "cargo": "Vendedora",
    "salario": 4000.00,
    "dataAdmissao": "2024-12-01",
    "ativo": true
  }'

# Vendedor 2
curl -X POST http://localhost:8084/api/lojas/$LOJA_ID/funcionarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Roberto Vendas Costa",
    "cpf": "300.400.500-60",
    "email": "roberto@hondasjc.com.br",
    "telefone": "(12) 99300-4005",
    "cargo": "Vendedor",
    "salario": 4000.00,
    "dataAdmissao": "2024-12-01",
    "ativo": true
  }'

# 4. Cadastrar estoque inicial (5 veículos)
# Honda Civic
curl -X POST http://localhost:8087/api/lojas/$LOJA_ID/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Honda",
    "modelo": "Civic EXL 2.0",
    "placa": "AAA-1A11",
    "chassi": "CIVIC001",
    "cor": "Preto",
    "tipo": "Sedan",
    "combustivel": "Gasolina",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 185000.00
  }'

# Honda HR-V
curl -X POST http://localhost:8087/api/lojas/$LOJA_ID/veiculos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Honda",
    "modelo": "HR-V Touring CVT",
    "placa": "BBB-2B22",
    "chassi": "HRV001",
    "cor": "Branco Pérola",
    "tipo": "SUV",
    "combustivel": "Gasolina",
    "status": "DISPONIVEL",
    "ano": 2024,
    "quilometragem": 0,
    "preco": 165000.00
  }'

# Mais 3 veículos...

# 5. Cadastrar produtos (peças e acessórios)
curl -X POST http://localhost:8085/api/lojas/$LOJA_ID/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Filtro de Ar Condicionado Honda",
    "descricao": "Filtro de cabine original Honda",
    "categoria": "Filtros",
    "fabricante": "Honda",
    "preco": 85.00,
    "quantidadeEstoque": 50,
    "ativo": true
  }'

echo "✅ Loja completa montada com sucesso!"
```

---

### **Cenário 2: Simular um dia de vendas**

```bash
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

echo "🌅 MANHÃ - Receber clientes"

# Cliente 1: Família interessada em SUV
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pedro Henrique Família",
    "cpf": "400.500.600-70",
    "telefone": "(11) 99400-5006",
    "email": "pedro@familia.com",
    "endereco": "Rua das Famílias, 123",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }'

# Cliente 2: Jovem profissional
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Beatriz Profissional Silva",
    "cpf": "500.600.700-80",
    "telefone": "(11) 99500-6007",
    "email": "beatriz@prof.com",
    "endereco": "Av. Paulista, 1000",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }'

echo "☀️ TARDE - Fechar vendas"

# Venda 1: Família compra SUV (10h30)
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 10,
    "funcionarioId": 2,
    "veiculoId": 3,
    "valorTotal": 395000.00,
    "formaPagamento": "FINANCIAMENTO",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-11",
    "observacoes": "Venda realizada às 10:30. Cliente veio com família. Financiamento em 72x."
  }'

# Venda 2: Jovem compra Hatchback (14h00)
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 11,
    "funcionarioId": 3,
    "veiculoId": 6,
    "valorTotal": 105000.00,
    "formaPagamento": "FINANCIAMENTO",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-11",
    "observacoes": "Venda realizada às 14:00. Primeiro carro da cliente. Financiamento em 48x."
  }'

echo "🌙 FIM DO DIA - Ver resultados"
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8082/api/lojas/1/dashboard"

echo "✅ Total vendido hoje: R$ 500.000,00"
```

---

### **Cenário 3: Gestão de estoque e produtos**

```bash
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

echo "📦 CADASTRAR LINHA COMPLETA DE PRODUTOS"

# Linha de Filtros
for i in {1..5}; do
  curl -X POST http://localhost:8085/api/lojas/1/produtos \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
      \"nome\": \"Filtro Tipo $i\",
      \"descricao\": \"Filtro para diversos modelos\",
      \"categoria\": \"Filtros\",
      \"fabricante\": \"Fabricante X\",
      \"preco\": $((30 + i * 10)).00,
      \"quantidadeEstoque\": $((100 + i * 20)),
      \"ativo\": true
    }"
done

---

### **Cenário 4: Relatório mensal de vendas**

```bash
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

echo "📊 RELATÓRIO DE VENDAS - DEZEMBRO 2024"

# Buscar vendas do período
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8086/api/lojas/1/vendas/periodo?inicio=2024-12-01&fim=2024-12-31"

# Buscar faturamento total
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8086/api/lojas/1/vendas/total"

# Dashboard completo
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8082/api/lojas/dashboard"

echo "✅ Relatório gerado!"
```

---

## 🔐 Autenticação

### **Usuários criados automaticamente:**

| Username | Senha | Perfil | Permissões |
|----------|-------|--------|------------|
| admin | admin123 | ADMIN | ✅ Acesso completo (GET, POST, PUT, DELETE) |
| user | user123 | USER | ✅ Apenas consulta (GET) |

### **Diferenciação de Perfis:**

#### **ADMIN (Executivos - CIOs/CEOs)**
- ✅ GET - Consultar dados
- ✅ POST - Criar registros
- ✅ PUT - Atualizar registros completos
- ✅ DELETE - Remover registros

**Uso:** Gerentes, diretores e executivos com poder de modificação.

#### **USER (Analistas/Consultores)**
- ✅ GET - Consultar dados
- ❌ POST - Não pode criar
- ❌ PUT - Não pode atualizar
- ❌ DELETE - Não pode deletar

**Uso:** Analistas, auditores e consultores que precisam apenas visualizar dados.

---

### **Login:**

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "perfil": "ADMIN"
}
```

**💡 Copie o token! Use em todas as requisições:**
```
Authorization: Bearer SEU_TOKEN
```

---

## 🎯 EXEMPLOS COMPLETOS DE PERMISSÕES POR PERFIL

### **📊 Comparativo de Acesso:**

| Operação | Endpoint | ADMIN | USER | Resposta USER |
|----------|----------|-------|------|---------------|
| **Consultar** | GET /api/lojas | ✅ | ✅ | 200 OK |
| **Criar** | POST /api/lojas | ✅ | ❌ | 403 Forbidden |
| **Atualizar** | PUT /api/lojas/{id} | ✅ | ❌ | 403 Forbidden |
| **Deletar** | DELETE /api/clientes/{id} | ✅ | ❌ | 403 Forbidden |

---

### **1️⃣ OPERAÇÃO GET - Ambos Podem Acessar**

#### **✅ ADMIN pode consultar:**
```bash
# Login como ADMIN
TOKEN_ADMIN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

# GET - Listar todas as lojas
curl -H "Authorization: Bearer $TOKEN_ADMIN" \
  http://localhost:8082/api/lojas

# GET - Dashboard geral
curl -H "Authorization: Bearer $TOKEN_ADMIN" \
  http://localhost:8082/api/lojas/dashboard

# GET - Clientes de uma loja
curl -H "Authorization: Bearer $TOKEN_ADMIN" \
  http://localhost:8083/api/lojas/1/clientes

# GET - Venda detalhada
curl -H "Authorization: Bearer $TOKEN_ADMIN" \
  http://localhost:8086/api/lojas/1/vendas/1/detalhada
```

**Resposta:** `200 OK` + dados JSON

---

#### **✅ USER também pode consultar:**
```bash
# Login como USER
TOKEN_USER=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","senha":"user123"}' | jq -r '.token')

# GET - Listar todas as lojas (FUNCIONA!)
curl -H "Authorization: Bearer $TOKEN_USER" \
  http://localhost:8082/api/lojas

# GET - Dashboard geral (FUNCIONA!)
curl -H "Authorization: Bearer $TOKEN_USER" \
  http://localhost:8082/api/lojas/dashboard

# GET - Clientes de uma loja (FUNCIONA!)
curl -H "Authorization: Bearer $TOKEN_USER" \
  http://localhost:8083/api/lojas/1/clientes

# GET - Veículos disponíveis (FUNCIONA!)
curl -H "Authorization: Bearer $TOKEN_USER" \
  http://localhost:8087/api/lojas/1/veiculos
```

**Resposta:** `200 OK` + dados JSON

**💡 Resumo GET:** Tanto ADMIN quanto USER podem consultar qualquer dado com GET!

---

### **2️⃣ OPERAÇÃO POST - Apenas ADMIN**

#### **✅ ADMIN pode criar:**
```bash
TOKEN_ADMIN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

# POST - Criar nova loja
curl -X POST http://localhost:8082/api/lojas \
  -H "Authorization: Bearer $TOKEN_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Toyota Santos",
    "cnpj": "99.888.777/0001-66",
    "endereco": "Av. Ana Costa, 400",
    "cidade": "Santos",
    "estado": "SP",
    "telefone": "(13) 3322-1100",
    "email": "santos@toyota.com.br",
    "ativa": true
  }'
```

**Resposta:** `201 Created` + dados da loja criada

```bash
# POST - Criar cliente
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Francisco Almeida",
    "cpf": "666.777.888-99",
    "telefone": "(11) 99666-7777",
    "email": "francisco@email.com",
    "endereco": "Rua Nova, 500",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }'
```

**Resposta:** `201 Created` + dados do cliente criado

```bash
# POST - Registrar venda
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer $TOKEN_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "funcionarioId": 2,
    "veiculoId": 1,
    "valorTotal": 125000.00,
    "formaPagamento": "FINANCIAMENTO",
    "status": "CONCLUIDA",
    "dataVenda": "2024-12-11",
    "observacoes": "Venda aprovada"
  }'
```

**Resposta:** `201 Created` + dados da venda criada

---

#### **❌ USER NÃO pode criar:**
```bash
TOKEN_USER=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","senha":"user123"}' | jq -r '.token')

# POST - Tentar criar loja (BLOQUEADO!)
curl -X POST http://localhost:8082/api/lojas \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Toyota Santos",
    "cnpj": "99.888.777/0001-66",
    "endereco": "Av. Ana Costa, 400",
    "cidade": "Santos",
    "estado": "SP",
    "telefone": "(13) 3322-1100",
    "email": "santos@toyota.com.br",
    "ativa": true
  }'
```

**Resposta:** `403 Forbidden`
```json
{
  "timestamp": "2024-12-11T00:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Acesso negado",
  "path": "/api/lojas"
}
```

```bash
# POST - Tentar criar cliente (BLOQUEADO!)
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Cliente Teste",
    "cpf": "111.222.333-44",
    "telefone": "(11) 91111-2222",
    "email": "teste@email.com",
    "endereco": "Rua X, 100",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }'
```

**Resposta:** `403 Forbidden`

**💡 Resumo POST:** Apenas ADMIN pode criar novos registros. USER recebe 403 Forbidden.

---

### **3️⃣ OPERAÇÃO PUT - Apenas ADMIN**

#### **✅ ADMIN pode atualizar:**
```bash
TOKEN_ADMIN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

# PUT - Atualizar dados completos da loja
curl -X PUT http://localhost:8082/api/lojas/1 \
  -H "Authorization: Bearer $TOKEN_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Toyota Moema - ATUALIZADO",
    "cnpj": "10.234.567/0001-89",
    "endereco": "Av. Moema, 1000 - NOVO ENDEREÇO",
    "cidade": "São Paulo",
    "estado": "SP",
    "telefone": "(11) 3456-7890",
    "email": "moema@toyota.com.br",
    "ativa": true
  }'
```

**Resposta:** `200 OK` + dados atualizados

```bash
# PUT - Atualizar funcionário
curl -X PUT http://localhost:8084/api/lojas/1/funcionarios/1 \
  -H "Authorization: Bearer $TOKEN_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carlos Silva - PROMOVIDO",
    "cpf": "987.654.321-00",
    "email": "carlos@toyota.com.br",
    "telefone": "(11) 91234-5678",
    "cargo": "Gerente Regional",
    "salario": 12000.00,
    "dataAdmissao": "2020-03-15",
    "ativo": true
  }'
```

**Resposta:** `200 OK` + dados atualizados

---

#### **❌ USER NÃO pode atualizar:**
```bash
TOKEN_USER=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","senha":"user123"}' | jq -r '.token')

# PUT - Tentar atualizar loja (BLOQUEADO!)
curl -X PUT http://localhost:8082/api/lojas/1 \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Toyota Moema - ATUALIZADO",
    "cnpj": "10.234.567/0001-89",
    "endereco": "Av. Moema, 1000",
    "cidade": "São Paulo",
    "estado": "SP",
    "telefone": "(11) 3456-7890",
    "email": "moema@toyota.com.br",
    "ativa": true
  }'
```

**Resposta:** `403 Forbidden`

**💡 Resumo PUT:** Apenas ADMIN pode atualizar registros completos. USER recebe 403 Forbidden.

---

### **4️⃣ OPERAÇÃO LOGIN - Apenas ADMIN**

#### **✅ ADMIN pode modificar campos específicos:**
```bash
TOKEN_ADMIN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')


**Resposta:** `200 OK` + produto com estoque atualizado


```

**Resposta:** `200 OK` + veículo com status atualizado

---

#### **❌ USER NÃO pode modificar:**
```bash
TOKEN_USER=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","senha":"user123"}' | jq -r '.token')


```

**Resposta:** `403 Forbidden`


---

### **5️⃣ OPERAÇÃO DELETE - Apenas ADMIN**

#### **✅ ADMIN pode deletar:**
```bash
TOKEN_ADMIN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

# DELETE - Remover cliente
curl -X DELETE http://localhost:8083/api/lojas/1/clientes/5 \
  -H "Authorization: Bearer $TOKEN_ADMIN"
```

**Resposta:** `204 No Content` (cliente removido)

```bash
# DELETE - Remover produto
curl -X DELETE http://localhost:8085/api/lojas/1/produtos/10 \
  -H "Authorization: Bearer $TOKEN_ADMIN"
```

**Resposta:** `204 No Content` (produto removido)

```bash
# DELETE - Remover veículo
curl -X DELETE http://localhost:8087/api/lojas/1/veiculos/8 \
  -H "Authorization: Bearer $TOKEN_ADMIN"
```

**Resposta:** `204 No Content` (veículo removido)

---

#### **❌ USER NÃO pode deletar:**
```bash
TOKEN_USER=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","senha":"user123"}' | jq -r '.token')

# DELETE - Tentar remover cliente (BLOQUEADO!)
curl -X DELETE http://localhost:8083/api/lojas/1/clientes/5 \
  -H "Authorization: Bearer $TOKEN_USER"
```

**Resposta:** `403 Forbidden`

```bash
# DELETE - Tentar remover produto (BLOQUEADO!)
curl -X DELETE http://localhost:8085/api/lojas/1/produtos/10 \
  -H "Authorization: Bearer $TOKEN_USER"
```

**Resposta:** `403 Forbidden`

**💡 Resumo DELETE:** Apenas ADMIN pode deletar registros. USER recebe 403 Forbidden.

---

### **📋 RESUMO COMPLETO DE PERMISSÕES**

#### **✅ ADMIN (admin/admin123) - Acesso Total:**

| Verbo HTTP | Operação | Exemplo | Resposta |
|------------|----------|---------|----------|
| **GET** | Consultar | `GET /api/lojas` | 200 OK |
| **POST** | Criar | `POST /api/lojas/1/clientes` | 201 Created |
| **PUT** | Atualizar completo | `PUT /api/lojas/1` | 200 OK |
| **DELETE** | Remover | `DELETE /api/clientes/1` | 204 No Content |

**Casos de uso:**
- ✅ Gerentes que precisam modificar dados
- ✅ Executivos (CIO/CEO) com poder de decisão
- ✅ Administradores do sistema
- ✅ Diretores com permissão total

---

#### **🔍 USER (user/user123) - Apenas Leitura:**

| Verbo HTTP | Operação | Exemplo | Resposta |
|------------|----------|---------|----------|
| **GET** | Consultar | `GET /api/lojas` | ✅ 200 OK |
| **POST** | Criar | `POST /api/lojas/1/clientes` | ❌ 403 Forbidden |
| **PUT** | Atualizar completo | `PUT /api/lojas/1` | ❌ 403 Forbidden |
| **DELETE** | Remover | `DELETE /api/clientes/1` | ❌ 403 Forbidden |

**Casos de uso:**
- ✅ Analistas que precisam apenas ver dados
- ✅ Auditores consultando informações
- ✅ Consultores gerando relatórios
- ✅ Estagiários com acesso limitado

---

### **🎯 Script de Teste Completo:**

```bash
#!/bin/bash

echo "🔐 TESTE DE PERMISSÕES - ADMIN vs USER"
echo "========================================"
echo ""

# Login ADMIN
echo "📝 Fazendo login como ADMIN..."
TOKEN_ADMIN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')
echo "✅ Token ADMIN: ${TOKEN_ADMIN:0:20}..."
echo ""

# Login USER
echo "📝 Fazendo login como USER..."
TOKEN_USER=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","senha":"user123"}' | jq -r '.token')
echo "✅ Token USER: ${TOKEN_USER:0:20}..."
echo ""

echo "========================================"
echo "TESTE 1: GET - Ambos devem conseguir"
echo "========================================"

echo "ADMIN consultando lojas..."
curl -s -H "Authorization: Bearer $TOKEN_ADMIN" \
  http://localhost:8082/api/lojas | jq -r '.[] | .nome' | head -2
echo "✅ ADMIN conseguiu consultar!"
echo ""

echo "USER consultando lojas..."
curl -s -H "Authorization: Bearer $TOKEN_USER" \
  http://localhost:8082/api/lojas | jq -r '.[] | .nome' | head -2
echo "✅ USER conseguiu consultar!"
echo ""

echo "========================================"
echo "TESTE 2: POST - Apenas ADMIN consegue"
echo "========================================"

echo "ADMIN criando cliente..."
ADMIN_POST=$(curl -s -w "\n%{http_code}" -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Cliente Teste ADMIN",
    "cpf": "111.222.333-44",
    "telefone": "(11) 91111-2222",
    "email": "teste@admin.com",
    "endereco": "Rua Teste, 100",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }')
HTTP_CODE_ADMIN=$(echo "$ADMIN_POST" | tail -1)
if [ "$HTTP_CODE_ADMIN" = "201" ]; then
  echo "✅ ADMIN criou cliente! (201 Created)"
else
  echo "❌ ADMIN falhou (código: $HTTP_CODE_ADMIN)"
fi
echo ""

echo "USER tentando criar cliente..."
USER_POST=$(curl -s -w "\n%{http_code}" -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Cliente Teste USER",
    "cpf": "222.333.444-55",
    "telefone": "(11) 92222-3333",
    "email": "teste@user.com",
    "endereco": "Rua Teste, 200",
    "cidade": "São Paulo",
    "estado": "SP",
    "ativo": true
  }')
HTTP_CODE_USER=$(echo "$USER_POST" | tail -1)
if [ "$HTTP_CODE_USER" = "403" ]; then
  echo "✅ USER foi bloqueado corretamente! (403 Forbidden)"
else
  echo "❌ USER não deveria conseguir (código: $HTTP_CODE_USER)"
fi
echo ""

echo "========================================"
echo "TESTE 3: DELETE - Apenas ADMIN consegue"
echo "========================================"

echo "ADMIN deletando cliente..."
ADMIN_DELETE=$(curl -s -w "\n%{http_code}" -X DELETE http://localhost:8083/api/lojas/1/clientes/1 \
  -H "Authorization: Bearer $TOKEN_ADMIN")
HTTP_CODE_ADMIN_DEL=$(echo "$ADMIN_DELETE" | tail -1)
if [ "$HTTP_CODE_ADMIN_DEL" = "204" ]; then
  echo "✅ ADMIN deletou cliente! (204 No Content)"
else
  echo "ℹ️ Cliente pode não existir (código: $HTTP_CODE_ADMIN_DEL)"
fi
echo ""

echo "USER tentando deletar cliente..."
USER_DELETE=$(curl -s -w "\n%{http_code}" -X DELETE http://localhost:8083/api/lojas/1/clientes/2 \
  -H "Authorization: Bearer $TOKEN_USER")
HTTP_CODE_USER_DEL=$(echo "$USER_DELETE" | tail -1)
if [ "$HTTP_CODE_USER_DEL" = "403" ]; then
  echo "✅ USER foi bloqueado corretamente! (403 Forbidden)"
else
  echo "❌ USER não deveria conseguir (código: $HTTP_CODE_USER_DEL)"
fi
echo ""

echo "========================================"
echo "✅ TESTES CONCLUÍDOS!"
echo "========================================"
echo ""
echo "📊 RESUMO:"
echo "  • ADMIN: Acesso total (GET, POST, PUT, DELETE)"
echo "  • USER: Apenas leitura (GET)"
echo ""
```

**Salve como `test-permissions.sh` e execute:**
```bash
chmod +x test-permissions.sh
./test-permissions.sh
```

---

## 📡 Endpoints Principais

### **1. Lojas (Central) - 8082**

```http
GET  /api/lojas                    # Listar todas
POST /api/lojas                    # Criar loja
GET  /api/lojas/{id}               # Detalhes da loja
PUT  /api/lojas/{id}               # Atualizar loja
GET  /api/lojas/dashboard          # 🎯 Dashboard GERAL (todas as lojas)
GET  /api/lojas/{id}/dashboard     # Dashboard de UMA loja
```

**Dashboard Geral** (chama TODOS os serviços):
```bash
curl -H "Authorization: Bearer TOKEN" \
  http://localhost:8082/api/lojas/dashboard
```

**Retorna:**
```json
{
  "totalLojas": 2,
  "totalClientes": 150,
  "totalFuncionarios": 25,
  "totalProdutos": 500,
  "totalVendas": 85,
  "totalVeiculos": 120,
  "faturamentoTotal": 12500000.00,
  "lojas": [
    {
      "id": 1,
      "nome": "Toyota Moema SP",
      "cidade": "São Paulo",
      "clientes": 100,
      "funcionarios": 15,
      "vendas": 50,
      "faturamento": 8000000.00
    }
  ]
}
```

---

### **2. Clientes - 8083**

```http
GET    /api/lojas/{lojaId}/clientes           # Listar clientes da loja
POST   /api/lojas/{lojaId}/clientes           # Criar cliente
GET    /api/lojas/{lojaId}/clientes/{id}      # Buscar cliente
PUT    /api/lojas/{lojaId}/clientes/{id}      # Atualizar
DELETE /api/lojas/{lojaId}/clientes/{id}      # Deletar
GET    /api/lojas/{lojaId}/clientes/count     # Contar
```

**Exemplo:**
```bash
curl -H "Authorization: Bearer TOKEN" \
  http://localhost:8083/api/lojas/1/clientes
```

---

### **3. Funcionários - 8084**

```http
GET    /api/lojas/{lojaId}/funcionarios       # Listar
POST   /api/lojas/{lojaId}/funcionarios       # Criar
GET    /api/lojas/{lojaId}/funcionarios/{id}  # Buscar
PUT    /api/lojas/{lojaId}/funcionarios/{id}  # Atualizar
DELETE /api/lojas/{lojaId}/funcionarios/{id}  # Deletar
```

---

### **4. Produtos - 8085**

```http
GET    /api/lojas/{lojaId}/produtos           # Listar
POST   /api/lojas/{lojaId}/produtos           # Criar
GET    /api/lojas/{lojaId}/produtos/{id}      # Buscar
PUT    /api/lojas/{lojaId}/produtos/{id}      # Atualizar
DELETE /api/lojas/{lojaId}/produtos/{id}      # Deletar
```

---

### **5. Vendas - 8086 (Orquestrador)**

```http
GET  /api/lojas/{lojaId}/vendas                    # Listar vendas
POST /api/lojas/{lojaId}/vendas                    # Registrar venda
GET  /api/lojas/{lojaId}/vendas/{id}               # Buscar venda
GET  /api/lojas/{lojaId}/vendas/{id}/detalhada     # 🎯 Venda COMPLETA (agrega tudo)
GET  /api/lojas/{lojaId}/vendas/periodo?inicio=X&fim=Y  # Por período
GET  /api/lojas/{lojaId}/vendas/total              # Faturamento total
```

**Venda Detalhada** (chama Lojas + Clientes + Funcionários + Veículos):
```bash
curl -H "Authorization: Bearer TOKEN" \
  http://localhost:8086/api/lojas/1/vendas/1/detalhada
```

**Retorna:**
```json
{
  "id": 1,
  "valorTotal": 120000.00,
  "formaPagamento": "FINANCIAMENTO",
  "status": "CONCLUIDA",
  "dataVenda": "2024-11-15",
  "loja": {
    "id": 1,
    "nome": "Toyota Moema SP",
    "cnpj": "12.345.678/0001-00"
  },
  "cliente": {
    "id": 1,
    "nome": "João Silva",
    "cpf": "123.456.789-00"
  },
  "funcionario": {
    "id": 2,
    "nome": "Paulo Vendedor",
    "cargo": "Vendedor"
  },
  "veiculo": {
    "id": 1,
    "modelo": "Corolla XEI 2.0",
    "placa": "ABC1234"
  }
}
```

---

### **6. Veículos - 8087**

```http
GET    /api/lojas/{lojaId}/veiculos           # Listar
POST   /api/lojas/{lojaId}/veiculos           # Criar
GET    /api/lojas/{lojaId}/veiculos/{id}      # Buscar
PUT    /api/lojas/{lojaId}/veiculos/{id}      # Atualizar
DELETE /api/lojas/{lojaId}/veiculos/{id}      # Deletar
```

---

## 🔗 HATEOAS

Todas as respostas incluem **links HATEOAS**:

```json
{
  "id": 1,
  "nome": "João Silva",
  "_links": {
    "self": {
      "href": "/api/lojas/1/clientes/1"
    },
    "loja": {
      "href": "/api/lojas/1"
    }
  }
}
```

---

## 🗄️ Bancos H2

Cada serviço tem seu banco independente:

| Serviço | H2 Console | JDBC URL |
|---------|------------|----------|
| Auth | http://localhost:8081/h2-console | `jdbc:h2:mem:auth_db` |
| Lojas | http://localhost:8082/h2-console | `jdbc:h2:mem:lojas_db` |
| Clientes | http://localhost:8083/h2-console | `jdbc:h2:mem:clientes_db` |
| Funcionários | http://localhost:8084/h2-console | `jdbc:h2:mem:funcionarios_db` |
| Produtos | http://localhost:8085/h2-console | `jdbc:h2:mem:produtos_db` |
| Vendas | http://localhost:8086/h2-console | `jdbc:h2:mem:vendas_db` |
| Veículos | http://localhost:8087/h2-console | `jdbc:h2:mem:veiculos_db` |

**Acesso:** User: `sa` | Password: (vazio)

---

## 🎯 Fluxo de Uso

### **1. CEO quer ver todas as lojas:**
```bash
# Login
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"admin123"}' | jq -r '.token')

# Dashboard geral
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8082/api/lojas/dashboard
```

### **2. Gerente quer cadastrar cliente:**
```bash
curl -X POST http://localhost:8083/api/lojas/1/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Silva",
    "cpf": "12345678900",
    "telefone": "11999999999",
    "email": "maria@email.com"
  }'
```

### **3. Vendedor registra venda completa:**
```bash
# 1. Criar venda
curl -X POST http://localhost:8086/api/lojas/1/vendas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "funcionarioId": 2,
    "veiculoId": 1,
    "valorTotal": 120000.00,
    "formaPagamento": "FINANCIAMENTO",
    "status": "PENDENTE",
    "dataVenda": "2024-12-10"
  }'

# 2. Ver venda detalhada (orquestração)
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8086/api/lojas/1/vendas/1/detalhada
```

---

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security 6** (JWT)
- **Spring Data JPA**
- **Spring HATEOAS**
- **H2 Database**
- **RestTemplate** (comunicação entre serviços)
- **Maven**

---

## ✅ Características

✅ **Arquitetura de Microserviços**  
✅ **Loja como recurso central**  
✅ **Comunicação entre serviços** (RestTemplate)  
✅ **Orquestração** (Vendas agrega dados de múltiplos serviços)  
✅ **HATEOAS** (RESTful Level 3)  
✅ **JWT** (autenticação e autorização)  
✅ **Validação** (cada serviço valida se loja existe)  
✅ **Dashboard agregado** (visão geral de todas lojas)  
✅ **7 bancos H2 independentes**  
✅ **CRUD completo** em todos os recursos  

---

## 📚 Estrutura de Diretórios

```
automanager-final/
├── auth-service/           # Autenticação JWT
├── lojas-service/          # ⭐ CENTRAL
├── clientes-service/       # Clientes por loja
├── funcionarios-service/   # Funcionários por loja
├── produtos-service/       # Produtos por loja
├── vendas-service/         # 🎯 ORQUESTRADOR
├── veiculos-service/       # Veículos por loja
└── README.md              # Este arquivo
```

---

## 🎓 Desenvolvido para

**Atividade:** ATVV - Desenvolvimento Web III  
**Professor:** Dr. Eng. Gerson Penha  
**Instituição:** Fatec São José dos Campos  
**Ano:** 2024  

---

## 🚀 Pronto para uso!

Este sistema implementa uma **arquitetura real de microserviços** com:
- ✅ Separação de responsabilidades
- ✅ Comunicação entre serviços
- ✅ Orquestração de dados
- ✅ HATEOAS
- ✅ JWT
- ✅ Loja como centro do sistema

**Perfeito para CIOs e CEOs monitorarem todas as unidades em tempo real!** 🎯
