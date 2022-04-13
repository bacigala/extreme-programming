package sk.bacigala.hikeplanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sk.bacigala.hike.Hike;
import sk.bacigala.hike.HikeController;
import sk.bacigala.peak.Peak;
import sk.bacigala.peak.PeakController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@SpringBootTest
class HikePlannerApplicationTests {

	@Test
	void TestDbConnect() throws SQLException {
		Connection con = Database.connect();
		Assertions.assertTrue(con.isValid(10));
		con.close();
	}

	@Test
	void TestGetPeaks() {
		PeakController pc = new PeakController();
		Map<String, String> payload = Collections.emptyMap();
		ArrayList<Peak> peaks =  pc.get(payload);
		Assertions.assertTrue(!peaks.isEmpty());
	}

	@Test
	void TestGetPeakByName() {
		PeakController pc = new PeakController();
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("nameSearch", "Gerlach");
		ArrayList<Peak> peakGerlach =  pc.get(payload);
		Assertions.assertEquals("Gerlachovský štít", peakGerlach.get(0).getName());
	}

	@Test
	void TestAddPeak() {
		PeakController pc = new PeakController();
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("name", "MyPeak");
		payload.put("height", "123456");
		payload.put("latitude", "10:20:30");
		payload.put("longitude", "30:20:10");
		var id = pc.create(payload);
		payload.clear();
		payload.put("id", id.toString());
		ArrayList<Peak> peakMyPeak =  pc.get(payload);
		Assertions.assertEquals("MyPeak", peakMyPeak.get(0).getName());
		Assertions.assertEquals(123456, peakMyPeak.get(0).getHeight());
		Assertions.assertEquals("10:20:30", peakMyPeak.get(0).getLatitude());
		Assertions.assertEquals("30:20:10", peakMyPeak.get(0).getLongitude());
	}

	@Test
	void TestGetHikes() {
		HikeController hikeController = new HikeController();
		Map<String, String> payload = Collections.emptyMap();
		ArrayList<Hike> hikes =  hikeController.search(payload);
		Assertions.assertTrue(!hikes.isEmpty());
		System.out.println("Hikes size: " + hikes.size());
	}

	@Test
	void TestCreateAndDeleteHike() {
		HikeController hikeController = new HikeController();
		Map<String, String> payload = Collections.emptyMap();
		ArrayList<Hike> hikes =  hikeController.search(payload);
		payload = new HashMap<String, String>();
		payload.put("name", "TestCreateHike");
		payload.put("date", "2022-04-13");
		payload.put("peak_id", "10");
		payload.put("difficulty", "1");
		payload.put("author_id", "2");
		String response =  hikeController.create(payload);
		Assertions.assertNotEquals("FAIL", response);

		payload = new HashMap<String, String>();
		payload.put("id", response);
		response =  hikeController.delete(payload);
		Assertions.assertEquals("OK", response);
	}

	@Test
	void TestCreateSearchAndDeleteHike() {
		HikeController hikeController = new HikeController();
		Map<String, String> payload = Collections.emptyMap();
		ArrayList<Hike> hikes =  hikeController.search(payload);
		Assertions.assertNotEquals(0, hikes.size());

		payload = new HashMap<String, String>();
		payload.put("name", "TestCreateHike2");
		payload.put("date", "2022-05-14");
		payload.put("peak_id", "20");
		payload.put("difficulty", "1");
		payload.put("author_id", "2");
		String response =  hikeController.create(payload);
		Assertions.assertNotEquals("FAIL", response);

		payload = new HashMap<String, String>();
		payload.put("id", response);
		hikes =  hikeController.search(payload);
		Assertions.assertEquals(1, hikes.size());
		Assertions.assertEquals(response, Long.toString(hikes.get(0).getId()));
		Assertions.assertEquals("TestCreateHike2", hikes.get(0).getName());
		Assertions.assertEquals("2022-05-14", hikes.get(0).getDate().toString());
		Assertions.assertEquals("20", Long.toString(hikes.get(0).getPeak_id()));
		Assertions.assertEquals("2", Long.toString(hikes.get(0).getAuthor_id()));

		payload = new HashMap<String, String>();
		payload.put("id", response);
		response =  hikeController.delete(payload);
		Assertions.assertEquals("OK", response);
	}

