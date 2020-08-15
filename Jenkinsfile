pipeline { 

  parameters {
      string(name: 'VERSION', defaultValue: 'version', description: 'Application version')
  }
  environment {
            registryUrl = "https://docker.pkg.github.com"
            registry = "caiovbraga/bpdts-demo/"
            app = "fast-api"
            registryCredentials = 'github-token'
            appImage = ''
  }

  agent any

  stages {
        stage ('Build Image') {
            steps {
                script {
                      appImage = docker.build("${registry}${app}", "./fast-api")
                }
            }
        }

        stage ('Push to Registry') {
            steps {
                script {
                      docker.withRegistry(registryUrl, registryCredentials) {
                          appImage.push("${params.VERSION}-build_${env.BUILD_NUMBER}")
                      }
                }
            }
        }

  }

}