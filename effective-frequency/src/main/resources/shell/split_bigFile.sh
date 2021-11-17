#!/bin/bash
big_file_path=$1
big_file_name=$2

if [ -z $big_file_path ];then
  echo `date +"%Y-%m-%d %H:%M:%S"` "请输出要拆分的文件路径";
  exit 1;
fi

if [ -z $big_file_name ];then
  echo `date +"%Y-%m-%d %H:%M:%S"` "请输出要拆分的文件名称 ";
  exit 1;
fi

if [[ ! -f $big_file_path/$big_file_name ]];then
  echo `date +"%Y-%m-%d %H:%M:%S"` "$big_file_path/$big_file_name 文件不存在";
  exit 1;
fi

echo  `date +"%Y-%m-%d %H:%M:%S"` "要拆分的文件 $big_file_path/$big_file_name"

file_size=$(du -sm $big_file_path/$bip_file_name | awk '{ print $1 }')
echo `date +"%Y-%m-%d %H:%M:%S"` "要拆分的文件大小" $file_size"m"

if [ $file_size -gt 100 ];then
  val n
  if [ `expr $file_size % 100` == 0 ];then
    n=`expr $file_size / 100`
  else
    n=`expr $file_size / 100 + 1`
  fi
  echo `date +"%Y-%m-%d %H:%M:%S"` "准备文件拆分分数为 $n"
  split -b 100m "$big_file_path/$big_file_name $big_file_path/ff_"
  echo `date +"%Y-%m-%d %H:%M:%S"` "拆分执行码 $?"
fi
exit 0