	@Test
	void TestCreateModifyAndDeleteHike() {
		HikeController hikeController = new HikeController();
		Map<String, String> payload;

		payload = new HashMap<String, String>();
		payload.put("name", "TestCreateHike3");
		payload.put("date", "2022-01-14");
		payload.put("peak_id", "30");
		payload.put("difficulty", "3");
		payload.put("author_id", "3");
		String response =  hikeController.create(payload);
		String id = response;
		Assertions.assertNotEquals("FAIL", response);

		payload = new HashMap<String, String>();
		payload.put("id", response);
		ArrayList<Hike> hikes =  hikeController.search(payload);
		Assertions.assertEquals(1, hikes.size());
		Assertions.assertEquals(response, Long.toString(hikes.get(0).getId()));
		Assertions.assertEquals("TestCreateHike3", hikes.get(0).getName());
		Assertions.assertEquals("2022-01-14", hikes.get(0).getDate().toString());
		Assertions.assertEquals("30", Long.toString(hikes.get(0).getPeak_id()));
		Assertions.assertEquals("3", Long.toString(hikes.get(0).getAuthor_id()));

		payload = new HashMap<String, String>();
		payload.put("id", response);
		payload.put("name", "TestCreateHike35");
		payload.put("date", "2022-01-20");
		payload.put("peak_id", "35");
		payload.put("difficulty", "1");
		payload.put("author_id", "5");
		response =  hikeController.modify(payload);
		Assertions.assertEquals("OK", response);

		payload = new HashMap<String, String>();
		payload.put("id", id);

		hikes =  hikeController.search(payload);
		Assertions.assertEquals(1, hikes.size());
		Assertions.assertEquals(id, Long.toString(hikes.get(0).getId()));
		Assertions.assertEquals("TestCreateHike35", hikes.get(0).getName());
		Assertions.assertEquals("2022-01-20", hikes.get(0).getDate().toString());
		Assertions.assertEquals("35", Long.toString(hikes.get(0).getPeak_id()));
		Assertions.assertEquals("5", Long.toString(hikes.get(0).getAuthor_id()));

		payload = new HashMap<String, String>();
		payload.put("id", id);
		response =  hikeController.delete(payload);
		Assertions.assertEquals("OK", response);
	}

	@AfterAll
	static void tearDown() {
		HikeController hikeController = new HikeController();
		Map<String, String> payload;
		String response;

		payload = new HashMap<String, String>();
		payload.put("name", "TestCreateHike");
		ArrayList<Hike> hikes = hikeController.search(payload);
		if (hikes != null) {
			payload = new HashMap<String, String>();
			payload.put("id", hikes.get(0).toString());
			response = hikeController.delete(payload);
			System.out.println(response);
		}


		payload.put("name", "TestCreateHike");
		hikes = hikeController.search(payload);
		if (hikes != null) {
			payload = new HashMap<String, String>();
			payload.put("id", hikes.get(0).toString());
			response = hikeController.delete(payload);
			System.out.println(response);
		}

		payload.put("name", "TestCreateHike3");
		hikes = hikeController.search(payload);
		if (hikes != null) {
			payload = new HashMap<String, String>();
			payload.put("id", hikes.get(0).toString());
			response = hikeController.delete(payload);
			System.out.println(response);
		}

		payload.put("name", "TestCreateHike35");
		hikes = hikeController.search(payload);
		if (hikes != null) {
			payload = new HashMap<String, String>();
			payload.put("id", hikes.get(0).toString());
			response = hikeController.delete(payload);
			System.out.println(response);
		}
	}
}

