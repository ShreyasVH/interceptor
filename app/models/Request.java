package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
public class Request
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String host;

    private String port;

    private String path;

    private String method;

    private String payload;

    private String headers;

    @OneToOne(cascade = CascadeType.ALL)
    private Response response;
}
