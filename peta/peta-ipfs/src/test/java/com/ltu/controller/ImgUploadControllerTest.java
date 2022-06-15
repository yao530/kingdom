package com.ltu.controller;

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** 
* ImgUploadController Tester. 
* 
* @author <Authors name> 
* @since <pre>Aug 14, 2020</pre> 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ImgUploadControllerTest { 

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
    *
    * Method: imgUploadIO(MultipartFile file, HttpServletRequest request)
    *
    */
    @Test
    public void testImgUploadIO() throws Exception {

        log.info("test hello");
        TestCase.assertEquals(1, 1);
    }


} 
