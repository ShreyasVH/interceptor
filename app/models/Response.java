package models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "responses")
@Getter
@Setter
public class Response extends Model
{
    @Id
    private Long id;

    private JsonNode headers;

    private int status;

    private String body;

    private long duration;
}
