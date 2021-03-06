# BPDTS demo
The purpose of this project is to demonstrate how I would address the task described [here](http://bpdts-test-app-v4.herokuapp.com/instructions).

## Task
Build an API which calls this API, and returns people who are listed as either living in London, or whose current coordinates are within 60 miles of London.

---
## Python
Source code can be found in `fast-api/main.py`. This single file REST server is my proposal to address the issue described in the task.

### Docker
`fast-api/Dockerfile` is used to build a custom image based on the official python3 image, installs the required packages and copy the application code.

---
## Java
Source code can be found in `java-api/java-api`. SpringBoot REST server.

### Docker
`java-api/Dockerfile` is used to build a custom image based on Openjdk 11.

---
## Deployment

### Ansible
An ansible playbook under `ansible/` is used to copy a docker compose stack in the target host, build the application images, create 2 container replicas for each service and start the services.


### Jenkins
Also a `Jenkinsfile` is used to build and push a container image to the github repository. However, this image is not used in the example deployment as Ansible builds the image locally.

### Kubernetes
Under directory `kubernetes/` there is a manifest file for the deployment of the fast-api image in a kubernetes cluster, the deployment uses the image built in Jenkins. Deployment not used in the example.

### Example
A running version of this code can be found in the links below

https://fast-api.cvbtechnology.com/docs

* **/london** : Get London Users
* **/within-london-radius** : Get Users within 60 mile radius from London

*_Original endpoints also exposed ( /instructions, /users, /city ... )_

https://java-bpdts.cvbtechnology.com/

* **/london** : Get London Users
* **/within-london-radius** : Get Users within 60 mile radius from London



The diagram below is a representation of how the example is set up in the target host.

The application is deployed in a multi-purpose server. The traefik container is a proxy server used to expose both the example but it's covered in this repo.

![deployment](./bpdts_demo.jpg)

---
## Notes
This project is a simple illustration of microservice deployment based on the task given.

In a production scenario a good strategy would be a managed Kubernetes cluster by a major cloud provider such as Amazon AWS or Azure, if budget allows.

I believe Kubernetes makes it easier to scale systems and a major provider gives you the high availability needed without the need to manually configure and manage a multi-master cluster.

Using AWS as an example, I would write Terraform scripts to set up the underling infrastructure (VPC, EKS, etc.). Make sure security groups only exposes ports needed by the services, isolate instances in private subnets. On the application side, set up TLS encryption between services, check firewalls rules, etc

