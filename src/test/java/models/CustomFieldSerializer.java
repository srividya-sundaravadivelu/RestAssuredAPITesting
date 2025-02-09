package models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class CustomFieldSerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (value instanceof Integer) {

			// For Integer type: Only serialize if greater than 0
			if ((Integer) value > 0) {
				gen.writeNumber((Integer) value);
			} else {
				gen.writeNull();
			}
		} else {
			gen.writeObject(value); // For any other types, default serialization
		}
	}
}
