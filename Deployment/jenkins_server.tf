provider "aws" {
  region = "ap-southeast-1"
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
  key_name = "jenkins_server_keys"
  monitoring = false
  security_groups = [
    "sg-0a8342b765494da31"]
  source_dest_check = true
  subnet_id = "subnet-63d2112b"
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

