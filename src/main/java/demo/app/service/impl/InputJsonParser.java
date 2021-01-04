package demo.app.service.impl;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

import demo.app.model.PostModel;
import demo.app.service.InputContentType;
import demo.app.service.InputParser;
import lombok.extern.slf4j.Slf4j;

@Component(InputContentType.Constant.JSON_TEXT)
@Slf4j
public class InputJsonParser implements InputParser {

	@Override
	public List<PostModel> parse(Reader reader) {
		StringBuilder jsonContent = new StringBuilder();
		try(BufferedReader bfr = new BufferedReader(reader)) {
			bfr.lines().forEach(jsonContent::append);
			if(jsonContent.length() > 0) {
				JsonElement jsonElement = JsonParser.parseString(jsonContent.toString());
				if(jsonElement.isJsonArray()) {
					return parseJsonArray(jsonElement.getAsJsonArray());
				} else if(jsonElement.isJsonObject()) {
					return parseObject(jsonElement.getAsJsonObject());
				}
			}
		} catch (Exception e) {
			log.error("Read JSON error...", e);
		}
		return new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	private List<PostModel> parseJsonArray(JsonArray jsonArray) {
		final Gson gson = new Gson();
		List<Object> jsonList = gson.fromJson(jsonArray, List.class);
		return jsonList.stream()
				.map(linkedTreeMap -> (LinkedTreeMap<Object, Object>)linkedTreeMap)
				.map(map -> {
					JsonElement jsonElement = gson.toJsonTree(map);
					return gson.fromJson(jsonElement, PostModel.class);
				})
				.collect(Collectors.toList());
	}
	
	private List<PostModel> parseObject(JsonObject jsonObj) {
		JsonArray newJsonArray = new JsonArray();
		newJsonArray.add(jsonObj);
		return parseJsonArray(newJsonArray);
	}
}
