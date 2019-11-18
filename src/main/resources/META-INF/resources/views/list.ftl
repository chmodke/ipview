<!DOCTYPE HTML>
<html>
<head>
    <title>首页</title>
    <meta charset="utf-8"/>
    <link href="favicon.ico" rel="icon" type="image/x-icon"/>
    <style>

        .container {
            width: 600px;
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

        .ip_col td div {
            position: absolute;
            display: none;
            color: #ae029b;
        }

        .ip_col:hover td div {
            display: initial;
        }
    </style>
</head>
<body>
<div class="container">
    <div>
        <div>
            <span>当前系统时间:${nowDate}</span>
            <span style="float: right"><a href="about.html"><b>about</b></a></span>
        </div>
        <div>
            <span>每${timeInterval}秒重新扫描，立即</span>
            <span><a id="refresh" href="javascript:;">刷新</a></span>
            <span id="wait_refresh"></span>
        </div>
    </div>
    <table>
        <thead>
        <tr>
            <th>IP</th>
            <th>STATUS</th>
            <th>LAST_SCANNED_TIME</th>
            <th>HOSTNAME</th>
        </tr>
        </thead>
        <tbody>
        <#list ip_list as ip>
            <tr class="ip_col">
                <td>${ip.IP} </td>
                <#if (ip.STATUS=="0")>
                    <td style="color: green">Alive
                        <div>在线</div>
                    </td>
                <#elseif (ip.STATUS=="1")>
                    <td style="color: red">Dead
                        <div>离线</div>
                    </td>
                <#else>
                    <td style="color: red">${ip.STATUS} </td>
                </#if>
                <td>${ip.LAST_UP_TIME} </td>
                <td>${ip.HOSTNAME} </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>


</body>
<script type="application/javascript">
    document.getElementById("refresh").onclick = function (ev) {
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
            document.getElementById("wait_refresh").innerText = "扫描中...";

        } else {
            alert("Your browser does not support XMLHTTP.");
        }
    }
</script>
</html>