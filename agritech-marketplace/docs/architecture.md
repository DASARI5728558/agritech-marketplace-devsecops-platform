# Project Architecture

## End-to-End Architecture

```mermaid
flowchart TD
    A[Developer] --> B[GitHub Repository]
    B --> C[GitHub Actions CI/CD Pipeline]

    C --> D[SonarQube Scan]
    C --> E[Maven Test]
    C --> F[Docker Build]
    C --> G[Terraform Apply]

    D --> H[Quality Gate]
    F --> I[Trivy Image Scan]
    G --> J[AWS VPC / IAM / EKS / RDS / ECR]

    H --> K[Amazon ECR]
    I --> K
    K --> L[Helm Deployment]
    L --> M[Amazon EKS Cluster]

    subgraph EKS[Amazon EKS Cluster]
        N[Ingress Controller]
        O[Frontend: nginx static site + /api proxy]
        P[Spring Boot API: Farmers / Buyers / Produce / Orders]
        Q[ConfigMaps & Secrets]
        N --> O --> P
        Q --> P
    end

    M --> EKS
    P --> R[Amazon RDS MySQL]

    P --> S[Spring Boot Actuator]
    T[Kubernetes Metrics] --> U[Prometheus]
    S --> U
    V[Node Exporter] --> U
    U --> W[Grafana]
    X[End Users] --> N
```

## Security Pipeline

```mermaid
flowchart TD
    A[Git Push] --> B[GitHub Actions]
    B --> C[SonarQube]
    C --> D[Code Quality Report]
    B --> E[Maven Tests]
    B --> F[Docker Build]
    F --> G[Trivy Scan]
    G --> H[Vulnerability Report]
    H --> I[Push to Amazon ECR]
    I --> J[Deploy to EKS]
```

## Monitoring Flow

```mermaid
flowchart TD
    A[Spring Boot Pods] --> B[Spring Boot Actuator]
    B --> C[/actuator/prometheus]
    D[Kubernetes Metrics] --> E[Prometheus]
    F[Node Exporter] --> E
    C --> E
    E --> G[Grafana]
    G --> H[CPU / Memory / Pods / Nodes / JVM / API Metrics]
```
