package praktikum;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ingredients {
    @SerializedName("_id")
    String id;

    public Ingredients(String id) {
        this.id = id;
    }
}