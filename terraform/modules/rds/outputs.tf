output "db_endpoint" {
  value = aws_db_instance.mysql.address
}

output "db_port" {
  value = aws_db_instance.mysql.port
}

output "security_group_id" {
  value = aws_security_group.rds.id
}
