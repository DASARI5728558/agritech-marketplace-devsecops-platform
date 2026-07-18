resource "aws_db_subnet_group" "this" {
  name       = "${var.project_name}-${var.environment}-mysql-subnet-group"
  subnet_ids = var.private_subnets
}

resource "aws_security_group" "rds" {
  name        = "${var.project_name}-${var.environment}-rds-sg"
  description = "Allow MySQL access from EKS nodes"
  vpc_id      = var.vpc_id

  dynamic "ingress" {
    for_each = var.allowed_security_group_ids
    content {
      description     = "MySQL from EKS node security group"
      from_port       = 3306
      to_port         = 3306
      protocol        = "tcp"
      security_groups = [ingress.value]
    }
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_instance" "mysql" {
  identifier = "${var.project_name}-${var.environment}-mysql"

  engine         = "mysql"
  engine_version = "8.0"
  instance_class = "db.t3.micro"

  allocated_storage     = 20
  max_allocated_storage = 20
  storage_type          = "gp3"
  storage_encrypted     = true

  db_name  = var.database_name
  username = var.database_username
  password = var.database_password
  port     = 3306

  db_subnet_group_name   = aws_db_subnet_group.this.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  publicly_accessible    = false
  multi_az               = false

  backup_retention_period = 1
  backup_window           = "18:00-19:00"
  maintenance_window      = "sun:19:30-sun:20:30"

  deletion_protection = false
  skip_final_snapshot = true

  performance_insights_enabled = false

  tags = {
    Name = "${var.project_name}-${var.environment}-mysql"
  }
}
