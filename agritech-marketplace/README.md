# AgriTech Marketplace

A production-style marketplace where **farmers list produce** and **buyers browse and order it**, deployed with a full DevSecOps pipeline: a **Spring Boot API** and a **static frontend** running on **Amazon EKS**, AWS infrastructure provisioned with **Terraform**, containers shipped through **Amazon ECR**, a pipeline secured with **SonarQube + Trivy**, and a platform observed with **Prometheus + Grafana**.

> Designed as a recruiter-friendly portfolio project showing the full software delivery lifecycle — IaC, CI/CD, containerization, Kubernetes, security scanning, and monitoring — wrapped around a real product domain instead of a toy CRUD app.

---

## Architecture

```text
Developer
  │
  ▼
GitHub Repository
  │
  ▼
GitHub Actions CI/CD Pipeline
  │
  ├── SonarQube Scan ── Quality Gate
  ├── Maven Test (backend)
  ├── Docker Build (backend + frontend) ─── Trivy Image Scan
  ├── Push Images to Amazon ECR
  ├── Terraform Apply ── AWS VPC / IAM / EKS / RDS / ECR
  └── Helm Upgrade
       │
       ▼
Amazon EKS Cluster
  ├── Ingress Controller
  ├── Frontend (nginx: static site + /api/ reverse proxy)
  ├── Spring Boot API (Farmers, Buyers, Produce, Orders)
  ├── ConfigMaps & Secrets
  └── Prometheus Metrics Endpoint
       │
       ▼
Amazon RDS MySQL

Monitoring: Kubernetes Metrics + Spring Boot Actuator + Node Exporter → Prometheus → Grafana
```

---

## Tech Stack

| Area | Tools |
|---|---|
| Infrastructure | Terraform, AWS VPC, IAM, Amazon EKS, Amazon ECR, Amazon RDS MySQL |
| CI/CD | GitHub Actions, Docker, Helm |
| Security | SonarQube, Trivy |
| Kubernetes | Deployments, Services, Ingress, ConfigMaps, Secrets, HPA |
| Backend | Spring Boot 3, Spring Data JPA, MySQL |
| Frontend | Static HTML/CSS/JS, served by nginx |
| Monitoring | Spring Boot Actuator, Prometheus, Grafana |

---

## Repository Structure

```text
.
├── app/                     # Spring Boot API (Farmers, Buyers, Produce, Orders)
├── frontend/                # Static AgriTech Marketplace site (catalog, cart, auth pages)
├── nginx/                   # nginx config: serves frontend, proxies /api/ to backend
├── helm/agrimarket/         # Helm chart for the API + frontend on EKS
├── terraform/                # AWS infrastructure as code
├── monitoring/               # Prometheus values and Grafana dashboard
├── .github/workflows/        # CI/CD pipeline
├── docs/                     # Diagrams and LinkedIn post
└── scripts/                  # Local helper scripts
```

---

## Domain Model

- **Farmers** create listings for **Produce** (crop, unit, price, quantity available, organic flag, harvest date).
- **Buyers** place **Orders** against produce listings; stock is decremented and the order total is computed server-side.
- Orders move through a status lifecycle: `PENDING → CONFIRMED → SHIPPED → DELIVERED` (or `CANCELLED`).

## Application APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/health` | Root-level health check (used by the container HEALTHCHECK) |
| GET | `/api/v1/health` | Versioned health response |
| GET / POST | `/api/v1/farmers` | List / create farmers |
| GET / PUT / DELETE | `/api/v1/farmers/{id}` | Read, update, or remove a farmer |
| GET / POST | `/api/v1/buyers` | List / create buyers |
| GET / PUT / DELETE | `/api/v1/buyers/{id}` | Read, update, or remove a buyer |
| GET / POST | `/api/v1/produce` | List / create produce (filter with `?category=` or `?farmerId=`) |
| GET / PUT / DELETE | `/api/v1/produce/{id}` | Read, update, or remove a produce listing |
| GET / POST | `/api/v1/orders` | List / create orders (filter with `?buyerId=` or `?farmerId=`) |
| PATCH | `/api/v1/orders/{id}/status` | Update an order's status |
| DELETE | `/api/v1/orders/{id}` | Cancel/remove an order |
| GET | `/actuator/prometheus` | Prometheus metrics endpoint |

Example — create a produce listing:

```json
POST /api/v1/produce
{
  "name": "Vermicompost — Premium Grade",
  "category": "potato",
  "unit": "10 kg bag",
  "pricePerUnit": 179.00,
  "quantityAvailable": 500,
  "organic": true,
  "harvestDate": "2026-06-01",
  "description": "Fully matured earthworm castings for root vegetables.",
  "farmerId": 1
}
```

Example — place an order:

