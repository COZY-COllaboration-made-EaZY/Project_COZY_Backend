#!/usr/bin/env sh
set -e

if [ -z "$AWS_S3_BUCKET" ]; then
  echo "AWS_S3_BUCKET is not set"
  exit 1
fi

DEFAULT_KEY="${DEFAULT_PROFILE_IMAGE_KEY:-profile_images/Default_Profile.png}"
SOURCE_PATH="${1:-./assets/Default_Profile.png}"

if [ ! -f "$SOURCE_PATH" ]; then
  echo "Default image file not found: $SOURCE_PATH"
  exit 1
fi

aws s3 cp "$SOURCE_PATH" "s3://$AWS_S3_BUCKET/$DEFAULT_KEY" --content-type image/png
echo "Uploaded to s3://$AWS_S3_BUCKET/$DEFAULT_KEY"
