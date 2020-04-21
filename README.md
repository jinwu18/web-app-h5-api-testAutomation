**设计思想**

- 通过rest assured实现API自动化，保证后端API主要功能及业务流程

- 通过selenium实现web UI自动化，appium+stf实现android UI自动化，保证前端页面操作功能及业务流程

- 通过testng对测试脚本进行管理，extent report 实现报告输出

- 通过maven实现脚本CI（jerkins），tomcat实现测试报告线上化

- 使用java作为脚本语言

**设计框架(testFrame.jpg)**

![](https://github.com/jinwu18/testAutomation/blob/master/testFrame.jpg)

**说明** 

- TestNG：测试用例管理

- Maven：jar包管理

- ExtentReport：测试报告

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


**测试报告(extentReport.jpg)**

脚本执行后，通过IReporterListener监听生成extent report，/test-output/ExtentHtml.html
![](https://github.com/jinwu18/testAutomation/blob/master/extentReport.jpg)

**testng.xml说明**
执行所需参数全部写于xml文件中，比如执行app测试，在本地部署完appium、模拟器等环境后，在testng-app.xml文件配置如下信息：
- <parameter name = "port" value="4723" />
- <parameter name = "udid" value="192.168.174.101:5555" /> 
- <parameter name = "platformVersion" value="7.1.1" />
- <parameter name = "appPackage" value="com.android.contacts" />
- <parameter name = "appActivity" value="com.android.contacts.activities.PeopleActivity" />	
在test中添加对应的测试脚本
