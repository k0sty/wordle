# Wordle::Web_Service

A sub-project which contains an AWS Lambda Handler for offering REST of WorldSolver.

If scripts are run successfully, a Lambda function will exist.  (It is then up to the user to manually expose via API GW or other.)

This sub-project derived from repo [aws-lambda-developer-guide](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic)

## UI / HTML

The directory [static_html](static_html) has html+JS+CSS for invoking the endpoint and displaying results.  Note that the Lambda function's `Access-Control-Allow-Origin` header value is curerntly `*`.

## Symlinking

This web_service is a plan spring-web project, but uses symlinks to particular packages and resources in the base project (i.e. trie, utils, and word list files in `/resources`)

## SingletonSteward

This is a wrapping class to prevent multiple instances of Wordle functional classes are not created, and that resource files are read+parsed only once (on spring bootRun etc.).

## Scripts

- `1-create-bucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.
- `4-cleanup.sh` has not been tested

### Requirements

- [Java 8 runtime environment (SE JRE)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Gradle 5](https://gradle.org/releases/) or [Maven 3](https://maven.apache.org/docs/history.html)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) v1.17 or newer.

And for AWS CLI, the permissions/policy of the identify/resource (e.g. if this is deployed from an EC2 devOps server), must have the following permissions as a minimal set for the scripts `1-create-bucket.sh`, `2-deploy.sh`:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "cloudformation:Create*",
                "cloudformation:Describe*",
                "cloudformation:ExecuteChangeSet",
                "cloudformation:Get*",
                "s3:CreateBucket",
                "s3:GetObject",
                "s3:PutObject",
                "iam:CreateRole",
                "iam:AttachRolePolicy",
                "iam:GetRole",
                "iam:DeleteRole",
                "iam:DetachRolePolicy",
                "iam:PassRole",
                "lambda:CreateFunction",
                "lambda:DeleteFunction",
                "lambda:GetFunction",
                "lambda:UpdateFunctionCode",
                "lambda:UpdateFunctionConfiguration"
            ],
            "Resource": "*"
        }
    ]
}
```