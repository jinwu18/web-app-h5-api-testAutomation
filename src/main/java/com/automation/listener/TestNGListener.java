package com.automation.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestNGListener extends TestListenerAdapter {
	
	Log4jLogger logger = new Log4jLogger();
	
    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);

        // List of test results which we will delete later
        ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
        // collect all id's from passed test
        Set<Integer> passedTestIds = new HashSet<Integer>();
        for (ITestResult passedTest : testContext.getPassedTests()
                .getAllResults()) {
            //logger.info("PassedTests = " + passedTest.getName());
        	logger.info("PassedTests = " + passedTest.getName());
            passedTestIds.add(getId(passedTest));
        }

        // Eliminate the repeat methods
        Set<Integer> failedTestIds = new HashSet<Integer>();
        for (ITestResult failedTest : testContext.getFailedTests()
                .getAllResults()) {
            //logger.info("failedTest = " + failedTest.getName());
        	logger.info("failedTest = " + failedTest.getName());
            int failedTestId = getId(failedTest);

            // if we saw this test as a failed test before we mark as to be
            // deleted
            // or delete this failed test if there is at least one passed
            // version
            if (failedTestIds.contains(failedTestId)
                    || passedTestIds.contains(failedTestId)) {
                testsToBeRemoved.add(failedTest);
            } else {
                failedTestIds.add(failedTestId);
            }
        }

        // finally delete all tests that are marked
        for (Iterator<ITestResult> iterator = testContext.getFailedTests()
                .getAllResults().iterator(); iterator.hasNext();) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                //logger.info("Remove repeat Fail Test: " + testResult.getName());
            	logger.info("Remove repeat Fail Test: " + testResult.getName());
                iterator.remove();
            }
        }
    }

    private int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id = id + result.getMethod().getMethodName().hashCode();
        id = id
                + (result.getParameters() != null ? Arrays.hashCode(result
                        .getParameters()) : 0);
        return id;
    } 
    
    @Override
    public void onTestFailure(ITestResult result) {  
    	logger.error("Test Failure");
        super.onTestFailure(result);
    }  
    
    @Override
    public void onTestSuccess(ITestResult result) {  
        // TODO Auto-generated method stub  
    	logger.info("Test Success");
        super.onTestSuccess(result);
    }  
    
    @Override
    public void onTestSkipped(ITestResult result) {  
        // TODO Auto-generated method stub  
    	logger.error("Test Skip");
        super.onTestSuccess(result);
    }  
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {  
        // TODO Auto-generated method stub  
    	logger.error("Test Success Partially");
        super.onTestSuccess(result);  
    }  
    
    @Override
    public void onStart(ITestContext testContext) {
    	logger.info("Test Start");
        super.onStart(testContext);
    }
}
