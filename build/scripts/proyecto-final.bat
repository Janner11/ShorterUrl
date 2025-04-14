@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem
@rem SPDX-License-Identifier: Apache-2.0
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  proyecto-final startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and PROYECTO_FINAL_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\proyecto-final-1.0-SNAPSHOT.jar;%APP_HOME%\lib\javalin-6.4.0.jar;%APP_HOME%\lib\slf4j-simple-2.0.16.jar;%APP_HOME%\lib\jsoup-1.15.3.jar;%APP_HOME%\lib\java-jwt-3.19.2.jar;%APP_HOME%\lib\jackson-annotations-2.17.2.jar;%APP_HOME%\lib\jackson-core-2.17.2.jar;%APP_HOME%\lib\jackson-databind-2.17.2.jar;%APP_HOME%\lib\mongodb-driver-sync-5.3.0.jar;%APP_HOME%\lib\grpc-netty-1.50.2.jar;%APP_HOME%\lib\grpc-core-1.50.2.jar;%APP_HOME%\lib\gson-2.13.0.jar;%APP_HOME%\lib\core-3.4.1.jar;%APP_HOME%\lib\jbcrypt-0.4.jar;%APP_HOME%\lib\jaxws-api-2.3.1.jar;%APP_HOME%\lib\jaxws-rt-2.3.5.jar;%APP_HOME%\lib\jaxb-impl-2.3.5.jar;%APP_HOME%\lib\jaxb-api-2.3.1.jar;%APP_HOME%\lib\javax.activation-api-1.2.0.jar;%APP_HOME%\lib\grpc-protobuf-1.50.2.jar;%APP_HOME%\lib\grpc-stub-1.50.2.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\websocket-jetty-server-11.0.24.jar;%APP_HOME%\lib\jetty-webapp-11.0.24.jar;%APP_HOME%\lib\websocket-servlet-11.0.24.jar;%APP_HOME%\lib\jetty-servlet-11.0.24.jar;%APP_HOME%\lib\jetty-security-11.0.24.jar;%APP_HOME%\lib\websocket-core-server-11.0.24.jar;%APP_HOME%\lib\jetty-server-11.0.24.jar;%APP_HOME%\lib\websocket-jetty-common-11.0.24.jar;%APP_HOME%\lib\websocket-core-common-11.0.24.jar;%APP_HOME%\lib\jetty-http-11.0.24.jar;%APP_HOME%\lib\jetty-io-11.0.24.jar;%APP_HOME%\lib\jetty-xml-11.0.24.jar;%APP_HOME%\lib\jetty-util-11.0.24.jar;%APP_HOME%\lib\slf4j-api-2.0.16.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.9.25.jar;%APP_HOME%\lib\kotlin-stdlib-1.9.25.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.9.25.jar;%APP_HOME%\lib\mongodb-driver-core-5.3.0.jar;%APP_HOME%\lib\bson-record-codec-5.3.0.jar;%APP_HOME%\lib\bson-5.3.0.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.50.2.jar;%APP_HOME%\lib\grpc-api-1.50.2.jar;%APP_HOME%\lib\guava-31.1-android.jar;%APP_HOME%\lib\error_prone_annotations-2.37.0.jar;%APP_HOME%\lib\javax.xml.soap-api-1.4.0.jar;%APP_HOME%\lib\policy-2.7.10.jar;%APP_HOME%\lib\ha-api-3.1.13.jar;%APP_HOME%\lib\management-api-3.2.3.jar;%APP_HOME%\lib\gmbal-api-only-4.0.3.jar;%APP_HOME%\lib\streambuffer-1.5.10.jar;%APP_HOME%\lib\saaj-impl-1.5.3.jar;%APP_HOME%\lib\stax-ex-1.8.3.jar;%APP_HOME%\lib\mimepull-1.9.15.jar;%APP_HOME%\lib\FastInfoset-1.2.18.jar;%APP_HOME%\lib\jakarta.mail-1.6.7.jar;%APP_HOME%\lib\jakarta.activation-1.2.2.jar;%APP_HOME%\lib\woodstox-core-6.2.6.jar;%APP_HOME%\lib\stax2-api-4.2.1.jar;%APP_HOME%\lib\jakarta.xml.ws-api-2.3.3.jar;%APP_HOME%\lib\jakarta.xml.bind-api-2.3.3.jar;%APP_HOME%\lib\jakarta.xml.soap-api-1.4.2.jar;%APP_HOME%\lib\jakarta.jws-api-2.1.0.jar;%APP_HOME%\lib\jakarta.annotation-api-1.3.5.jar;%APP_HOME%\lib\netty-codec-http2-4.1.79.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.79.Final.jar;%APP_HOME%\lib\perfmark-api-0.25.0.jar;%APP_HOME%\lib\netty-codec-http-4.1.79.Final.jar;%APP_HOME%\lib\netty-handler-4.1.79.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.79.Final.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\proto-google-common-protos-2.9.0.jar;%APP_HOME%\lib\protobuf-java-3.21.7.jar;%APP_HOME%\lib\jetty-jakarta-servlet-api-5.0.2.jar;%APP_HOME%\lib\websocket-jetty-api-11.0.24.jar;%APP_HOME%\lib\jakarta.activation-api-1.2.2.jar;%APP_HOME%\lib\annotations-4.1.1.4.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.21.jar;%APP_HOME%\lib\netty-codec-socks-4.1.79.Final.jar;%APP_HOME%\lib\netty-codec-4.1.79.Final.jar;%APP_HOME%\lib\netty-transport-4.1.79.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.79.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.79.Final.jar;%APP_HOME%\lib\netty-common-4.1.79.Final.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\checker-qual-3.12.0.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\grpc-context-1.50.2.jar;%APP_HOME%\lib\annotations-13.0.jar


@rem Execute proyecto-final
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PROYECTO_FINAL_OPTS%  -classpath "%CLASSPATH%" edu.pucmm.Main %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable PROYECTO_FINAL_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%PROYECTO_FINAL_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
