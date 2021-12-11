package ee.tlu.forum.model;

import ee.tlu.forum.utils.RoleHelper;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Role {

    public Role(Long id, String name, String bodyCss, String textCss) {
        this.id = id;
        this.name = name;
        this.bodyCss = bodyCss;
        this.textCss = textCss;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String bodyCss = "#5D90CB";

    @NotNull
    private String textCss = "#FFFFFF";

    @Transient
    private String displayName;

    public String getDisplayName() {
        return RoleHelper.toDisplayName(this.name);
    }



}
