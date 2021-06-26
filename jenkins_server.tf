variable "aws_region_id" {
  type = string
  description = "The name of the AWS region where everything referenced in this file is expected to exist, and where everything new will be created."
}

variable "ssh_key_name" {
  type = string
  description = "The name of the EC2 key pair that can be used to SSH into the instance"
}

variable "security_group_id" {
  type = string
  description = "The id of the EC2 security group that'll be applied to the EC2 instance"
}

variable "subnet_id" {
  type = string
  description = "The id of the AWS subnet that the EC2 instance will run in"
}

provider "aws" {
  region = var.aws_region_id
}

# aws_iam_role.ec2_ssm_instance_profile:
resource "aws_iam_role" "ec2_ssm_role" {
  name = "ec2_ssm_role"
  force_detach_policies = true
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": {
    "Effect": "Allow",
    "Principal": {"Service": "ec2.amazonaws.com"},
    "Action": "sts:AssumeRole"
  }
}
EOF
}

# aws_iam_role_policy_attachment.ec2_ssm_attach:
resource "aws_iam_role_policy_attachment" "ec2_ssm_policy_attach" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
  role = aws_iam_role.ec2_ssm_role.name
}

# aws_iam_instance_profile.ec2_ssm_instance_profile:
resource "aws_iam_instance_profile" "ec2_ssm_instance_profile" {
  name = "ec2_ssm_instance_profile"
  role = aws_iam_role.ec2_ssm_role.name
}

# aws_instance.jenkins_server:
resource "aws_instance" "jenkins_server" {
  ami = "ami-093da183b859d5a4b"
  iam_instance_profile = aws_iam_instance_profile.ec2_ssm_instance_profile.name
  user_data = ""
  associate_public_ip_address = true
  disable_api_termination = false
  ebs_optimized = false
  get_password_data = false
  hibernation = false
  instance_type = "t2.small"
  ipv6_address_count = 0
  ipv6_addresses = []
  key_name = var.ssh_key_name
  monitoring = false
  security_groups = [
    var.security_group_id
  ]
  source_dest_check = true
  subnet_id = var.subnet_id
  tags = {}
  tenancy = "default"
  volume_tags = {}

  credit_specification {
    cpu_credits = "standard"
  }

  metadata_options {
    http_endpoint = "enabled"
    http_put_response_hop_limit = 1
    http_tokens = "optional"
  }

  root_block_device {
    delete_on_termination = true
    encrypted = false
    volume_size = 8
    volume_type = "gp2"
  }

  timeouts {}
}

output "jenkins_server_ip" {
  value = aws_instance.jenkins_server.public_ip
}

