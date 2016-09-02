Features

1. Custom classes will generate by tool itself without dev interaction at compiling phase.
2. All the inputs are configurable if that inputs define as parameters when test case written by selenium ide.
3. Inputs can be configured both test suite wise and test case wise (priority will be given to test case).
4. Can be use testng or junit as test framework , only have to configure it through domain.properties file.
5. Use both selenium RC and Webdriver, only have to configure it through Test.xml file.
6. Replace ide generated methods with custom methods in java source.

domain properties

#local or remote
driver.mode=local
hub.url=http://127.0.0.1:4444/wd/hub

Browser properties


browserName=firefox
platform
version


browser names

 	FIREFOX = "firefox" 
    FIREFOX_2 = "firefox2" 
    FIREFOX_3 = "firefox3" 
    FIREFOX_PROXY = "firefoxproxy" 
    FIREFOX_CHROME = "firefoxchrome" 
    GOOGLECHROME = "googlechrome" 
    SAFARI = "safari" 
    OPERA = "opera"  
    IEXPLORE= "iexplore" 
    IEXPLORE_PROXY= "iexploreproxy" 
    SAFARI_PROXY = "safariproxy" 
    CHROME = "chrome" 
    KONQUEROR = "konqueror" 
    MOCK = "mock" 
    IE_HTA="iehta" 

    ANDROID = "android" 
    HTMLUNIT = "htmlunit" 
    IE = "internet explorer" 
    IPHONE = "iPhone"  
    IPAD = "iPad"  
    PHANTOMJS = "phantomjs" 
     
    
Platforms

WINDOWS
XP
VISTA
WIN8
MAC
UNIX
LINUX
ANDROID
ANY
