package by.skachkovdmitry.audit.core.dto;

import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserActionAuditParam {

    private String user;

    private String from;

    private String to;
}
