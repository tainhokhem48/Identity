test potman 
username: admin
password: admin

1. create Database:  codespring
2. POST http://localhost:8000/identity/auth/token  {"username": "admin","password": "admin"} (Login)
3. POST http://localhost:8000/identity/auth/logout
   {
            "token": "aofdgidl;daswa....."
   }
   POST http://localhost:8000/identity/permissions
   {
            "name": "CREATE_DATA",
            "description": "CREATE_DATA"
   }
   POST http://localhost:8000/identity/roles
   {
            "name": "ADMIN",
            "description": "ADMIN",
            "permissions":["CREATE_DATA"]
   }
   http://localhost:8000/identity/auth/refresh
   {
    "token": "asiouahg..."
   }
   http://localhost:8000/identity/auth/introspect
   {
    "token": "akjpdaisp...."  
   }
5. CRUD user:  POST http://localhost:8000/identity/users
   {
            "username": "taitai2",
            "password": "123456789",
            "firstName": "ooasaa",
            "lastName": "aaaaaa",
            "dob": "1997-11-12"
   }  
                DELETE http://localhost:8000/identity/users/{userId}
                DELETE http://localhost:8000/identity/roles/{roleName}                                   
                PUT    http://localhost:8000/identity/users/{userId}
   {
            "password": "admin",
            "firstName": "oOsaka",
            "lastName": "titan",
            "dob": "1991-11-12",
   }
                GET  http://localhost:8000/identity/users
                GET  http://localhost:8000/identity/users/{userId}
                GET  http://localhost:8000/identity/users/myinfo
                GET  http://localhost:8000/identity/roles
                GET  http://localhost:8000/identity/permissions
