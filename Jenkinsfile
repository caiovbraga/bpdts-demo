pipeline { 

  parameters {
      string(name: 'VERSION', defaultValue: 'version', description: 'Application version')
  }
  environment {
            registryUrl = "https://docker.pkg.github.com"
            registry = "caiovbraga/bpdts-demo/"
            app = "fast-api"
            app2 = "java-api"
            registryCredentials = 'github-token'
            appImage = ''
            appImage2 = ''
  }

  agent {
    docker {
        image 'maven:3-alpine' 
        args '-v /root/.m2:/root/.m2' 
    }
  }

  stages {
        stage ('Build Image 1') {
            steps {
                script {                   
                    appImage = docker.build("${registry}${app}", "./fast-api")
                }
            }
        }

        stage ('Build Image 2') {
            steps {
                script {                   
                    sh 'cd java-api/java-api && mvn -B -DskipTests clean package'
                    appImage2 = docker.build("${registry}${app2}", "./java-api")
                }
            }
        }

        stage ('Push to Registry') {
            steps {
                script {
                      docker.withRegistry(registryUrl, registryCredentials) {
                          appImage.push("${params.VERSION}-build_${env.BUILD_NUMBER}")
                          appImage2.push("${params.VERSION}-build_${env.BUILD_NUMBER}")
                      }
                }
            }
        }

  }

}