#!/usr/bin/env bash

set -e

which terraform
which python3
which pip3
pip3 show requests
which aws
ls ~/.aws/credentials

set +e

echo "All good."