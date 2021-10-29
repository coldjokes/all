package com.dosth.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.dto.Lattice;
import com.dosth.tool.entity.enums.EnumDoor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @description 测试Map转Json
 * @author guozhidong
 *
 */
public class TestMap2Json {

	public static void main(String[] args) {
		Map<ExtraCabinet, List<Card>> cardMap = new HashMap<ExtraCabinet, List<Card>>();
		ExtraCabinet cabinet = new ExtraCabinet(null, "1", null, "001", 0, "LEFT", 406, "PLC");
		List<Card> cardList = new ArrayList<>();

		Card card = new Card("123", "192.168.1.101", "502", 1, 1);
		card.setDoor(EnumDoor.LEFT.name());
		if (cardList.contains(card)) {
			card = cardList.remove(cardList.indexOf(card));
		}
//		card.getLatticeList().add(new Lattice("staId", "recordId", null, null, 0, 9, 0, null, 1, 9, 40));
		cardList.add(card);
		cardMap.put(cabinet, cardList);
		String json = map2Json(cardMap);
		System.out.println(json);
		cardMap = json2Map(json);
		for (Entry<ExtraCabinet, List<Card>> entry : cardMap.entrySet()) {
			System.out.println(entry.getKey().toString());
			for (Card c : entry.getValue()) {
				System.out.println(c.toString());
				for (Lattice lattice : c.getLatticeList()) {
					System.out.println(lattice.toString());
				}
			}
		}
	}
	
	private static String map2Json(Map<ExtraCabinet, List<Card>> map) {
		try {
			return new ObjectMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Map<ExtraCabinet, List<Card>> json2Map(String json) {
		try {
			return new ObjectMapper().readValue(json, new TypeReference<Map<ExtraCabinet, List<Card>>>() {
			});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}