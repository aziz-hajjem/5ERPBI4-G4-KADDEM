pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    stages {
        stage('Checkout Git repository') {
            steps {
                echo 'Pulling Git repository'
                git branch: 'khmiriiheb-5BI4-G1', url: 'https://github.com/ihebkh/5BI4-G1-Kaddem.git'
            }
        }

        stage('Maven Clean Compile') {
            steps {
                echo 'Running Maven Clean and Compile'
                sh 'mvn clean compile'
            }
        }

        stage('Maven Install') {
            steps {
                echo 'Running Maven Install'
                sh 'mvn install'
            }
        }

        stage('Build Package') {
            steps {
                echo 'Running Maven Package'
                sh 'mvn package'
            }
        }

        stage('Tests - JUnit/Mockito') {
            steps {
                echo 'Running Tests'
                sh 'mvn test'
            }
        }

        stage('Generate JaCoCo Report') {
            steps {
                echo 'Generating JaCoCo Report'
                sh 'mvn jacoco:report'
            }
        }

        stage('JaCoCo Coverage Report') {
            steps {
                echo 'Publishing JaCoCo Coverage Report'
                step([$class: 'JacocoPublisher',
                      execPattern: '**/target/jacoco.exec',
                      classPattern: '**/classes',
                      sourcePattern: '**/src',
                      exclusionPattern: '/target/**/,**/*Test,**/*_javassist/**'
                ])
            }
        }

       stage('SonarQube Analysis') {
           steps {
               sh '''
                   mvn sonar:sonar \
                       -Dsonar.host.url=http://192.168.33.10:9000 \
                       -Dsonar.login=admin \
                       -Dsonar.password=201JmT1896@@ \
                       -Dsonar.exclusions="src/main/java/tn/esprit/spring/kaddem/entities/Equipe.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/DetailEquipe.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Etudiant.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Departement.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/DepartementRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/DepartementServiceImpl.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/EquipeRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/EquipeServiceImpl.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/EtudiantRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/EtudiantServiceImpl.java,
                       src/main/java/tn/esprit/spring/kaddem/KaddemApplication.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Niveau.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Option.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Universite.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/UniversiteRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/UniversiteServiceImpl.java"
               '''
           }
       }


        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus Repository'
                sh 'mvn clean deploy -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker Image'
                    def dockerImage = docker.build("ihebkh336/kaddem:0.0.1")
                }
            }
        }

        stage('Deploy Image to DockerHub') {
            steps {
                script {
                    echo 'Logging into DockerHub and Pushing Image'
                    sh 'docker login -u ihebkh336 -p a1b2c3IHEB'
                    sh 'docker push ihebkh336/kaddem:0.0.1'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    echo 'Deploying with Docker Compose'
                    sh 'docker-compose up -d'
                }
            }
        }


    }
    post {
        success {
            mail to: 'khmiriiheb3@gmail.com',
                 subject: "Pipeline Jenkins - Success - Build #${BUILD_NUMBER}",
                 body: """Pipeline Jenkins

                 Final Report: The pipeline has completed successfully. Build number: ${BUILD_NUMBER}. No action required."""
        }
        failure {
            mail to: 'khmiriiheb3@gmail.com',
                 subject: "Pipeline Jenkins - Failure - Build #${BUILD_NUMBER}",
                 body: """Pipeline Jenkins

                 Final Report: The pipeline has failed. Build number: ${BUILD_NUMBER}. Please check the logs and take necessary actions."""
        }
        always {
            echo 'Pipeline completed.'
        }
    }
}