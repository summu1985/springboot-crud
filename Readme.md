
1. Add from git
2. Choose URL as : https://github.com/summu1985/csharp-crud-api.git
3. Provide application name and name
4. Choose resource type as "DeploymentConfig"
5. Uncheck "Auto deploy when new Image is available" in Deployment
6. Add a deployment environment variable, key = DefaultConnection, value = Host=postgresql;Port=5432;Database=postgres;Username=postgres;Password=<password>
7.Deploy Postgres
8. Click +Add
9. Select Database
10. Search using Postgre as filter and choose the Postgre Template (not the ephemeral one) and click instantiate template
11. Make sure Database service name is "postgresql", Connection username is "postgres", Connection password is <password> and Database name is postgres. Click create
12. Go to Topology mode, click on postgresql deployment config, and navigate to the postgresql pod. Click on terminal
sh-4.4$ psql -U postgres;
psql (10.23)
Type "help" for help.
postgres=# \l
                                 List of databases
   Name    |  Owner   | Encoding |  Collate   |   Ctype    |   Access privileges   
-----------+----------+----------+------------+------------+-----------------------
 postgres  | postgres | UTF8     | en_US.utf8 | en_US.utf8 | 
 template0 | postgres | UTF8     | en_US.utf8 | en_US.utf8 | =c/postgres          +
           |          |          |            |            | postgres=CTc/postgres
 template1 | postgres | UTF8     | en_US.utf8 | en_US.utf8 | =c/postgres          +
           |          |          |            |            | postgres=CTc/postgres
(3 rows)

postgres=#CREATE TABLE "users" (
  "id" SERIAL PRIMARY KEY,
  "name" VARCHAR(50) NOT NULL,
  "email" VARCHAR(255) NOT NULL
);CREATE TABLE
postgres=# \dt;
         List of relations
 Schema | Name  | Type  |  Owner   
--------+-------+-------+----------
 public | users | table | postgres
(1 row)

postgres=# 
13. Go to topology view, click on route exposed by the application.
14. Now add some data using curl
curl -X POST <application route>/api/users
   -H "Content-Type: application/json"
   -d '{"name": "Sumit", "email": "summukhe@redhat.com"}' 
15. Now check if you can view the added user by fetching all users from the application
curl -X GET <application route>/api/users
curl -X GET https://backend-tekton-demo-dev.apps.cluster-m227s.m227s.sandbox1272.opentlc.com/api/users 
You should get a response as such
[{"id":1,"name":"Sumit","email":"summukhe@redhat.com"}]

Install openshift pipelines via operator hub

16. Go to Pipelines options in developer view and click on create "pipeline"
17. Ensure the option of "Pipeline builder" is chosen for configure via
18. Give name as "backend-pipeline"
19. Click on add task, search for git-clone and select "Install and add"
20. Add a workspace and give name as "shared-workspace"
21. Click on the git-clone task and add url as "https://github.com/summu1985/csharp-crud-api.git", revision as master, workspace output as the "shared-workspace"
22. Click on + sign after git-clone, click on add task, search s2i-dotnet, click on install and add, give image name (image stream name) like : image-registry.openshift-image-registry.svc:5000/tekton-demo-dev/backend, in workspace, select source as "shared-workspace".
23. Click on + sign after s2i-dotnet, click on add task, search for openshift-client, click on (install and) add. Once added in pipeline, click on the openshift-client task, on right hand side, in SCRIPT, add "oc rollout latest dc/backend". Click on save
24. Now select the pipeline name, on right hand side, select the action "start" to start the pipeline.
25. Select Pipelineruns and select the latest entry, select the log tab to look at the logs of the tasks of the pipeline
26. For deploying into the uat project, permission must be granted to pull image from dev project to the uat project.
oc policy add-role-to-user system:image-puller system:serviceaccount:tekton-demo-uat:default -n tekton-demo-dev
27. Now edit the backend-pipeline again to one more task at end, click add task and choose openshift-client, and in the SCRIPT option, enter "oc tag backend:latest backend-uat:latest" and click save.
28. Trigger one more time the build
29. Now create the UAT project tekton-demo-uat
30. Click on +Add, select container image, select image stream tag from internal registry and choose tekton-demo-dev project, image stream : backend-uat and tag as latest.
31. Give Application name and Name, select resource type as deploymentconfig
32. Add a deployment environment variable, key = DefaultConnection, value = Host=postgresql;Port=5432;Database=postgres;Username=postgres;Password=<password>
33. Add a Postgre DB deployment from template, as mentioned previously
