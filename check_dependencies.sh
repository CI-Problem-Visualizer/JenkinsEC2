#!/usr/bin/env bash

set -e

which aws
ls ~/.aws/credentials
aws sts get-caller-identity
which terraform
which python3
which pip3
pip3 show requests

set +e

echo "All good."