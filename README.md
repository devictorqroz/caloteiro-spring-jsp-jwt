<h1 align="center">Projeto Caloteiros (CRUD com JSP e JWT)</h1>

<p align="center">
  <strong>Um sistema web monolítico para gerenciamento de devedores, construído com Java, Spring Boot, JSP e JWT.</strong>
</p>

<br>

## 📋 Índice 
* [Descrição do Projeto](#-descrição-do-projeto)
* [Funcionalidades](#-funcionalidades)
* [Pré-requisitos](#-pré-requisitos)
* [Obter a Imagem Docker](#-obter-a-imagem-docker)
* [Como Executar](#️-como-executar)
* [Tecnologias Utilizadas](#-tecnologias-utilizadas)
* [Pessoa Desenvolvedora](#-pessoa-desenvolvedora)
* [Licença](#️-licença)
* [Conclusão](#-conclusão)

<br>

## 📝 Descrição do Projeto

Este repositório contém o código-fonte do **Projeto Caloteiros**, um sistema CRUD (Create, Read, Update, Delete) para gerenciamento de devedores.  
A aplicação foi desenvolvida como um projeto de estudo e portfólio, com foco em demonstrar habilidades em desenvolvimento backend com o ecossistema **Java e Spring**.

A aplicação é construída em uma arquitetura **monolítica**, utilizando:

- **Java 17** e **Spring Boot 3** para o backend.  
- **Spring Security** para autenticação e autorização baseadas em **JWT (JSON Web Tokens)**.  
- **JSP (JavaServer Pages)** com **JSTL 2.1** para renderização do frontend (views).  
- **MySQL** como banco de dados relacional.  
- **OpenAPI/Swagger** para documentação dos endpoints REST.  
- Implantação containerizada com **Docker** e suporte para execução na **AWS** via Elastic Beanstalk e RDS.

<br>

## 🚀 Funcionalidades

* **Autenticação de Usuários:** Login seguro com Spring Security e JWT.
* **Gerenciamento de Devedores (CRUD):**
  * Cadastro de novos devedores.
  * Listagem de todos os devedores cadastrados.
  * Edição de informações de um devedor existente.
  * Exclusão de um devedor.
* **Controle de Acesso:** Apenas usuários autenticados podem acessar as funcionalidades.
* **Documentação da API:** Documentação interativa com Swagger/OpenAPI acessível via `/swagger-ui.html`.
* **API de Métricas:** Endpoint `/actuator/prometheus` para exposição de métricas da JVM e da aplicação.
* **Stack de Observabilidade:** Integração com Prometheus e Grafana (veja o repositório [caloteiros-observability-stack](https://github.com/devictorqroz/caloteiros-observability-stack)).

<br>

## 🔧 Pré-requisitos

Para construir e executar o projeto a partir do código-fonte, certifique-se de possuir:

- **Java Development Kit (JDK) 17 ou superior**
- **Maven 3.8 ou superior**
- **Docker**

<br>

## 🐳 Obter a Imagem Docker

Antes de executar o container, é necessário ter a imagem Docker da aplicação disponível localmente.  
Você pode **baixar a imagem pronta do Docker Hub (recomendado)** ou **gerar sua própria imagem localmente**.

---

### 🔹 Opção 1 — Baixar a imagem do Docker Hub (recomendado)

```bash
docker pull devictorqroz/caloteiros-jsp-jwt:latest
``` 
Após o download, siga para a seção Como Executar.

### 🔹 Opção 2 — Gerar a imagem manualmente a partir do Dockerfile

Se preferir construir a imagem localmente (por exemplo, após editar o código):

Certifique-se de estar na raiz do projeto (onde está o arquivo Dockerfile).

Execute o comando abaixo para criar a imagem:

```
docker build -t caloteiros-jsp-jwt:latest .
```

Em seguida, execute a aplicação conforme mostrado na próxima seção.

<br>

## ▶️ Como Executar

A maneira recomendada para executar esta aplicação é utilizando **Docker**, o que garante um ambiente consistente e isolado.

### 🧩 Execução via Docker (Perfis `dev` ou `prod`)

O projeto suporta dois perfis de execução:  
- **dev** → Para execução local e testes.  
- **prod** → Para execução em ambiente de produção (ex: AWS Elastic Beanstalk).

Ambos exigem as mesmas variáveis de ambiente.  
Basta escolher o perfil desejado e executá-lo conforme o exemplo abaixo:

```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \       # ou "prod"
  -e DB_HOST=host.docker.internal \
  -e DB_NAME=caloteiros_db \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=123456 \
  -e JWT_SECRET=minha_chave_jwt_segura \
  -e MAIL_HOST=smtp.sendgrid.net \
  -e MAIL_PORT=587 \
  -e MAIL_USERNAME=apikey \
  -e MAIL_PASSWORD=SG.xxxxxx \
  -e MAIL_FROM_ADDRESS=seu_email@dominio.com \
  -e APP_BASE_URL=http://localhost:8080 \
  --name caloteiros-container \
  devictorqroz/caloteiros-jsp-jwt:latest
```

🔹 Substitua os valores conforme seu ambiente.

🔹 O perfil prod deve ser usado apenas em ambientes implantados (ex: AWS Elastic Beanstalk).

🔹 A aplicação estará disponível em http://localhost:8080.

🔹 Caso o banco de dados esteja fora do Docker (na sua máquina local), use host.docker.internal como host.

<br>


### 🌍 Variáveis de Ambiente


| Variável                 | Descrição                                              | Exemplo / Valor Padrão                  |
| ------------------------ | ------------------------------------------------------ | --------------------------------------- |
| `SPRING_PROFILES_ACTIVE` | Define o perfil ativo da aplicação                     | `dev` ou `prod`                         |
| `DB_HOST`                | Host do banco de dados MySQL                           | `host.docker.internal` ou `rds.aws.com` |
| `DB_NAME`                | Nome do banco de dados                                 | `caloteiros_db`                         |
| `DB_USERNAME`            | Usuário do banco de dados                              | `root`                                  |
| `DB_PASSWORD`            | Senha do banco de dados                                | `123456`                                |
| `JWT_SECRET`             | Chave secreta usada para assinar tokens JWT            | `minha_chave_jwt_segura`                |
| `MAIL_HOST`              | Host SMTP usado para envio de e-mails                  | `smtp.sendgrid.net`                     |
| `MAIL_PORT`              | Porta SMTP                                             | `587`                                   |
| `MAIL_USERNAME`          | Usuário SMTP (geralmente `apikey` no SendGrid)         | `apikey`                                |
| `MAIL_PASSWORD`          | Senha SMTP ou chave SendGrid                           | `SG.xxxxxx`                             |
| `MAIL_FROM_ADDRESS`      | Endereço de e-mail remetente                           | `seu_email@dominio.com`                 |
| `APP_BASE_URL`           | URL base da aplicação (utilizada em links e templates) | `http://localhost:8080`                 |

<br>

⚠️ Importante:
Certifique-se de que todas essas variáveis estejam configuradas corretamente antes de executar o container.
Em ambientes AWS (Elastic Beanstalk, ECS ou EC2), essas variáveis devem ser definidas diretamente nas configurações do ambiente.



## 🛠️ Tecnologias Utilizadas

As seguintes ferramentas, bibliotecas e serviços foram utilizadas na construção do projeto:

<table>
<tr>
<td align="center"><strong>Frontend (View)</strong></td>
<td align="center"><strong>Backend</strong></td>
<td align="center"><strong>Banco de Dados</strong></td>
<td align="center"><strong>Servidor de Aplicação</strong></td>
<td align="center"><strong>DevOps & Cloud</strong></td>
</tr>
<tr>
<td>
<img src="https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=oracle&logoColor=white" alt="JSP">
<img src="https://img.shields.io/badge/JSTL-2.1-blue?style=for-the-badge" alt="JSTL 2.1">
<img src="https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white" alt="jQuery">
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" alt="HTML5">
<img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white" alt="CSS3">
</td>
<td>
<img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17">
<img src="https://img.shields.io/badge/Spring_Boot-3-F2F4F9?style=for-the-badge&logo=spring-boot" alt="Spring Boot 3">
<img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" alt="Spring Security">
<img src="https://img.shields.io/badge/Spring_Cache-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Cache">
<img src="https://img.shields.io/badge/Spring_Actuator-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Actuator">
<img src="https://img.shields.io/badge/OpenAPI/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger/OpenAPI">
<img src="https://img.shields.io/badge/SLF4J-006699?style=for-the-badge" alt="SLF4J">
<img src="https://img.shields.io/badge/Logback-228B22?style=for-the-badge&logo=logback&logoColor=white" alt="Logback">
<img src="https://img.shields.io/badge/SendGrid-1A82E2?style=for-the-badge&logo=sendgrid&logoColor=white" alt="SendGrid">
</td>
<td>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
</td>
<td align="center">
<img src="https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black" alt="Apache Tomcat">
</td>
<td>
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
<img src="https://img.shields.io/badge/Docker_Hub-384d54?style=for-the-badge&logo=docker&logoColor=white" alt="Docker Hub">
<img src="https://img.shields.io/badge/Amazon_AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white" alt="AWS">
</td>
</tr>
</table>

<br>

## 👨‍💻 Pessoa Desenvolvedora

Este projeto foi desenvolvido por:

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/devictorqroz">
        <img src="https://github.com/devictorqroz.png" width="100px;" alt="foto de perfil no GitHub"/>
        <br />
        <sub><b>Victor Queiroz</b></sub>
      </a>
    </td>
  </tr>
</table>

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/victorqroz/)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:victorqueiroz.ti@gmail.com)

<br>

## ⚖️ Licença

Este projeto está sob a licença **MIT**.  
Para mais detalhes, veja o arquivo [LICENSE](https://github.com/devictorqroz/caloteiro-spring-jsp-jwt/blob/main/LICENSE).

<br>

## ✅ Conclusão

O **Projeto Caloteiros** foi desenvolvido como um estudo prático para consolidar conhecimentos sobre:
- Desenvolvimento backend com **Spring Boot**.
- Autenticação e autorização via **JWT**.
- Integração com **JSP/JSTL**.
- Documentação de API com **Swagger/OpenAPI**.
- Deploy containerizado com **Docker** e execução na **AWS**.  

Serviu como uma excelente base para compreender todo o ciclo de desenvolvimento de um sistema Java completo — do backend ao deploy em nuvem.

> ✨ Obrigado por visitar este repositório! Sinta-se à vontade para explorar o código e sugerir melhorias.



