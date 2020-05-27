package org.gentar.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/** Collection of utility methods to work with JSON strings. */
public class JsonHelper
{
  private static ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
    objectMapper.setDateFormat(df);
  }

  private JsonHelper() {}

  /**
   * Parses the given JSON string into a Java object using a standard Jackson mapper.
   *
   * @param json the JSON string to parse.
   * @param toClass class of the target object.
   * @param <T> type of the target object.
   * @return the Java object the JSON string was mapped into.
   * @throws IOException if the JSON string could not be parsed into an object of the given target
   *     type.
   */
  public static <T> T fromJson(String json, Class<T> toClass) throws IOException {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper.readValue(json, toClass);
  }

  /**
   * Parses the given JSON string into a Java object using a standard Jackson mapper. This method
   * allows to specify a {@link TypeReference} that describes the class to parse and thus supports
   * parsing generic types.
   *
   * @param json the JSON string to parse.
   * @param typeReference a {@link TypeReference} object that describes the class to parse.
   * @param <T> type of the target object.
   * @return the Java object the JSON string was mapped into.
   * @throws IOException if the JSON string could not be parsed into an object of the given target
   *     type.
   */
  public static <T> T fromJson(String json, TypeReference<T> typeReference) throws IOException {
    return objectMapper.readValue(json, typeReference);
  }

  public static <T> T fromJson(InputStream is, Class<T> toClass) throws IOException {
    return objectMapper.readValue(is, toClass);
  }

  /** Maps the given object to JSON using a standard Jackson mapper. */
  public static <T> String toJson(T object) throws JsonProcessingException
  {
    return objectMapper.writeValueAsString(object);
  }

}
