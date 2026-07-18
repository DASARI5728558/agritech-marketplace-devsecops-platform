# Terraform Infrastructure

This Terraform code provisions the AWS foundation for the AgriTech Marketplace project:

- VPC with public/private subnets
- Two Amazon ECR repositories (backend API image + frontend image)
- Amazon EKS cluster with managed node groups
- Amazon RDS MySQL database
- Security groups for EKS-to-RDS connectivity

> Keep `*.tfvars` and state files out of Git. Use an S3 backend with DynamoDB locking for team usage.
