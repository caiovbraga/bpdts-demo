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

  agent any

  stages {
        stage ('Build Image fast-api') {
            steps {
                script {                   
                    appImage = docker.build("${registry}${app}", "./fast-api")
                }
            }
        }

        stage ('Maven package') {
            steps {
                script {
                     withMaven( maven: 'maven-3' ) {
                        sh 'cd java-api/java-api && mvn -B -DskipTests clean package'
                     }
                 }
             }
        }
                
        stage ('Build Image java-api') {
            steps {
                script {                   
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