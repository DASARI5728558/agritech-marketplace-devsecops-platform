variable "aws_region" {
  description = "AWS region for all resources."
  type        = string
  default     = "ap-south-1"
}

variable "project_name" {
  description = "Project name used in resource naming."
  type        = string
  default     = "agritech-marketplace"
}

variable "environment" {
  description = "Environment name."
  type        = string
  default     = "dev"
}

variable "vpc_cidr" {
  description = "CIDR range for the VPC."
  type        = string
  default     = "10.20.0.0/16"
}

variable "ecr_repository_name" {
  description = "ECR repository name for the backend API image."
  type        = string
  default     = "agrimarket-api"
}

variable "ecr_frontend_repository_name" {
  description = "ECR repository name for the static frontend image."
  type        = string
  default     = "agrimarket-frontend"
}

variable "eks_cluster_version" {
  description = "EKS Kubernetes version."
  type        = string
  default     = "1.30"
}

variable "node_instance_types" {
  description = "EC2 instance types for EKS managed node group."
  type        = list(string)
  default     = ["t3.micro"]
}

variable "node_min_size" {
  description = "Minimum EKS node count."
  type        = number
  default     = 2
}

variable "node_desired_size" {
  description = "Desired EKS node count."
  type        = number
  default     = 2
}

variable "node_max_size" {
  description = "Maximum EKS node count."
  type        = number
  default     = 4
}

variable "database_name" {
  description = "RDS MySQL database name."
  type        = string
  default     = "agrimarket"
}

variable "database_username" {
  description = "RDS MySQL app username."
  type        = string
  default     = "app_user"
}

variable "db_password" {
  description = "RDS MySQL password. Pass via GitHub secret or local tfvars."
  type        = string
  sensitive   = true
}

variable "rds_instance_class" {
  description = "RDS instance class."
  type        = string
  default     = "db.t3.micro"
}

variable "rds_allocated_storage" {
  description = "RDS allocated storage in GB."
  type        = number
  default     = 20
}

variable "rds_multi_az" {
  description = "Enable Multi-AZ RDS."
  type        = bool
  default     = false
}
