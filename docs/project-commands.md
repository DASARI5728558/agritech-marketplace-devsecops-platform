# Command Reference

## Local

```bash
./scripts/local-up.sh
curl http://localhost:8080/api/v1/health
curl http://localhost:8080/api/v1/produce
curl http://localhost:8080/api/v1/farmers
./scripts/local-down.sh
```

## Terraform

```bash
cd terraform/environments/dev
terraform init
terraform fmt -recursive
terraform validate
terraform plan -var='db_password=REPLACE_ME'
terraform apply -var='db_password=REPLACE_ME'
```

## EKS Access

```bash
aws eks update-kubeconfig --region ap-south-1 --name agritech-marketplace-dev
kubectl get nodes
```

## Helm Deploy

```bash
helm upgrade --install agrimarket helm/agrimarket \
  --namespace agrimarket --create-namespace \
  --set image.repository=<account>.dkr.ecr.ap-south-1.amazonaws.com/agrimarket-api \
  --set image.tag=latest \
  --set nginx.image.repository=<account>.dkr.ecr.ap-south-1.amazonaws.com/agrimarket-frontend \
  --set nginx.image.tag=latest \
  --set database.host=<rds-endpoint> \
  --set secrets.databaseUsername=app_user \
  --set secrets.databasePassword=<password> \
  --set monitoring.serviceMonitor.enabled=true
```

## Monitoring

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm upgrade --install monitoring prometheus-community/kube-prometheus-stack \
  --namespace monitoring --create-namespace \
  -f monitoring/prometheus/kube-prometheus-stack-values.yaml
```
