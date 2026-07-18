terraform {
  required_version = ">= 1.7.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  # Recommended for real usage:
  # backend "s3" {
  #   bucket         = "your-terraform-state-bucket"
  #   key            = "agrimarket/dev/terraform.tfstate"
  #   region         = "ap-south-1"
  #   dynamodb_table = "terraform-locks"
  #   encrypt        = true
  # }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = local.tags
  }
}

data "aws_availability_zones" "available" {
  state = "available"
}

locals {
  project      = var.project_name
  environment  = var.environment
  cluster_name = "${local.project}-${local.environment}"

  azs = slice(data.aws_availability_zones.available.names, 0, 3)

  tags = {
    Project     = local.project
    Environment = local.environment
    ManagedBy   = "Terraform"
    Owner       = "AgriTechMarketplace"
  }
}

module "network" {
  source = "../../modules/network"

  project_name = local.project
  environment  = local.environment
  vpc_cidr     = var.vpc_cidr
  azs          = local.azs
}

module "ecr" {
  source = "../../modules/ecr"

  repository_name = var.ecr_repository_name
  scan_on_push    = true
}

module "ecr_frontend" {
  source = "../../modules/ecr"

  repository_name = var.ecr_frontend_repository_name
  scan_on_push    = true
}

module "eks" {
  source = "../../modules/eks"

  cluster_name    = local.cluster_name
  cluster_version = var.eks_cluster_version
  vpc_id          = module.network.vpc_id
  private_subnets = module.network.private_subnets

  node_instance_types     = var.node_instance_types
  node_min_size           = var.node_min_size
  node_desired_size       = var.node_desired_size
  node_max_size           = var.node_max_size
  ebs_csi_driver_role_arn = aws_iam_role.ebs_csi_driver.arn
}

module "rds" {
  source = "../../modules/rds"

  project_name               = local.project
  environment                = local.environment
  vpc_id                     = module.network.vpc_id
  vpc_cidr                   = var.vpc_cidr
  private_subnets            = module.network.private_subnets
  allowed_security_group_ids = [module.eks.node_security_group_id]
  database_name              = var.database_name
  database_username          = var.database_username
  database_password          = var.db_password
  instance_class             = var.rds_instance_class
  allocated_storage          = var.rds_allocated_storage
  multi_az                   = var.rds_multi_az

  depends_on = [module.eks]
}
data "aws_iam_policy_document" "ebs_csi_driver_assume_role" {
  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRoleWithWebIdentity"]

    principals {
      type        = "Federated"
      identifiers = [module.eks.oidc_provider_arn]
    }

    condition {
      test     = "StringEquals"
      variable = "${module.eks.oidc_provider}:sub"
      values   = ["system:serviceaccount:kube-system:ebs-csi-controller-sa"]
    }

    condition {
      test     = "StringEquals"
      variable = "${module.eks.oidc_provider}:aud"
      values   = ["sts.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "ebs_csi_driver" {
  name               = "AmazonEKS_EBS_CSI_DriverRole"
  assume_role_policy = data.aws_iam_policy_document.ebs_csi_driver_assume_role.json
}

resource "aws_iam_role_policy_attachment" "ebs_csi_driver_attach" {
  role       = aws_iam_role.ebs_csi_driver.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy"
}

