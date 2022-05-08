config = {
  _id : "replication",
  members: [
    {_id:0,host : "mongo1:27020"},
    {_id:1,host : "mongo2:27021"},
    {_id:2,host : "mongo3:27022"},
  ]
}

rs.initiate(config);

rs.conf();