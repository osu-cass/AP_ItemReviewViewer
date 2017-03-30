/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.smarterbalanced.irv.services;

import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.irv.model.ItemScoreInfo;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author temp_rreddy
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/scoring-engine-test-context.xml" })
public class ItemReviewScoringServiceTest {

	private static final Logger _logger = LoggerFactory.getLogger(ItemReviewScoringServiceTest.class);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	//@Test
	public void scoreHTQItemTest() {

		try {
			
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("StudentResponse_ItemType_HTQ_I-187-2822.xml");
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			
			ItemReviewScoringService itemScoringService = new ItemReviewScoringService();
			ItemScoreInfo itemScoreInfo = itemScoringService.scoreItem(writer.toString(), "I-187-2822");

			Assert.assertTrue(itemScoreInfo.getPoints() == 1);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//@Test
	public void scoreMIItemTest() {

		try {
		
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("StudentResponse_ItemType_MI_I-187-2637.xml");
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			
			ItemReviewScoringService itemScoringService = new ItemReviewScoringService();
			ItemScoreInfo itemScoreInfo = itemScoringService.scoreItem(writer.toString(), "I-187-2637");

			Assert.assertTrue(itemScoreInfo.getPoints() == 1);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	//@Test
	public void scoreGIItemTest() {

		try {
		
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("StudentResponse_ItemType_GI_I-187-3417.xml");
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			
			ItemReviewScoringService itemScoringService = new ItemReviewScoringService();
			ItemScoreInfo itemScoreInfo = itemScoringService.scoreItem(writer.toString(), "I-187-3417");

			Assert.assertTrue(itemScoreInfo.getPoints() == 1);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	//@Test
	public void scoreTIItemTest() {

		try {
		
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("StudentResponse_ItemType_TI_I-187-2788.xml");
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			
			ItemReviewScoringService itemScoringService = new ItemReviewScoringService();
			ItemScoreInfo itemScoreInfo = itemScoringService.scoreItem(writer.toString(), "I-187-2788");

			Assert.assertTrue(itemScoreInfo.getPoints() == 1);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	//@Test
	public void scoreEQItemTest() {

		try {
		
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("StudentResponse_ItemType_EQ_I-187-3258.xml");
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			
			ItemReviewScoringService itemScoringService = new ItemReviewScoringService();
			ItemScoreInfo itemScoreInfo = itemScoringService.scoreItem(writer.toString(), "I-187-3258");

			Assert.assertTrue(itemScoreInfo.getPoints() == 1);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	//@Test
	public void scoreEBSRItemTest() {

		try {
		
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("StudentResponse_ItemType_EBSR_I-187-2812.xml");
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			
			ItemReviewScoringService itemScoringService = new ItemReviewScoringService();
			ItemScoreInfo itemScoreInfo = itemScoringService.scoreItem(writer.toString(), "I-187-2812");

			Assert.assertTrue(itemScoreInfo.getPoints() == 1);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
	@Test
	public void scoreMCItemTest() {

		try {
		
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("StudentResponse_ItemType_MC_I-187-1598.txt");
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			
			ItemReviewScoringService itemScoringService = new ItemReviewScoringService();
			ItemScoreInfo itemScoreInfo = itemScoringService.scoreItem(writer.toString(), "I-187-1598");

			Assert.assertTrue(itemScoreInfo.getPoints() == 1);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Test
	public void scoreMSItemTest() {

		try {
		
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("StudentResponse_ItemType_MS_I-187-2814.txt");
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer);
			
			ItemReviewScoringService itemScoringService = new ItemReviewScoringService();
			ItemScoreInfo itemScoreInfo = itemScoringService.scoreItem(writer.toString(), "I-187-2814");

			Assert.assertTrue(itemScoreInfo.getPoints() == 1);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
}
