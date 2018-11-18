@echo Information about "jar" command
jar --help
@pause
@echo Creating jar file
jar --create --file myfirst.jar --manifest manifest.mf -C files/ .
@echo Creation finished
@pause
@echo Moving jar file to folder MyJar
copy /Y myfirst.jar MyJar\myfirst.jar
del myfirst.jar
@echo Moved
@echo Launch jar file by any key
@pause
cd MyJar
java -jar myfirst.jar
pause