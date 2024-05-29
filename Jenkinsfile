pipeline {
    agent any
    parameters {
                activeChoice choiceType: 'PT_MULTI_SELECT', description: 'Please Select Your Test Case', filterLength: 1, filterable: true, name: 'TestCase', randomName: 'choice-parameter-3185416231415200', script: scriptlerScript(isSandboxed: true, scriptlerBuilder: [builderId: '1716130847879_4', parameters: [], propagateParams: false, scriptId: 'TestCase.groovy'])
                activeChoice choiceType: 'PT_CHECKBOX', description: 'Run Script In Selenium Grid', filterLength: 1, filterable: false, name: 'GridMode', randomName: 'choice-parameter-3185883074336700', script: scriptlerScript(isSandboxed: true, scriptlerBuilder: [builderId: '1716131314720_6', parameters: [], propagateParams: false, scriptId: 'GridMode.groovy'])
               activeChoice choiceType: 'PT_MULTI_SELECT', description: 'Select the browsers', filterLength: 1, filterable: false, name: 'Browsers', randomName: 'choice-parameter-3357113069137900', script: scriptlerScript(isSandboxed: true, scriptlerBuilder: [builderId: '1716302544592_3', parameters: [], propagateParams: false, scriptId: 'browsers.groovy'])
                  string defaultValue: '4445', description: 'Enter the Grid Port Number', name: 'GridPort', trim: true
                 string description: 'The default profile path value to launch the chrome browser, this will helps to skip SSO. This field is not mandatory', name: 'ProfilePath', trim: true
                base64File description: 'Upload the file', name: 'FILE'


    }
        stages{
        stage('checkout code'){
                    steps{
                        checkout scmGit(branches: [[name: '*/EjazUpdate']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-dba-rsa', url: 'https://github.com/doingERP-com/doingERP.git']])
                    }
                }

        stage('process input file'){
            steps{
                 withFileParameter('FILE') {
                        bat 'copy /Y %FILE% %WORKSPACE%\\src\\test\\resources\\businessflows\\Sample.xlsx'

                    }
            }
        }

        stage('Clean repo'){
            steps{

                bat 'mvn clean'
            }
        }
        stage('Test script'){
            steps{
                bat 'mvn test "-Dprofile.path=%ProfilePath%" "-Dtest.cases=%TestCase%" "-Dgrid.mode=%GridMode%" "-Dbrowsers=%Browsers%" "-Dbuild.no=%BUILD_NUMBER%" "-Dgrid.port=%GridPort%"'
            }
        }

    }
post {
    always {
        script {
            // Define the base URL where the videos are stored
            def baseUrl = "http://gridview.doingerp.com/video/"

                def selectedTestCasesString = params.TestCase

                // Split the selected test cases string by commas
                def selectedTestCases = selectedTestCasesString.split(',')

	// Get the build number
            def buildNumber = env.BUILD_NUMBER

            // Create a map to store the video URLs for each test case
            def videoUrlsMap = [:]

            // Iterate through the selected test cases
            selectedTestCases.each { testCase ->
                // Construct the URL for the test case videos
                def testCaseUrl = "${baseUrl}${testCase.replaceAll(' ', '_')}_${buildNumber}.mp4"

                // Add the constructed URL to the map for the corresponding test case
                videoUrlsMap[testCase] = testCaseUrl
            }

            // Print the video URLs for each test case
            videoUrlsMap.each { testCase, videoUrl ->
                println "Test case: ${testCase}, Video URL: ${videoUrl}"
            }

            // Generate HTML for the Video Report section
            def videoReportHTML = """
                <h2>Video Report</h2>
                <ul>
            """
            videoUrlsMap.each { testCase, videoUrl ->
                videoReportHTML += "<li><iframe href='${videoUrl}'>${testCase}</a></li> <iframe src='${videoUrl}' height='200' width='300' title='${testCase}'></iframe>"
            }
            videoReportHTML += "</ul>"

            // Write the Video Report HTML to a file
            writeFile file: "${env.WORKSPACE}/target/results/latest/video_report.html", text: videoReportHTML
        }
publishHTML(allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'target/results/latest', reportFiles: 'TestReport.html', reportName: 'Summary Report', reportTitles: '')
                    publishHTML(allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'target/results/latest', reportFiles: 'video_report.html', reportName: 'Video Report', reportTitles: '')
    }
}


}
