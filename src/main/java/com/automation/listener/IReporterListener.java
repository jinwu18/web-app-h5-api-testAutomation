package com.automation.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.automation.framework.AbastractBase;
import com.automation.utils.FileUtil;
import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class IReporterListener implements IReporter {

    private static final Calendar CALENDAR = Calendar.getInstance();

    private static Map<String, ExtentTest> classTestMap = new HashMap<>();
    
    AbastractBase ab = new AbastractBase();
    
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        ExtentService.getInstance().setReportUsesManualConfiguration(true);
        ExtentService.getInstance().setAnalysisStrategy(AnalysisStrategy.SUITE);
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            
            List<String> suiteList = new ArrayList<String>();
            ExtentTest suiteTest = null;
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                
                if(!suiteList.contains(r.getTestContext().getSuite().getName())){
                	suiteList.add(r.getTestContext().getSuite().getName());
                	suiteTest = ExtentService.getInstance().createTest(r.getTestContext().getSuite().getName());  
                }
                buildTestNodes(suiteTest, context.getFailedTests(), Status.FAIL);
                buildTestNodes(suiteTest, context.getSkippedTests(), Status.SKIP);
                buildTestNodes(suiteTest, context.getPassedTests(), Status.PASS);  
            }
        }
        for (String s : Reporter.getOutput()) {
            ExtentService.getInstance().setTestRunnerOutput("<p>" + s + "</p>");
        }

        ExtentService.getInstance().flush();
        extentReportCssJSSet(outputDirectory);
    	FileUtil fileUtil = new FileUtil();
    	fileUtil.fileCopy(outputDirectory + File.separator + "ExtentHtml.html", outputDirectory + File.separator + ab.testReportName + ".html");
    }
    
    private void extentReportCssJSSet(String outputDirectory){
        String rawHTML = outputDirectory + File.separator + "ExtentHtml.html";
        Document doc;
		try {
			doc = Jsoup.parse(new File(rawHTML), "utf-8");
			updateCssLink(doc);
			updateJSSrc(doc);
			updateLogoSrc(doc);
	        FileOutputStream fos = new FileOutputStream(new File(rawHTML), false);
	        // 设置输出流
	        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
	        // doc写入文件中
	        osw.write(doc.html());
	        osw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void updateCssLink(Document doc) {
        Elements eles = doc.select("link");
        for(Element ele:eles){
        	if(ele.attr("href").contains("extent.css")){
        		System.out.println(ele.attr("href").toString());
        		ele.attr("href", "./css/extent.css");
        	}
        }
    }
    
    private void updateJSSrc(Document doc) {
        Elements eles = doc.select("script");
        for(Element ele:eles){
        	if(ele.attr("src").contains("extent.js")){
        		System.out.println(ele.attr("src").toString());
        		ele.attr("src", "./js/extent.js");
        	}
        }
    }
    
    private void updateLogoSrc(Document doc) {
        Elements eles = doc.select("img");
        for(Element ele:eles){
        	if(ele.attr("src").contains("logo.png")){
        		System.out.println(ele.attr("src").toString());
        		ele.attr("src", "./logo/logo.png");
        	}
        }
    }
    
    @SuppressWarnings("static-access")
	private void buildTestNodes(ExtentTest suiteTest, IResultMap tests, Status status) {
        ExtentTest testNode;
        ExtentTest classNode;

        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                String className = result.getInstance().getClass().getSimpleName();

                if (classTestMap.containsKey(className)) {
                    classNode = classTestMap.get(className);
                } else {
                    classNode = suiteTest.createNode(className);
                    classTestMap.put(className, classNode);
                }

                testNode = classNode.createNode(result.getMethod().getMethodName() + "-" + ab.caseName + "-" + ab.testEnv(ab.testEnv),
                        result.getMethod().getDescription());

                String[] groups = result.getMethod().getGroups();
                assignGroups(testNode, groups);

                if (result.getThrowable() != null) {
                    testNode.log(status, result.getThrowable());
                } else {
                    testNode.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }
                

                testNode.getModel().getLogContext().getAll().forEach(x -> x.setTimestamp(getTime(result.getEndMillis())));
                testNode.getModel().setStartTime(getTime(result.getStartMillis()));
                testNode.getModel().setEndTime(getTime(result.getEndMillis()));
            }
        }
    }

    private Date getTime(long millis) {
        CALENDAR.setTimeInMillis(millis);
        return CALENDAR.getTime();
    }

    public static void assignGroups(ExtentTest test, String[] groups) {
        if (groups.length > 0) {
            for (String g : groups) {
                if (g.startsWith("d:") || g.startsWith("device:")) {
                    String d = g.replace("d:", "").replace("device:", "");
                    test.assignDevice(d);
                } else if (g.startsWith("a:") || g.startsWith("author:")) {
                    String a = g.replace("a:", "").replace("author:", "");
                    test.assignAuthor(a);
                } else if (g.startsWith("t:") || g.startsWith("tag:")) {
                    String t = g.replace("t:", "").replace("tag:", "");
                    test.assignCategory(t);
                } else {
                    test.assignCategory(g);
                }
            }
        }
    }
}