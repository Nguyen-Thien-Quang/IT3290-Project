RESTORE DATABASE FoodProject
FROM DISK = '/var/opt/mssql/backup/FoodProject.bak'
WITH MOVE 'FoodProject' TO '/var/opt/mssql/data/FoodProject.mdf',
     MOVE 'FoodProject_log' TO '/var/opt/mssql/data/FoodProject_log.ldf',
     REPLACE;
GO