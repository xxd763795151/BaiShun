# BaiShun
the VIP users information management for barbershop

**理发店会员信息管理系统**

# 介绍

理发店会员信息的简单管理(CRUD)系统，记录操作变量日志，并保存最近60天内的，支持数据备份。

不支持登录、会话管理，所以没有权限认证相关能力支持。因为这玩意产生的背景是在一个朋友的要求下，部署在自己电脑上的，单机应用所以没有外部流量进入，所以没在考虑这些因素。

本身这个应用最好是写成一个pc软件安装在人家电脑上，结果服务器我用java写成web了，为了解决小白用户的安装使用体验考虑，我写了一个c++的客户端（界面库duilib），并打包了jre，数据库用的h2（计算过数据量）Embedded mode并持久化到本地。最后统一打包(用的nisi，根目录下有一个基本的打包的脚本文件）为一个exe安装包，可以在任何windows电脑上按照向导一步步安装启动应用。

# 技术栈

spring boot + h2 + jpa + bootstrap， c++ + duilib



# NISI打包步骤

1. 先从网上下载一个nisi的安装包，并安装到windows系统上

2. 设置要打包的目录格式，格式如下，目录名就是这样，不要乱改：

   > baishun_setup
   >
   > ----conf
   >
   > ----h2
   >
   > ----jre1.8.0_66
   >
   > ----skin
   >
   > ----baishun.jar
   >
   > ----BaiShunStart.exe
   >
   > ----BaishunStart.ico
   >
   > ----DuiLib_u.dll
   >
   > ----favicon.ico
   >
   > ----start.bat
   >
   > bs_setup.nsi

   上面是根目录（baishun_setup）和它下面的1级子目录或文件，将上面的目录及下面的内容从源码里copy出来整理为这个结构，关于baishun.jar可以在web工程目录下执行./package.bat打包，从target目录下copy过来。

   默认没有开启邮箱备份，如果需要开启，可以修改start.bat的启动参数配置

   bs_setup.nsi要和baishun_setup目录同级

3. 右键点击bs_setup.nsi，选择Compile NSIS Script，或者下面带压缩那个，也可以打开NSIS，选择编译脚本，执行完成后，便会生成一个exe的安装包