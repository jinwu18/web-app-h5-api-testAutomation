**说明** 

- TestNG：测试用例管理

- Maven：jar包管理

- ExtentReport：测试报告

**设计框架**

![](https://github.com/jinwu18/testAutomation/blob/master/testFrame.jpg)

**代码结构** 

│─lib  项目引用jar包及WebDriver执行文件

├─common 公共模块 

│  ├─WebCommon - web端公共方法类

│  ├─ApiCommon - api端公共方法类

│  ├─AppCommon - app端公共方法类

├─framework 测试框架

│  ├─AbastractBase - 自动化测试基础类

│  ├─WebDriverManager - web端driver管理基础类

│  ├─WebTestNGBase - web脚本执行基础类

│  ├─AppTestNGBase - app脚本执行基础类

├─listener 测试监听类（日志、结果、报告）

│  ├─IReporterListener - 测试报告监听类

│  ├─TestNGListener - 测试结果监听类

│  ├─RetryListener - 测试脚本重试监听类

├─utils 测试工具类（文件处理等）

├─log 日志输出位置

├─xml testng.xml文件（测试类驱动文件，包含web、h5、api、app）

├─resource 资源文件夹

│  ├─main - log4j、report、selenium等配置文件属性

│  ├─test - extent report

├─page 测试页面对象类

├─script 业务、功能脚本

**如何使用**

1、通过eclipse导入maven项目

2、安装eclipse安装testng插件

3、在xml文件目录下执行测试脚本 


**测试报告**

脚本执行后，通过IReporterListener监听生成extent report，/test-output/ExtentHtml.html
![](https://github.com/jinwu18/testAutomation/blob/master/extentReport.jpg)

**app**
app需要配置stf
