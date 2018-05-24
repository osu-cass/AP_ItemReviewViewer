#!/bin/bash
set -ev

cd docker/
docker build -f Dockerfile.code -t osucass/ap_itemreviewviewer:$BRANCH .
docker push osucass/ap_itemreviewviewer:$BRANCH
