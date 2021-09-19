# Authentication-ver-1
Cung cấp cho người dùng khả năng tạo tài khoản (đầy đủ chức năng Log in/ Log out)
# How to use
Vào file ApiService, chỉnh địa chỉ ip thành ip hiện tại của mạng mà đth và server đang dùng.
Mở Terminal chạy: json-server --host "ip" --watch db.json
File json ở đâu thì mở Terminal ở đó, form db.json (tk admin: admin@gmail.com, mk: admin):
------------------
{
  "users": [
    {
      "id": 1,
      "token": "io.jsonwebtoken.impl.DefaultJwtBuilder@ae408ae",
      "userEmail": "admin@gmail.com",
      "userHashPassword": "21, 23, 2f, 29, 7a, 57, a5, a7, 43, 89, 4a, 0e, 4a, 80, 1f, c3",
      "userName": "admin"
    }
  ]
}
------------------
