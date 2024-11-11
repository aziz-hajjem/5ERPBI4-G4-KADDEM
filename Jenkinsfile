pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    stages {
        stage('Checkout Git repository') {
            steps {
                echo 'Pulling Git repository'
                git branch: 'HouriMedAnis-5ERPBI4-G4', url: 'https://github.com/aziz-hajjem/5ERPBI4-G4-KADDEM.git'
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
                       -Dsonar.password=Dzenkinson12@ \
                       -Dsonar.exclusions="src/main/java/tn/esprit/spring/kaddem/entities/Contrat.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/DetailContrat.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/DetailEquipe.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Etudiant.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Equipe.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Contrat.java,
                       src/main/java/tn/esprit/spring/kaddem/entities/Departement.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/ContratRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/EquipeRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/DepartementRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/DepartementServiceImpl.java,
                       src/main/java/tn/esprit/spring/kaddem/controllers/ContratRestController.java,
                       src/main/java/tn/esprit/spring/kaddem/services/ContratServiceImpl.java,
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
                    def dockerImage = docker.build("hourianis50/kaddem:0.0.1")
                }
            }
        }

        stage('Deploy Image to DockerHub') {
            steps {
                script {
                    echo 'Logging into DockerHub and Pushing Image'
                    sh 'docker login -u hourianis50 -p Dzenkinson12'
                    sh 'docker push hourianis50/kaddem:0.0.1'
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
    //hello azouz
     post {
    success {
        mail to: 'mohamedanis.houri@esprit.tn',
            subject: "✅ Jenkins Pipeline Success - Build #${BUILD_NUMBER}",
            mimeType: 'text/html',
            body: """
            <html>
            <body>
                <h2 style="color: #4CAF50;">Jenkins Pipeline - Success</h2>
                <p>Dear Mohamed Anis,</p>
                <p>The Jenkins pipeline has completed successfully.</p>
                <table style="border-collapse: collapse; width: 50%;">
                    <tr>
                        <td style="border: 1px solid #ddd; padding: 8px;"><strong>Build Number</strong></td>
                        <td style="border: 1px solid #ddd; padding: 8px;">${BUILD_NUMBER}</td>
                    </tr>
                    <tr>
                        <td style="border: 1px solid #ddd; padding: 8px;"><strong>Status</strong></td>
                        <td style="border: 1px solid #ddd; padding: 8px;">Success</td>
                    </tr>
                </table>
                <p>No further action is required.</p>
                <p>Best regards,<br>Jenkins CI/CD System</p>
            </body>
            </html>
            """
    }
    failure {
        mail to: 'mohamedanis.houri@esprit.tn',
            subject: "❌ Jenkins Pipeline Failure - Build #${BUILD_NUMBER}",
            mimeType: 'text/html',
            body: """
            <html>
            <body>
                <h2 style="color: #f44336;">Jenkins Pipeline - Failure</h2>
                <p>Dear Mohamed Anis,</p>
                <p>The Jenkins pipeline has encountered an error and failed.</p>
                <table style="border-collapse: collapse; width: 50%;">
                    <tr>
                        <td style="border: 1px solid #ddd; padding: 8px;"><strong>Build Number</strong></td>
                        <td style="border: 1px solid #ddd; padding: 8px;">${BUILD_NUMBER}</td>
                    </tr>
                    <tr>
                        <td style="border: 1px solid #ddd; padding: 8px;"><strong>Status</strong></td>
                        <td style="border: 1px solid #ddd; padding: 8px;">Failure</td>
                    </tr>
                </table>
                <p>Please check the Jenkins logs for details and take the necessary actions.</p>
                <p>Best regards,<br>Jenkins CI/CD System</p>
            </body>
            </html>
            """
    }
    always {
        echo 'Pipeline completed.'
    }
}

}