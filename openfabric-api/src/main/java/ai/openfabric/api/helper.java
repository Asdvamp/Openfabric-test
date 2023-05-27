package ai.openfabric.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.function.Supplier;

@UtilityClass
public class helper {
    @Autowired ObjectMapper mapper;

    public static String writeToString(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException jsonProcessingException) {
            return StringUtils.EMPTY;
        }
    }

    public static <T> Optional<T> getNullSafeObject(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
