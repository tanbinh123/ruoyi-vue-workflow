<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>欢迎</title>
</head>
<body>
<h2>登录中...</h2>
<script>
    window.onload = function () {
        alert("${token}")
        window.opener.postMessage("${token}", "*");
        window.close();
    }
</script>
</body>
</html>