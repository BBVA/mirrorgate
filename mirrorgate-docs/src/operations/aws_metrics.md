# AWS metrics

MirrorGate provides an AWS collector that retrieves metrics about the number of requests, the number of healthy checks, 4XX and 5XX errors occurred in an ALB or the infrastructure cost of an Amazon Web Services account.

## AWS roles and policies needed

Since this collector is intended to gather information from different AWS Cloudwatch sources, we need to make sure that we have permission to access those sources. To make this information accessible for the collector, you need to create the following role on the AWS account where the resources to be monitored are located:

**delegated-cloudwatch-metrics-role**

with the following trust relationship:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": [
          "arn:aws:iam::{AWS_Account}:role/{role}",
          "arn:aws:iam::{AWS_Account}:user/{UserId}"
        ]
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```
and a policy that allows that role to access the following resources
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "apigateway:GET",
                "cloudwatch:getMetricStatistics",
                "elasticloadbalancing:DescribeLoadBalancers",
                "ce:GetCostAndUsage",
                "elasticloadbalancing:DescribeTargetGroups"
            ],
            "Resource": [
                "*"
            ]
        }
    ]
}
```
Note that the permissions can be fine grained to allow only queries to the methods and resources employed.
This allows the user or role used for running the code to impersonate a user in the receiving account and perform the queries to Cloudwatch.
- _{AWS_Account}_ : Refers to the Amazon account number from where the code of the AWS collector is being executed.
- _{role}_: Refers to the Amazon role used by the collector.
- _{UserId}_: Refers to the Amazon user id used by a user that wants to test the requests that the collector makes.

## How to configure it in the backoffice

The AWS collector will filter the results and will only take the ones that come with the *AWS/* prefix. The expected info from the GET endpoint
should follow this patterns:
```
AWS/{AWS_Account}
AWS/{AWS_Account}/{ALB_name}
```
