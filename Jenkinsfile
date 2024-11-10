pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    stages {
        stage('Checkout Git repository') {
            steps {
                echo 'Pulling Git repository'
                git branch: 'SaadaouiWalid-5ERPBI4-G4', url: 'https://github.com/aziz-hajjem/5ERPBI4-G4-KADDEM.git'
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
                       -Dsonar.password=Walid1@sdaaa \
                       -Dsonar.exclusions="src/main/java/tn/esprit/spring/kaddem/entities/Equipe.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/DetailEquipe.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Etudiant.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Universite.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/UniversiteRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Contrat.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Departement.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/ContratRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/DepartementRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/DepartementServiceImpl.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/EquipeRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/EquipeServiceImpl.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/EtudiantRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/EtudiantServiceImpl.java,
                       src/main/java/tn/esprit/spring/kaddem/KaddemApplication.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Niveau.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Option.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Contrat.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/ContratRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/ContratServiceImpl.java"
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
                    def dockerImage = docker.build("walid259/kaddem:0.0.1")
                }
            }
        }

        stage('Deploy Image to DockerHub') {
            steps {
                script {
                    echo 'Logging into DockerHub and Pushing Image'
                    sh 'docker login -u walid259 -p walidsda1'
                    sh 'docker push walid259/kaddem:0.0.1'
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
            echo 'Sending success email...'
            mail to: 'khmiriiheb3@gmail.com',
                 subject: "Pipeline Jenkins - Success - Build #${BUILD_NUMBER}",
                 body: """<html>
                            <body>
                                <h2 style="color: #4CAF50;">Khmiri_Iheb_5ERPBI4 Build ${BUILD_NUMBER}</h2>
                                <div style="border: 2px solid #4CAF50; padding: 10px;">
                                    <h3 style="background-color: #4CAF50; color: white; padding: 10px; text-align: center;">
                                        Pipeline Status: SUCCESS
                                    </h3>
                                    <p>Check the <a href="${BUILD_URL}console">console output</a> for more details.</p>
                                </div>
                            </body>
                          </html>""",
                 mimeType: 'text/html'
        }
        failure {
            echo 'Sending failure email...'
            mail to: 'khmiriiheb3@gmail.com',
                 subject: "Pipeline Jenkins - Failure - Build #${BUILD_NUMBER}",
                 body: """<html>
                            <body>
                                <h2 style="color: #D32F2F;">Khmiri_Iheb_5ERPBI4 Build ${BUILD_NUMBER}</h2>
                                <div style="border: 2px solid #D32F2F; padding: 10px;">
                                    <h3 style="background-color: #D32F2F; color: white; padding: 10px; text-align: center;">
                                        Pipeline Status: FAILURE
                                    </h3>
                                    <p>Check the <a href="${BUILD_URL}console">console output</a> for more details.</p>
                                </div>
                            </body>
                          </html>""",
                 mimeType: 'text/html'
        }
}