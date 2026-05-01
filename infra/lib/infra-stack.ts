import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as ecr from 'aws-cdk-lib/aws-ecr';
import * as efs from 'aws-cdk-lib/aws-efs';
import * as elbv2 from 'aws-cdk-lib/aws-elasticloadbalancingv2';
import * as iam from 'aws-cdk-lib/aws-iam';

export class InfraStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        // Use default VPC
        const vpc = ec2.Vpc.fromLookup(this, 'DefaultVPC', {
            isDefault: true,
        });

        const fileSystem = new efs.FileSystem(this, 'EmployeesEfs', {
            vpc,
        });

        const efsSecurityGroup = fileSystem.connections.securityGroups[0];

        const taskDefinition = new ecs.FargateTaskDefinition(this, 'TaskDef', {
            cpu: 512,
            memoryLimitMiB: 1024,
        });

        const accessPoint = fileSystem.addAccessPoint('AccessPoint', {
            path: '/data',
            createAcl: {
                ownerUid: '1000',
                ownerGid: '1000',
                permissions: '750',
            },
            posixUser: {
                uid: '1000',
                gid: '1000',
            },
        });

        taskDefinition.addVolume({
            name: 'postgres-data',
            efsVolumeConfiguration: {
                fileSystemId: fileSystem.fileSystemId,
                authorizationConfig: {
                    accessPointId: accessPoint.accessPointId,
                    iam: 'ENABLED',
                },
                transitEncryption: 'ENABLED',
            },
        });

        taskDefinition.addToTaskRolePolicy(new iam.PolicyStatement({
            actions: [
                'elasticfilesystem:ClientMount',
                'elasticfilesystem:ClientWrite',
                'elasticfilesystem:ClientRootAccess',
            ],
            resources: ['*'],
        }));

        const postgresContainer = taskDefinition.addContainer('Postgres', {
            image: ecs.ContainerImage.fromRegistry('postgres:16'),
            environment: {
                POSTGRES_DB: 'employees',
                POSTGRES_USER: 'user',
                POSTGRES_PASSWORD: 'password',
                PGDATA: '/var/lib/postgresql/data/pgdata',
            },
            logging: ecs.LogDrivers.awsLogs({ streamPrefix: 'postgres' }),
            healthCheck: {
                command: ['CMD-SHELL', 'pg_isready -U user'],
            },
            user: '1000:1000', // 🔥 CLAVE
        });

        postgresContainer.addPortMappings({
            containerPort: 5432,
        });

        postgresContainer.addMountPoints({
            containerPath: '/var/lib/postgresql/data',
            sourceVolume: 'postgres-data',
            readOnly: false,
        });

        // Existing ECR repository
        const repo = ecr.Repository.fromRepositoryName(
            this,
            'EmployeesRepo',
            'employees-api'
        );

        const imageTag = this.node.tryGetContext('imageTag') || 'latest';

        const appContainer = taskDefinition.addContainer('App', {
            image: ecs.ContainerImage.fromEcrRepository(repo, imageTag),
            logging: ecs.LogDrivers.awsLogs({ streamPrefix: 'app' }),

            environment: {
                SPRING_DATASOURCE_URL: 'jdbc:postgresql://localhost:5432/employees',
                SPRING_DATASOURCE_USERNAME: 'user',
                SPRING_DATASOURCE_PASSWORD: 'password',
            },
        });

        appContainer.addPortMappings({
            containerPort: 8080,
        });

        appContainer.addContainerDependencies({
            container: postgresContainer,
            condition: ecs.ContainerDependencyCondition.HEALTHY,
        });

        // ECS Cluster
        const cluster = new ecs.Cluster(this, 'EmployeesCluster', {
            vpc,
            clusterName: 'employees-cdk-cluster',
        });

        const fargateService = new ecs.FargateService(this, 'Service', {
            cluster,
            taskDefinition,
            desiredCount: 1,
            assignPublicIp: true,
            vpcSubnets: {
                subnetType: ec2.SubnetType.PUBLIC,
            },
            serviceName: 'employees-cdk-service',
        });

        efsSecurityGroup.addIngressRule(
            fargateService.connections.securityGroups[0],
            ec2.Port.tcp(2049),
            'Allow ECS to access EFS'
        );


        const lb = new elbv2.ApplicationLoadBalancer(this, 'LB', {
            vpc,
            internetFacing: true,
        });

        const listener = lb.addListener('Listener', {
            port: 80,
        });

        listener.addTargets('ECS', {
            port: 80,
            targets: [
                fargateService.loadBalancerTarget({
                    containerName: 'App',
                    containerPort: 8080,
                }),
            ],
            healthCheck: {
                path: '/employees',
            },
        });

        // Allow outbound HTTPS to ECR
        fargateService.connections.allowToAnyIpv4(
            ec2.Port.tcp(443),
            'Allow HTTPS outbound to ECR'
        );

        // Allow public HTTP traffic
        lb.connections.allowFromAnyIpv4(
            ec2.Port.tcp(80),
            'Allow HTTP access'
        );
    }
}