```json
POST /api/v1/orders
{
  "buyerId": 1,
  "produceId": 1,
  "quantity": 5
}
```

> Note: the static frontend in `frontend/` ships with its own demo product catalog (`frontend/js/data.js`) and a localStorage-based cart/checkout, so it can be explored without the backend running. Swap `data.js` for calls to `/api/v1/produce` to wire the UI directly to the live API.

---

## Local Development

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker
- Docker Compose

### Run locally with Docker Compose

```bash
./scripts/local-up.sh
```

This starts three containers: MySQL, the Spring Boot API (`agritech-backend-svc`), and the nginx-served frontend.

Then open:

- Marketplace site: <http://localhost:8080/>
- API through nginx: <http://localhost:8080/api/v1/health>
- Spring Actuator: <http://localhost:8080/api/v1/health> (proxied) or directly on the API container
- Prometheus metrics: exposed on the API container at `/actuator/prometheus`

Stop local stack:

```bash
./scripts/local-down.sh
```

---

## AWS Deployment Overview

### 1. Configure GitHub Secrets

Add these secrets to your GitHub repository:

| Secret | Description |
|---|---|
| `AWS_ACCESS_KEY_ID` | AWS access key for CI/CD |
| `AWS_SECRET_ACCESS_KEY` | AWS secret key for CI/CD |
| `SONAR_TOKEN` | SonarQube/SonarCloud token |
| `SONAR_HOST_URL` | SonarQube server URL |
| `RDS_MASTER_PASSWORD` | RDS password |

Add these GitHub repository variables:

| Variable | Example |
|---|---|
| `AWS_REGION` | `ap-south-1` |
| `EKS_CLUSTER_NAME` | `agritech-marketplace-dev` |
| `ECR_REPOSITORY` | `agrimarket-api` |
| `ECR_FRONTEND_REPOSITORY` | `agrimarket-frontend` |

### 2. Provision Infrastructure

```bash
cd terraform/environments/dev
terraform init
terraform plan -var='db_password=CHANGE_ME'
terraform apply -var='db_password=CHANGE_ME'
```

This provisions VPC/IAM/EKS/RDS plus **two** ECR repositories — one for the backend API image, one for the frontend image.

### 3. Deploy with Helm

```bash
aws eks update-kubeconfig --region ap-south-1 --name agritech-marketplace-dev

helm upgrade --install agrimarket ./helm/agrimarket \
  --namespace agrimarket --create-namespace \
  --set image.repository=<account-id>.dkr.ecr.ap-south-1.amazonaws.com/agrimarket-api \
  --set image.tag=latest \
  --set nginx.image.repository=<account-id>.dkr.ecr.ap-south-1.amazonaws.com/agrimarket-frontend \
  --set nginx.image.tag=latest \
  --set database.host=<rds-endpoint> \
  --set secrets.databaseUsername=app_user \
  --set secrets.databasePassword=<password> \
  --set monitoring.serviceMonitor.enabled=true
```

The chart deploys the API and the frontend as separate Deployments/Services behind a shared Ingress, plus a fixed-name `agritech-backend-svc` Service so the hostname baked into the frontend's nginx config resolves correctly regardless of the Helm release name.

---

## Monitoring Installation

Install Prometheus and Grafana with kube-prometheus-stack:

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

helm upgrade --install monitoring prometheus-community/kube-prometheus-stack \
  --namespace monitoring --create-namespace \
  -f monitoring/prometheus/kube-prometheus-stack-values.yaml
```

Import the dashboard from:

```text
monitoring/grafana/dashboards/spring-boot-eks-dashboard.json
```

---

## CI/CD Pipeline

```text
Git Push
  │
  ▼
GitHub Actions
  ├── Checkout Code
  ├── SonarQube Analysis
  ├── Run Unit Tests
  ├── Terraform Apply AWS Infrastructure
  ├── Build Backend Image ── Trivy Scan ── Push to ECR
  ├── Build Frontend Image ── Trivy Scan ── Push to ECR
  └── Helm Upgrade to Amazon EKS (both images)
```

---

## Portfolio Highlights

- Real product domain (marketplace, not a to-do list) with a proper entity model and stock-aware ordering
- Complete DevSecOps lifecycle implementation across two deployable artifacts (API + static site)
- Real AWS-managed Kubernetes architecture
- Infrastructure as Code with reusable Terraform modules
- Secure build pipeline with code quality and image vulnerability scans for both images
- Helm-based Kubernetes deployment
- Prometheus scraping via Spring Boot Actuator
- Grafana dashboard for JVM, API, pod, CPU, and memory metrics

---

## LinkedIn Caption

See [`docs/linkedin-post.md`](docs/linkedin-post.md) for a polished post you can publish with this project.
