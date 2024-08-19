test potman 
username: admin
password: admin

1. create Database:  codespring   
2. POST http://localhost:8000/identity/auth/token       (Login)
   {
    "username": "admin","password": "admin"
   } 
3. POST http://localhost:8000/identity/auth/logout
   {
            "token": "aofdgidl;daswa....."
   }
4.  POST http://localhost:8000/identity/permissions
   {
            "name": "CREATE_DATA",
            "description": "CREATE_DATA"
   }
5.  POST http://localhost:8000/identity/roles
   {
            "name": "ADMIN",
            "description": "ADMIN",
            "permissions":["CREATE_DATA"]
   }
6. POST http://localhost:8000/identity/auth/refresh
   {
    "token": "asiouahg..."
   }
7. POST http://localhost:8000/identity/auth/introspect
   {
    "token": "akjpdaisp...."  
   }
8.  POST http://localhost:8000/identity/users
   {
            "username": "taitai2",
            "password": "123456789",
            "firstName": "ooasaa",
            "lastName": "aaaaaa",
            "dob": "1997-11-12"
   }  
9.  DELETE http://localhost:8000/identity/users/{userId}
10. DELETE http://localhost:8000/identity/roles/{roleName}                                   
11. PUT    http://localhost:8000/identity/users/{userId}
   {
            "password": "admin",
            "firstName": "oOsaka",
            "lastName": "titan",
            "dob": "1991-11-12",
   }
12. GET  http://localhost:8000/identity/users
13. GET  http://localhost:8000/identity/users/{userId}
14. GET  http://localhost:8000/identity/users/myinfo
15. GET  http://localhost:8000/identity/roles
16. GET  http://localhost:8000/identity/permissions
