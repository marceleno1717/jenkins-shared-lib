def call(String imageName, String newTag, String deploymentFile, String githubCreds, String email, String repoUrl){
    def repo = repoUrl.split("https://")[0]
    withCredentials([
        usernamePassword(credentialsId: githubCreds, usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_PASSWORD')
    ]){
        sh """
            sed -i 's|image: ${imageName}:.*|image: ${imageName}:${newTag}|' ${deploymentFile}

            git config user.email ${email}
            git config user.name ${GITHUB_USERNAME}

            git add ${deploymentFile}
            git commit -m "Update deployment image to ${imageName}:${newTag}!"

            echo "pushing to ${repo}..."
            git push https://${GITHUB_USERNAME}:${GITHUB_PASSWORD}@${repo} HEAD:main
        """
    }
}