package by.skachkovdmitry.audit.core.dto;

import by.skachkovdmitry.audit.config.properies.LocalDateTimeUnixTimestampSerializer;
import by.skachkovdmitry.audit.config.properies.UnixTimestampToLocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserActionAuditParam {

    private UUID user;

    @JsonSerialize(using = LocalDateTimeUnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime from;

    @JsonSerialize(using = LocalDateTimeUnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime to;
}
