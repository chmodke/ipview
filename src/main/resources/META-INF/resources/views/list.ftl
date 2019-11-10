<!DOCTYPE HTML>
<html>
<head>
    <title>首页</title>
    <meta charset="utf-8"/>
    <style>

        .container {
            width: 800px;
            margin: 20px auto 0 auto;
        }

        table, table tr th, table tr td {
            border: 1px solid #0094ff;
            white-space: nowrap;
            padding: 0 5px 0 5px;
        }

        table {
            width: 200px;
            min-height: 25px;
            line-height: 25px;
            text-align: center;
            border-collapse: collapse;
            padding: 2px;
        }
    </style>
</head>
<body>
<div class="container">
    <div>
        <a id="refresh" href="javascript:;">刷新</a>
    </div>
    <table>
        <thead>
        <tr>
            <th>IP</th>
            <th>STATUS</th>
            <th>LAST_SCAN_TIME</th>
        </tr>
        </thead>
        <tbody>
        <#list ip_list as ip>
            <tr>
                <td>${ip.IP} </td>
                <td>${ip.STATUS} </td>
                <td>${ip.LAST_UP_TIME} </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>


</body>
<script type="application/javascript">
    var refresh = document.getElementById("refresh")
    refresh.onclick = function (ev) {
        xmlhttp = null;
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        if (xmlhttp != null) {
            xmlhttp.open("GET", "refresh", true);
            xmlhttp.send(null);
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    if (xmlhttp.status == 200) {
                        window.location.reload();
                    }
                }
            };

        } else {
            alert("Your browser does not support XMLHTTP.");
        }
    }
</script>
</html>