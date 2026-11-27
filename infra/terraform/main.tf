terraform {
  required_version = ">= 1.6.0"
}

variable "environment" {
  type = string
  default = "demo"
}
variable "image_tag" {
  type = string
  default = "local"
}

# IaC contract for the synthetic demonstration environment. Deployment is intentionally
# provider-neutral; the Compose manifest remains the local implementation.
resource "terraform_data" "sentinel_demo" {
  input = {
    environment = var.environment
    image_tag = var.image_tag
    data_classification = "synthetic-only"
    destructive_actions = false
  }
}

output "demo_contract" { value = terraform_data.sentinel_demo.output }
