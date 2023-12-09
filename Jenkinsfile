def harborUrl = "harbor.zhch.lan"
def harborAuth = "7e2f3356-45b3-4d89-b727-967569d5c2ae"
def harborProject = "tensquare"

def gitUrl = "git@git.zhch.lan:test/tensquare_parent.git"
def gitAuth = "4488bfb8-2d68-4419-a79e-ccbb9928c3fe"

def projectName = "tensquare"
def version = new Date().format("yyyy.MMdd.HHmmss", TimeZone.getTimeZone('Asia/Shanghai'))
def workDir = "/root/jenkins/build"
def contextPath = "${workDir}/${projectName}/${version}"

node {
    //获取当前选择的微服务名称
    def selectedServices = "${SERVICE_NAME}".split(",")
    //获取当前选择的服务器名称
    def selectedHosts = "${HOST_NAME}".split(",")

    stage('Clone') {
        echo "Create contextPath: ${contextPath}"
        sh "mkdir -p ${contextPath}"
        dir("${contextPath}") {
            echo "Checkout start"
            checkout scmGit(branches: [[name: '*/${BRANCH_NAME}']], extensions: [], userRemoteConfigs: [[credentialsId: "${gitAuth}", url: "${gitUrl}"]])
            echo "Checkout done."
        }
    }

    // 小项目，直接审查整个项目的代码，没有只审查选中的模块
    stage('Check') {
        script {
            scannerHome = tool 'sonarqube-scanner-5.0.1'
        }
        dir("${contextPath}") {
            withSonarQubeEnv('sonarqube-9.9') {
                sh "${scannerHome}/bin/sonar-scanner"
            }
        }
    }

    stage('Build,Publish') {
        dir("${contextPath}") {
            // 安装父工程 -N,--non-recursive 表示不递归到子项目
            sh "mvn clean install -N"
            // 安装 common module
            sh "mvn -f tensquare_common clean install -Dmaven.test.skip=true"
        }

        // 登录 harbor
        withCredentials([usernamePassword(credentialsId: "$harborAuth", passwordVariable: 'PASSWD', usernameVariable: 'UNAME')]) {
            //sh "docker login -u $UNAME -p $PASSWD $harborUrl"
            sh "echo $PASSWD | docker login -u $UNAME --password-stdin $harborUrl"
        }

        for(int i=0; i<selectedServices.length; i++){
            def serviceInfo = selectedServices[i].split("#");
            //当前遍历的微服务名称
            def serviceName = serviceInfo[0]
            //当前遍历的微服务端口
            def servicePort = serviceInfo[1]

            def containerName = "${projectName}-${BRANCH_NAME}-${serviceName}"
            def image = "${harborUrl}/${harborProject}/${containerName}:${version}"

            dir("${contextPath}/${serviceName}"){
                sh "mvn clean package -Dmaven.test.skip=true"
                sh "mv target/*.jar target/app.jar"

                sh "docker build -t ${image} ."

                // 推送镜像
                sh "docker push ${image}"

                // 删除本地镜像
                sh "docker rmi ${image}"
            }

            // 部署
            for(int j=0; j<selectedHosts.length; j++){
                def host = selectedHosts[j]
                sshPublisher(publishers: [sshPublisherDesc(configName: "$host", transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/root/tensquare/deploy.sh ${harborUrl} ${image} ${containerName} ${servicePort} /root/${projectName}/${serviceName}/conf --spring.config.additional-location=/conf/additional.yml", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
   }

    stage("Clean") {
        echo "Start clean ..."
        sh "rm -rf ${contextPath}"
        sh "rm -rf ${contextPath}@tmp"
        echo "Clean done."
    }
}