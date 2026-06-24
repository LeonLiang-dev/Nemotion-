# MariaDB Runtime Placeholder

Put the extracted Windows MariaDB distribution in this directory before running:

```bat
build.bat preflight
build.bat package
```

The expected layout is:

```text
packaging\windows\mariadb\
  bin\mysqld.exe
  bin\mysql.exe
  bin\mysqladmin.exe
  bin\mariadb-install-db.exe
```

Use the official Windows ZIP distribution. Do not commit the binary runtime into
source control; it is large and should be supplied by the Windows build machine.
