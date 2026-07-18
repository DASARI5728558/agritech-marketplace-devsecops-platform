APP_DIR := app
TF_DIR := terraform/environments/dev
HELM_CHART := helm/agrimarket
NAMESPACE := agrimarket
RELEASE := agrimarket

.PHONY: help test build local-up local-down terraform-fmt terraform-validate helm-template

help:
	@echo "Available commands:"
	@echo "  make test                Run Maven tests"
	@echo "  make build               Build Spring Boot app"
	@echo "  make local-up            Start local Docker Compose stack"
	@echo "  make local-down          Stop local Docker Compose stack"
	@echo "  make terraform-fmt       Format Terraform files"
	@echo "  make terraform-validate  Validate Terraform environment"
	@echo "  make helm-template       Render Helm manifests locally"

test:
	mvn -f $(APP_DIR)/pom.xml clean test

build:
	mvn -f $(APP_DIR)/pom.xml clean package

local-up:
	./scripts/local-up.sh

local-down:
	./scripts/local-down.sh

terraform-fmt:
	terraform -chdir=terraform fmt -recursive

terraform-validate:
	terraform -chdir=$(TF_DIR) init
	terraform -chdir=$(TF_DIR) validate

helm-template:
	helm template $(RELEASE) $(HELM_CHART) --namespace $(NAMESPACE)
