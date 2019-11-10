<!DOCTYPE HTML>
<html>
<head>
    <title>首页</title>
    <meta charset="utf-8"/>
    <style>
        table, table tr th, table tr td {
            border: 1px solid #0094ff;
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
<table>
    <thead>
    <tr>
        <th>IP</th>
        <th>STATUS</th>
    </tr>
    </thead>
    <tbody>
    <#list ip_list as ip>
        <tr>
            <td>${ip.IP} </td>
            <td>${ip.STATUS} </td>
        </tr>
    </#list>
    </tbody>
</table>


</body>
</html>