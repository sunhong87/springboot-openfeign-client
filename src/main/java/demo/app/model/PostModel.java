package demo.app.model;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostModel {
	private Integer id;

	@CsvBindByPosition(position = 0)
	private String title;

	@CsvBindByPosition(position = 1)
	private String body;

	@CsvBindByPosition(position = 2)
	private Integer userId;
	
	private String error;
}
