package demo.app.service.impl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import demo.app.model.PostModel;
import demo.app.service.InputContentType;
import demo.app.service.InputParser;
import lombok.extern.slf4j.Slf4j;

@Component(InputContentType.Constant.CSV_TEXT)
@Slf4j
public class InputCsvParser implements InputParser {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<PostModel> parse(Reader reader) {
		try(CSVReader csvReader = new CSVReader(reader)) {
			CsvToBean<PostModel> postModelCsvbean = new CsvToBeanBuilder(reader).withType(PostModel.class).build();
			return postModelCsvbean.parse();
		} catch (Exception e) {
			log.error("Read CSV error...", e);
		}
		return new ArrayList<>();
	}
}
