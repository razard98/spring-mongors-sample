# docker compose + mongodb replSet

*MongoDB의 ReplicaSet 구성은 데이터베이스의 고가용성 환경을 위해 필요한 기능

*서비스중인 MongoDB의 인스턴스에 문제가 생겼을 때, 구성원 중에 하나인 복제 노드가 장애 노드를 즉시 대체

- Primary : 클라이언트에서 DB로 읽기 및 쓰기 작업을 한다.
- Secondary : Primary로 부터 데이터를 동기화 한다.
- Arbiter: 데이터 동기화하지 않으며 ReplicaSet의 복구를 돕는 역할을 한다.


### Setup
<pre>
$cd setup
$docker build -t setup-rspl .
</pre>
### mongodb 설치 & 기동
<pre>
$cd ../
$docker-compose up -d
</pre>
<pre>
$docker ps
CONTAINER ID   IMAGE                             COMMAND                  CREATED       STATUS       PORTS                                                  NAMES
e38805497b9f   mongo                             "docker-entrypoint.s…"   2 hours ago   Up 2 hours   0.0.0.0:27022->27017/tcp                               mongodb-replicaset_mongo3_1
70eeb4b3bb20   mongo                             "docker-entrypoint.s…"   2 hours ago   Up 2 hours   0.0.0.0:27021->27017/tcp                               mongodb-replicaset_mongo2_1
5b3dc3388b57   mongo                             "docker-entrypoint.s…"   2 hours ago   Up 2 hours   0.0.0.0:27020->27017/tcp                               mongodb-replicaset_mongo1_1
</pre>

#### mongodb 접속
<pre>
$docker exec -it mongodb-replicaset_mongo1_1 mongo --port 27020
replication:PRIMARY> db.version()
5.0.8
</pre>

#### 계정 생성
<pre>
replication:PRIMARY> use testdb
switched to db testdb
replication:PRIMARY> db.createUser({user:"test",pwd:"mypassword",roles:["readWrite"]})
Successfully added user: { "user" : "test", "roles" : [ "readWrite" ] }
</pre>

#### Collection 생성
<pre>
replication:PRIMARY> db.createCollection("customer")

</pre>

#### Sample 데이터 저장 & 조회
<pre>
replication:PRIMARY> db.getCollection("customer").insert({firstName:"kim",lastName:"minsung"})
{ "_id" : ObjectId("62775604fcc9ee00d991a024"), "firstName" : "kim", "lastName" : "minsung" }

replication:PRIMARY> db.getCollection("customer").find().pretty()
{
	"_id" : ObjectId("627754290cdf5173ac823e0f"),
	"firstName" : "hong",
	"lastName" : "kildong",
	"_class" : "com.example.mongors.user.domain.Customer"
}
{
	"_id" : ObjectId("627754290cdf5173ac823e10"),
	"firstName" : "kang",
	"lastName" : "sunghyun",
	"_class" : "com.example.mongors.user.domain.Customer"
}
</pre>

#### 데이터 업데이트
<pre>
replication:PRIMARY> db.customer.updateOne({_id:ObjectId("627754290cdf5173ac823e10")},{$set:{firstName:"kim"}})
{ "acknowledged" : true, "matchedCount" : 1, "modifiedCount" : 1 }
replication:PRIMARY> db.customer.find({_id:ObjectId("627754290cdf5173ac823e10")})
{ "_id" : ObjectId("627754290cdf5173ac823e10"), "firstName" : "kim", "lastName" : "sunghyun", "_class" : "com.example.mongors.user.domain.Customer" }
</pre>

#### 데이터 삭제
<pre>
replication:PRIMARY> db.customer.deleteOne({firstName:"hong"})
{ "acknowledged" : true, "deletedCount" : 1 }
</pre>
