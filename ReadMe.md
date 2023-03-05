#

## Description
This is a default spring-boot java application using Java 17.

the following implementations are included:
- lombok
- mapstruct
- test-containers
- flyway-core
- spring-boot-starter-validation
- postgreSQl DB
- Docker and Docker-compose examples
- Automatic model auditing
- REST exception handling

## Usage

#### Copy the template to a new repo
```bash
git remote rm origin
git remote add origin <NEW_REPO_LOCATION.git>
git branch -M main
git push -u origin main
```

#### delete the git history 
```bash
git checkout --orphan latest_branch
git add -A
git commit -am "initial commit"
git branch -D main
git branch -m main
git push -f origin main
```