import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as ecs_patterns from 'aws-cdk-lib/aws-ecs-patterns';
import * as ecr from 'aws-cdk-lib/aws-ecr';

export class InfraStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        // Use default VPC
        const vpc = ec2.Vpc.fromLookup(this, 'DefaultVPC', {
            isDefault: true,
        });

        // ECS Cluster
        const cluster = new ecs.Cluster(this, 'EmployeesCluster', {
            vpc,
            clusterName: 'employees-cdk-cluster',
        });

        // Existing ECR repository
        const repo = ecr.Repository.fromRepositoryName(
            this,
            'EmployeesRepo',
            'employees-api'
        );

        // Fargate Service with Load Balancer
        const fargateService = new ecs_patterns.ApplicationLoadBalancedFargateService(
            this,
            'EmployeesService',
            {
                cluster,
                cpu: 256,
                memoryLimitMiB: 512,
                desiredCount: 1,
                publicLoadBalancer: true,
                serviceName: 'employees-cdk-service',

                taskSubnets: {
                    subnetType: ec2.SubnetType.PUBLIC,
                },

                assignPublicIp: true,

                taskImageOptions: {
                    image: ecs.ContainerImage.fromEcrRepository(repo, 'latest'),
                    containerPort: 8080,
                    enableLogging: true,
                },
            }
        );

        fargateService.targetGroup.configureHealthCheck({
            path: '/employees',
            healthyHttpCodes: '200',
        });

        // Allow outbound HTTPS to ECR
        fargateService.service.connections.allowToAnyIpv4(
            ec2.Port.tcp(443),
            'Allow HTTPS outbound to ECR'
        );

        // Allow public HTTP traffic
        fargateService.loadBalancer.connections.allowFromAnyIpv4(
            ec2.Port.tcp(80),
            'Allow HTTP access'
        );
    }
}