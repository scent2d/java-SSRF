# java-SSRF
Test SSRF


# 도커 빌드
docker-compose build
docker-compose up

# 서비스
http POST http://localhost:8080/api/docs/create name=SensitiveDocument url=https://raw.githubusercontent.com/sharathkramadas/k8s-nuclei-templates/main/README.md
http GET http://localhost:8080/api/docs/render\?url\=https://raw.githubusercontent.com/sharathkramadas/k8s-nuclei-templates/main/README.md

# 공격
http POST http://localhost:8080/api/docs/create name=AnotherSensitiveDocument url=http://169.254.169.254
http GET http://localhost:8080/api/docs/render\?url\=http://169.254.169.254

# Secure
http POST http://localhost:8080/api/docs/secure/create name=AnotherSensitiveDocument url=http://169.254.169.254         => 400 Error
http GET http://localhost:8080/api/docs/secure/render\?url\=http://169.254.169.254                                      => 400 Error

# 접근이 허용된 도메인 = "raw.githubusercontent.com"
http POST http://localhost:8080/api/docs/secure/create name=SensitiveDocument url=https://raw.githubusercontent.com/sharathkramadas/k8s-nuclei-templates/main/README.md

