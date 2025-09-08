package kg.mlsp.staffcontrol.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PrivilegeActivationRequestDto {
    @NotNull
    private ModelType modelType;   // SUB_SERVICE | ROLE

    @NotNull
    private Integer modelId;       // id SubService или Role

    @NotNull
    private UUID userId;

    @NotNull
    private ActionType actionType; // ACTIVATE | DEACTIVATE

    public enum ModelType {
        SUB_SERVICE, ROLE;
        @JsonCreator
        public static ModelType from(String v) { return ModelType.valueOf(v.toUpperCase()); }
        @JsonValue
        public String toJson() { return name(); }
    }

    public enum ActionType {
        ACTIVATE, DEACTIVATE;
        @JsonCreator
        public static ActionType from(String v) { return ActionType.valueOf(v.toUpperCase()); }
        @JsonValue
        public String toJson() { return name(); }
    }
}
