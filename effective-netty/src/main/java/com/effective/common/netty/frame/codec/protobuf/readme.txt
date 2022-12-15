protoc --java_out = "path.proto"

通过“--java_out”指定生成JAVA代码保存的目录，
后面紧跟“.proto”文件的路径。此后我们看到生成 了Package和一个PersonProto.java文件，
我们只需要把此java文件复制到项目中即可。