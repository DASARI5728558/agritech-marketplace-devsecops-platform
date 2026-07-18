variable "cluster_name" {
  type = string
}

variable "cluster_version" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "private_subnets" {
  type = list(string)
}

variable "node_instance_types" {
  type = list(string)
}

variable "node_min_size" {
  type = number
}

variable "node_desired_size" {
  type = number
}

variable "node_max_size" {
  type = number
}
variable "ebs_csi_driver_role_arn" {
  description = "IAM role ARN for the EBS CSI driver service account (IRSA)"
  type        = string
}