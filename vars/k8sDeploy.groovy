def call(String imageName, String newTag, String deploymentFile) {
    sh """
    sed -i 's|image: ${imageName}:.*|image: ${imageName}:${newTag}|' ${deploymentFile}

    kubectl apply -f ${deploymentFile}

    DEPLOY_NAME=\$(awk '/^metadata:/{f=1;next} f&&/^  name:/{print \$2;exit}' ${deploymentFile})
    NAMESPACE=\$(awk '/^metadata:/{f=1;next} f&&/^  namespace:/{print \$2;exit}' ${deploymentFile})

    kubectl rollout status deployment/\${DEPLOY_NAME} -n \${NAMESPACE} --timeout=120s
    """
}
